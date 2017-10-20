package com.android.slowlife.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.android.slowlife.fragment.DoorPickingFragment;
import com.android.slowlife.fragment.PickupHomeFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/14 0014.
 */

public class ExpressNavBarAdapter extends FragmentPagerAdapter {
    private ArrayList<String> list;
    private Fragment doorPickingFragment, pickupHomeFragment;

    public ExpressNavBarAdapter(FragmentManager fm, ArrayList<String> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (doorPickingFragment == null)
                    doorPickingFragment = new DoorPickingFragment();
                return doorPickingFragment;
            case 1:
                if (pickupHomeFragment == null)
                    pickupHomeFragment = new PickupHomeFragment();
                return pickupHomeFragment;
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
