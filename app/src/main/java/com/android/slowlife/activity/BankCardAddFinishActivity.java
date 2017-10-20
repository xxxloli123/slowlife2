package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.util.CacheActivity;

/**
 * Created by Administrator on 2017/2/1 0001.
 */

public class BankCardAddFinishActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout go_back,finishLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_add_finish);
        initView();
    }
    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        finishLayout= (RelativeLayout) findViewById(R.id.finish_layout);
        finishLayout.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_rl://返回
                finish();
                break;
            case R.id.finish_layout:
                Intent intent=new Intent(this,WithdrawActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
