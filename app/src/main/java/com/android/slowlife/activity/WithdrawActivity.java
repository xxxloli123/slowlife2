package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.PayUtil;
import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.util.CacheActivity;

/**
 * Created by Administrator on 2017/2/1 0001.
 */

public class WithdrawActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout go_back;
    public static final String BALANCE = "balance";
    private double balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_withdraw);
        balance = getIntent().getDoubleExtra(BALANCE, -1);
        if (balance == -1) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        ((TextView) findView(R.id.cash)).setText(String.valueOf(balance));
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.ali:
                startActivity(new Intent(this, CashAliActivity.class));
                break;
            case R.id.car:
                Toast.makeText(this, "暂未开通", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        PayUtil.clear();
        super.onDestroy();
    }
}
