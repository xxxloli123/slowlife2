package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Observable;
import com.Observer;
import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.util.SimpleCallback;
import com.interfaceconfig.Config;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 提现到支付宝
 */
public class CashAliActivity extends BaseActivity implements Observer<Boolean> {

    @Bind(R.id.cash_sum)
    EditText sum;
    @Bind(R.id.cash_name)
    EditText name;
    @Bind(R.id.isbind)
    TextView textView;
    private boolean binded;
    private Info info;
    private Double balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_ali);
        ButterKnife.bind(this);
        info = ((MyApplication) getApplication()).getInfo();
        if (info == null) {
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        loadAccountInfo();
    }

    private void loadAccountInfo() {
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
                balance = Double.valueOf(json.getString("balance"));      //  余额
                binded = json.getString("alipayBinding").equals("yes");
                if (binded) {
                    textView.setText("已绑定");
                } else textView.setText("未绑定");
            }

        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.cashAliPay:
                cash();
                break;
            case R.id.cash_bind:
                if (!binded)
                    startActivity(new Intent(this, BindAliPayActivity.class));
                break;
        }
    }

    private void cash() {
        if (!binded) {
            Toast.makeText(this, "请先绑定支付宝账号", Toast.LENGTH_SHORT).show();
            return;
        }
        String txt = sum.getText().toString().trim();
        if (isEmpty(txt) || !txt.matches("^\\d$")) {
            Toast.makeText(this, "请输入正确的提现金额", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = this.name.getText().toString().trim();
        if (isEmpty(name)) {
            Toast.makeText(this, "请输入开户人姓名", Toast.LENGTH_SHORT).show();
            return;
        }

//        [userId, type, userName, amount]
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId())
                .addFormDataPart("type", "Alipay")
                .addFormDataPart("userName", name)
                .addFormDataPart("amount", txt)
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(Config.Url.getUrl(Config.CASH))
                .build();
        new OkHttpClient().newCall(request).enqueue(new SimpleCallback(this) {
            @Override
            public void onSuccess(String tag, JSONObject json) throws JSONException {
                Toast.makeText(CashAliActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void update(Observable observable, Boolean aBoolean) {
        if (aBoolean) finish();
    }
}
