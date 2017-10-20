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
import com.android.slowlife.objectmodel.MerchantEntity;
import com.android.slowlife.view.RatingBar;
import com.android.slowlife.view.SlideView;

import java.util.List;

/**
 * Created by Administrator on 2017/1/22 0022.
 */

public class MerchantCollectAdapter extends BaseAdapter{
    public static SlideView itemDelete = null;
    private Context context;
    private List<MerchantEntity> list;
    private LayoutInflater inflater;
    private ViewHolder holder;
    private OnSlideClickListener mListener;

    public MerchantCollectAdapter(Context context, List<MerchantEntity> list, OnSlideClickListener listener){
        this.context=context;
        this.list=list;
        inflater = LayoutInflater.from(context);
        this.mListener=listener;
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_merchant_collect, null);
            holder = new ViewHolder();
            holder.head = (ImageView) convertView.findViewById(R.id.merchant_head_image);
            holder.name = (TextView) convertView.findViewById(R.id.merchant_name_text);
            holder.distance = (TextView) convertView.findViewById(R.id.distance_text);
            holder.address = (TextView) convertView.findViewById(R.id.merchant_address_text);
            holder.grade = (RatingBar) convertView.findViewById(R.id.product_ratingBar);
            holder.monthlySalesAmount = (TextView) convertView.findViewById(R.id.merchant_sale_text);
            holder.priceSending = (TextView) convertView.findViewById(R.id.upto_amount_text);
            holder.deliveryCost = (TextView) convertView.findViewById(R.id.delivery_cost_address);
            holder.time= (TextView) convertView.findViewById(R.id.minute_address);
            holder.delete= (TextView) convertView.findViewById(R.id.delete);
            holder.slideView= (LinearLayout) convertView.findViewById(R.id.slideview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getName());
//        holder.distance.setText(list.get(position).getDistance());
//        holder.address.setText(list.get(position).getAddress());
//        holder.deliveryCost.setText(list.get(position).getDeliveryCost());
//        holder.monthlySalesAmount.setText(list.get(position).getMonthlySalesAmount());
//        holder.priceSending.setText(list.get(position).getPriceSending());
//        holder.time.setText(list.get(position).getTime()+"");
        if(mListener != null){
            final int pos = position;
            final View item = convertView;
            holder.delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mListener.onDeleteClick(pos, item);
                }
            });
            holder.slideView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(pos, item);
                }
            });
        }
        return convertView;
    }
     class ViewHolder {
        private ImageView head;
        private TextView name;
        private TextView address;
        private RatingBar grade;
        private TextView monthlySalesAmount;
        private TextView priceSending;
        private TextView deliveryCost;
        private TextView distance;
        private TextView time;
        private ViewGroup deleteHolder;
        private TextView delete;
         private LinearLayout slideView;
    }

    public static void ItemDeleteReset() {
        if (itemDelete != null) {
            itemDelete.reSet();
        }
    }
    public interface OnSlideClickListener{
        public void onItemClick(int position, View item);
        public void onDeleteClick(int position, View item);
    }
}

