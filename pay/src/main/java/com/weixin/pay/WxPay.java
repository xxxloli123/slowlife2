package com.weixin.pay;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by sgrape on 2017/5/25.
 * e-mail: sgrape1153@gmail.com
 */

public class WxPay {
    static final String APP_ID="wxa16fd44d55d01e5c";
    public static void Pay(final Context context, String userId, String orderId) {
        final IWXAPI api = WXAPIFactory.createWXAPI(context, APP_ID);
        final Handler handler = new Handler(context.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        api.sendReq((BaseReq) msg.obj);
                        break;
                }
            }
        };
        //  是否支持支付
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) return;
        final String url = "http://192.168.1.112:8080/slowlife/wxpay/createwxtrade";
        if (!api.registerApp(APP_ID)) {
            Toast.makeText(context, "注册到微信失败", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", userId)
                .addFormDataPart("orderId", orderId)
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.obtainMessage(1, "网络错误").sendToTarget();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.code() != 200) {
                        handler.obtainMessage(1, "服务器错误").sendToTarget();
                        return;
                    }
                    String buf = response.body().string();
                    if (!TextUtils.isEmpty(buf)) {
                        Log.e("get server pay params:", buf);
                        JSONObject res = new JSONObject(buf);
                        if (null != res && res.has("wxresult")) {
                            JSONObject json = res.getJSONObject("wxresult");
                            final PayReq req = new PayReq();
                            req.appId = APP_ID;
                            req.partnerId = json.getJSONArray("mch_id").getString(0);
                            req.prepayId = json.getJSONArray("prepay_id").getString(0);
                            req.nonceStr = json.getJSONArray("nonce_str").getString(0);
                            req.timeStamp = String.valueOf(System.currentTimeMillis());
                            req.packageValue = req.sign + "=WXPay";
                            req.sign = json.getJSONArray("sign").getString(0);
                            req.extData = "app data"; // optional
                            Toast.makeText(context, "正常调起支付", Toast.LENGTH_SHORT).show();
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
//                                    api.sendReq(req);
                                }
                            });
                            handler.obtainMessage(3, req).sendToTarget();
                        } else {
                            handler.obtainMessage(1, "返回错误").sendToTarget();
                        }
                    } else {
                        Log.d("PAY_GET", "服务器请求错误");
                        handler.obtainMessage(1, "服务器请求错误").sendToTarget();
                    }
                } catch (Exception e) {
                    Log.e("PAY_GET", "异常：" + e.getMessage());
                    handler.obtainMessage(1, "异常：" + e.getMessage()).sendToTarget();
                }
            }
        });
    }





}
