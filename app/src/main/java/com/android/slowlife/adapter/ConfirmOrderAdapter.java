package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.ConfirmOrderEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

public class ConfirmOrderAdapter extends BaseAdapter {
    private List<ConfirmOrderEntity> list;
    private Context mContext;
    private LayoutInflater inflater;

    public ConfirmOrderAdapter(Context mContext, List<ConfirmOrderEntity> list) {
        this.mContext = mContext;
        this.list = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list == null || list.size() == 0 ? 0 : list.size();
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
        ViewHolder holder=null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_confirm_order_commodity, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.introduce.setText(list.get(position).getName());
        holder.price.setText("￥"+list.get(position).getPrice());
        holder.number.setText("×"+list.get(position).getNumber());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.introduce)
        TextView introduce;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.number)
        TextView number;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
