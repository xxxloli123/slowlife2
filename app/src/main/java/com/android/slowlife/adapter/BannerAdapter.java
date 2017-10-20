package com.android.slowlife.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.slowlife.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * Created by ChenJian
 * 2016.11.29 14:18:18.
 */

public class BannerAdapter extends PagerAdapter {
    private Context mContext;
    private List<Map<String,Object>> urlDate;

    public BannerAdapter(Context context, List<Map<String,Object>> urlDate) {
        this.mContext = context;
        this.urlDate = urlDate;
    }

    @Override
    public int getCount() {
        if (urlDate == null || urlDate.size() < 1) {
            return 0;
        }

        // 这个demo的精髓在于下面这行代码。让PagerAdapter的长度为Integer.MAX_VALUE
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.padapter_main_banner, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_main_banner_content);

        // 此处必须将position对imgIds.length取余数，不然基本上会出现数组越界的错误
//        imageView.setImageResource(imgIds[position % imgIds.length]);
        Picasso.with(mContext).load(urlDate.get(position).get("url").toString()).into(imageView);
        container.addView(view);
        return view;
    }
}
