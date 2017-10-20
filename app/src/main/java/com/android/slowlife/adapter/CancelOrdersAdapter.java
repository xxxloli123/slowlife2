package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.CancelOrdersEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/2/6 0006.
 */

public class CancelOrdersAdapter extends BaseAdapter {
    private List<CancelOrdersEntity> list;
    private Context mContext;
    private LayoutInflater inflater;

    public CancelOrdersAdapter(Context mContext, List<CancelOrdersEntity> list) {
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
            convertView = inflater.inflate(R.layout.item_cancel_orders, null);
            holder = new ViewHolder();
            holder.productName= (TextView) convertView.findViewById(R.id.product_name);
            holder.priceText= (TextView) convertView.findViewById(R.id.price_text);
            holder.productNumberText= (TextView) convertView.findViewById(R.id.product_number_text);
            holder.productImage= (ImageView) convertView.findViewById(R.id.product_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.productName.setText(list.get(position).getName());
        holder.priceText.setText("￥"+list.get(position).getPrice()+"");
        holder.productNumberText.setText("×"+list.get(position).getNum());
        return convertView;
    }

    class ViewHolder {
        private ImageView productImage;
        private TextView productName;
        private TextView priceText;
        private TextView productNumberText;
    }
}
