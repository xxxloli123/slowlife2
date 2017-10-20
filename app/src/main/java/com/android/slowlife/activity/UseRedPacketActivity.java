package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.UseRedPacketNavBarAdapter;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.view.PagerSlidingTabStrip;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/25 0025.
 */

public class UseRedPacketActivity extends BaseActivity implements View.OnClickListener {

    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mPager;
    private RelativeLayout go_back;
    private LinearLayout convertLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }

        if (((MyApplication) getApplication()).getInfo() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_use_red_packet);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pagerSlidingTab);
        mPager = (ViewPager) findViewById(R.id.viewPager);
        TabsInformation();
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        convertLayout = (LinearLayout) findViewById(R.id.convert_layout);
        convertLayout.setOnClickListener(this);
    }

    /**
     * 根据选择的不同界面选择不同tabs
     */
    private void TabsInformation() {
        ArrayList<String> list = new ArrayList<>();
        list.add("未使用");
        list.add("使用记录");
        list.add("过期红包");
        mPager.setAdapter(new UseRedPacketNavBarAdapter(getSupportFragmentManager(), list));
        mPagerSlidingTabStrip.setViewPager(mPager);
        mPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.grpkg1:                               // 兑换红包
                intent = new Intent(this, ConvertRedPacketActivity.class);
                startActivity(intent);
                break;
            case R.id.grpkg2:                               // 领取红包
                intent = new Intent(this, IntegralConvertActivity.class);
                startActivity(intent);
                break;
        }
    }
}
