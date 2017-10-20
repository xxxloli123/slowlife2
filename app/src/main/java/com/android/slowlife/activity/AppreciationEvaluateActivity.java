package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.MainActivity;
import com.android.slowlife.R;
import com.android.slowlife.util.CacheActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/8 0008.
 */

public class AppreciationEvaluateActivity extends BaseActivity {
    @Bind(R.id.delete_rl)
    RelativeLayout deleteRl;
    @Bind(R.id.integral_text)
    TextView integralText;
    @Bind(R.id.finish_bt)
    Button finishBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_appreciation_evaluate);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.delete_rl, R.id.finish_bt})
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.delete_rl:
                finish();
                break;
            case R.id.finish_bt:
                intent=new Intent(this, MainActivity.class);
                intent.putExtra("id", 1);
                CacheActivity.finishActivity();
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
