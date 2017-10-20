package com.android.slowlife.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.android.slowlife.fragment.UseRedPacketFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/25 0025.
 */

public class UseRedPacketNavBarAdapter extends FragmentPagerAdapter {
    private ArrayList<String> list;
    private UseRedPacketFragment frag1, frag2, frag3;

    public UseRedPacketNavBarAdapter(FragmentManager fm, ArrayList<String> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (frag1 == null)
                    frag1 = new UseRedPacketFragment("UnUsed");
                return frag1;
            case 1:
                if (frag2 == null)
                    frag2 = new UseRedPacketFragment("Used");
                return frag2;
            case 2:
                if (frag3 == null)
                    frag3 = new UseRedPacketFragment("Expired");
                return frag3;
            default:
                return null;
        }
    }

    public void setTitle(int index, String title) {
        list.set(index, title);
        notifyDataSetChanged();
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
