package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.IntegralConvertAdapter;
import com.android.slowlife.adapter.IntegralGridviewAdapter;
import com.android.slowlife.objectmodel.ConvertRecommendEntity;
import com.android.slowlife.objectmodel.IntegralConverEntity;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/23 0023.
 * 积分商城
 */

public class IntegralShopActivity extends BaseActivity implements View.OnClickListener ,AdapterView.OnItemClickListener{

    private RelativeLayout go_back;
    private TextView credits;
    private LinearLayout integral, record;
    private RecyclerView integral_section;
    private MyGridView convert_gridview;
    private IntegralGridviewAdapter integralGridviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_integral_shop);
        initView();
        List<IntegralConverEntity> integralList=getIntegralConver();
        initLinearRv(integralList);
        List<ConvertRecommendEntity> convertRecommendList=getIntegral();
        integralGridviewAdapter=new IntegralGridviewAdapter(this,convertRecommendList);
        convert_gridview.setAdapter(integralGridviewAdapter);
        convert_gridview.setOnItemClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        credits= (TextView) findViewById(R.id.integral_text);
        integral= (LinearLayout) findViewById(R.id.integral_layout);
        integral.setOnClickListener(this);
        record= (LinearLayout) findViewById(R.id.record_layout);
        record.setOnClickListener(this);
        integral_section= (RecyclerView) findViewById(R.id.convert_rv);
        convert_gridview= (MyGridView) findViewById(R.id.convert_recommend_gridview);
    }

    /**
     * 单行左右滑动列表测试数据
     * */
    private List<IntegralConverEntity> getIntegralConver(){
        int[] icon = new int[]{R.drawable.convert};
        List<IntegralConverEntity> integralList = new ArrayList<>();
        IntegralConverEntity integral=null;
        for (int i = 0; i < 7; i++) {
            integral= new IntegralConverEntity();
            integral.setIntegralUrl(icon[0]);
            integral.setIntegralSection(i+"");
            integralList.add(integral);
        }
        return integralList;
    }
    /**
     * gridview列表测试数据
     * */
    private List<ConvertRecommendEntity> getIntegral(){
        int[] icon = new int[]{R.drawable.pictures};
        List<ConvertRecommendEntity> integralList = new ArrayList<>();
        ConvertRecommendEntity integral=null;
        for (int i = 0; i < 4; i++) {
            integral= new ConvertRecommendEntity();
            integral.setIntegralUrl(icon[0]);
            integral.setName("老坑冰A货翡翠圆口手镯"+i);
            integral.setCredits(3000+i+"");
            integralList.add(integral);
        }
        return integralList;
    }
    /**
     * 单行左右滑动列表
     */
    private void initLinearRv(final List<IntegralConverEntity> integralConverList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        integral_section.setLayoutManager(linearLayoutManager);
        IntegralConvertAdapter adapter = new IntegralConvertAdapter(this,integralConverList, integral_section);
        integral_section.setAdapter(adapter);
        adapter.setItemClickListener(new IntegralConvertAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(IntegralShopActivity.this,IntegralConvertActivity.class);
                intent.putExtra("title",integralConverList.get(position).getIntegralSection()+"元兑换");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.integral_layout://积分
                break;
            case R.id.record_layout://兑换记录
                intent=new Intent(this,ConvertRecordActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this,GoodsDetailsActivity.class);
        startActivity(intent);
    }
}
