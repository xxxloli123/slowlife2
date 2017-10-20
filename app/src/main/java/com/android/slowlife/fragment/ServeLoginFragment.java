package com.android.slowlife.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.R;
import com.android.slowlife.activity.LoginActivity;
import com.android.slowlife.activity.RegisterActivity;
import com.android.slowlife.activity.ResetPasswordActivity;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.view.ShSwitchView;
import com.google.gson.Gson;
import com.interfaceconfig.Config;
import com.slowlife.lib.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/21 0021.
 * <p>
 * TODO 密码登录
 */

public class ServeLoginFragment extends Fragment implements View.OnClickListener {
    private TextView forgetPassowrd, register;
    private Button login;
    private ShSwitchView switchView;
    private EditText phoneEdit, passwordEdit;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (login != null)
                        Toast.makeText(login.getContext(), "登录失败", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    decode(msg.obj.toString());
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        forgetPassowrd = (TextView) view.findViewById(R.id.forget_passowrd_text);
        forgetPassowrd.setOnClickListener(this);
        passwordEdit = (EditText) view.findViewById(R.id.password_edit);
        phoneEdit = (EditText) view.findViewById(R.id.phone_edit);
        login = (Button) view.findViewById(R.id.fragment_login_bt);
        login.setOnClickListener(this);
        switchView = (ShSwitchView) view.findViewById(R.id.switch_view);
        register = (TextView) view.findViewById(R.id.forget_register);
        register.setOnClickListener(this);
        switchView.setOn(false);
        switchView.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                if (isOn) {
                    //选中状态 显示明文--设置为可见的密码
                    passwordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.forget_passowrd_text:
                intent = new Intent(getActivity(), ResetPasswordActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.forget_register:
                startActivity(new Intent(getContext(), RegisterActivity.class));
                break;
            case R.id.fragment_login_bt:
                doLogin();
                break;
        }
    }

    private void doLogin() {
        String account = phoneEdit.getText().toString().trim();
        String pwd = passwordEdit.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(getContext(), "账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!account.matches("^1[3|4|5|7|8]\\d{9}$")) {
            Toast.makeText(getContext(), "手机号格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject userJson = new JSONObject();
        try {


            userJson.put("phone", account);
            userJson.put("type", "General");
            userJson.put("password", MD5.md5Pwd(pwd));
            userJson.put("phoneType", "Android");
            userJson.put("token", ((MyApplication) getActivity().getApplication()).getToken());

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("userStr", userJson.toString())
                    .build();

            final Request request = new Request.Builder()
                    .url(Config.Url.getUrl(Config.LOGIN))
                    .tag(Config.SMS_CODE)
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
            ((MyApplication) getContext().getApplicationContext()).setInfo(new Gson().fromJson(jsonObject.getString("user"), Info.class));
            if (getActivity() != null)
                ((LoginActivity) getActivity()).signed();
//            Info info=new Gson().fromJson()
        } catch (JSONException e) {
            Toast.makeText(getContext(), "解析数据失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
