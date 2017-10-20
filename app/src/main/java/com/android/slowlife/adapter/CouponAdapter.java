package com.android.slowlife.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.activity.DetailActivity;
import com.android.slowlife.objectmodel.CouponEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/24 0024.
 */

public class CouponAdapter extends BaseAdapter{

    private Context mContext;
    private List<CouponEntity> list;
    private LayoutInflater inflater;
    public CouponAdapter(Context mContext, List<CouponEntity> list){
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
            convertView=inflater.inflate(R.layout.item_coupon, null);
            holder=new ViewHolder();
            holder.name= (TextView) convertView.findViewById(R.id.name_text);
            holder.availability= (TextView) convertView.findViewById(R.id.availability_text);
            holder.time= (TextView) convertView.findViewById(R.id.time_text);
            holder.address= (TextView) convertView.findViewById(R.id.address_text);
            holder.scope= (TextView) convertView.findViewById(R.id.scope_text);
            holder.price= (TextView) convertView.findViewById(R.id.price_text);
            holder.yes=convertView.findViewById(R.id.yes_view);
            holder.no=convertView.findViewById(R.id.no_view);
            holder.yes_use= (TextView) convertView.findViewById(R.id.yes_use_text);
            holder.no_use= (ImageView) convertView.findViewById(R.id.no_use_image);
            holder.is_use= (RelativeLayout) convertView.findViewById(R.id.is_use_layout);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getName());
        holder.availability.setText("满"+list.get(position).getAvailability()+"可用");
        holder.address.setText("仅用于"+list.get(position).getName()+"（"+list.get(position).getAvailability()+"）");
        holder.price.setText(list.get(position).getPrice());
        holder.scope.setText(list.get(position).getScope());
        if("1".equals(list.get(position).getIsUse())){
            holder.yes.setVisibility(View.VISIBLE);
            holder.no.setVisibility(View.GONE);
            holder.yes_use.setVisibility(View.VISIBLE);
            holder.no_use.setVisibility(View.GONE);
        }else{
            holder.yes.setVisibility(View.GONE);
            holder.no.setVisibility(View.VISIBLE);
            holder.yes_use.setVisibility(View.GONE);
            holder.no_use.setVisibility(View.VISIBLE);
        }
        holder.is_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, DetailActivity.class);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        private TextView name;
        private TextView availability;
        private TextView time;
        private TextView address;
        private TextView scope;
        private TextView price;
        private View yes,no;
        private TextView yes_use;
        private ImageView no_use;
        private RelativeLayout is_use;
    }
}
