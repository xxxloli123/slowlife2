package com.android.slowlife.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.activity.SearchOrderActivity;
import com.android.slowlife.bean.OrderStatus;
import com.android.slowlife.objectmodel.OrdersEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/2/5 0005.
 */

public class OrdersAdapter extends BaseAdapter {
    protected boolean showStatus;
    protected List<OrdersEntity> list;
    protected LayoutInflater inflater;
    protected boolean showCb;

    public OrdersAdapter(Context mContext, List<OrdersEntity> list) {
        this(mContext, list, false);
    }

    public OrdersAdapter(Context context, List<OrdersEntity> list, boolean showCheckbox) {
        this(context, list, showCheckbox, true);
    }

    public OrdersAdapter(Context context, List<OrdersEntity> list, boolean showCheckbox, boolean showStatus) {
        this.list = list;
        inflater = LayoutInflater.from(context);
        showCb = showCheckbox;
        this.showStatus = showStatus;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public OrdersEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_orders, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name_text);
            holder.time = (TextView) convertView.findViewById(R.id.time_text);
            holder.deliveryTime= (TextView) convertView.findViewById(R.id.delivery_time_text);
            holder.shopNumber = (TextView) convertView.findViewById(R.id.shop_number_text);
            holder.orderState = (TextView) convertView.findViewById(R.id.order_state);
            holder.checkBox = (FrameLayout) convertView.findViewById(R.id.checkbox_layout);
            holder.address = (TextView) convertView.findViewById(R.id.address_text);
            holder.tel = (TextView) convertView.findViewById(R.id.tel);
            if (showCb) holder.checkBox.setVisibility(View.VISIBLE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OrdersEntity oe = list.get(position);
        holder.name.setText(oe.getReceiverName());
        holder.tel.setVisibility(View.GONE);
        holder.tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "";
                if (oe.getOrderStatus().equals(OrderStatus.UnReceivedOrder)) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:02372721515"));
                    v.getContext().startActivity(intent);
                } else if (oe.getOrderStatus().equals(OrderStatus.ReceivedOrder)) {
                    phone = oe.getPostmanPhone();
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                    v.getContext().startActivity(intent);
                } else if (oe.getOrderStatus().equals(OrderStatus.UnPayed)) {
                    phone = oe.getPostmanPhone();
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                    v.getContext().startActivity(intent);
                }
            }
        });
        if (showStatus) {
            holder.orderState.setText(oe.getStatus_value());
            if (oe.getOrderStatus().equals(OrderStatus.UnReceivedOrder)) {
                holder.orderState.setTextColor(Color.RED);
                showTel(holder, "联系公司");
            } else if (oe.getOrderStatus().equals(OrderStatus.ReceivedOrder)) {
                showTel(holder, "联系快递员");
                holder.orderState.setTextColor(Color.BLUE);
            } else if (oe.getOrderStatus().equals(OrderStatus.GoodsDelivery)) {
                //showTel(holder, "物流信息");
                holder.orderState.setTextColor(Color.GREEN);
            } else if (oe.getOrderStatus().equals(OrderStatus.UnPayed)) {
                holder.orderState.setTextColor(Color.parseColor("#ff8040"));
                showTel(holder, "联系快递员");
            } else if (oe.getOrderStatus().equals(OrderStatus.Complete)) {
                holder.orderState.setTextColor(Color.BLACK);
                if (oe.getType().equals("Intercity")) {
                    showTel(holder, "物流信息");
                    holder.tel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), SearchOrderActivity.class);
                            intent.putExtra(SearchOrderActivity.ORDERNUM, oe.getLogisticsNumber());
                            intent.putExtra(SearchOrderActivity.COMPANY, oe.getUserChoiceCommpanyName());
                            v.getContext().startActivity(intent);
                        }
                    });
                } else holder.tel.setVisibility(View.GONE);
            } else {
                holder.tel.setVisibility(View.GONE);
                holder.orderState.setTextColor(Color.BLACK);
            }
        } else {
            if (holder.orderState.getVisibility() == View.VISIBLE)
                holder.orderState.setVisibility(View.GONE);
            if (holder.tel.getVisibility() == View.VISIBLE)
                holder.tel.setVisibility(View.GONE);
        }
        holder.time.setText(oe.getCreateDate());
        holder.deliveryTime.setText(oe.getPostmanDate());
//        if (oe.getPostmanDate()==null){
//            holder.time.setText(oe.getCreateDate());
//        }else
            holder.shopNumber.setText(oe.getOrderNumber());
        holder.address.setText(String.format("%s%s%s%s%s", oe.getEndPro(), oe.getEndCity(),
                oe.getEndDistrict(), oe.getEndStreet(), oe.getEndHouseNumber()).replace("null",""));
        if (TextUtils.isEmpty(holder.address.getText()))
            holder.address.setText(String.format("%s%s%s%s%s", oe.getStartPro(), oe.getStartCity(),
                    oe.getStartDistrict(), oe.getStartStreet(), oe.getStartHouseNumber()).replace("null",""));
        return convertView;
    }

    public void notifyDataSetChanged(List<OrdersEntity> list) {
        this.list = list;
        super.notifyDataSetChanged();
    }

    private void showTel(ViewHolder holder, String txt) {
        if (holder.tel.getVisibility() != View.VISIBLE)
            holder.tel.setVisibility(View.VISIBLE);
        holder.tel.setText(txt);
    }

    class ViewHolder {
        FrameLayout checkBox;
        TextView address;
        TextView name;
        TextView time;
        TextView deliveryTime;
        TextView orderState;
        TextView shopNumber;
        TextView tel;
    }
}
