package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.UseRedPacketEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/25 0025.
 */

public class ConvertRedPacketAdapter extends BaseAdapter {
    private Context mContext;
    private List<UseRedPacketEntity> list;
    private LayoutInflater inflater;
    public ConvertRedPacketAdapter(Context mContext,List<UseRedPacketEntity> list){
        this.mContext=mContext;
        this.list=list;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
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
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_use_red_packet, null);
            holder=new ViewHolder();
            holder.availability= (TextView) convertView.findViewById(R.id.availability_text);
            holder.time= (TextView) convertView.findViewById(R.id.time_text);
            holder.price= (TextView) convertView.findViewById(R.id.price_text);
            holder.phone=(TextView) convertView.findViewById(R.id.phone_text);
            holder.yes_use= (TextView) convertView.findViewById(R.id.yes_use_text);
            holder.no_use= (ImageView) convertView.findViewById(R.id.no_use_image);
            holder.is_use= (LinearLayout) convertView.findViewById(R.id.is_use_layout);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.availability.setText("满"+list.get(position).getAvailability()+"积分兑换");
        holder.price.setText(list.get(position).getPrice());
        holder.phone.setText("可在每个超市里使用");
        holder.yes_use.setText("立即兑换");
        return convertView;
    }
    class ViewHolder{
        private TextView availability;
        private TextView time;
        private TextView phone;
        private TextView price;
        private TextView yes_use;
        private ImageView no_use;
        private LinearLayout is_use;
        private TextView is_use_text;
    }
}
