package com.android.slowlife.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.R;
import com.android.slowlife.view.PageGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/19 .
 */
public class PageingGridViewAdapter2 extends PageGridView.PagingAdapter<PageingGridViewAdapter2.MyVH> implements PageGridView.OnItemClickListener {
    private List<String> mData = new ArrayList<>();
    private Context mContext;
    private int width;
    private int screenWidth;

    public PageingGridViewAdapter2(List<String> data, Context mContext) {
        this.mContext = mContext;
        this.mData.addAll(data);
        width = mContext.getResources().getDisplayMetrics().widthPixels / 4;
        screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_menu_icons, parent, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = width;
        params.width = width;
        view.setLayoutParams(params);
        return new MyVH(view);
    }

    private int[] iconUrl = new int[]{R.drawable.test41, R.drawable.test42, R.drawable.test43,
            R.drawable.test44, R.drawable.test45, R.drawable.test46, R.drawable.test47, R.drawable.test48,
            R.drawable.test41, R.drawable.test42, R.drawable.test43,
            R.drawable.test44, R.drawable.test45, R.drawable.test46, R.drawable.test47, R.drawable.test48,
            R.drawable.test41, R.drawable.test42, R.drawable.test43,
            R.drawable.test44, R.drawable.test45, R.drawable.test46, R.drawable.test47, R.drawable.test48};
    private String[] iconName = new String[]{"姐妹超市", "果蔬生禽", "房产", "生活帮手", "土货进城", "特色美食", "特价抢购", "跳蚤市场",
            "姐妹超市", "果蔬生禽", "房产", "生活帮手", "土货进城", "特色美食", "特价抢购", "跳蚤市场",
            "姐妹超市", "果蔬生禽", "房产", "生活帮手", "土货进城", "特色美食", "特价抢购", "跳蚤市场"};
    private int[] iconUrl2 = new int[]{R.drawable.test61, R.drawable.test62, R.drawable.test63,
            R.drawable.test64, R.drawable.test61, R.drawable.test62, R.drawable.test63,
            R.drawable.test64};

    @Override
    public void onBindViewHolder(MyVH holder, int position) {
//        if (TextUtils.isEmpty(mData.get(position))) {
//            holder.icon.setVisibility(View.GONE);
//        } else {
//            holder.icon.setVisibility(View.VISIBLE);
//        }

        holder.icon.setImageResource(iconUrl2[position]);
        holder.tv_title.setText(iconName[position]);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public List getData() {
        return mData;
    }

    @Override
    public Object getEmpty() {
        return "";
    }

    @Override
    public void onItemClick(PageGridView pageGridView, int position) {
        String gridview = "";
        if (pageGridView == pageGridView) {
            gridview = "第一个GridView";
        }
        Toast.makeText(mContext, gridview + " 第" + (position + 1) + "个item 被点击" + " 值：" + mData.get(position), Toast.LENGTH_SHORT).show();
    }

    public static class MyVH extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public ImageView icon;

        public MyVH(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.iconName);
            icon = (ImageView) itemView.findViewById(R.id.iconUrl);
        }
    }

    int scrollX = 0;
    boolean isAuto = false;
    int Target = 0;

    public class MyScrollListener extends RecyclerView.OnScrollListener {


        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            Log.i("zzz", "onScrollStateChanged state=" + newState + " isAuto=" + isAuto);
            // recyclerView.smoothScrollBy(10,0);
            if (newState == 0) {
                if (!isAuto) {
                    int p = scrollX / screenWidth;
                    int offset = scrollX % screenWidth;
                    if (offset > screenWidth / 2) {
                        p++;
                    }
                    Target = p * screenWidth;
                    isAuto = true;
                    recyclerView.smoothScrollBy(Target - scrollX, 0);
                }
            } else if (newState == 2) {
                isAuto = false;
            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            scrollX += dx;
            Log.i("zzz", "onScrolled dx=" + dx + " scrollX=" + scrollX);
        }
    }
}