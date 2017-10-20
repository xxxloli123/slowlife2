package com.android.slowlife.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.android.slowlife.R;
import com.android.slowlife.fragment.FragmentContent;
import com.android.slowlife.view.PagerSlidingTabStrip;

import java.util.ArrayList;

public class NavBarAdapter extends FragmentPagerAdapter  implements PagerSlidingTabStrip.IconTabProvider {

    private ArrayList<String> titles;
    private final int[] ICONS = {R.mipmap.ic_home_white_24dp};

    public NavBarAdapter(FragmentManager fm, ArrayList<String> list) {
        super(fm);
        this.titles = list;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle b = new Bundle();
        b.putString("title", titles.get(position));
        return FragmentContent.getInstance(b);
    }

    @Override
    public int getPageIconResId(int position) {
        return R.mipmap.ic_music_note_white_24dp;
    }
}
