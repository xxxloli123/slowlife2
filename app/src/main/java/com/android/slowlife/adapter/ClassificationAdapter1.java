package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.ClassificationEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public class ClassificationAdapter1 extends BaseAdapter {
    private List<ClassificationEntity> list;
    private Context mContext;
    private LayoutInflater inflater;
    private int checkItemPosition = -1;

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    public ClassificationAdapter1(Context mContext, List<ClassificationEntity> list) {
        this.mContext = mContext;
        this.list = list;
        inflater = LayoutInflater.from(mContext);
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_classification_listview1, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getName());
        holder.number.setText(list.get(position).getNumber() + "");
        if (checkItemPosition == position) {
            holder.name.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.number.setTextColor(mContext.getResources().getColor(R.color.red));
        }else {
            holder.name.setTextColor(mContext.getResources().getColor(R.color.text_color));
            holder.number.setTextColor(mContext.getResources().getColor(R.color.hint_text_color));
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.number)
        TextView number;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
