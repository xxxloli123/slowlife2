package com.android.slowlife.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
 * Created by Administrator on 2017/2/10 0010.
 */

public class ReportMerchantActivity extends BaseActivity {
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.radio1)
    MyRadioButton radio1;
    @Bind(R.id.radio1_1)
    MyRadioButton radio11;
    @Bind(R.id.radio1_2)
    MyRadioButton radio12;
    @Bind(R.id.radio2)
    MyRadioButton radio2;
    @Bind(R.id.radio2_1)
    MyRadioButton radio21;
    @Bind(R.id.radio2_2)
    MyRadioButton radio22;
    @Bind(R.id.radio2_3)
    MyRadioButton radio23;
    @Bind(R.id.radio3)
    MyRadioButton radio3;
    @Bind(R.id.radio3_1)
    MyRadioButton radio31;
    @Bind(R.id.radio3_2)
    MyRadioButton radio32;
    @Bind(R.id.submit_bt)
    Button submitBt;
    @Bind(R.id.radioGroup1)
    RadioGroup radioGroup1;
    @Bind(R.id.radioGroup2_2)
    RadioGroup radioGroup22;
    @Bind(R.id.radioGroup2)
    RadioGroup radioGroup2;
    @Bind(R.id.radioGroup3)
    RadioGroup radioGroup3;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;
    private String s="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_report_merchant);
        ButterKnife.bind(this);
        radioGroupOnclick();
    }

    /**
     * radioGroup点击事件
     */
    private void radioGroupOnclick() {
        //父gadioGroup
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (!radio1.isChecked()) {//商家资质问题(当radiobutton处于没选中的状态时，设置她子控件也处于没选中的状态)
                    radioGroup1.clearCheck();
                }
                if (!radio2.isChecked()) {//商家信息问题
                    radioGroup2.clearCheck();
                    radioGroup22.clearCheck();
                }
                if (!radio3.isChecked()) {//商家品类问题
                    radioGroup3.clearCheck();
                }
            }
        });
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //商家营业执照或经营许可证不符(判断是否被选中，如果选中则设置他的父亲也被选中)
                if (radio11.isChecked()) {
                    radio1.setChecked(true);
                    s="商家营业执照或经营许可证不符";
                }
                //商家营业执照或经营许可证缺失
                if (radio12.isChecked()) {
                    radio1.setChecked(true);
                    s="商家营业执照或经营许可证缺失";
                }
            }
        });
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //商家图片与实景不符
                if (radio23.isChecked()) {
                    radio2.setChecked(true);
                    radioGroup22.clearCheck();
                    s="商家图片与实景不符";
                }
            }
        });
        radioGroup22.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //商家地址有误(判断是否被选中，如果选中则设置他的父亲也被选中)
                if (radio21.isChecked()) {
                    radio2.setChecked(true);
                    radio23.setChecked(false);
                    s="商家地址有误";
                }
                //商家电话有误
                if (radio22.isChecked()) {
                    radio2.setChecked(true);
                    radio23.setChecked(false);
                    s="商家电话有误";
                }
            }
        });
        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //商家品类错误(判断是否被选中，如果选中则设置他的父亲也被选中)
                if (radio31.isChecked()) {
                    radio3.setChecked(true);
                    s="商家品类错误";
                }
                //商家未设置品类
                if (radio32.isChecked()) {
                    radio3.setChecked(true);
                    s="商家未设置品类";
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.back_rl, R.id.submit_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.submit_bt:
                if(Common.isNull(s)){
                    Toast.makeText(this,"请选择举报类型",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
