package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.CouponCentreEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/24 0024.
 */

public class CouponCentreAdapter extends BaseAdapter{
    private Context mContext;
    private List<CouponCentreEntity> list;
    private LayoutInflater inflater;
    public CouponCentreAdapter(Context mContext, List<CouponCentreEntity> list){
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
            convertView=inflater.inflate(R.layout.item_coupon_centre, null);
            holder=new ViewHolder();
            holder.image= (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    class ViewHolder{
        private ImageView image;
    }
}
