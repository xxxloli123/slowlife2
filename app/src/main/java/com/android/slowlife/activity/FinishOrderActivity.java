package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.CancelOrdersAdapter;
import com.android.slowlife.adapter.DiscountAdapter;
import com.android.slowlife.objectmodel.CancelOrdersEntity;
import com.android.slowlife.objectmodel.DiscountEntity;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.Common;
import com.android.slowlife.view.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/7 0007.
 */

public class FinishOrderActivity extends BaseActivity {
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.phone_rl)
    RelativeLayout phoneRl;
    @Bind(R.id.order_tail_after_text)
    TextView orderTailAfterText;
    @Bind(R.id.refund_text)
    TextView refundText;
    @Bind(R.id.evaluate_text)
    TextView evaluateText;
    @Bind(R.id.name_text)
    TextView nameText;
    @Bind(R.id.shop_name_layout)
    RelativeLayout shopNameLayout;
    @Bind(R.id.listview)
    MyListView listview;
    @Bind(R.id.delivery_cost_text)
    TextView deliveryCostText;
    @Bind(R.id.listview1)
    MyListView listview1;
    @Bind(R.id.out_of_pocket_expenses_text)
    TextView outOfPocketExpensesText;
    @Bind(R.id.contact_text)
    TextView contactText;
    @Bind(R.id.transport_time_text)
    TextView transportTimeText;
    @Bind(R.id.name_phone_text)
    TextView namePhoneText;
    @Bind(R.id.address_text)
    TextView addressText;
    @Bind(R.id.mode_distribution_text)
    TextView modeDistributionText;
    @Bind(R.id.delivery_man_name_text)
    TextView deliveryManNameText;
    @Bind(R.id.delivery_man_phone_text)
    TextView deliveryManPhoneText;
    @Bind(R.id.order_number_text)
    TextView orderNumberText;
    @Bind(R.id.copy_text)
    TextView copyText;
    @Bind(R.id.mode_payment_text)
    TextView modePaymentText;
    @Bind(R.id.time_text)
    TextView timeText;
    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.image1)
    ImageView image1;
    private CancelOrdersAdapter cancelOrdersAdapter;
    private DiscountAdapter discountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_finish_orders);
        ButterKnife.bind(this);
        List<CancelOrdersEntity> list = getOrders();
        cancelOrdersAdapter = new CancelOrdersAdapter(this, list);
        listview.setAdapter(cancelOrdersAdapter);
        List<DiscountEntity> list1 = getOrders1();
        discountAdapter = new DiscountAdapter(this, list1);
        listview1.setAdapter(discountAdapter);
    }

    /**
     * listview测试数据
     */
    private List<CancelOrdersEntity> getOrders() {
        List<CancelOrdersEntity> list = new ArrayList<CancelOrdersEntity>();
        CancelOrdersEntity cancelOrdersEntity = null;
        for (int i = 0; i < 3; i++) {
            cancelOrdersEntity = new CancelOrdersEntity();
            cancelOrdersEntity.setName("韩版音乐耳机耳罩可折叠耳包式保暖男耳暖耳朵护耳捂女韩国加厚学生保暖脖套纯色百搭双圈毛绒围脖");
            cancelOrdersEntity.setPrice(i + 40);
            cancelOrdersEntity.setNum(i + 1);
            if (i == 1 || i == 2) {
                cancelOrdersEntity.setName("韩版音乐耳机耳罩可折叠耳包式保暖男耳暖耳朵护耳捂女");
            }
            list.add(cancelOrdersEntity);
        }
        return list;
    }

    /**
     * listview1测试数据
     */
    private List<DiscountEntity> getOrders1() {
        List<DiscountEntity> list = new ArrayList<>();
        DiscountEntity discountEntity = null;
        for (int i = 0; i < 3; i++) {
            discountEntity = new DiscountEntity();
            discountEntity.setName("红包抵扣");
            discountEntity.setPrice(10 + i);
            discountEntity.setTag("0");
            if (i == 2) {
                discountEntity.setName("新春特价");
                discountEntity.setPrice(20);
                discountEntity.setTag("1");
            }
            list.add(discountEntity);
        }
        return list;
    }

    @OnClick({R.id.image1,R.id.order_tail_after_text, R.id.contact_text, R.id.back_rl, R.id.phone_rl, R.id.refund_text, R.id.evaluate_text, R.id.shop_name_layout, R.id.delivery_man_phone_text, R.id.copy_text})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.phone_rl:
                Common.phoneDialog(this, "12345678");
                break;
            case R.id.refund_text://申请退款
                intent = new Intent(this, ApplyRefundActivity.class);
                startActivity(intent);
                break;
            case R.id.evaluate_text://评价得积分
                intent = new Intent(this, EvaluateActivity.class);
                startActivity(intent);
                break;
            case R.id.shop_name_layout://进入相应商店
                intent = new Intent(this, DetailActivity.class);
                startActivity(intent);
                break;
            case R.id.delivery_man_phone_text://送货人电话
                Common.dialPhone(this, deliveryManPhoneText.getText().toString().trim());
                break;
            case R.id.copy_text://复制
                Common.copyText(this, orderNumberText.getText().toString().trim());
                break;
            case R.id.contact_text://联系商家
                Common.phoneDialog(this, "12345678");
                break;
            case R.id.order_tail_after_text://订单跟踪
                intent = new Intent(this, OrderTailAfterActivity.class);
                startActivity(intent);
                break;
            case R.id.image1:
                intent = new Intent(this, DetailActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
