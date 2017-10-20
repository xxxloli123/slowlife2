package com.android.slowlife.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.interfaceconfig.Config;

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
 * Created by sgrape on 2017/5/19.
 * e-mail: sgrape1153@gmail.com
 */

public class NickActivity extends BaseActivity implements TextWatcher {
    @Bind(R.id.nick)
    EditText nick;
    private Info info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nick_activity);
        info = ((MyApplication) getApplication()).getInfo();
        nick.setText(info.getNickName());
        nick.addTextChangedListener(this);
        nick.setImeActionLabel("完成", EditorInfo.IME_ACTION_DONE);
        nick.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submit();
                    return true;
                }
                return false;
            }
        });
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.done:
                submit();
                break;
        }
    }

    private void submit() {
        if (info == null) return;
        String txt = nick.getText().toString().trim();
        if (TextUtils.isEmpty(txt) && !txt.matches("[\u4E00-\u9FA5a-zA-Z0-9]")) {
            Toast.makeText(this, "昵称格式错误", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            nick.setEnabled(false);
            JSONObject json = new JSONObject();
            json.put("id", info.getId());
            json.put("nickName", nick.getText().toString().trim());
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("userStr", json.toString()).build();
            Request request = new Request.Builder().url(Config.Url.getUrl(Config.NICKNAME))
                    .post(requestBody).build();
            new OkHttpClient().newCall(request).enqueue(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSuccess(Call call, Response response, JSONObject json) throws JSONException {
        if (json.getInt("statusCode") == 200) {
            Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show();
            MyApplication app = ((MyApplication) getApplication());
            app.getInfo().setNickName(nick.getText().toString().trim());
            app.updateInfoCache();
            finish();
        } else Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
        nick.setEnabled(true);
    }

    @Override
    protected void onFail(Call call, IOException e) {
        super.onFail(call, e);
        nick.setEnabled(true);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (count > 1) {
            nick.setText(s);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
