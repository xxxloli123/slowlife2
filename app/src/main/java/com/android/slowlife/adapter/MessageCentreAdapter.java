package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.MessgeEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/25 0025.
 */

public class MessageCentreAdapter extends BaseAdapter {
    private List<MessgeEntity> list;
    private LayoutInflater inflater;

    public MessageCentreAdapter(Context mContext, List<MessgeEntity> list) {
        this.list = list;
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
            convertView = inflater.inflate(R.layout.item_message_centre, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.message = (TextView) convertView.findViewById(R.id.order);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).getMessagecontent());
        holder.message.setText(list.get(position).getMessagetitle());
        holder.time.setText(list.get(position).getCreateDate());
        return convertView;
    }

    public void notifyDataSetChanged(List<MessgeEntity> msgs) {
        this.list = msgs;
        notifyDataSetChanged();
    }

    class ViewHolder {
        private ImageView image;
        private TextView message;
        private TextView name;
        private TextView time;
    }
}
