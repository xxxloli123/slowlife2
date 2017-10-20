package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.IntegralConvertShopEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/31 0031.
 */

public class IntegralConvertShopAdapter extends BaseAdapter {
    private Context mContext;
    private List<IntegralConvertShopEntity> list;
    private LayoutInflater inflater;
    public IntegralConvertShopAdapter(Context mContext, List<IntegralConvertShopEntity> list){
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
            convertView=inflater.inflate(R.layout.item_integral_convert, null);
            holder=new ViewHolder();
            holder.name= (TextView) convertView.findViewById(R.id.name_text);
            holder.head= (ImageView) convertView.findViewById(R.id.head_image);
            holder.integral= (TextView) convertView.findViewById(R.id.integral_text);
            holder.convert= (TextView) convertView.findViewById(R.id.covert_text);
            holder.member= (ImageView) convertView.findViewById(R.id.member_image);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getName());
        holder.integral.setText(list.get(position).getIntegral()+"积分");
        if("1".equals(list.get(position).getTag())){
            holder.member.setVisibility(View.VISIBLE);
        }else{
            holder.member.setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder{
        private TextView name;
        private ImageView head,member;
        private TextView integral;
        private TextView convert;
    }
}
