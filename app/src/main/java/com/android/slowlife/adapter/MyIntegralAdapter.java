package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.OrderIntegralEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/24 0024.
 */

public class MyIntegralAdapter extends BaseAdapter{
    private Context mContext;
    private List<OrderIntegralEntity> list;
    private LayoutInflater inflater;
    public MyIntegralAdapter(Context mContext, List<OrderIntegralEntity> list){
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
            convertView=inflater.inflate(R.layout.item_my_integral, null);
            holder=new ViewHolder();
            holder.integral= (TextView) convertView.findViewById(R.id.integral_text);
            holder.time= (TextView) convertView.findViewById(R.id.integral_time);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.integral.setText(list.get(position).getIntegral());
        return convertView;
    }
    class ViewHolder{
        private TextView time;
        private TextView integral;
    }
}
