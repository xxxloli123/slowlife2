package com.android.slowlife.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.CouponNavBarAdapter;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.view.PagerSlidingTabStrip;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/24 0024.
 */

public class CouponActivity extends BaseActivity implements View.OnClickListener{
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mPager;
    private RelativeLayout go_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_coupon);
        mPagerSlidingTabStrip= (PagerSlidingTabStrip) findViewById(R.id.pagerSlidingTab);
        mPager= (ViewPager) findViewById(R.id.viewPager);
        TabsInformation();
        go_back= (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
    }
    /**
     *根据选择的不同界面选择不同tabs
     * */
    private void TabsInformation(){
        ArrayList<String> list = new ArrayList<>();
        list.add("全部(0)");
        list.add("可用优惠(2)");
        list.add("过期优惠(0)");
        mPager.setAdapter(new CouponNavBarAdapter(getSupportFragmentManager(), list));
        mPagerSlidingTabStrip.setViewPager(mPager);
        mPager.setCurrentItem(0);
        mPagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("", "onPageSelected:" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagerSlidingTabStrip.setOnPagerTitleItemClickListener(new PagerSlidingTabStrip.OnPagerTitleItemClickListener() {
            @Override
            public void onSingleClickItem(int position) {//单击

            }

            @Override
            public void onDoubleClickItem(int position) {//双击

            }
        });
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
