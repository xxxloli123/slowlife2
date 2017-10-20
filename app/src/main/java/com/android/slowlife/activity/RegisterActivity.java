package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.android.slowlife.BaseActivity;
import com.android.slowlife.MainActivity;
import com.android.slowlife.R;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.util.SimpleCallback;
import com.android.slowlife.view.TimeButton;
import com.google.gson.Gson;
import com.interfaceconfig.Config;
import com.slowlife.lib.MD5;
import com.slowlife.map.interfaces.APSImpl;
import com.slowlife.map.interfaces.APSInterface;

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

public class RegisterActivity extends BaseActivity implements APSInterface.OnApsChanged {

    @Bind(R.id.phone_edit)
    EditText phone;
    @Bind(R.id.password_edit)
    EditText pwd;
    @Bind(R.id.register_code)
    EditText code;
    @Bind(R.id.password_edit1)
    EditText pwd1;
    @Bind(R.id.verification_code)
    TimeButton getCode;
    @Bind(R.id.register_nick)
    EditText nick;
    private JSONObject smsJson;
    private APSImpl aps;
    private String adCode;
    private String account;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (((MyApplication) getApplication()).getLocation() == null) {
            aps = new APSImpl(this);
            aps.addListener(this);
            aps.onCreate();
        }
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 11)
                    if (!getCode.isEnabled()) getCode.setEnabled(true);
                    else {
                        if (getCode.isEnabled()) getCode.setEnabled(false);
                    }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.verification_code:
                getCode();
                break;
            case R.id.fragment_register_bt:
                register();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (aps != null) aps.onPause();
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        getCode.setTextAfters("").setTextAfter("秒后重发").setTextBefore("获取验证码").setLenght(60 * 1000);
        getCode.setEnabled(false);
        final String phone = this.phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!phone.matches("^1[3|4|5|7|8][0-9]{9}$")) {
            Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Config.Url.getUrl(Config.SMS_CODE);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone", phone)
                .addFormDataPart("codeType", "0")
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .tag(Config.SMS_CODE)
                .post(requestBody)
                .build();
        OkHttpClient okhttp = new OkHttpClient();
        okhttp.newCall(request).enqueue(this);

    }

    /**
     * 注册
     */
    private void register() {
        account = this.phone.getText().toString().trim();
        String code = this.code.getText().toString().trim();
        password = this.pwd.getText().toString().trim();
        String pwd1 = this.pwd1.getText().toString().trim();
        if (isEmpty(account)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!account.matches("^1[3|4|5|7|8][0-9]{9}$")) {
            Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEmpty(code)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isEmpty(password) || !password.matches("^[0-9a-zA-Z_\\*\\.\\?\\-\\+]{6,20}$")) {
            Toast.makeText(this, "密码格式不对,请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(pwd1)) {
            Toast.makeText(this, "请再次输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (smsJson == null) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            smsJson.put("phone", account);
            smsJson.put("code", code);
            JSONObject userJson = new JSONObject();
            userJson.put("phone", account);
            userJson.put("password", MD5.md5Pwd(password));
            userJson.put("type", "General");
            userJson.put("phoneType", "Android");
            userJson.put("token", ((MyApplication) getApplication()).getToken());
            AMapLocation address = ((MyApplication) getApplication()).getLocation();
            String adCode = "";
            String addr = "";
            if (address != null) {
                adCode = address.getAdCode();
                addr = address.getProvince() + address.getCity() + address.getDistrict();
            }
            System.out.println(address);
            String url = Config.Url.getUrl(Config.REGISTER);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("userStr", userJson.toString())
                    .addFormDataPart("smsStr", smsJson.toString())
                    .addFormDataPart("adcode", adCode)
                    .addFormDataPart("area", addr)
                    .build();

            final Request request = new Request.Builder()
                    .url(url)
                    .tag(Config.REGISTER)
                    .post(requestBody)
                    .build();
            new OkHttpClient().newCall(request).enqueue(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSuccess(Call call, Response response, JSONObject json) throws JSONException {
        Object tag = response.request().tag();
        if (tag.equals(Config.SMS_CODE)) {
            smsJson = new JSONObject();
            smsJson.put("id", json.getJSONObject("sms").getString("id"));
            smsJson.put("phone", json.getJSONObject("sms").getString("phone"));
        } else if (tag.equals(Config.REGISTER)) {
            ((MyApplication) getApplication()).setInfo(new Gson().fromJson(json.getString("user"), Info.class));
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            login();
        }
    }

    private void login() throws JSONException {
        JSONObject userJson = new JSONObject();
        userJson.put("phone", account);
        userJson.put("type", "General");
        userJson.put("password", MD5.md5Pwd(password));
        userJson.put("phoneType", "Android");
        userJson.put("token", ((MyApplication) getApplication()).getToken());

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userStr", userJson.toString())
                .build();

        final Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.LOGIN))
                .tag(Config.SMS_CODE)
                .post(requestBody)
                .build();
        new OkHttpClient().newCall(request).enqueue(new SimpleCallback(this) {

            @Override
            public void onSuccess(String tag, JSONObject json) throws JSONException {
                ((MyApplication) getApplication()).setInfo(new Gson().fromJson(
                        json.getString("user"), Info.class));
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("rpkg", true);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onFail(Call call, IOException e) {
        super.onFail(call, e);
    }

    @Override
    public void onChanged(AMapLocation location) {
        aps.onPause();
        adCode = location.getAdCode();
        MyApplication app = (MyApplication) getApplication();
//        app.setAddress(address);
        app.setLocation(location);
    }

    @Override
    public void Fail() {

    }
}
