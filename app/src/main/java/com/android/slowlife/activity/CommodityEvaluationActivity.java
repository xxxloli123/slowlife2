package com.android.slowlife.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.CommodityEvaluationAdapter;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.view.CustomRadioGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/15 0015.
 */

public class CommodityEvaluationActivity extends BaseActivity {
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.customRadioGroup)
    CustomRadioGroup customRadioGroup;
    @Bind(R.id.listview)
    ListView listview;
    private CommodityEvaluationAdapter adapter;
    private String[] labStr = {"全部", "满意", "黑色", "送货快", "商品不错", "不满意", "玫瑰金色", "金色", "银色"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_commodity_evaluation);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        for (int i = 0; i < labStr.length; i++) {
            RadioButton radioButton = (RadioButton) this.getLayoutInflater().inflate(R.layout.item_commodity_evaluation_addlab_rb, null);
            radioButton.setText(labStr[i] + "(10)");
            customRadioGroup.addView(radioButton);
        }
        String[] name = {"4566y7787", "123444", "3333", "玫瑰金色","24444", "333333", "1ftf", "gfghb", "assss", "kkkk"};
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < name.length; i++) {
            list.add(name[i]);
        }
        adapter = new CommodityEvaluationAdapter(this, list);
        listview.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.back_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
        }
    }
}
