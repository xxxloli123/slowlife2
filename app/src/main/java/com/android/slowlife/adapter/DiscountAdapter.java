package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.DiscountEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7 0007.
 */

public class DiscountAdapter extends BaseAdapter {
    private List<DiscountEntity> list;
    private Context mContext;
    private LayoutInflater inflater;

    public DiscountAdapter(Context mContext, List<DiscountEntity> list) {
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
            convertView = inflater.inflate(R.layout.item_discount, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getName());
        holder.deduction.setText("-￥"+list.get(position).getPrice());
        if("0".equals(list.get(position).getTag())){
            holder.text.setText("红");
            holder.text.setBackgroundResource(R.drawable.login_corners_bgall);
        }else {
            holder.text.setText("特");
            holder.text.setBackgroundResource(R.drawable.orange_have_solid_bgall);
        }
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.text)
        TextView text;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.deduction)
        TextView deduction;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
