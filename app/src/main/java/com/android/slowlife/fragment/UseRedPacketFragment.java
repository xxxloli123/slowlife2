package com.android.slowlife.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/1/25 0025.
 */
@SuppressLint("ValidFragment")
public class UseRedPacketFragment extends UseRedPkgFrag implements View.OnClickListener {
    private ListView listView;
    private LinearLayout no_red_packet;
    private SwipeRefreshLayout srl;

    public UseRedPacketFragment() {
    }

    public UseRedPacketFragment(String arg) {
        super(arg);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {

        }
    }

    @Override
    public Context getContext() {
        if (getView() != null) return getView().getContext();
        if (super.getContext() != null) return super.getContext();
        if (getActivity() != null) return getActivity();
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private Handler handler = new Handler();
}
