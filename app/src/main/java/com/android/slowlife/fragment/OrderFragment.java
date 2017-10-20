package com.android.slowlife.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.slowlife.R;
import com.android.slowlife.activity.LoginActivity;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.util.Common;
import com.android.slowlife.view.PagerSlidingTabStrip;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/1/19 .
 */
public class OrderFragment extends Fragment {
    protected View rootView;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.tabs)
    PagerSlidingTabStrip tabs;
    private boolean isFirst;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            if (getView() == null) {
                rootView = inflater.inflate(R.layout.frag_order, null, false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    (rootView.findViewById(R.id.ll)).setPadding(0, Common.getStatusHeight(getActivity()), 0, 0);
                }
            } else rootView = getView();
        }
        ButterKnife.bind(this, rootView);
        isFirst = getArguments().getBoolean("isFirst");
        if (getUserVisibleHint())
            loadData();
        return rootView;
    }

    private void loadData() {
        if (pagerAdapter == null) {
            pagerAdapter = new PagerAdapter(getChildFragmentManager());
            pager.setAdapter(pagerAdapter);
            tabs.setViewPager(pager);
        }
        Info info = ((MyApplication) rootView.getContext().getApplicationContext()).getInfo();
        if (info == null && !isFirst) {
            isFirst = true;
            getArguments().putBoolean("isFirst", isFirst);
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return;
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (rootView != null && isVisibleToUser) loadData();
    }

    public static OrderFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getArguments().putBoolean("isFirst", isFirst);
        ButterKnife.unbind(this);
    }

    private PagerAdapter pagerAdapter;

    class PagerAdapter extends FragmentPagerAdapter {
        private AllOrderFragment frags[] = new AllOrderFragment[2];
        String[] titles = new String[]{
                "同城快递",
                "区外快递"
        };

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            frags[0] = AllOrderFragment.newInstance("type", "CityWide");
            frags[1] = AllOrderFragment.newInstance("type", "Intercity");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return frags[position];
        }

        @Override
        public int getCount() {
            return frags.length;
        }

    }
}
