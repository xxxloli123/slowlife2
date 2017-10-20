package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.Observable;
import com.Observer;
import com.alipay.sdk.PayUtil;
import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.util.SimpleCallback;
import com.interfaceconfig.Config;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class BindAliPayActivity extends BaseActivity implements Observer<Boolean> {

    OkHttpClient httpClient = new OkHttpClient();
    private Callback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_ali_pay);
    }


    public void onClick(View v) {
        if (v.getId() == R.id.back_rl) {
            finish();
            return;
        }
        Info info = ((MyApplication) getApplication()).getInfo();
        if (info == null) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        if (callback == null)
            callback = new SimpleCallback(this) {
                @Override
                public void onSuccess(String tag, JSONObject json) throws JSONException {
                    switch (tag) {
                        case "bind":
//                        {"orderId":"402881f05cc866d9015cc86b1c7b0002","message":"创建账户绑定订单成功(013)","statusCode":200}
                            pay(json.getString("orderId"));
                            break;
                        case "pay":
                            PayUtil.addObserver(BindAliPayActivity.this);
                            PayUtil.aliPay(BindAliPayActivity.this, json.getString("alipayBody"));
                            break;
                    }
                }
            };
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId())
                .addFormDataPart("type", "Alipay")
                .build();
        Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.ORDERBIND))
                .post(requestBody)
                .tag("bind")
                .build();
        httpClient.newCall(request).enqueue(callback);
    }


    private void pay(String orderid) {
        MultipartBody.Builder requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("orderIds", orderid);
        requestBody.addFormDataPart("way", "alipay");
        Request request = new Request.Builder()
                .url(Config.Url.getUrl("slowlife/apppay/createtrade"))
                .tag("pay")
                .post(requestBody.build())
                .build();
        new OkHttpClient().newCall(request).enqueue(callback);
    }

    @Override
    public void update(Observable observable, Boolean aBoolean) {
        if (aBoolean) {
            PayUtil.deleteObserver(this);
            finish();
        }
    }
}
