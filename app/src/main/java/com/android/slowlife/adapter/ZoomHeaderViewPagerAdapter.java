package com.android.slowlife.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.slowlife.R;

/**
 * Created by Administrator on 2017/2/4 .
 */

public class ZoomHeaderViewPagerAdapter extends RecyclerView.Adapter<ZoomHeaderViewPagerAdapter.ViewHolder> {
    private final static int TYPE_ADDRESS = 0;
    private final static int TYPE_TIME = 1;
    private final static int TYPE_RECOMMEND = 2;
    private final static int TYPE_COMMENT = 3;
    private final static int TYPE_IMAGE=4;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_ADDRESS:
                return new ViewHolder(inflater.inflate(R.layout.item_appraise3, parent, false));
            case TYPE_TIME:
                return new ViewHolder(inflater.inflate(R.layout.item_appraise2, parent, false));
            case TYPE_RECOMMEND:
                return new ViewHolder(inflater.inflate(R.layout.item_appraise4, parent, false));
            case TYPE_COMMENT:
                return new ViewHolder(inflater.inflate(R.layout.item_aooraise6, parent, false));
            case TYPE_IMAGE:
                return new ViewHolder(inflater.inflate(R.layout.item_appraise5, parent, false));
        }
        return new ViewHolder(inflater.inflate(R.layout.item_appraise5, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (position) {
            case 0:
                return TYPE_ADDRESS;
            case 1:
                return TYPE_TIME;
            case 2:
                return TYPE_RECOMMEND;
            case 3:
                return TYPE_COMMENT;
            case 4:
                return TYPE_IMAGE;
        }

        return TYPE_IMAGE;
    }
}
