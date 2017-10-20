package com.android.slowlife.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.OrderTailAfterAdapter;
import com.android.slowlife.util.CacheActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/6 0006.
 * <p>
 * TODO 订单跟踪
 */

public class OrderTailAfterActivity extends BaseActivity {
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.listview)
    ListView listview;
    private OrderTailAfterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_order_tail_after);
        ButterKnife.bind(this);
        List<String> list = getOrderTailAfter();
        adapter = new OrderTailAfterAdapter(this, list);
        listview.setAdapter(adapter);
    }

    /**
     * listview测试数据
     **/
    private List<String> getOrderTailAfter() {
        List<String> list = new ArrayList<String>();
        list.add("订单提交成功");
        list.add("订单已支付");
        list.add("订单已取消");
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
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
