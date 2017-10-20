package com.android.slowlife.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.util.CacheActivity;

/**
 * Created by Administrator on 2017/1/25 0025.
 */

public class SetActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout go_back, account, common, regards;
    private Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_set);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        account = (RelativeLayout) findViewById(R.id.account_rl);
        account.setOnClickListener(this);
        common = (RelativeLayout) findViewById(R.id.common_rl);
        common.setOnClickListener(this);
        regards = (RelativeLayout) findViewById(R.id.regards_rl);
        regards.setOnClickListener(this);
        edit = (Button) findViewById(R.id.edit_bt);
        edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl://返回
                finish();
                break;
            case R.id.account_rl://账户与安全
                intent = new Intent(this, AccountInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.common_rl://通用
                intent = new Intent(this, CommonActivity.class);
                startActivity(intent);
                break;
            case R.id.regards_rl://关于掌升活
                intent = new Intent(this, RegardsZhangshenghuoActivity.class);
                startActivity(intent);
                break;
            case R.id.edit_bt://退出登录
                if (((MyApplication) getApplication()).getInfo() == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                    return;
                }
                ((MyApplication) getApplication()).setInfo(null);
                setResult(Activity.RESULT_OK);
                finish();
                break;
        }
    }
}
