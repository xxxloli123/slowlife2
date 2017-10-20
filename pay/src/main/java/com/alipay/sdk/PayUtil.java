package com.alipay.sdk;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.Observable;
import com.Observer;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.AuthResult;
import com.alipay.sdk.pay.demo.PayResult;

import java.util.Map;

/**
 * Created by sgrape on 2017/5/24.
 * e-mail: sgrape1153@gmail.com
 */

public class PayUtil {

    private static Context context;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private static Observable<Boolean> observable;

    static {
        observable = new Observable();
    }

    public static void aliPay(final Activity act, final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
//                if (orderInfo.startsWith("alipay_sdk=alipay-sdk-java-dynamicVersionNo&"))
//                    orderInfo = orderInfo.substring("alipay_sdk=alipay-sdk-java-dynamicVersionNo&".length());
                PayTask alipay = new PayTask(act);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        context = act.getApplicationContext();
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public static void init(Context context) {
        PayUtil.context = context;
    }

    public static void clear() {
        context = null;
        observable.deleteObservers();
    }


    public static interface AliPayCallback {
        void pay(PayResult result);
    }

    public static Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    System.out.println(payResult);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    if (context != null)
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    observable.setChanged();
                    observable.notifyObservers(TextUtils.equals(resultStatus, "9000"));
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();
                    observable.setChanged();
                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        observable.notifyObservers(true);
                        if (context != null)
                            Toast.makeText(context,
                                    "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                    .show();
                    } else {
                        // 其他状态值则为授权失败
                        observable.notifyObservers(false);
                        if (context != null)
                            Toast.makeText(context,
                                    "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }
    };


    public static void addObserver(Observer o) {
        observable.addObserver(o);
    }

    public static void deleteObserver(Observer o) {
        observable.deleteObserver(o);
    }

}
