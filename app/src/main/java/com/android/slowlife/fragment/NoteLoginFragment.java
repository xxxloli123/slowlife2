package com.android.slowlife.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.R;
import com.android.slowlife.activity.HelpActivity;
import com.android.slowlife.activity.LoginActivity;
import com.android.slowlife.activity.RegisterActivity;
import com.android.slowlife.activity.ResetPasswordActivity;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.util.Common;
import com.android.slowlife.view.TimeButton;
import com.google.gson.Gson;
import com.interfaceconfig.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/21 0021.
 * TODO 短信登录
 */

public class NoteLoginFragment extends Fragment implements View.OnClickListener {
    private TextView warmPrompt;
    private Button login;
    private TimeButton sendText;
    private EditText phoneEdit, verificationCodeEdit;
    private RelativeLayout delete;
    private OkHttpClient okhttp;
    private JSONObject smsJson;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(getContext(), "登录失败", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    decode(msg.obj.toString());
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置字体颜色
        warmPrompt = (TextView) view.findViewById(R.id.hint);
        String hint = warmPrompt.getText().toString().trim();
        int fstart = hint.indexOf("《用户服务协议》");
        int fend = fstart + "《用户服务协议》".length();
        SpannableStringBuilder style = new SpannableStringBuilder(hint);
        style.setSpan(new ForegroundColorSpan(Color.RED), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        TextView tvColor = (TextView) view.findViewById(R.id.hint);
        tvColor.setText(style);
        //登录按钮
        login = (Button) view.findViewById(R.id.account_login_bt);
        login.setOnClickListener(this);
        sendText = (TimeButton) view.findViewById(R.id.verification_code);
        view.findViewById(R.id.forget_passowrd_text)
                .setOnClickListener(this);
        view.findViewById(R.id.forget_register)
                .setOnClickListener(this);
        sendText.setOnClickListener(this);
        phoneEdit = (EditText) view.findViewById(R.id.phone_edit);
        verificationCodeEdit = (EditText) view.findViewById(R.id.verification_code_edit);
        delete = (RelativeLayout) view.findViewById(R.id.delete_rl);
        delete.setOnClickListener(this);
        //如果电话号码为空设置发送验证码按钮不可点击
        phoneEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                switch (v.getId()) {
                    case R.id.phone_edit:
                        if (Common.isNull(phoneEdit.getText().toString().trim())) {
                            sendText.setEnabled(false);
                        }
                        break;
                }
            }
        });
        okhttp = new OkHttpClient();
        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 11) sendText.setEnabled(true);
                else sendText.setEnabled(false);
            }
        });
    }

    @OnClick({R.id.hint})
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.account_login_bt:
                if (Common.isNull(phoneEdit.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "手机号码错误", Toast.LENGTH_SHORT).show();
                } else if (Common.isNull(verificationCodeEdit.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "验证码错误", Toast.LENGTH_SHORT).show();
                } else {
                    doLogin();
                }
                break;
            case R.id.verification_code:
                sendText.setTextAfters("").setTextAfter("秒后重发").setTextBefore("发送验证码").setLenght(60 * 1000);
                sendText.setEnabled(false);
                getCode();
                break;
            case R.id.delete_rl:
                phoneEdit.setText("");
                break;
            case R.id.forget_passowrd_text:
                intent = new Intent(getActivity(), ResetPasswordActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.forget_register:
                startActivity(new Intent(getContext(), RegisterActivity.class));
                break;
            case R.id.hint:
                intent = new Intent(v.getContext(), HelpActivity.class);
                intent.putExtra(HelpActivity.URI,"app/appGeneral/serviceContract.html");
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        final String phone = phoneEdit.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
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
                .build();

        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    if (json.getInt("statusCode") == 200) {
                        //   login          // 8970
                        smsJson = new JSONObject();
                        smsJson.put("id", json.getJSONObject("sms").getString("id"));
                        smsJson.put("phone", json.getJSONObject("sms").getString("phone"));
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "解析错误", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 登录
     */
    private void doLogin() {
        if (smsJson == null) {
            Toast.makeText(getContext(), "请先获取验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        String phone = phoneEdit.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getContext(), phoneEdit.getHint(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!phone.matches("^1[3|4|5|7|8][0-9]{9}$")) {
            Toast.makeText(getContext(), "手机号格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        String code = verificationCodeEdit.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(getContext(), verificationCodeEdit.getHint(), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject userJson = new JSONObject();
            userJson.put("phone", phone);
            userJson.put("type", "General");
            userJson.put("phoneType", "Android");
            userJson.put("token", ((MyApplication) getActivity().getApplication()).getToken());
            smsJson.put("phone", phone);
            smsJson.put("code", code);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("userStr", userJson.toString())
                    .addFormDataPart("smsStr", smsJson.toString())
                    .build();

            final Request request = new Request.Builder()
                    .url(Config.Url.getUrl(Config.LOGIN))
                    .post(requestBody)
                    .build();
            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.obtainMessage(0).sendToTarget();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    handler.obtainMessage(1, response.body().string()).sendToTarget();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void decode(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getInt("statusCode") != 200) {
                if (jsonObject.has("message"))
                    Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), "登录成功", Toast.LENGTH_SHORT).show();
            ((MyApplication) getActivity().getApplication()).setInfo(new Gson().fromJson(jsonObject.getString("user"), Info.class));
            ((LoginActivity) getActivity()).signed();
//            Info info=new Gson().fromJson()
        } catch (JSONException e) {
            Toast.makeText(getContext(), "解析数据失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}
