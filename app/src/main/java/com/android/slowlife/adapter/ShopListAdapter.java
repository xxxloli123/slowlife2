package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.ShopEntity;
import com.android.slowlife.util.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/20 .
 */
public class ShopListAdapter extends BaseAdapter {
    private List<ShopEntity> list;
    private Context mContext;
    private LayoutInflater inflater;
    private boolean isNoData;
    private int mHeight;
    public static final int ONE_SCREEN_COUNT = 8; // 一屏能显示的个数，这个根据屏幕高度和各自的需求定
    public static final int ONE_REQUEST_COUNT = 10; // 一次请求的个数

    public ShopListAdapter(List<ShopEntity> list, Context context) {
        this.list = list;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }
    public void setData(List<ShopEntity> list) {
        clearAll();
        addALL(list);

        isNoData = false;
        if (list.size() == 1) {
            // 暂无数据布局
            isNoData = list.get(0).isNoData();
            mHeight = list.get(0).getHeight();
        } else {
            // 添加空数据
            if (list.size() < ONE_SCREEN_COUNT) {
                addALL(createEmptyList(ONE_SCREEN_COUNT - list.size()));
            }
        }
        notifyDataSetChanged();
    }
    // 创建不满一屏的空数据
    public List<ShopEntity> createEmptyList(int size) {
        List<ShopEntity> emptyList = new ArrayList<>();
        if (size <= 0) return emptyList;
        for (int i=0; i<size; i++) {
            emptyList.add(new ShopEntity());
        }
        return emptyList;
    }
    public void clearAll() {
        list.clear();
    }

    public void addALL(List<ShopEntity> list){
        if(list==null||list.size()==0)
            return;
        list.addAll(list);
    }
    public void updateData(List<ShopEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private int[] iconUrl = new int[]{R.drawable.test9, R.drawable.test9, R.drawable.test9,
            R.drawable.test9, R.drawable.test9, R.drawable.test9};

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = inflater.inflate(R.layout.item_hotshop, null);
        ImageView shopIcon = ViewHolder.getView(view, R.id.image_icon);
        TextView shopName = ViewHolder.getView(view, R.id.txt_name);
        shopIcon.setImageResource(iconUrl[i]);
        shopName.setText(list.get(i).shopName + "");
        return view;
    }
}
