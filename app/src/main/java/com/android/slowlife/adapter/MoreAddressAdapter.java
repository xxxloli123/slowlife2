package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.MoreAddressEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/11 0011.
 */

public class MoreAddressAdapter extends BaseAdapter {
    private List<MoreAddressEntity> list;
    private Context mContext;
    private LayoutInflater inflater;

    public MoreAddressAdapter(Context mContext, List<MoreAddressEntity> list) {
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
        ViewHolder holder=null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_more_address, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getName());
        holder.address.setText(list.get(position).getAddress());
        if("0".equals(list.get(position).getTag())){
            holder.tag.setVisibility(View.VISIBLE);
            holder.address.setTextColor(mContext.getResources().getColor(R.color.text_color));
        }else {
            holder.tag.setVisibility(View.GONE);
            holder.address.setTextColor(mContext.getResources().getColor(R.color.hint_text_color));
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.tag)
        TextView tag;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.address)
        TextView address;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
