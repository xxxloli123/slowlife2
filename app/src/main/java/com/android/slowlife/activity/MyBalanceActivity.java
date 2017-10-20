package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.MyIntegralAdapter;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.SimpleCallback;
import com.interfaceconfig.Config;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/1/25 0025.
 */

public class MyBalanceActivity extends BaseActivity implements View.OnClickListener {
    private ListView listView;
    private RelativeLayout go_back;
    private MyIntegralAdapter adapter;
    private LinearLayout noBalance, state;
    private Button balanceBt;
    private TextView balanceText;
    private String balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_my_balance);
        initView();
        loadAccountInfo();
    }

    private void loadAccountInfo() {
        Info info = ((MyApplication) getApplication()).getInfo();
        if (info == null) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
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
                balanceText.setText(json.getString("balance"));      //  余额
                balance = json.getString("balance");
                balanceBt.setEnabled(true);
            }

            @Override
            public void onFail(Call call) {
                balanceBt.setEnabled(false);
                super.onFail(call);
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.balance_listview);
        noBalance = (LinearLayout) findViewById(R.id.no_balance);
        state = (LinearLayout) findViewById(R.id.state);
        state.setOnClickListener(this);
        balanceBt = (Button) findViewById(R.id.balance_bt);
        balanceBt.setOnClickListener(this);
        balanceText = (TextView) findViewById(R.id.balance_text);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.state://余额说明
                intent = new Intent(this, HelpActivity.class);
                intent.putExtra(HelpActivity.URI,"app/appGeneral/theBalanceThat.html");
                startActivity(intent);
                break;
            case R.id.balance_bt://提现
                intent = new Intent(this, WithdrawActivity.class);
                intent.putExtra(WithdrawActivity.BALANCE, Double.valueOf(balance));
                startActivity(intent);
                break;
        }
    }
}
