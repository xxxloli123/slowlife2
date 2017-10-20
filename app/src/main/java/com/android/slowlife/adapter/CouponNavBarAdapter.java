package com.android.slowlife.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.android.slowlife.fragment.CouponFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/24 0024.
 */

public class CouponNavBarAdapter extends FragmentPagerAdapter {
    private ArrayList<String> list;
    private CouponFragment mCouponFragment;
    public CouponNavBarAdapter(FragmentManager fm, ArrayList<String> list) {
        super(fm);
        this.list=list;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                mCouponFragment=new CouponFragment();
                return mCouponFragment;
            case 1:
                mCouponFragment=new CouponFragment();
                return mCouponFragment;
            case 2:
                mCouponFragment=new CouponFragment();
                return mCouponFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }
}
