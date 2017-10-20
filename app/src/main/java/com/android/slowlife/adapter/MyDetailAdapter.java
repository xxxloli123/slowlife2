package com.android.slowlife.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/2 .
 */

public class MyDetailAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentlist;
    ArrayList<String> titles;

    public MyDetailAdapter(FragmentManager fm, List<Fragment> list, ArrayList<String> titles) {
        super(fm);
        this.fragmentlist = list;
        this.titles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return fragmentlist.size();
    }

    public Fragment getItem(int position) {
        return fragmentlist.get(position);
    }
}
