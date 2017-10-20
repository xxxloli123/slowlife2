package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.EvaluateAdapter;
import com.android.slowlife.objectmodel.CancelOrdersEntity;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.Common;
import com.android.slowlife.view.MyListView;
import com.android.slowlife.view.RatingBar;
import com.android.slowlife.view.ShSwitchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/7 0007.
 */

public class EvaluateActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener{
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.integral_text)
    TextView integralText;
    @Bind(R.id.submit_text)
    TextView submitText;
    @Bind(R.id.head_image)
    ImageView headImage;//头像
    @Bind(R.id.name_text)
    TextView nameText;
    @Bind(R.id.merchant_ratingBar)
    RatingBar merchantRatingBar;//商家评分
    @Bind(R.id.poor_taste)
    CheckBox poorTaste;
    @Bind(R.id.slow_delivery)
    CheckBox slowDelivery;
    @Bind(R.id.deal)
    CheckBox deal;
    @Bind(R.id.poor_service)
    CheckBox poorService;
    @Bind(R.id.high_price)
    CheckBox highPrice;
    @Bind(R.id.adhesive)
    CheckBox adhesive;
    @Bind(R.id.poor_packing)
    CheckBox poorPacking;
    @Bind(R.id.read_notes)
    CheckBox readNotes;
    @Bind(R.id.stale)
    CheckBox stale;
    @Bind(R.id.evaluate_edit)
    EditText evaluateEdit;
    @Bind(R.id.product_ratingBar)
    RatingBar productRatingBar;//产品评分
    @Bind(R.id.listview)
    MyListView listview;
    @Bind(R.id.evaluate_edit1)
    EditText evaluateEdit1;
    @Bind(R.id.delivery_service_ratingBar)
    RatingBar deliveryServiceRatingBar;//配送服务评分
    @Bind(R.id.fast_delivery)
    CheckBox fastDelivery;
    @Bind(R.id.arrive_time)
    CheckBox arriveTime;
    @Bind(R.id.provide_home)
    CheckBox provideHome;
    @Bind(R.id.wear_professional)
    CheckBox wearProfessional;
    @Bind(R.id.regardless_weather)
    CheckBox regardlessWeather;
    @Bind(R.id.service_attitude)
    CheckBox serviceAttitude;
    @Bind(R.id.well_preserved)
    CheckBox wellPreserved;
    @Bind(R.id.evaluate_edit2)
    EditText evaluateEdit2;
    @Bind(R.id.switch_view)
    ShSwitchView switchView;
    @Bind(R.id.switch_view1)
    ShSwitchView switchView1;
    private EvaluateAdapter adapter;
    private String s="";//保存CheckBox选中的值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_evaluate);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * 初始化控件
     */
    private void initView(){
        List<CancelOrdersEntity> list=getOrders();
        adapter=new EvaluateAdapter(this,list);
        listview.setAdapter(adapter);
        //商家评分
        merchantRatingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {

            }
        });
        //产品评分
        productRatingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {

            }
        });
        //配送服务评分
        deliveryServiceRatingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {

            }
        });
        //匿名评价
        switchView.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
            }
        });
        //收藏商家
        switchView1.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                if(isOn){
                    //选中状态
                }else {
                    //未选中状态
                }
            }
        });
        getCheckBoxOnclick();
    }
    /**
     *checkbox点击事件
     * */
    private void getCheckBoxOnclick(){
        poorTaste.setOnCheckedChangeListener(this);
        slowDelivery.setOnCheckedChangeListener(this);
        deal.setOnCheckedChangeListener(this);
        poorService.setOnCheckedChangeListener(this);
        highPrice.setOnCheckedChangeListener(this);
        adhesive.setOnCheckedChangeListener(this);
        poorPacking.setOnCheckedChangeListener(this);
        readNotes.setOnCheckedChangeListener(this);
        stale.setOnCheckedChangeListener(this);
        fastDelivery.setOnCheckedChangeListener(this);
        arriveTime.setOnCheckedChangeListener(this);
        provideHome.setOnCheckedChangeListener(this);
        wearProfessional.setOnCheckedChangeListener(this);
        regardlessWeather.setOnCheckedChangeListener(this);
        serviceAttitude.setOnCheckedChangeListener(this);
        wellPreserved.setOnCheckedChangeListener(this);
    }
    /**
     * 控件点击事件
     * */
    @OnClick({R.id.back_rl, R.id.integral_text, R.id.submit_text})
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.integral_text:
                break;
            case R.id.submit_text://提交评价
                intent=new Intent(this,AppreciationEvaluateActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    /**
     * listview测试数据
     */
    private List<CancelOrdersEntity> getOrders() {
        List<CancelOrdersEntity> list = new ArrayList<CancelOrdersEntity>();
        CancelOrdersEntity cancelOrdersEntity = null;
        for (int i = 0; i < 2; i++) {
            cancelOrdersEntity = new CancelOrdersEntity();
            cancelOrdersEntity.setName("韩版音乐耳机耳罩可折叠耳包式保暖男耳暖耳朵护耳捂女韩国加厚学生保暖脖套纯色百搭双圈毛绒围脖");
            if (i == 1 || i == 2) {
                cancelOrdersEntity.setName("韩版音乐耳机耳罩可折叠耳包式保暖男耳暖耳朵护耳捂女");
            }
            list.add(cancelOrdersEntity);
        }
        return list;
    }
    /**
     * CheckBox点击事件
     * */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if(Common.isNull(s)){
                s=buttonView.getText().toString().trim();
            }else{
                s=s+","+buttonView.getText().toString().trim();
            }
            Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
        }else{
            if(s.contains(","+buttonView.getText().toString().trim())){//判断字符是否在s字符串中存在
                s=getString(s,","+buttonView.getText().toString().trim());//删除的指定的字符串
            }else if(s.contains(buttonView.getText().toString().trim()+",")){
                s=getString(s,buttonView.getText().toString().trim()+",");
            }else if(s.contains(buttonView.getText().toString().trim())){
                s=getString(s,buttonView.getText().toString().trim());
            }
            Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 删除的指定的字符串
     * */
    private String getString(String s, String delete){//s是需要删除某个子串的字符串delete是需要删除的子串
        int postion = s.indexOf(delete);
        int length = delete.length();
        int Length = s.length();
        String newString = s.substring(0,postion) + s.substring(postion + length, Length);
        return newString;//返回已经删除好的字符串
    }
}
