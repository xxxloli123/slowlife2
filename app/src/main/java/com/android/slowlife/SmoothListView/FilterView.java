package com.android.slowlife.SmoothListView;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.adapter.ClassificationAdapter;
import com.android.slowlife.adapter.ClassificationAdapter1;
import com.android.slowlife.model.FilterData;
import com.android.slowlife.model.FilterEntity;
import com.android.slowlife.model.FilterTwoEntity;
import com.android.slowlife.objectmodel.ClassificationEntity;
import com.android.slowlife.view.ConstellationAdapter;
import com.android.slowlife.view.ListDropDownAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 16/4/20.
 */
public class FilterView extends LinearLayout implements View.OnClickListener {

    @Bind(R.id.tv_category_title)
    TextView tvCategoryTitle;
    @Bind(R.id.iv_category_arrow)
    ImageView ivCategoryArrow;
    @Bind(R.id.ll_category)
    LinearLayout llCategory;
    @Bind(R.id.tv_sort_title)
    TextView tvSortTitle;
    @Bind(R.id.iv_sort_arrow)
    ImageView ivSortArrow;
    @Bind(R.id.ll_sort)
    LinearLayout llSort;
    @Bind(R.id.tv_filter_title)
    TextView tvFilterTitle;
    @Bind(R.id.iv_filter_arrow)
    ImageView ivFilterArrow;
    @Bind(R.id.ll_filter)
    LinearLayout llFilter;
    @Bind(R.id.ll_head_layout)
    LinearLayout llHeadLayout;
    @Bind(R.id.view_mask_bg)
    View viewMaskBg;
    @Bind(R.id.lv_left)
    ListView lvLeft;
    @Bind(R.id.lv_right)
    ListView lvRight;
    @Bind(R.id.ll_content_list_view)
    LinearLayout llContentListView;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.number)
    TextView number;
    @Bind(R.id.all_businesses)
    RelativeLayout allBusinesses;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.listview1)
    ListView listview1;
    @Bind(R.id.classification)
    LinearLayout classificationLayout;
    @Bind(R.id.constellation)
    GridView constellation;
    @Bind(R.id.constellation1)
    GridView constellation1;
    @Bind(R.id.empty)
    TextView empty;
    @Bind(R.id.ok)
    TextView ok;
    @Bind(R.id.customLayout)
    LinearLayout customLayout;

    private Context mContext;
    private LayoutInflater inflater;
    private Activity mActivity;

    private int filterPosition = -1;
    private int lastFilterPosition = -1;
    public static final int POSITION_CATEGORY = 0; // 分类的位置
    public static final int POSITION_SORT = 1; // 排序的位置
    public static final int POSITION_FILTER = 2; // 筛选的位置

    private boolean isShowing = false;
    private int panelHeight;
    private FilterData filterData;

    private ListDropDownAdapter ageAdapter;
    private ConstellationAdapter constellationAdapter;
    private ConstellationAdapter constellationAdapter1;

    private String classification[] = {"快餐便当", "特色菜系", "异国料理", "小吃夜宵", "甜品饮料"};
    private Integer icon[] = {R.drawable.b4, R.drawable.b5, R.drawable.b6, R.drawable.b7, R.drawable.b8};
    private String classifications[] = {"全部快餐便当", "简餐", "盖浇饭", "米粉面条", "香锅砂锅", "麻辣烫", "火锅", "盖饭", "炒饭"};
    private String sortText[] = {"智能排序", "距离最近", "销量最高", "人气最高"};
    private Integer sortIcon[] = {R.drawable.test71, R.drawable.test72, R.drawable.test73, R.drawable.test74};
    private String constellations[] = {"中通物流", "蜂鸟专送"};
    private Integer shipping[] = {R.drawable.b, R.drawable.b1};
    private String constellations1[] = {"新用户优惠", "特价秒杀", "下单立减", "正品优惠"};
    private Integer shipping1[] = {R.drawable.b, R.drawable.b2, R.drawable.b3, R.drawable.b1};


    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_filter_layout, this);
        ButterKnife.bind(this, view);

        initView();
        initListener();
    }

    private void initView() {
        viewMaskBg.setVisibility(GONE);
        llContentListView.setVisibility(GONE);
    }

    private void initListener() {
        llCategory.setOnClickListener(this);
        llSort.setOnClickListener(this);
        llFilter.setOnClickListener(this);
        viewMaskBg.setOnClickListener(this);
        llContentListView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_category:
                filterPosition = 0;
                if (onFilterClickListener != null) {
                    onFilterClickListener.onFilterClick(filterPosition);
                }
                break;
            case R.id.ll_sort:
                filterPosition = 1;
                if (onFilterClickListener != null) {
                    onFilterClickListener.onFilterClick(filterPosition);
                }
                break;
            case R.id.ll_filter:
                filterPosition = 2;
                if (onFilterClickListener != null) {
                    onFilterClickListener.onFilterClick(filterPosition);
                }
                break;
            case R.id.view_mask_bg:
                hide();
                break;
        }
    }

    // 复位筛选的显示状态
    public void resetViewStatus() {
        tvCategoryTitle.setTextColor(mContext.getResources().getColor(R.color.text_color));
        ivCategoryArrow.setImageResource(R.drawable.test77);

        tvSortTitle.setTextColor(mContext.getResources().getColor(R.color.text_color));
        ivSortArrow.setImageResource(R.drawable.test77);

        tvFilterTitle.setTextColor(mContext.getResources().getColor(R.color.text_color));
        ivFilterArrow.setImageResource(R.drawable.test77);
    }

    // 复位所有的状态
    public void resetAllStatus() {
        resetViewStatus();
        hide();
    }

    // 设置分类数据
    private void setCategoryAdapter() {
        lvLeft.setVisibility(GONE);
        lvRight.setVisibility(GONE);
        classificationLayout.setVisibility(VISIBLE);
        customLayout.setVisibility(GONE);
        final ClassificationAdapter adapter = new ClassificationAdapter(mContext, getList());
        final ClassificationAdapter1 adapter1 = new ClassificationAdapter1(mContext, getList1());
        listview.setAdapter(adapter);
        allBusinesses.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCategoryTitle.setText(name.getText().toString().trim());
                hide();
            }
        });
        //点击监听
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setCheckItem(position);
                listview1.setAdapter(adapter1);
            }
        });
        //选中监听
        listview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter.setCheckItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter1.setCheckItem(position);
                tvCategoryTitle.setText(classifications[position]);
                hide();
            }
        });
    }

    /**
     * 分类listview填充数据
     */
    private List<ClassificationEntity> getList() {
        List<ClassificationEntity> list = new ArrayList<ClassificationEntity>();
        ClassificationEntity entity = null;
        for (int i = 0; i < classification.length; i++) {
            entity = new ClassificationEntity();
            entity.setName(classification[i]);
            entity.setUrl(icon[i]);
            entity.setNumber(465);
            list.add(entity);
        }
        return list;
    }

    /**
     * 分类listview1测试数据
     */
    private List<ClassificationEntity> getList1() {
        List<ClassificationEntity> list = new ArrayList<ClassificationEntity>();
        ClassificationEntity entity = null;
        for (int i = 0; i < classifications.length; i++) {
            entity = new ClassificationEntity();
            entity.setName(classifications[i]);
            entity.setNumber(65);
            list.add(entity);
        }
        return list;
    }

    // 设置排序数据
    private void setSortAdapter() {
        lvLeft.setVisibility(GONE);
        classificationLayout.setVisibility(View.GONE);
        lvRight.setVisibility(VISIBLE);
        customLayout.setVisibility(GONE);
        ageAdapter = new ListDropDownAdapter(mContext, Arrays.asList(sortText), Arrays.asList(sortIcon));
        lvRight.setAdapter(ageAdapter);
        lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ageAdapter.setCheckItem(position);
                tvSortTitle.setText(sortText[position]);
                hide();
            }
        });
    }

    // 设置筛选数据
    private void setFilterAdapter() {
        lvLeft.setVisibility(GONE);
        lvRight.setVisibility(GONE);
        classificationLayout.setVisibility(GONE);
        customLayout.setVisibility(VISIBLE);
        constellationAdapter = new ConstellationAdapter(mContext, Arrays.asList(constellations), Arrays.asList(shipping));
        constellation.setAdapter(constellationAdapter);
        constellationAdapter1 = new ConstellationAdapter(mContext, Arrays.asList(constellations1), Arrays.asList(shipping1));
        constellation1.setAdapter(constellationAdapter1);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        //清空
        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        constellation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                constellationAdapter.setCheckItem(position);
            }
        });
        constellation1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                constellationAdapter1.setCheckItem(position);
            }
        });
    }

    // 动画显示
    public void show(int position) {
        if (isShowing && lastFilterPosition == position) {
            hide();
            return;
        } else if (!isShowing) {
            viewMaskBg.setVisibility(VISIBLE);
            llContentListView.setVisibility(VISIBLE);
            llContentListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    llContentListView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    panelHeight = llContentListView.getHeight();
                    ObjectAnimator.ofFloat(llContentListView, "translationY", -panelHeight, 0).setDuration(200).start();
                }
            });
        }
        isShowing = true;
        resetViewStatus();
        rotateArrowUp(position);
        rotateArrowDown(lastFilterPosition);
        lastFilterPosition = position;

        switch (position) {
            case POSITION_CATEGORY:
                tvCategoryTitle.setTextColor(mActivity.getResources().getColor(R.color.red));
                ivCategoryArrow.setImageResource(R.drawable.test76);
                setCategoryAdapter();
                break;
            case POSITION_SORT:
                tvSortTitle.setTextColor(mActivity.getResources().getColor(R.color.red));
                ivSortArrow.setImageResource(R.drawable.test76);
                setSortAdapter();
                break;
            case POSITION_FILTER:
                tvFilterTitle.setTextColor(mActivity.getResources().getColor(R.color.red));
                ivFilterArrow.setImageResource(R.drawable.test76);
                setFilterAdapter();
                break;
        }
    }

    // 隐藏动画
    public void hide() {
        isShowing = false;
        resetViewStatus();
        rotateArrowDown(filterPosition);
        rotateArrowDown(lastFilterPosition);
        filterPosition = -1;
        lastFilterPosition = -1;
        viewMaskBg.setVisibility(View.GONE);
        ObjectAnimator.ofFloat(llContentListView, "translationY", 0, -panelHeight).setDuration(200).start();
    }

    // 设置筛选数据
    public void setFilterData(Activity activity, FilterData filterData) {
        this.mActivity = activity;
        this.filterData = filterData;
    }

    // 是否显示
    public boolean isShowing() {
        return isShowing;
    }

    public int getFilterPosition() {
        return filterPosition;
    }

    // 旋转箭头向上
    private void rotateArrowUp(int position) {
        switch (position) {
            case POSITION_CATEGORY:
                rotateArrowUpAnimation(ivCategoryArrow);
                break;
            case POSITION_SORT:
                rotateArrowUpAnimation(ivSortArrow);
                break;
            case POSITION_FILTER:
                rotateArrowUpAnimation(ivFilterArrow);
                break;
        }
    }

    // 旋转箭头向下
    private void rotateArrowDown(int position) {
        switch (position) {
            case POSITION_CATEGORY:
                rotateArrowDownAnimation(ivCategoryArrow);
                break;
            case POSITION_SORT:
                rotateArrowDownAnimation(ivSortArrow);
                break;
            case POSITION_FILTER:
                rotateArrowDownAnimation(ivFilterArrow);
                break;
        }
    }

    // 旋转箭头向上
    public static void rotateArrowUpAnimation(final ImageView iv) {
        if (iv == null) return;
        RotateAnimation animation = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        iv.startAnimation(animation);
    }

    // 旋转箭头向下
    public static void rotateArrowDownAnimation(final ImageView iv) {
        if (iv == null) return;
        RotateAnimation animation = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        iv.startAnimation(animation);
    }

    // 筛选视图点击
    private OnFilterClickListener onFilterClickListener;

    public void setOnFilterClickListener(OnFilterClickListener onFilterClickListener) {
        this.onFilterClickListener = onFilterClickListener;
    }

    public interface OnFilterClickListener {
        void onFilterClick(int position);
    }

    // 分类Item点击
    private OnItemCategoryClickListener onItemCategoryClickListener;

    public void setOnItemCategoryClickListener(OnItemCategoryClickListener onItemCategoryClickListener) {
        this.onItemCategoryClickListener = onItemCategoryClickListener;
    }

    public interface OnItemCategoryClickListener {
        void onItemCategoryClick(FilterTwoEntity leftEntity, FilterEntity rightEntity);
    }

    // 排序Item点击
    private OnItemSortClickListener onItemSortClickListener;

    public void setOnItemSortClickListener(OnItemSortClickListener onItemSortClickListener) {
        this.onItemSortClickListener = onItemSortClickListener;
    }

    public interface OnItemSortClickListener {
        void onItemSortClick(FilterEntity entity);
    }

    // 筛选Item点击
    private OnItemFilterClickListener onItemFilterClickListener;

    public void setOnItemFilterClickListener(OnItemFilterClickListener onItemFilterClickListener) {
        this.onItemFilterClickListener = onItemFilterClickListener;
    }

    public interface OnItemFilterClickListener {
        void onItemFilterClick(FilterEntity entity);
    }

}
