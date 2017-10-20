package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/6 0006.
 */

public class OrderTailAfterAdapter extends BaseAdapter {
    private List<String> list;
    private Context mContext;
    private LayoutInflater inflater;

    public OrderTailAfterAdapter(Context mContext, List<String> list) {
        this.list = list;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_order_tail_after, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.orderStateText.setText(list.get(position));
        if (position == 0) {
            holder.image1.setVisibility(View.VISIBLE);
            holder.image2.setVisibility(View.GONE);
            holder.orderStateText.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.view1.setVisibility(View.GONE);
            holder.orderReasonText.setVisibility(View.VISIBLE);
        } else {
            holder.image1.setVisibility(View.GONE);
            holder.image2.setVisibility(View.VISIBLE);
            holder.orderStateText.setTextColor(mContext.getResources().getColor(R.color.text_color));
            holder.view1.setVisibility(View.VISIBLE);
            holder.orderReasonText.setVisibility(View.GONE);
        }
        if (position == list.size() - 1) {
            holder.view2.setVisibility(View.GONE);
        } else {
            holder.view2.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.view_1)
        View view1;
        @Bind(R.id.image_1)
        ImageView image1;
        @Bind(R.id.image_2)
        ImageView image2;
        @Bind(R.id.view_2)
        View view2;
        @Bind(R.id.layout1)
        LinearLayout layout1;
        @Bind(R.id.order_state_text)
        TextView orderStateText;
        @Bind(R.id.time_text)
        TextView timeText;
        @Bind(R.id.layout)
        RelativeLayout layout;
        @Bind(R.id.order_reason_text)
        TextView orderReasonText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
