package com.android.slowlife.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.util.CacheActivity;

/**
 * Created by Administrator on 2017/1/24 0024.
 */

public class IntegralProblemActivity extends BaseActivity implements OnClickListener{
    private RelativeLayout go_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_integral_problem);
        go_back= (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_rl:
                finish();
                break;
        }
    }
}
