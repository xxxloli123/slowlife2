package com.android.slowlife.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.view.TimeButton;

/**
 * Created by Administrator on 2017/1/21 0021.
 * <p>
 * TODO 设置新密码
 */

public class ResetPasswordFinishActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout back;
    private TextView clientPhone;
    private Button finishs;
    private TimeButton sendText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_reset_password_finish);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back_rl);
        back.setOnClickListener(this);
        finishs = (Button) findViewById(R.id.finish_bt);
        finishs.setOnClickListener(this);
        clientPhone = (TextView) findViewById(R.id.client_phone_text);
        clientPhone.setOnClickListener(this);
        sendText = (TimeButton) findViewById(R.id.retry_code_text);
        sendText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        String s = null;
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.retry_code_text://重发验证码
                sendText.setTextAfters("").setTextAfter("秒后重发").setTextBefore("重发验证码").setLenght(60 * 1000);
                sendText.setEnabled(false);
                break;
            case R.id.finish_bt:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.client_phone_text://拨打电话
                s = clientPhone.getText().toString().trim();
                intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + s));
                startActivity(intent);
                break;
        }
    }
}
