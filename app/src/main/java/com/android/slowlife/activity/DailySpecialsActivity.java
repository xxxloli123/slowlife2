package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.DailySpecialsAdapter;
import com.android.slowlife.objectmodel.SpecialOfferProductEntity;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.Common;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/9 0009.
 */

public class DailySpecialsActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.gridView)
    GridView gridView;
    private DailySpecialsAdapter adapter;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_daily_specials);
        ButterKnife.bind(this);
        Intent intent=this.getIntent();
        title=intent.getExtras().getString("title");
        if(!Common.isNull(title)){
            titleText.setText(title);
        }else{
            titleText.setText("天天特价");
        }
        adapter=new DailySpecialsAdapter(this,getList());
        gridView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * gridview测试数据
     */
    private List<SpecialOfferProductEntity> getList() {
        List<SpecialOfferProductEntity> list = new ArrayList<SpecialOfferProductEntity>();
        SpecialOfferProductEntity entity = null;
        for (int i = 0; i < 10; i++) {
            entity = new SpecialOfferProductEntity();
            entity.setName("Rumbu line石英表" + i);
            entity.setDiscount("0.8折");
            entity.setOriginalPrice(12 + i);
            entity.setSpecialOffer(1 + i);
            list.add(entity);
        }
        return list;
    }

    @OnClick({R.id.back_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent=new Intent();
//        startActivity(intent);
    }
}
