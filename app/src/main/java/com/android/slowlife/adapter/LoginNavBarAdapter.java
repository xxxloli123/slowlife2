package com.android.slowlife.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.android.slowlife.fragment.NoteLoginFragment;
import com.android.slowlife.fragment.ServeLoginFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/21 0021.
 */

public class LoginNavBarAdapter extends FragmentPagerAdapter {
    private ArrayList<String> list;
    // 短信登录
    private NoteLoginFragment mNoteLoginFragment;
    // 密码登录
    private ServeLoginFragment mServeLoginFragment;

    public LoginNavBarAdapter(FragmentManager fm, ArrayList<String> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (mServeLoginFragment == null) mServeLoginFragment = new ServeLoginFragment();
                return mServeLoginFragment;
            case 1:
                if (mNoteLoginFragment == null)
                    mNoteLoginFragment = new NoteLoginFragment();
                return mNoteLoginFragment;
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
