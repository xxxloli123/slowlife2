package com.android.slowlife.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.view.TimeButton;
import com.interfaceconfig.Config;
import com.slowlife.lib.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/31 0031.
 */

public class ChangePhoneActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout go_back;
    private TimeButton sendText;
    private EditText phone, verificationCode;
    private Button ensure;
    @Bind(R.id.changPhone_password)
    EditText password;
    private JSONObject smsJson;
    private Info info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        info = ((MyApplication) getApplication()).getInfo();
        if (info == null) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        setContentView(R.layout.activity_bound_phone);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        sendText = (TimeButton) findViewById(R.id.send_code_text);
        phone = (EditText) findViewById(R.id.phone);
        verificationCode = (EditText) findViewById(R.id.verification_code);
        ensure = (Button) findViewById(R.id.ensure_bt);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl://返回
                finish();
                break;
            case R.id.send_code_text://发送验证码
                sendText.setTextAfters("重发验证码（").setTextAfter("）").setTextBefore("发送验证码").setLenght(60 * 1000);
                sendText.setEnabled(false);
                getCode();
                break;
            case R.id.ensure_bt:
                setPhone();
                break;
        }
    }

    private void setPhone() {
        String phone = this.phone.getText().toString().trim();
        String code = this.verificationCode.getText().toString().trim();
        String newPhone = this.phone.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        if (!phone.matches("^1[3|5|7|8]\\d{9}$")
                || !newPhone.matches("^1[3|5|7|8]\\d{9}$")) {
            Toast.makeText(this, "手机号格式不对", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isEmpty(code)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isEmpty(pwd)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            smsJson.put("code", code);
            JSONObject userJson = new JSONObject();
            userJson.put("phone", newPhone);
            userJson.put("password", MD5.md5Pwd(pwd));
            userJson.put("id", info.getId());

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("userStr", userJson.toString())
                    .addFormDataPart("smsStr", smsJson.toString())
                    .build();

            final Request request = new Request.Builder()
                    .url(Config.Url.getUrl(Config.MOTIFYPHONE))
                    .tag(Config.MOTIFYPHONE)
                    .post(requestBody)
                    .build();
            OkHttpClient okhttp = new OkHttpClient();
            okhttp.newCall(request).enqueue(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCode() {
        String phone = this.phone.getText().toString().trim();
        if (isEmpty(phone)) {
            Toast.makeText(this, "this.phone.getHint():" + this.phone.getHint(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!phone.matches("^1[3|5|7|8]\\d{9}$")) {
            Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Config.Url.getUrl(Config.SMS_CODE);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone", phone)
                .addFormDataPart("codeType", "2")
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .tag(Config.SMS_CODE)
                .post(requestBody)
                .build();
        OkHttpClient okhttp = new OkHttpClient();
        okhttp.newCall(request).enqueue(this);
    }


    @Override
    protected void onFail(Call call, IOException e) {
        super.onFail(call, e);
    }

    @Override
    protected void onSuccess(Call call, Response response, JSONObject json) throws JSONException {
        switch (response.request().tag().toString()) {
            case Config.SMS_CODE:
                smsJson = new JSONObject();
                smsJson.put("id", json.getJSONObject("sms").getString("id"));
                smsJson.put("phone", json.getJSONObject("sms").getString("phone"));
                Toast.makeText(this, "获取成功", Toast.LENGTH_SHORT).show();
                break;
            case Config.MOTIFYPHONE:
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                info.setPhone(phone.getText().toString().trim());
                setResult(Activity.RESULT_OK);
                finish();
                break;
        }
    }
}
