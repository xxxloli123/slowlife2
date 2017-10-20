package com.android.slowlife.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.IconEntity;
import com.android.slowlife.view.RecyclerViewReboundAnimator;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19 .
 */
public class QuickMenuAdapter extends RecyclerView.Adapter {

    public List<IconEntity> list;
    private Context mContext;
    private RecyclerViewReboundAnimator mReboundAnimator;
    private int mColumn = 1;

    public QuickMenuAdapter(List<IconEntity> list, RecyclerView recyclerView) {
        this.list = list;
        mReboundAnimator = new RecyclerViewReboundAnimator(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager.getClass().equals(LinearLayoutManager.class)) {
            mColumn = 1;
        } else if (layoutManager.getClass().equals(GridLayoutManager.class)) {
            GridLayoutManager glm = (GridLayoutManager) layoutManager;
            mColumn = glm.getSpanCount();
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_icon2, parent, false);
        mReboundAnimator.onCreateViewHolder(viewGroup, mColumn);
        return new ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IconEntity item = list.get(position);
        mContext = holder.itemView.getContext().getApplicationContext();
        ViewHolder vh = (ViewHolder) holder;
//        Glide.with(mContext).load("file:///android_asset/" + item.iconUrl).into(vh.iv);
        vh.iv.setImageResource(item.iconUrl);
//        vh.tv.setText(item.iconName + "-" + item.standby1);
        vh.tv.setText(item.iconName);
        vh.price1.setText(item.price1);
        vh.price1.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
        vh.price.setText(item.standby1);
        mReboundAnimator.onBindViewHolder(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return list == null || list.isEmpty() ? 0 : list.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv;
        public TextView tv;
        public TextView price;
        public TextView price1;

        public ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iconUrl);
            tv = (TextView) itemView.findViewById(R.id.name);
            price= (TextView) itemView.findViewById(R.id.price);
            price1= (TextView) itemView.findViewById(R.id.price1);
        }
    }
}
