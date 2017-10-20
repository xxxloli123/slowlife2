package com.android.slowlife.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.activity.ConfirmOrderActivity;
import com.android.slowlife.activity.EditAddressActivity;
import com.android.slowlife.activity.SelectReceiptAddressActivity;
import com.android.slowlife.objectmodel.AddressEntity;
import com.android.slowlife.view.MyRadioButton;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

public class SelectReceiptAddressAdapter extends BaseAdapter {
    private List<AddressEntity> list;
    private Context mContext;
    private LayoutInflater inflater;
    private int temp = -6;

    public SelectReceiptAddressAdapter(Context mContext, List<AddressEntity> list) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_select_receipt_address, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameText.setText(list.get(position).getPersonname());
        holder.phoneText.setText(list.get(position).getPersonphone());
        holder.addressText.setText(list.get(position).getHouseNumber());
        holder.editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditAddressActivity.class);
                ((SelectReceiptAddressActivity) mContext).startActivityForResult(intent, 1);
            }
        });
        holder.radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    temp = position;
                    notifyDataSetChanged();
                    Intent intent = new Intent(mContext, ConfirmOrderActivity.class);
                    intent.putExtra("name", list.get(position).getPersonname());
                    intent.putExtra("phone", list.get(position).getPersonphone());
                    intent.putExtra("address", list.get(position).getHouseNumber());
                    // 转化为activity，然后finish就行了
                    Activity activity = (Activity) mContext;
                    activity.setResult(2, intent);
                    activity.finish();
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
        @Bind(R.id.name_text)
        TextView nameText;
        @Bind(R.id.call_text)
        TextView callText;
        @Bind(R.id.phone_text)
        TextView phoneText;
        @Bind(R.id.tag_text)
        TextView tagText;
        @Bind(R.id.address_text)
        TextView addressText;
        @Bind(R.id.edit_layout)
        LinearLayout editLayout;
        @Bind(R.id.radio)
        MyRadioButton radio;
        @Bind(R.id.layout)
        LinearLayout layout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
