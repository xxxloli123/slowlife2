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

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.util.CacheActivity;

/**
 * Created by Administrator on 2017/1/31 0031.
 */

public class AccountMessageActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout go_back;
    private EditText name;
    private Button ensure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_account_message);
        initView();
    }
    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        ensure= (Button) findViewById(R.id.ensure_bt);
        ensure.setOnClickListener(this);
        name= (EditText) findViewById(R.id.name);
        name.addTextChangedListener(new TextWatcher() {
            //text  输入框中改变后的字符串信息
            //start 输入框中改变后的字符串的起始位置
            //before 输入框中改变前的字符串的位置 默认为0
            //count 输入框中改变后的一共输入字符串的数量
            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count,int after) {

            }

            @Override
            public void afterTextChanged(Editable edit) {
                //edit  输入结束呈现在输入框中的信息
                if(name.length()>=5){
                    ensure.setBackgroundResource(R.drawable.login_corners_bgall);
                }else {
                    ensure.setBackgroundResource(R.drawable.login1_corners_bgall);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl://返回
                finish();
                break;
            case R.id.ensure_bt:
                if(name.length()>=5){
                    delgeteDialog();
                }
                break;
        }
    }

    /**
     * dialog窗口
     * */
    private void delgeteDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout mDialog = (LinearLayout) inflater.inflate(R.layout.dialog_revise_name, null);
        final Dialog dialog = new AlertDialog.Builder(this).create();
        TextView textView= (TextView) mDialog.findViewById(R.id.name_text);
        textView.setText("用户名只能修改一次确认将用户名修改为["+name.getText().toString().trim()+"]");
        Button mYes = (Button) mDialog.findViewById(R.id.sure_bt);
        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button mNo = (Button) mDialog.findViewById(R.id.cancel_bt);
        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setContentView(mDialog);
    }
}
