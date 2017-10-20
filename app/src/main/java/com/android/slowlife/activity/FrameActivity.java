package com.android.slowlife.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.fragment.FindFragment;
import com.android.slowlife.fragment.IndexFragment;
import com.android.slowlife.fragment.MyFragment;
import com.android.slowlife.fragment.OrderFragment;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;


/**
 * Created by Administrator on 2017/1/19 .
 */
public class FrameActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {
    private IndexFragment indexFragment;
    private FindFragment findFragment;
    private OrderFragment orderFragment;
    private MyFragment myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC
                );
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_home_white_24dp, "首页").setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.mipmap.ic_book_white_24dp, "发现").setActiveColorResource(R.color.teal))
                .addItem(new BottomNavigationItem(R.mipmap.ic_music_note_white_24dp, "订单").setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.mipmap.ic_tv_white_24dp, "我的").setActiveColorResource(R.color.brown))
                .setFirstSelectedPosition(0)
                .initialise();

        bottomNavigationBar.setTabSelectedListener(this);
        setDefaultFragment();

    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.layFrame, IndexFragment.newInstance("首页"));
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        FragmentManager fm = getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (indexFragment == null) {
                    indexFragment = IndexFragment.newInstance("首页");
                }
                transaction.replace(R.id.layFrame, indexFragment);
                break;
            case 1:
                if (findFragment == null) {
                    findFragment = FindFragment.newInstance("发现");
                }
                transaction.replace(R.id.layFrame, findFragment);
                break;
            case 2:
                if (orderFragment == null) {
                    orderFragment = OrderFragment.newInstance("订单");
                }
                transaction.replace(R.id.layFrame, orderFragment);
                break;
            case 3:
                if (myFragment == null) {
                    myFragment = MyFragment.newInstance("我的");
                }
                transaction.replace(R.id.layFrame, myFragment);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();

    }

    @Override
    public void onTabUnselected(int position) {
    }

    @Override
    public void onTabReselected(int position) {

    }
}
