package com.android.slowlife.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.fragment.MoreAddressFragment;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.view.SearchView1;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/11 0011.
 */

public class MoreAddressActivity extends BaseActivity {
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.searchview)
    SearchView1 searchview;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_more_address);
        ButterKnife.bind(this);
        initView();
        initEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
    /**
     * 初始化控件
    */
    private void initView() {
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            private String[] mTitles = new String[]{"全部", "写字楼", "小区", "学校"};
            @Override
            public Fragment getItem(int position) {
//                if (position == 0) {
//                    return new WithdrawAlipayFragment();
//                }
                return new MoreAddressFragment();
            }
            @Override
            public int getCount() {
                return mTitles.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }

        });

        tabLayout.setupWithViewPager(pager);
    }

    private void initEvents() {

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == tabLayout.getTabAt(0)) {
                    //one.setIcon(getResources().getDrawable(R.drawable.tab1_corners_bgall));
                    pager.setCurrentItem(0);
                } else if (tab == tabLayout.getTabAt(1)) {
//                    two.setIcon(getResources().getDrawable(R.drawable.tab1_corners_bgall));
                    pager.setCurrentItem(1);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    @OnClick(R.id.back_rl)
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_rl:
                finish();
                break;
        }
    }
}
