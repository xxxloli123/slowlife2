package com.android.slowlife.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.Common;
import com.android.slowlife.view.MyRadioButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/7 0007.
 */

public class ApplyRefundActivity1 extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.price_text)
    TextView priceText;
    @Bind(R.id.radio1)
    MyRadioButton radio1;
    @Bind(R.id.radio2)
    MyRadioButton radio2;
    @Bind(R.id.radio3)
    MyRadioButton radio3;
    @Bind(R.id.radio4)
    MyRadioButton radio4;
    @Bind(R.id.result_edit)
    EditText resultEdit;
    @Bind(R.id.submit_bt)
    Button submitBt;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;
    private MyRadioButton radioButton;
    private String selectText="";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_apply_refund1);
        ButterKnife.bind(this);
        initView();
    }
    /**
     * 初始化控件
     * */
    private void initView(){
        radioGroup.setOnCheckedChangeListener(this);
    }
    @OnClick({R.id.back_rl, R.id.submit_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.submit_bt:
                if(Common.isNull(selectText)){
                    Toast.makeText(this,"请选择退款原因",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,selectText,Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        radioButton = (MyRadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
        selectText = radioButton.getText().toString().trim();
        switch (checkedId){
            case R.id.radio1:
                break;
            case R.id.radio2:
                break;
            case R.id.radio3:
                break;
            case R.id.radio4:
                selectText = resultEdit.getText().toString().trim();
                break;
        }
    }
}
