package com.android.slowlife.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.CouponCentreAdapter;
import com.android.slowlife.objectmodel.CouponCentreEntity;
import com.android.slowlife.util.CacheActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/24 0024.
 */

public class CouponCentreActiviy extends BaseActivity implements View.OnClickListener{

    private RelativeLayout go_back;
    private ListView listView;
    private CouponCentreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_coupon_centre);
        initView();
        List<CouponCentreEntity> list=new ArrayList<CouponCentreEntity>();
        int[] icon = new int[]{R.drawable.coupon};
        CouponCentreEntity coupon=null;
        for(int i=0;i<6;i++){
            coupon=new CouponCentreEntity();
            coupon.setImageUrl(icon[0]);
            list.add(coupon);
        }
        adapter=new CouponCentreAdapter(this,list);
        listView.setAdapter(adapter);
    }
    /**
     * 初始化控件
     * */
    private void initView(){
        go_back= (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        listView= (ListView) findViewById(R.id.coupon_listview);
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
