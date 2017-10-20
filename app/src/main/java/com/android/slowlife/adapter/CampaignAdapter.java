package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.CampaignEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/10 0010.
 */

public class CampaignAdapter extends BaseAdapter {
    private Context mContext;
    private List<CampaignEntity> list;
    private LayoutInflater inflater;

    public CampaignAdapter(Context mContext, List<CampaignEntity> list) {
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
            convertView = inflater.inflate(R.layout.item_campaign, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.text1.setText(list.get(position).getName());
        if("0".equals(list.get(position).getTag())){
            holder.text.setText("新");
            holder.text.setBackgroundResource(R.drawable.green_bgall);
        }else if("1".equals(list.get(position).getTag())){
            holder.text.setText("减");
            holder.text.setBackgroundResource(R.drawable.red1_bgall);
        }else{
            holder.text.setText("折");
            holder.text.setBackgroundResource(R.drawable.red1_bgall);
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.text)
        TextView text;
        @Bind(R.id.text1)
        TextView text1;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
