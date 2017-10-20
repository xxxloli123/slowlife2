package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.activity.ConfirmOrderActivity;
import com.android.slowlife.objectmodel.ServiceTimeEntity;
import com.android.slowlife.view.MyRadioButton;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

public class PopwindowsConfirmOrderAdapter extends BaseAdapter {
    private List<ServiceTimeEntity> list;
    private Context mContext;
    private LayoutInflater inflater;
    private int temp = -6;

    public PopwindowsConfirmOrderAdapter(Context mContext, List<ServiceTimeEntity> list) {
        this.list = list;
        this.mContext = mContext;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_popwindows_confirm_order, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.date.setText(list.get(position).getDate());
        holder.time.setText(list.get(position).getTime());
        holder.distributionCost.setText("("+list.get(position).getDistribution_cost()+"元配送费)");
        holder.radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    temp = position;
                    notifyDataSetChanged();
                    if(position==0){
                        ConfirmOrderActivity.time.setText("尽快送达|预计"+list.get(position).getTime());
                    }else{
                        ConfirmOrderActivity.time.setText(list.get(position).getDate()+list.get(position).getTime());
                    }
                }
            }
        });
        if (temp == position) {// 选中的条目和当前的条目是否相等
            holder.radio.setChecked(true);
        } else {
            holder.radio.setChecked(false);
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.date)
        TextView date;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.distribution_cost)
        TextView distributionCost;
        @Bind(R.id.radio)
        MyRadioButton radio;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
