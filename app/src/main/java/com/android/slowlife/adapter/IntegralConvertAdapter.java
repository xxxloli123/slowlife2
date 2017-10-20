package com.android.slowlife.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.IntegralConverEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/23 0023.
 */

public class IntegralConvertAdapter extends RecyclerView.Adapter{
    private List<IntegralConverEntity> list;
    private Context mContext;
    //private RecyclerViewReboundAnimator mReboundAnimator;
    private int mColumn = 1;
    private MyItemClickListener mItemClickListener;

    public IntegralConvertAdapter(Context mContext,List<IntegralConverEntity> list,RecyclerView recyclerView){
        this.list=list;
        this.mContext=mContext;
        //mReboundAnimator = new RecyclerViewReboundAnimator(recyclerView);
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
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_integral_convert_rv, parent, false);
        //mReboundAnimator.onCreateViewHolder(viewGroup, mColumn);
        return new IntegralConvertAdapter.ViewHolder(viewGroup,mItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IntegralConverEntity item = list.get(position);
        mContext = holder.itemView.getContext().getApplicationContext();
        IntegralConvertAdapter.ViewHolder vh = (IntegralConvertAdapter.ViewHolder) holder;
//        Glide.with(mContext).load("file:///android_asset/" + item.iconUrl).into(vh.iv);
        vh.iv.setImageResource(item.getIntegralUrl());
        vh.tv.setText(item.getIntegralSection() + "元兑换");
        //mReboundAnimator.onBindViewHolder(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return list == null || list.isEmpty() ? 0 : list.size();
    }


    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private MyItemClickListener mListener;
        public ImageView iv;
        public TextView tv;

        public ViewHolder(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
            //将全局的监听赋值给接口
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
            iv = (ImageView) itemView.findViewById(R.id.integral_image);
            tv = (TextView) itemView.findViewById(R.id.integral_section_text);
        }
        /**
         * 实现OnClickListener接口重写的方法
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }

        }
    }
    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }
    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}
