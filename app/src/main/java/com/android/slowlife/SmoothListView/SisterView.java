package com.android.slowlife.SmoothListView;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.android.slowlife.R;
import com.android.slowlife.activity.IntegralConvertActivity;
import com.android.slowlife.adapter.IntegralConvertAdapter;
import com.android.slowlife.adapter.SisterMarketRvAdapter;
import com.android.slowlife.view.SpacesItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/15 0015.
 */

public class SisterView extends AbsHeaderView<List<String>> {
    @Bind(R.id.convert_rv)
    RecyclerView convertRv;

    public SisterView(Activity activity) {
        super(activity);
    }

    @Override
    protected void getView(List<String> list, ListView listView) {
        View view = mInflate.inflate(R.layout.view_sister, listView, false);
        ButterKnife.bind(this, view);
        initLinearRv(list);
        listView.addHeaderView(view);
    }
    /**
     * 单行左右滑动列表
     */
    private void initLinearRv(final List<String> list) {
        convertRv.addItemDecoration(new SpacesItemDecoration(20));
//        String str[] = {"首饰", "户外健身", "女装", "男装", "童装", "头饰", "水晶鞋"};
//        final List<String> list = new ArrayList<String>();
//        for (int i = 0; i < str.length; i++) {
//            list.add(str[i]);
//        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        convertRv.setLayoutManager(linearLayoutManager);
        SisterMarketRvAdapter adapter = new SisterMarketRvAdapter(mActivity, list, convertRv);
        convertRv.setAdapter(adapter);
        adapter.setItemClickListener(new IntegralConvertAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mActivity, IntegralConvertActivity.class);
                intent.putExtra("title", list.get(position));
                mActivity.startActivity(intent);
            }
        });
    }
}
