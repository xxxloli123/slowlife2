package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.slowlife.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/1 0001.
 */

public class PreferenceAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] iconName = new String[]{"姐妹超市", "果蔬生禽", "房产", "生活帮手", "土货进城", "特色美食", "特价抢购", "跳蚤市场"};
    private int[] iconUrl2 = new int[]{R.drawable.test61, R.drawable.test62, R.drawable.test63,
            R.drawable.test64, R.drawable.test61, R.drawable.test62, R.drawable.test63,
            R.drawable.test64};

    public PreferenceAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return iconUrl2 == null || iconUrl2.length <= 0 ? 0 : iconUrl2.length;
    }

    @Override
    public Object getItem(int position) {
        return iconUrl2[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_menu_icons, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.iconUrl.setImageResource(iconUrl2[position]);
        holder.iconName.setText(iconName[position]);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iconUrl)
        ImageView iconUrl;
        @Bind(R.id.iconName)
        TextView iconName;
        @Bind(R.id.layout_grid)
        LinearLayout layoutGrid;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
