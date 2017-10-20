package com.android.slowlife.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Observable;
import com.Observer;
import com.alipay.sdk.PayUtil;
import com.alipay.sdk.pay.demo.AuthResult;
import com.alipay.sdk.pay.demo.PayResult;
import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.OrdersAdapter;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.fragment.WOrderFrag;
import com.android.slowlife.objectmodel.OrdersEntity;
import com.android.slowlife.objectmodel.RPkg;
import com.android.slowlife.util.SimpleCallback;
import com.android.slowlife.view.RadioRelativeLayout;
import com.android.slowlife.wxapi.WXPayEntryActivity;
import com.interfaceconfig.Config;
import com.slowlife.lib.MD5;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PayActivity extends BaseActivity {
    @Bind(R.id.bredPacket)
    TextView bredPacket;
    private List<OrdersEntity> orders;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private RadioRelativeLayout check;
    @Bind(R.id.pay)
    View btnPay;
    @Bind(R.id.orderList)
    ListView listView;
    @Bind(R.id.total)
    TextView total;
    @Bind(R.id.rpkdc)
    TextView rpkgc;

    private OrdersAdapter adapter;
    private ArrayList<RPkg> pkgs;
    private Info info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        info = ((MyApplication) getApplication()).getInfo();
        //红包
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId())
                .build();
        Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.ACCOUNT))
                .post(requestBody).build();
        new OkHttpClient().newCall(request).enqueue(new SimpleCallback(this) {
            @Override
            public void onSuccess(String tag, JSONObject json) throws JSONException {
//                String.format("预估金额:%s元", jsonObject.getString("cost"))
                bredPacket.setText(String.format("当前红包有:%s个", json.getString("RedPacketsNumber")));     // 红包
            }
        });
        init();
    }

    protected void init() {
        orders = getIntent().getParcelableArrayListExtra("orders");
        if (orders == null || orders.isEmpty()) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        adapter = new WOrderFrag.Adapter(this, orders, false);
        listView.setAdapter(adapter);
        double p = 0;
        for (OrdersEntity oe : orders) {
            p += oe.getUserActualFee();
        }

        DecimalFormat df = new DecimalFormat("#0.00");
        total.setText(String.format("总价: %s  | 优惠: %s", df.format(p), "0"));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.pay: {
                pay();
            }
            break;
            case R.id.selectRPKG:
                Intent intent = new Intent(this, UseRPKGActivity.class);
                intent.putExtra("count", orders.size());
                startActivityForResult(intent, 199);
                break;
        }
    }

    private void pay() {
        if (info == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return;
        }
        StringBuilder ids = new StringBuilder();
        for (OrdersEntity order : orders) {
            ids.append(order.getId()).append(",");
        }
        StringBuilder pkgids = new StringBuilder();
        if (pkgs != null)
            for (RPkg pkg : pkgs) {
                pkgids.append(pkg.getId()).append(",");
            }
        if (check.getId() != R.id.wxpay) {
            MultipartBody.Builder requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("orderIds", ids.substring(0, ids.length() - 1));
            if (pkgids.length() > 0)
                requestBody.addFormDataPart("userRedPacketsId", pkgids.substring(0, pkgids.length() - 1));
            if (check.getTag().toString() != "wx")
                requestBody.addFormDataPart("way", check.getTag().toString());
            Request request = new Request.Builder().url(Config.Url.getUrl("slowlife/apppay/createtrade"))
                    .post(requestBody.build())
                    .tag(check.getTag())
                    .build();
            new OkHttpClient().newCall(request).enqueue(callback);
        } else wxPay();
    }

    private void showDialog(JSONObject json) throws JSONException {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout mDialog = (LinearLayout) inflater.inflate(R.layout.dialog_revise_phone, null);
        final Dialog dialog = new AlertDialog.Builder(this).create();
        Button mYes = (Button) mDialog.findViewById(R.id.sure_bt);
        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button mNo = (Button) mDialog.findViewById(R.id.cancel_bt);
        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
        TextView tv = (TextView) mDialog.findViewById(R.id.title);
        tv.setText(String.format("实际支付%s元(现金支付暂不支持红包)", json.getString("fee")));
        dialog.show();
        dialog.getWindow().setContentView(mDialog);
    }

    public void onCheck(View v) {
        if (check != null) check.toggle();
        check = (RadioRelativeLayout) v;
        btnPay.setEnabled(true);
    }

    private void wxPay() {
        WXPayEntryActivity.addObserver(new PayObserver());
        final PayReq req = new PayReq();
        req.appId = "wxa16fd44d55d01e5c";
        req.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        req.packageValue = "Sign=WXPay";
        final IWXAPI api = WXAPIFactory.createWXAPI(this, "wxa16fd44d55d01e5c");
        //  是否支持支付
        final String url = Config.Url.getUrl("slowlife/wxpay/createwxtrade");
        try {
            boolean reg = api.registerApp("wxa16fd44d55d01e5c");
            if (!reg) {
                Toast.makeText(this, "注册到微信失败", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
        }
        Info info = ((MyApplication) getApplication()).getInfo();
        if (info == null) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (OrdersEntity oe : orders) {
            sb.append(oe.getId())
                    .append(",");
        }
        StringBuilder pkgids = new StringBuilder();
        if (pkgs != null)
            for (RPkg pkg : pkgs) {
                pkgids.append(pkg.getId()).append(",");
            }
        String ids = sb.substring(0, sb.length() - 1);
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("orderIds", ids);
        if (pkgids.length() > 0)
            requestBody.addFormDataPart("userRedPacketsIds", pkgids.substring(0, pkgids.length() - 1));

        Request request = new Request.Builder()
                .post(requestBody.build())
                .url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.obtainMessage(0, "网络错误").sendToTarget();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.code() != 200) {
                    mHandler.obtainMessage(0, "服务器错误").sendToTarget();
                    return;
                }
                final String buf = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (!TextUtils.isEmpty(buf)) {
                                JSONObject res = new JSONObject(buf);
                                Log.e("get server pay params:", buf);
                                if (res.getInt("statusCode") != 200) {
                                    Toast.makeText(PayActivity.this, res.getString("message"), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (null != res && res.has("wxresult")) {
                                    JSONObject json = res.getJSONObject("wxresult");
                                    final PayReq req = new PayReq();
                                    req.appId = "wxa16fd44d55d01e5c";
                                    req.partnerId = json.getJSONArray("mch_id").getString(0);
                                    req.prepayId = json.getJSONArray("prepay_id").getString(0);
                                    req.nonceStr = json.getJSONArray("nonce_str").getString(0);
                                    req.timeStamp = String.valueOf(System.currentTimeMillis());
                                    req.packageValue = "Sign=WXPay";


                                    StringBuffer signsb = new StringBuffer();
                                    signsb.append("appid=wxa16fd44d55d01e5c&noncestr=")
                                            .append(req.nonceStr)
                                            .append("&package=")
                                            .append(req.packageValue)
                                            .append("&partnerid=")
                                            .append(req.partnerId)
                                            .append("&prepayid=")
                                            .append(req.prepayId)
                                            .append("&timestamp=")
                                            .append(req.timeStamp)
                                            .append("&key=")
                                            .append("e866f0d58b12ae41dd677d2c7f313d5b");
                                    req.sign = MD5.md5(signsb.toString()).toUpperCase();

                                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                    boolean result = api.sendReq(req);
                                    if (result)
                                        Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(PayActivity.this, "调用支付失败", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d("PAY_GET", "服务器请求错误");
                                Toast.makeText(PayActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("PAY_GET", "异常：" + e.getMessage());
                            mHandler.obtainMessage(0, "异常：" + e.getMessage()).sendToTarget();
                        }
                    }
                });
            }
        });
    }

    //[tradeNoBody, cost, string, userId]
    private void wallet(JSONObject json) throws JSONException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("tradeNoBody", json.getString("tradeNoBody"))
                .addFormDataPart("cost", json.getString("cost"))
                .addFormDataPart("string", json.getString("string"))
                .addFormDataPart("userId", info.getId())
                .addFormDataPart("paypassword", "")
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(Config.Url.getUrl("slowlife/apppay/walletpay"))
                .build();
        new OkHttpClient().newCall(request).enqueue(new SimpleCallback(this) {
            @Override
            public void onSuccess(String tag, JSONObject json) throws JSONException {
                Toast.makeText(PayActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            }

        });
    }

    private void cash(JSONObject json) throws JSONException {
        Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show();
    }

    public static String createSign(JSONObject parame) throws JSONException {
        StringBuffer buffer = new StringBuffer();
        Iterator<String> iterator = parame.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = parame.getString(key);
            if (null != value && !"".equals(value) && !"sign".equals(key) && !"key".equals(key)) {
                buffer.append(key + "=" + value + "&");
            }
        }
        buffer.append("key=e866f0d58b12ae41dd677d2c7f313d5b");
        String sign = MD5.md5(buffer.toString()).toUpperCase();
        return sign;
    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(PayActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                case 0: {
                    Toast.makeText(PayActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        PayUtil.clear();
        WXPayEntryActivity.clear();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        pkgs = data.getParcelableArrayListExtra("pkgs");
        double p = 0;
        for (OrdersEntity oe : orders) {
            p += oe.getCost() + oe.getUrgentFee();
        }
        double pk = 0;
        for (RPkg pkg : pkgs) {
            pk += pkg.getCost();
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        rpkgc.setText("-" + df.format(pk));
        total.setText(String.format("总价: %s  | 优惠: %s", df.format(p), df.format(pk)));
    }


    private Callback callback = new SimpleCallback(this) {
        @Override
        public void onSuccess(String tag, JSONObject json) throws JSONException {
            System.out.println(json);
            switch (tag) {
                case "alipay":
                    String orderinfo = json.getString("alipayBody");
                    PayUtil.addObserver(new PayObserver());
                    PayUtil.aliPay(PayActivity.this, orderinfo);
                    break;
                case "wallet":
                    wallet(json);
                    break;
                case "cash":
                    showDialog(json);
                    break;
            }
        }
    };


    class PayObserver implements Observer<Object> {

        @Override
        public void update(Observable observable, Object obj) {

            if (obj instanceof Boolean && ((boolean) obj)) {
                setResult(Activity.RESULT_OK);
                finish();
            }
            if (obj instanceof BaseResp) {
                BaseResp resp = (BaseResp) obj;
                String str = "支付失败";
                switch (resp.errCode) {
                    case 0:
                        str = "支付成功";
                        setResult(Activity.RESULT_OK);
                        finish();
                        break;
                    case -1:
                        str = "取消支付";
                        break;
                    case -2:
                        str = "支付失败";
                        break;
                }
                Toast.makeText(PayActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
