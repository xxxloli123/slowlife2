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

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/1/24 0024.
 * 我的积分
 */

public class MyIntegralActivity extends BaseActivity implements View.OnClickListener {
    private ListView listView;
    private RelativeLayout go_back;
    private MyIntegralAdapter adapter;
    private LinearLayout noIntegral, state;
    private Button integralBt;
    private TextView integralText;
    private int integral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_my_integral);
        initView();
        listView.setAdapter(adapter);
        loadAccountInfo();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.integral_listview);
        noIntegral = (LinearLayout) findViewById(R.id.no_integral);
        state = (LinearLayout) findViewById(R.id.state);
        state.setOnClickListener(this);
        integralBt = (Button) findViewById(R.id.integral_bt);
        integralBt.setOnClickListener(this);
        integralText = (TextView) findViewById(R.id.integral_text);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.state://积分说明
                intent = new Intent(this, HelpActivity.class);
                intent.putExtra(HelpActivity.URI, "app/appGeneral/integralProblems.html");
                startActivity(intent);
                break;
            case R.id.integral_bt:
                intent = new Intent(this, ConvertRedPacketActivity.class);
                startActivity(intent);
                break;
        }
    }


    private void loadAccountInfo() {
        final Info info = ((MyApplication) getApplication()).getInfo();
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
                integral = json.getInt("integral");
                integralText.setText(String.valueOf(integral));     // 积分
            }
        });
    }
}
