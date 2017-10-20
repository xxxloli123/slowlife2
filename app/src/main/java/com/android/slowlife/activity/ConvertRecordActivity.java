package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.ConvertRecordAdapter;
import com.android.slowlife.objectmodel.IntegralConvertShopEntity;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/31 0031.
 */

public class ConvertRecordActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private RelativeLayout go_back;
    private ListView listView;
    private ConvertRecordAdapter adapter;
    private List<IntegralConvertShopEntity> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_convert_record);
        initView();
        list=getShopData();
        adapter=new ConvertRecordAdapter(this,list);
        listView.setAdapter(adapter);
    }
    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        listView= (ListView) findViewById(R.id.listview);
        listView.setOnItemClickListener(this);
    }
    /**
     * listview测试数据
     * */
    private List<IntegralConvertShopEntity> getShopData(){
        List<IntegralConvertShopEntity> list=new ArrayList<IntegralConvertShopEntity>();
        IntegralConvertShopEntity shop=null;
        for (int i=0;i<10;i++){
            shop=new IntegralConvertShopEntity();
            shop.setIntegral("3000");
            shop.setName("女士手表腕表石英表"+i);
            list.add(shop);
        }
        return list;
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl://返回
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this,GoodsDetailsActivity.class);
        if(!Common.isNull(list)&&list.size()>0){
            intent.putExtra("title",list.get(position).getName());
        }
        startActivity(intent);
    }
}
