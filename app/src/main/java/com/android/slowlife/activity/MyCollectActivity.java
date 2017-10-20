package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.MainActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.MerchantCollectAdapter;
import com.android.slowlife.adapter.MerchantCollectAdapter.OnSlideClickListener;
import com.android.slowlife.objectmodel.MerchantEntity;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.view.ListViewCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/22 0022.
 */

public class MyCollectActivity extends BaseActivity implements View.OnClickListener ,OnSlideClickListener {
    private RelativeLayout go_back, delete, else_merchant,delete_merchant_rl,delete_me;
    private ListViewCompat merchant_list;
    private LinearLayout no_business, have_business;
    private Button home_stroll;
    private List<MerchantEntity> merchantList;
    private MerchantCollectAdapter merchantCollectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_my_collect);
        initView();
        acquireMerchantInformation();
        merchantCollectAdapter=new MerchantCollectAdapter(this,merchantList,this);
        merchant_list.setAdapter(merchantCollectAdapter);
    }

    /**
     *初始化控件
     * */
    private void initView(){
        go_back= (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        delete_me= (RelativeLayout) findViewById(R.id.delete_me);
        delete_merchant_rl= (RelativeLayout) findViewById(R.id.delete_me);
        delete= (RelativeLayout) findViewById(R.id.delete_rl);
        delete.setOnClickListener(this);
        else_merchant= (RelativeLayout) findViewById(R.id.else_merchant_rl);
        no_business= (LinearLayout) findViewById(R.id.no_business_layout);
        have_business= (LinearLayout) findViewById(R.id.have_business_layout);
        home_stroll= (Button) findViewById(R.id.home_stroll_bt);
        home_stroll.setOnClickListener(this);
        merchant_list= (ListViewCompat) findViewById(R.id.collect_merchant_list);
    }
    /**
     * listview测试数据
     * */
    private void acquireMerchantInformation(){
        merchantList=new ArrayList<MerchantEntity>();
        MerchantEntity merchant=null;
        for(int i=0;i<4;i++){
            merchant=new MerchantEntity();
            merchant.setName("王家铺子"+i);
            merchant.setAddress("（重庆市江北区观音桥融景城）");
            merchant.setDeliveryCost("3");
            merchant.setDistance("100");
            merchant.setGrade(4);
            merchant.setTime(34);
            merchant.setMonthlySalesAmount("23546");
            merchant.setPriceSending("20");
            merchantList.add(merchant);
        }
    }
    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.back_rl:
                finish();
                break;
            case R.id.delete_rl:
                delete_me.setVisibility(View.GONE);
                break;
            case R.id.home_stroll_bt://去首页逛逛吧
                intent=new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    /**
     * listview item 点击事件
     * */
    @Override
    public void onItemClick(int position, View item) {
        startActivity(new Intent(this, DetailActivity.class));
    }

    /**
     * 删除按钮
     * */
    @Override
    public void onDeleteClick(int position, View item) {
        merchant_list.slideBack();
        merchantList.remove(position);
        merchantCollectAdapter.notifyDataSetChanged();
    }

}
