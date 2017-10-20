package com.android.slowlife.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.Common;

/**
 * Created by Administrator on 2017/2/1 0001.
 */

public class BankCardTypeActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout go_back;
    private EditText phoneEdit;
    private TextView cardTypeText;
    private Button nextStepBt;
    private LinearLayout agreeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_bankcard_type);
        initView();
    }
    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        cardTypeText= (TextView) findViewById(R.id.card_type_text);
        nextStepBt= (Button) findViewById(R.id.next_step_bt);
        nextStepBt.setOnClickListener(this);
        phoneEdit= (EditText) findViewById(R.id.phone_edit);
        phoneEdit.addTextChangedListener(textWatcher);
        agreeLayout= (LinearLayout) findViewById(R.id.agree);
        agreeLayout.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl://返回
                finish();
                break;
            case R.id.next_step_bt://下一步
                if(Common.isNull(phoneEdit.getText().toString().trim())){
                    Toast.makeText(this,"手机号为空或手机号错误",Toast.LENGTH_SHORT).show();
                }else{
                    delgeteDialog();
                }
                break;
            case R.id.agree://同意协议
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
    /**
     * dialog窗口
     * */
    private void delgeteDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout mDialog = (LinearLayout) inflater.inflate(R.layout.dialog_cardholder_state, null);
        final Dialog dialog = new AlertDialog.Builder(this).create();
        TextView title= (TextView) mDialog.findViewById(R.id.title);
        title.setText("手机号说明");
        TextView content= (TextView) mDialog.findViewById(R.id.name);
        content.setText(R.string.phone_state);
        Button mYes = (Button) mDialog.findViewById(R.id.sure_bt);
        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent=new Intent(BankCardTypeActivity.this,WriteCorrectingCodeActivity.class);
                intent.putExtra("phone",phoneEdit.getText().toString().trim());
                startActivity(intent);
                BankCardTypeActivity.this.finish();
            }
        });
        dialog.show();
        dialog.getWindow().setContentView(mDialog);
    }
}
