package com.android.slowlife.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.slowlife.R;
import com.android.slowlife.activity.CouponCentreActiviy;
import com.android.slowlife.adapter.CouponAdapter;
import com.android.slowlife.objectmodel.CouponEntity;
import com.android.slowlife.util.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/24 0024.
 */

public class CouponFragment extends Fragment implements View.OnClickListener{
    private RelativeLayout delete,delete_rl,delete_layout;
    private ListView listView;
    private CouponAdapter adapter;
    private LinearLayout no_coupon;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_coupon,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        delete_layout= (RelativeLayout) view.findViewById(R.id.delete_layout);
        delete= (RelativeLayout) view.findViewById(R.id.delete_me);
        delete.setOnClickListener(this);
        delete_rl= (RelativeLayout) view.findViewById(R.id.delete_rl);
        delete_rl.setOnClickListener(this);
        no_coupon= (LinearLayout) view.findViewById(R.id.no_coupon);
        listView= (ListView) view.findViewById(R.id.coupon_listview);
        List<CouponEntity> list=getCouponInformation();
        if(!Common.isNull(list)&&list.size()>0){
            listView.setVisibility(View.VISIBLE);
            no_coupon.setVisibility(View.GONE);
            adapter=new CouponAdapter(getActivity(),list);
            listView.setAdapter(adapter);
        }else{
           listView.setVisibility(View.GONE);
           no_coupon.setVisibility(View.VISIBLE);
        }
    }

    /**
     * listview测试数据
     * */
    private List<CouponEntity> getCouponInformation(){
        List<CouponEntity> list=new ArrayList<CouponEntity>();
        CouponEntity coupon=null;
        for (int i = 0; i < 4; i++) {
            coupon= new CouponEntity();
            coupon.setName("订一锅.大龙老火锅（优惠300）"+i);
            coupon.setAddress("重庆市渝北区大竹林街道康庄美地B区"+i);
            coupon.setAvailability("300");
            coupon.setIsUse("1");
            coupon.setScope("仅限在线支付");
            coupon.setPrice("15");
            if(i==3){
                coupon.setIsUse("0");
            }
            list.add(coupon);
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.delete_rl:
                delete_layout.setVisibility(View.GONE);
                break;
            case R.id.delete_me:
                intent=new Intent(getActivity(), CouponCentreActiviy.class);
                startActivity(intent);
                break;
        }
    }
}
