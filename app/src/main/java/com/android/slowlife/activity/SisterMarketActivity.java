package com.android.slowlife.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.SmoothListView.FilterView;
import com.android.slowlife.SmoothListView.HeaderFilterView;
import com.android.slowlife.SmoothListView.SisterView;
import com.android.slowlife.SmoothListView.SmoothListView;
import com.android.slowlife.adapter.ShopListAdapter;
import com.android.slowlife.model.FilterData;
import com.android.slowlife.model.FilterEntity;
import com.android.slowlife.model.FilterTwoEntity;
import com.android.slowlife.objectmodel.ShopEntity;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.DensityUtil;
import com.android.slowlife.util.ModelUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/1/20 .
 */
public class SisterMarketActivity extends BaseActivity implements SmoothListView.ISmoothListViewListener{
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    RecyclerView convertRv;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.search)
    ImageView search;
    @Bind(R.id.search_rl)
    RelativeLayout searchRl;
    @Bind(R.id.top_layout)
    RelativeLayout topLayout;
    @Bind(R.id.listView)
    SmoothListView listView;
    @Bind(R.id.real_filterView)
    FilterView realFilterView;
    private Context mContext;
    private Activity mActivity;
    private int mScreenHeight; // 屏幕高度

    private HeaderFilterView headerFilterView; // 分类筛选视图
    private FilterData filterData; // 筛选数据

    private int titleViewHeight = 0; // 标题栏的高度

    private View itemHeaderFilterView; // 从ListView获取的筛选子View
    private int filterViewPosition = 4; // 筛选视图的位置
    private int filterViewTopMargin; // 筛选视图距离顶部的距离
    private boolean isScrollIdle = true; // ListView是否在滑动
    private boolean isStickyTop = false; // 是否吸附在顶部
    private boolean isSmooth = false; // 没有吸附的前提下，是否在滑动
    private int filterPosition = -1; // 点击FilterView的位置：分类(0)、排序(1)、筛选(2)



    private SisterView sisterView;
    private List<String> sisterList = new ArrayList<>();

    private ShopListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.layout_sistermarket);
        ButterKnife.bind(this);
        String titleName=getIntent().getStringExtra("name");
        title.setText(titleName);
        initData();
        initView();
        initListener();
    }

    private void initData() {
        mContext = this;
        mActivity = this;
        mScreenHeight = DensityUtil.getWindowHeight(this);

        // 筛选数据
        filterData = new FilterData();
        filterData.setCategory(ModelUtil.getCategoryData());
        filterData.setSorts(ModelUtil.getSortData());
        filterData.setFilters(ModelUtil.getFilterData());
        String str[] = {"首饰", "户外健身", "女装", "男装", "童装", "头饰", "水晶鞋"};
        for (int i = 0; i < str.length; i++) {
            sisterList.add(str[i]);
        }
    }

    private void initView() {
        // 设置单行滑动数据
        sisterView = new SisterView(this);
        sisterView.fillView(sisterList, listView);

        // 设置假FilterView数据
        headerFilterView = new HeaderFilterView(this);
        headerFilterView.fillView(new Object(), listView);

        // 设置真FilterView数据
        realFilterView.setFilterData(mActivity, filterData);
        realFilterView.setVisibility(View.GONE);

        // 设置ListView数据
        final List<ShopEntity> hotList = getShopList();
        adapter = new ShopListAdapter(hotList, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(SisterMarketActivity.this, DetailActivity.class));
            }
        });

        filterViewPosition = listView.getHeaderViewsCount() - 1;
    }
    // 填充数据
    private void fillAdapter(List<ShopEntity> list) {
        if (list == null || list.size() == 0) {
            int height = mScreenHeight - DensityUtil.dip2px(mContext, 0); // 95 = 标题栏高度 ＋ FilterView的高度
            adapter.setData(ModelUtil.getNoDataEntity(height));
        } else {
            adapter.setData(list);
        }
    }
    private void initListener() {
        // (假的ListView头部展示的)筛选视图点击
        headerFilterView.getFilterView().setOnFilterClickListener(new FilterView.OnFilterClickListener() {
            @Override
            public void onFilterClick(int position) {
                filterPosition = position;
                isSmooth = true;
                listView.smoothScrollToPositionFromTop(filterViewPosition, DensityUtil.dip2px(mContext, 0));
            }
        });

        // (真正的)筛选视图点击
        realFilterView.setOnFilterClickListener(new FilterView.OnFilterClickListener() {
            @Override
            public void onFilterClick(int position) {
                filterPosition = position;
                realFilterView.show(position);
                listView.smoothScrollToPositionFromTop(filterViewPosition, DensityUtil.dip2px(mContext, 0));
            }
        });

        // 分类Item点击
        realFilterView.setOnItemCategoryClickListener(new FilterView.OnItemCategoryClickListener() {
            @Override
            public void onItemCategoryClick(FilterTwoEntity leftEntity, FilterEntity rightEntity) {
                //fillAdapter(ModelUtil.getCategoryTravelingData(leftEntity, rightEntity));
            }
        });

        // 排序Item点击
        realFilterView.setOnItemSortClickListener(new FilterView.OnItemSortClickListener() {
            @Override
            public void onItemSortClick(FilterEntity entity) {
                //fillAdapter(ModelUtil.getSortTravelingData(entity));
            }
        });

        // 筛选Item点击
        realFilterView.setOnItemFilterClickListener(new FilterView.OnItemFilterClickListener() {
            @Override
            public void onItemFilterClick(FilterEntity entity) {
                //fillAdapter(ModelUtil.getFilterTravelingData(entity));
            }
        });

        listView.setRefreshEnable(true);
        listView.setLoadMoreEnable(true);
        listView.setSmoothListViewListener(this);
        listView.setOnScrollListener(new SmoothListView.OnSmoothScrollListener() {
            @Override
            public void onSmoothScrolling(View view) {
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isScrollIdle = (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 获取筛选View、距离顶部的高度
                if (itemHeaderFilterView == null) {
                    itemHeaderFilterView = listView.getChildAt(filterViewPosition - firstVisibleItem);
                }
                if (itemHeaderFilterView != null) {
                    filterViewTopMargin = DensityUtil.px2dip(mContext, itemHeaderFilterView.getTop());
                }

                // 处理筛选是否吸附在顶部
                if (filterViewTopMargin <= titleViewHeight || firstVisibleItem > filterViewPosition) {
                    isStickyTop = true; // 吸附在顶部
                    realFilterView.setVisibility(View.VISIBLE);
                } else {
                    isStickyTop = false; // 没有吸附在顶部
                    realFilterView.setVisibility(View.GONE);
                }

                if (isSmooth && isStickyTop) {
                    isSmooth = false;
                    realFilterView.show(filterPosition);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (realFilterView.isShowing()) {
            realFilterView.resetAllStatus();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.stopRefresh();
                listView.setRefreshTime("刚刚");}
      }, 2000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.stopLoadMore();
            }
        }, 2000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * listview测试数据
     */
    private List<ShopEntity> getShopList() {

        List<ShopEntity> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ShopEntity shopEntity = new ShopEntity();
            shopEntity.shopName = "王家铺子" + i;
            list.add(shopEntity);
        }
        return list;
    }


    @OnClick({R.id.back_rl, R.id.search_rl})
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.search_rl:
                break;
        }
    }
}
