package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.SelectReceiptAddressAdapter;
import com.android.slowlife.objectmodel.AddressEntity;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.view.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

public class SelectReceiptAddressActivity extends BaseActivity {
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.listview)
    MyListView listview;
    @Bind(R.id.out_range_listview)
    MyListView outRangeListview;
    @Bind(R.id.add_address_text)
    TextView addAddressText;
    private SelectReceiptAddressAdapter adapter;
    private SelectReceiptAddressAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_select_receipt_address);
        ButterKnife.bind(this);
        initView();
    }

    /**
     *初始化控件
     * */
    private void initView(){
        adapter=new SelectReceiptAddressAdapter(this,getAddressInformation());
        listview.setAdapter(adapter);
        adapter1=new SelectReceiptAddressAdapter(this,getAddressInformation());
        outRangeListview.setAdapter(adapter1);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    //
    @OnClick({R.id.back_rl, R.id.add_address_text})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.add_address_text:
                intent = new Intent(this, NewAddressActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * listview测试数据
     */
    private List<AddressEntity> getAddressInformation() {
        List<AddressEntity> list = new ArrayList<AddressEntity>();
        AddressEntity address = null;
        for (int i = 0; i < 3; i++) {
            address = new AddressEntity();
            address.setPersonname("默默" + i);
            address.setPersonphone("121345678910");
            address.setDistrict("重庆市渝北区大竹林街道康庄美地B区" + i);
            list.add(address);
        }
        return list;
    }
}
