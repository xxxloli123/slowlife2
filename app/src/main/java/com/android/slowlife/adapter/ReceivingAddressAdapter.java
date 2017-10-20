package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.AddressEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/11 0011.
 */

public class ReceivingAddressAdapter extends BaseAdapter {
    private List<AddressEntity> list;
    private Context mContext;
    private LayoutInflater inflater;

    public ReceivingAddressAdapter(Context mContext, List<AddressEntity> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_receiving_address, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameText.setText(list.get(position).getPersonname());
        holder.phoneText.setText(list.get(position).getPersonphone());
        holder.addressText.setText(list.get(position).getHouseNumber());
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
