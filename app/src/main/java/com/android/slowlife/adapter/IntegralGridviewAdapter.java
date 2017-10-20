package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.ConvertRecommendEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/23 0023.
 */

public class IntegralGridviewAdapter extends BaseAdapter{
    private List<ConvertRecommendEntity> list;
    private Context mContext;
    private LayoutInflater inflater;
    public IntegralGridviewAdapter(Context mContext, List<ConvertRecommendEntity> list){
        this.mContext=mContext;
        this.list=list;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return list == null || list.isEmpty() ? 0 : list.size();
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
            convertView = inflater.inflate(R.layout.item_convert_recommend_gridview, null);
            holder = new ViewHolder();
            holder.name= (TextView) convertView.findViewById(R.id.shop_name_text);
            holder.credits= (TextView) convertView.findViewById(R.id.credits_text);
            holder.shopPicture= (ImageView) convertView.findViewById(R.id.shop_picture_image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getName());
        holder.credits.setText(list.get(position).getCredits());
        holder.shopPicture.setImageResource(list.get(position).getIntegralUrl());
        return convertView;
    }

    class ViewHolder{
        private TextView name;
        private TextView credits;
        private ImageView shopPicture;
    }
}
