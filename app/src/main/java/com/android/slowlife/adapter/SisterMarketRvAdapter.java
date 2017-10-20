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

import java.util.List;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public class SisterMarketRvAdapter extends RecyclerView.Adapter{
    private List<String> list;
    private Context mContext;
//    private RecyclerViewReboundAnimator mReboundAnimator;
    private int mColumn = 1;
    private IntegralConvertAdapter.MyItemClickListener mItemClickListener;

    public SisterMarketRvAdapter(Context mContext, List<String> list, RecyclerView recyclerView){
        this.list=list;
        this.mContext=mContext;
       // mReboundAnimator = new RecyclerViewReboundAnimator(recyclerView);
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
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sister_market_rv, parent, false);
        //mReboundAnimator.onCreateViewHolder(viewGroup, mColumn);
        return new ViewHolder(viewGroup,mItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mContext = holder.itemView.getContext().getApplicationContext();
        ViewHolder vh = (ViewHolder) holder;
//        Glide.with(mContext).load("file:///android_asset/" + item.iconUrl).into(vh.iv);
        vh.text.setText(list.get(position));
        //mReboundAnimator.onBindViewHolder(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return list == null || list.isEmpty() ? 0 : list.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private IntegralConvertAdapter.MyItemClickListener mListener;
        public ImageView image;
        public TextView text;

        public ViewHolder(View itemView,IntegralConvertAdapter.MyItemClickListener myItemClickListener) {
            super(itemView);
            //将全局的监听赋值给接口
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
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
    public void setItemClickListener(IntegralConvertAdapter.MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}
