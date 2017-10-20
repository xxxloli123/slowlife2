package com.android.slowlife.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.android.slowlife.BaseActivity;
import com.android.slowlife.MainActivity;
import com.android.slowlife.R;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.view.TimeButton;
import com.google.gson.Gson;
import com.interfaceconfig.Config;
import com.slowlife.map.interfaces.APSImpl;
import com.slowlife.map.interfaces.APSInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/31 0031.
 */

public class BoundPhoneActivity extends BaseActivity implements View.OnClickListener, APSInterface.OnApsChanged {
    private RelativeLayout go_back;
    private TimeButton sendText;
    private EditText phone, verificationCode, passoword;
    private Button ensure;
    private JSONObject smsJson;
    private APSImpl aps;
    private String adcode;
    private String area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_bound_phone);
        aps = new APSImpl(this);
        AMapLocation address = ((MyApplication) getApplication()).getLocation();
        if (address != null) {
            adcode = address.getAdCode();
            area = String.format("%s %s %s", address.getProvince(), address.getCity(), address.getDistrict());
        } else {
            aps.onCreate();
            aps.addListener(this);
        }
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        sendText = (TimeButton) findViewById(R.id.send_code_text);
        sendText.setOnClickListener(this);
        phone = (EditText) findViewById(R.id.phone);
        verificationCode = (EditText) findViewById(R.id.verification_code);
        ensure = (Button) findViewById(R.id.ensure_bt);
        ensure.setOnClickListener(this);
        passoword = findView(R.id.changPhone_password);
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

                final String phone = this.phone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
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
                        .post(requestBody)
                        .tag("smsCode")
                        .build();
                new OkHttpClient().newCall(request).enqueue(this);
                break;
            case R.id.ensure_bt:
                delgeteDialog();
                break;
        }
    }

    /**
     * dialog窗口
     */
    private void delgeteDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout mDialog = (LinearLayout) inflater.inflate(R.layout.dialog_revise_phone, null);
        final Dialog dialog = new AlertDialog.Builder(this).create();
        Button mYes = (Button) mDialog.findViewById(R.id.sure_bt);
        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                try {
                    bindPhone();
                } catch (JSONException e) {
                    Toast.makeText(BoundPhoneActivity.this, "解析数据失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        Button mNo = (Button) mDialog.findViewById(R.id.cancel_bt);
        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setContentView(mDialog);
    }


    private void bindPhone() throws JSONException {
        Info info = ((MyApplication) getApplication()).getInfo();
        if (info==null){
            startActivity(new Intent(this,LoginActivity.class));
            return;
        }
        final String pwd = passoword.getText().toString().trim();
        if (isEmpty(pwd)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        String code = verificationCode.getText().toString().trim();
        if (isEmpty(code)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
//        userStr：包含id,phone(新号码),password smsStr:id,code,phone ;area:地区全名称； adcode：区县代码
        JSONObject userJson = new JSONObject();
        userJson.put("password", pwd);
        userJson.put("id", info.getId());
        userJson.put("phone", smsJson.getString("phone"));
        smsJson.put("code", code);

        MultipartBody.Builder requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userStr", userJson.toString())
                .addFormDataPart("smsStr", smsJson.toString());
        requestBody.addFormDataPart("area", isEmpty(area) ? "1" : area)
                .addFormDataPart("adcode", isEmpty(adcode) ? "1" : adcode);
        Request request = new Request.Builder()
                .url(Config.Url.getUrl("slowlife/appuser/userbingphone"))
                .post(requestBody.build())
                .tag("bind")
                .build();
        new OkHttpClient().newCall(request).enqueue(this);
    }

    @Override
    protected void onSuccess(Call call, Response response, JSONObject json) throws JSONException {
        super.onSuccess(call, response, json);
        final String tag = response.request().tag().toString();
        switch (tag) {
            case "smsCode": {
                smsJson = new JSONObject();
                smsJson.put("id", json.getJSONObject("sms").getString("id"));
                smsJson.put("phone", json.getJSONObject("sms").getString("phone"));
                Toast.makeText(this, "获取成功", Toast.LENGTH_SHORT).show();
                verificationCode.setEnabled(true);
                break;
            }
            default: {
                Info info = new Gson().fromJson(json.getString("userinfo"), Info.class);
                ((MyApplication) getApplication()).setInfo(info);
                Toast.makeText(this, "绑定成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BoundPhoneActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("rpkg", true);
                startActivity(intent);
                finish();
                break;
            }
        }
    }

    @Override
    protected void onPause() {
        aps.onPause();
        super.onPause();
    }

    @Override
    protected void onFail(Call call, IOException e) {
        super.onFail(call, e);
        verificationCode.setEnabled(true);
    }

    @Override
    public void onChanged(AMapLocation location) {
        adcode = location.getAdCode();
        area = String.format("%s %s %s", location.getProvince(), location.getCity(), location.getDistrict());
        if (!isEmpty(adcode)) {
            aps.onPause();
        }
    }

    @Override
    public void Fail() {

    }
}
