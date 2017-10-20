package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.view.RatingBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/2 .
 */

public class AppraiseListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private TradeNameAdapter adapter;
    private Context context;

    public AppraiseListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_appraise1, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        adapter = new TradeNameAdapter(context, getGridview());
        holder.gridView.setAdapter(adapter);
        return convertView;
    }

    /**
     * gridview添加数据
     */
    private List<String> getGridview() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 2; i++) {
            list.add("心心相印抽纸" + i);
        }
        return list;
    }

    static class ViewHolder {
        @Bind(R.id.image_icon_head)
        ImageView imageIconHead;
        @Bind(R.id.txt_name)
        TextView txtName;
        @Bind(R.id.rate_appraise_score)
        RatingBar rateAppraiseScore;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_appraise)
        TextView tvAppraise;
        @Bind(R.id.assist)
        ImageView assist;
        @Bind(R.id.gridView)
        GridView gridView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
