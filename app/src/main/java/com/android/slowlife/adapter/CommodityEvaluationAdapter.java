package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.view.RatingBar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/15 0015.
 */

public class CommodityEvaluationAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<String> list;

    public CommodityEvaluationAdapter(Context mContext, List<String> list) {
        this.mContext = mContext;
        this.list = list;
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
            convertView = inflater.inflate(R.layout.item_commodity_evaluation, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position));
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.head)
        ImageView head;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.ratingBar)
        RatingBar ratingBar;
        @Bind(R.id.minutes)
        TextView minutes;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.comment)
        TextView comment;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
