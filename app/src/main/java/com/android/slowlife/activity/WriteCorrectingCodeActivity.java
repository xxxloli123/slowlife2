package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.Common;
import com.android.slowlife.view.TimeButton;

/**
 * Created by Administrator on 2017/2/1 0001.
 */

public class WriteCorrectingCodeActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout go_back;
    private EditText verificationCodeEdit;
    private Button nextStepBt;
    private TextView phoneText;
    private TimeButton sendText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_write_correcting_code);
        initView();
    }
    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        nextStepBt= (Button) findViewById(R.id.next_step_bt);
        nextStepBt.setOnClickListener(this);
        verificationCodeEdit= (EditText) findViewById(R.id.verification_code_edit);
        verificationCodeEdit.addTextChangedListener(textWatcher);
        phoneText= (TextView) findViewById(R.id.phone_text);
        Intent intent=getIntent();
        String s=intent.getStringExtra("phone");
        phoneText.setText("请输入手机"+s+"收到的短信校验码");
        sendText= (TimeButton) findViewById(R.id.retry_code_text);
        sendText.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl://返回
                finish();
                break;
            case R.id.next_step_bt://下一步
                if(Common.isNull(verificationCodeEdit.getText().toString().trim())){
                    Toast.makeText(this,"校验码为空或校验码错误",Toast.LENGTH_SHORT).show();
                }else{
                    intent=new Intent(this,BankCardAddFinishActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.retry_code_text:
                sendText.setTextAfters("").setTextAfter("秒后重发").setTextBefore("发送验证码").setLenght(60 * 1000);
                sendText.setEnabled(false);
                break;
        }
    }
    /**
     * editText监听事件
     */
    private TextWatcher textWatcher = new TextWatcher() {
        //text  输入框中改变后的字符串信息
        //start 输入框中改变后的字符串的起始位置
        //before 输入框中改变前的字符串的位置 默认为0
        //count 输入框中改变后的一共输入字符串的数量
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //s输入结束呈现在输入框中的信息
            if(s.length()>0){
                nextStepBt.setBackgroundResource(R.drawable.login_corners_bgall);
            }else{
                nextStepBt.setBackgroundResource(R.drawable.login1_corners_bgall);
            }
        }
    };
}
