package com.android.slowlife.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.BaseFragment;
import com.android.slowlife.R;
import com.android.slowlife.activity.CityExpressActivity;
import com.android.slowlife.activity.CodeActivity;
import com.android.slowlife.activity.DetailActivity;
import com.android.slowlife.activity.OutsideExpressActivity;
import com.android.slowlife.activity.ReceivingAddressActivity;
import com.android.slowlife.activity.SearchActivity;
import com.android.slowlife.adapter.PageingGridViewAdapter;
import com.android.slowlife.adapter.PreferenceAdapter;
import com.android.slowlife.adapter.QuickMenuAdapter;
import com.android.slowlife.adapter.ShopListAdapter;
import com.android.slowlife.objectmodel.IconEntity;
import com.android.slowlife.objectmodel.ShopEntity;
import com.android.slowlife.util.Common;
import com.android.slowlife.util.LogUtils;
import com.android.slowlife.util.OkHttpUtils;
import com.android.slowlife.util.UrlConfig;
import com.android.slowlife.view.MScrollView;
import com.android.slowlife.view.MyGridView;
import com.android.slowlife.view.MyPageIndicator;
import com.android.slowlife.view.OnCountDownTimerListener;
import com.android.slowlife.view.PageGridView;
import com.android.slowlife.view.SearchView2;
import com.android.slowlife.view.SecondDownTimerView1;
import com.google.zxing.client.android.CaptureActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/19 .
 */
public class IndexFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private RecyclerView gridRV;
    private RecyclerView linearRv;
    private RecyclerView gridRV2;
    private ListView linearRv2;
    private PageGridView pageGridView, pageGridView2;
    private MyPageIndicator pageIndicator;
    private ImageView cityExpress;
    private TextView receivingAddress;
    private ImageView outsideExpress;
    private SecondDownTimerView1 timerView;
    private MScrollView scrollView;
    private MyGridView gridView;
    private PreferenceAdapter preferenceAdapter;
    private SearchView2 search, search1;
    private RelativeLayout scan;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static IndexFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        IndexFragment fragment = new IndexFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ((RelativeLayout) view.findViewById(R.id.ll)).setPadding(0, Common.getStatusHeight(getActivity()), 0, 0);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search = (SearchView2) view.findViewById(R.id.search);
        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                return false;
            }
        });
        search1 = (SearchView2) view.findViewById(R.id.search1);
        search1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                return false;
            }
        });
        linearRv = (RecyclerView) view.findViewById(R.id.linearRv);
        linearRv2 = (ListView) view.findViewById(R.id.linearRv2);
        linearRv2.setOnItemClickListener(this);
        cityExpress = (ImageView) view.findViewById(R.id.city_express_image);
        cityExpress.setOnClickListener(this);
        outsideExpress = (ImageView) view.findViewById(R.id.outside_express);
        outsideExpress.setOnClickListener(this);

        List<IconEntity> iconList = getDisList();
        initLinearRv(iconList);
        List<ShopEntity> hotList = getShopList();
        initLinearRv2(hotList);

        pageIndicator = (MyPageIndicator) view.findViewById(R.id.pageindicator);
        pageGridView = (PageGridView) view.findViewById(R.id.pagingGridView);
        PageingGridViewAdapter adapter = new PageingGridViewAdapter(data, getActivity());
        pageGridView.setAdapter(adapter);
        pageGridView.setOnItemClickListener(adapter);
        pageGridView.setPageIndicator(pageIndicator);

//        pageGridView2 = (PageGridView) view.findViewById(R.id.pagingGridView2);
//        PageingGridViewAdapter2 adapter2 = new PageingGridViewAdapter2(data1, getActivity());
//        pageGridView2.setAdapter(adapter2);
//        pageGridView2.setOnItemClickListener(adapter2);
        //品类优选
        gridView = (MyGridView) view.findViewById(R.id.gridView);
        preferenceAdapter = new PreferenceAdapter(getActivity());
        gridView.setAdapter(preferenceAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), DetailActivity.class));
            }
        });
        initView(view);
        countdownTimer();
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        scan = (RelativeLayout) view.findViewById(R.id.scan);
        scan.setOnClickListener(this);
        receivingAddress = (TextView) view.findViewById(R.id.receiving_address);
        receivingAddress.setOnClickListener(this);
        timerView = (SecondDownTimerView1) view.findViewById(R.id.timerView);
        scrollView = (MScrollView) view.findViewById(R.id.scrollView);
        View v = view.findViewById(R.id.layout1);
        scrollView.setV1(v);
    }

    /**
     * 导航菜单测试数据
     */
    List<String> data = new ArrayList<>();

    {
        for (int i = 1; i < 18; i++) {
            data.add(i + "");
        }

    }

    /**
     * 品类优选测试数据
     */
    List<String> data1 = new ArrayList<>();

    {
        for (int i = 1; i < 9; i++) {
            data1.add(i + "");
        }

    }

    /**
     * 单行左右滑动列表
     *
     * @param iconList
     */
    private void initLinearRv(List<IconEntity> iconList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        linearRv.setLayoutManager(linearLayoutManager);

        QuickMenuAdapter adapter = new QuickMenuAdapter(iconList, linearRv);
        linearRv.setAdapter(adapter);
    }

    /**
     * 推荐商家列表
     */
    private void initLinearRv2(List<ShopEntity> list) {
        ShopListAdapter adapter = new ShopListAdapter(list, getActivity());
        linearRv2.setAdapter(adapter);
        setListViewHeightBasedOnChildren(linearRv2);
    }

    private List<ShopEntity> getShopList() {

        List<ShopEntity> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ShopEntity shopEntity = new ShopEntity();
            shopEntity.shopName = "王家铺子" + i;
            list.add(shopEntity);
        }
        return list;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 天天惊喜测试数据
     */
    private int[] icon = new int[]{R.drawable.test51, R.drawable.test52, R.drawable.test53, R.drawable.test54, R.drawable.test55};

    private List<IconEntity> getDisList() {

        List<IconEntity> iconList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            IconEntity iconEntity = new IconEntity();
            iconEntity.iconUrl = icon[i];
            iconEntity.iconName = "围巾";
            iconEntity.standby1 = "￥19.9";
            iconEntity.price1 = "￥50.0";
            iconList.add(iconEntity);
        }
        return iconList;
    }

    /**
     * okhttp 网络请求框架
     *
     * @param v
     */
    Map<String, String> maps = new HashMap<>();

    @Override
    public void onClick(View v) {
//        OkHttpUtils.getInstance(getActivity()).getRequest(UrlConfig.TEST).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                //TODO 请求失败逻辑在这里处理
//                //TODO 注意:这里边都是子线程,所以要更改UI的时候需要发送到主线程才OK
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                //TODO 请求成功时候
//                //TODO 注意:这里边都是子线程,所以要更改UI的时候需要发送到主线程才OK
//                String result = response.body().string();
//                LogUtils.i("test==", result + "");
//                MovieSubject data = (MovieSubject) GsonUtils.getGsonData(MovieSubject.class, result);
//
//                LogUtils.i("test==", data.getMsg() + "");
//            }
//        });
        maps.put("user", "test");
        maps.put("phone", "test");
        maps.put("password", "test");
        OkHttpUtils.getInstance(getActivity()).postRequest(UrlConfig.login, maps).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.i("test==", result + "");
            }
        });

        Intent intent = null;
        switch (v.getId()) {
            case R.id.receiving_address:
                intent = new Intent(getActivity(), ReceivingAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.city_express_image://同城快递
                intent = new Intent(getActivity(), CityExpressActivity.class);
                startActivity(intent);
                break;
            case R.id.outside_express://区外快递
                intent = new Intent(getActivity(), OutsideExpressActivity.class);
                startActivity(intent);
                break;
            case R.id.scan://扫一扫
                popwindows();
                break;
        }
    }

    /**
     * 弹出popwindows
     */
    private void popwindows() {
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View view = mInflater.inflate(R.layout.popwindows_scan, null);
        final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //扫一扫
        view.findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivity(intent);
            }
        });
        //付款
        view.findViewById(R.id.payment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CodeActivity.class);
                startActivity(intent);
            }
        });
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 允许点击外部消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        //设置popwindows弹窗在某控件下面
        popupWindow.showAsDropDown(scan);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getActivity(), DetailActivity.class));
    }

    /**
     * 限时秒杀倒计时
     */
    private void countdownTimer() {
        timerView.setDownTime(50000000);
        timerView.setDownTimerListener(new OnCountDownTimerListener() {

            @Override
            public void onFinish() {
                Toast.makeText(getActivity(), "倒计时结束", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        timerView.startDownTimer();
    }
}
