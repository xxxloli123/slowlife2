package com.alipay.sdk.pay.demo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.util.OrderInfoUtil2_0;
import com.slowlife.pay.R;

import java.util.Map;

/**
 * 重要说明:
 * <p>
 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
 */
public class PayDemoActivity extends FragmentActivity {


    /**
     * 掌升活用户
     */
    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2017032306353407";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "2088421497164745";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "902225a5020148aeba385c95a5af270d";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCnQ" +
            "3f6+CEFJg6Ay7fmL+v7wgVp56fO9MyWQSNUIyOsIULZcogEd7PWZ4OrRT5v4ru2UEdIS9O5Af5XSeIL9IcZ1oyJ" +
            "8CeO7px4VuQiVoKgKQ0+lUrDGgOnn+l7WbWTo+5Wd05bioRALU7WfEkPoJpAQwa72apJlpJsDYcjCRZR3qd+Hi4" +
            "zYMTDQmwhRvlj69d3NXIe3wmwscbqhddiSaSyLEnH6FFnUAPQaS5vrEdOvTEbF6xcp/1DdNLw3xgz/QPCe8KB4w" +
            "pWWsCY9b5kroPUkElMWN85cU7kSD0bi7gAfNL3Hou6Xse3+40o9Mimnt8bdysRdf2aZD3jITnPbWUXAgMBAAECg" +
            "gEBAJIbvZLdbPqyY7jeO6Y39kvVECWS1mNSM+SMwe+dQmv6IgsQoQBGEDIZ+kqCEihqRs7bEWl101ypASP+xhbrY" +
            "5iOcfWMcwHILaZxQJc+OpztP0SL5OLItqiWZ734f0SYznl5flk4ho4JBfUWXDvqbtPzuM4fNUFS5lS8/G7+YSDhk" +
            "xI3UIgATt/C0smRNjquogMz0EIqDjR9+d+z/iXbjsjpxt8Db8VgJuRf1U9E3Ly011Yk8mlAxguHl51o1rOVqgE6x" +
            "uX5BAOPhS36A3HjTwJInQ2dW0Zns6P2+b1wdcpZbglKkQbYT/dJ9UJbpgsYqIJXMzd3jznuYEb+RyElkgECgYEA4" +
            "0Og/tyYj9HUeFI3ZIZxH8hhSno1NAbUeoa62lpQR7qntq0B6E0BsBsqE4YGfMfj8hBVo3iW94KeDNnh+G9DetLCB" +
            "cvxwDFcNSRj7GdujwJjW/qpWAJBvWD63zHPcBtsAAeB53U7z5kQUhWUtmP9Ufv+GvqwtMvW6ZfyuqHQ41kCgYEAv" +
            "GmqGuEUzenEKCB3CpSfFC/N6igNNrCJF+I6eGhzC8+gjQ5C7ZImWSAcEx4wrzAyTk3jDUNtG7wy6fwVpZRyI/Nst" +
            "vvGLECq7+uhMmIaUWEqo/REvQDLtqp2lsYzrjttla2taM6cWzePBTdcK47oS+SpVUg74bLx88A9vrc2re8CgYEAm" +
            "38FY/w5ooXWU3Ell3Fpa26mKthNGMjnjuIqbQZIUZvD7viXCoPN+NnNQ0h7ndsC+vf0MIJtAPLtAyv9Mgem6x3TR" +
            "TYStgbP6Uh52iG3AsCGjUOHpMD+i/4UCKzDGxiYH7kDSAPtpZA8G33Kn7TweazqYzlNuJaT+IZPvSTEkRkCgYA4O" +
            "7ExZIw5CNQqxcXBuqnf9ISYpmuLLxU7s6GP3ly34bghYWhIT55ya0r1ycTuBIx/Im/pvX4LERiBDWPIj42hfT2tP" +
            "2fePU7HXKQ3zV/degqn9HevkVU7p5TNW0qBbes/CR8AcOnpr6NYAzXXKkfantyLP4vfyPC9t1q2WShQsQKBgQCu4" +
            "QTNqmv2Xeu5QmN4PtwbvCnBP6GASAPow4mHvpIwaNsQN/VZme+Tg/G1louOx8uOZqmdPRo2wFBEBqtZ+C1AbjJBp" +
            "4sicSDgxzkx8a4JHH8y2y3Z2TNdp6L8Ku706S7CL3FiA0PuETiVMShZtTf/BS1g3HOkRwh7aKUaLyvJVQ==";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
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
                        Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayDemoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PayDemoActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayDemoActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_main);
    }

    /**
     * 支付宝支付业务
     *
     * @param v
     */
    public void payV2(View v) {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayDemoActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝账户授权业务
     *
     * @param v
     */
    public void authV2(View v) {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(PayDemoActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
    public void h5Pay(View v) {
        Intent intent = new Intent(this, H5PayDemoActivity.class);
        Bundle extras = new Bundle();
        /**
         * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
         * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
         * 商户可以根据自己的需求来实现
         */
        String url = "http://m.taobao.com";
        // url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
        extras.putString("url", url);
        intent.putExtras(extras);
        startActivity(intent);
    }

}
