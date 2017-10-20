package com.android.slowlife.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.BaseFragment;
import com.android.slowlife.R;
import com.android.slowlife.activity.DailySpecialsActivity;
import com.android.slowlife.activity.IntegralShopActivity;
import com.android.slowlife.activity.RecommentdAwardActivity;
import com.android.slowlife.adapter.SpecialOfferProductAdapter;
import com.android.slowlife.objectmodel.SpecialOfferProductEntity;
import com.android.slowlife.util.Common;
import com.android.slowlife.view.OnCountDownTimerListener;
import com.android.slowlife.view.SecondDownTimerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/1/19 .
 */
public class FindFragment extends BaseFragment {
    @Bind(R.id.integral_mall)
    ImageView integralMall;
    @Bind(R.id.recommend_award)
    ImageView recommendAward;
    @Bind(R.id.daily_specials_gridView)
    GridView dailySpecialsGridView;
    @Bind(R.id.view_more)
    TextView viewMore;
    @Bind(R.id.timerView)
    SecondDownTimerView timerView;
    @Bind(R.id.limited_commodity_gridView)
    GridView limitedCommodityGridView;
    @Bind(R.id.view_more1)
    TextView viewMore1;
    private SpecialOfferProductAdapter adapter;
    private SpecialOfferProductAdapter adapter1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        ButterKnife.bind(this, view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ((LinearLayout)view.findViewById(R.id.ll)).setPadding(0, Common.getStatusHeight(getActivity()), 0,0);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        countdownTimer();
        adapter=new SpecialOfferProductAdapter(getActivity(),getList());
        dailySpecialsGridView.setAdapter(adapter);
        adapter1=new SpecialOfferProductAdapter(getActivity(),getList(),1);
        limitedCommodityGridView.setAdapter(adapter1);
        gridviewOnclick();//gridview点击事件
    }

    public static FindFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        FindFragment fragment = new FindFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.integral_mall, R.id.recommend_award, R.id.view_more, R.id.view_more1})
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.integral_mall://积分商城
                intent=new Intent(getActivity(), IntegralShopActivity.class);
                startActivity(intent);
                break;
            case R.id.recommend_award://推荐有奖
                intent=new Intent(getActivity(), RecommentdAwardActivity.class);
                startActivity(intent);
                break;
            case R.id.view_more://天天特价
                intent=new Intent(getActivity(), DailySpecialsActivity.class);
                intent.putExtra("title","天天特价");
                startActivity(intent);
                break;
            case R.id.view_more1://限时商品
                intent=new Intent(getActivity(), DailySpecialsActivity.class);
                intent.putExtra("title","限时商品");
                startActivity(intent);
                break;
        }
    }
    /**
     * 限时秒杀倒计时
     * */
    private void countdownTimer(){
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
    /**
     * gridview测试数据
     * */
    private List<SpecialOfferProductEntity> getList(){
        List<SpecialOfferProductEntity> list=new ArrayList<SpecialOfferProductEntity>();
        SpecialOfferProductEntity entity=null;
        for(int i=0;i<5;i++){
            entity=new SpecialOfferProductEntity();
            entity.setName("Rumbu line石英表"+i);
            entity.setDiscount("0.8折");
            entity.setOriginalPrice(12+i);
            entity.setSpecialOffer(1+i);
            list.add(entity);
        }
        return list;
    }
    /**
     * gridview点击事件
     * */
    private void gridviewOnclick(){
        //天天特价
        dailySpecialsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), DailySpecialsActivity.class);
                intent.putExtra("title","天天特价");
                startActivity(intent);
            }
        });
        //限时商品
        limitedCommodityGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), DailySpecialsActivity.class);
                intent.putExtra("title","限时商品");
                startActivity(intent);
            }
        });
    }
}
