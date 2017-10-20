package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.MapView;
import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.objectmodel.CancelOrdersEntity;
import com.android.slowlife.objectmodel.DiscountEntity;
import com.android.slowlife.objectmodel.OrdersEntity;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.Common;
import com.android.slowlife.view.RoundProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/9 0009.
 * <p>
 * TODO   骑手正赶往商家
 */

public class NoSingleActivity extends BaseActivity {

    @Bind(R.id.mapView)
    MapView mapView;
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.phone_rl)
    RelativeLayout phoneRl;
    @Bind(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @Bind(R.id.roundProgressBar)
    RoundProgressBar roundProgressBar;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.progress_rl)
    RelativeLayout progressRl;
    @Bind(R.id.order_tail_after_text)
    TextView orderTailAfterText;
    @Bind(R.id.cancel_order_text)
    TextView cancelOrderText;
    @Bind(R.id.reminder_text)
    TextView reminderText;
    @Bind(R.id.contact_delivery_text)
    TextView contactDeliveryText;
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
    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.order_number_text)
    TextView orderNumberText;
    @Bind(R.id.copy_text)
    TextView copyText;
    @Bind(R.id.mode_payment_text)
    TextView modePaymentText;
    @Bind(R.id.time_text)
    TextView timeText;
    private OrdersEntity oe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_delivery_orders);
        orderTailAfterText.setText("骑手正赶往商家 >");
        roundProgressBar.setProgress(70);
        init();
    }

    protected void init() {
        oe = getIntent().getParcelableExtra("order");
        addressText.setText(String.format("%s%s%s%s%s", oe.getEndPro(), oe.getEndCity(),
                oe.getEndDistrict(), oe.getEndStreet(), oe.getEndHouseNumber()));
        deliveryManNameText.setText(oe.getPostmanName());
        deliveryManPhoneText.setText(oe.getPostmanPhone());
        orderNumberText.setText(oe.getId());
        timeText.setText(oe.getCreateDate());


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.back_rl, R.id.phone_rl, R.id.order_tail_after_text, R.id.cancel_order_text, R.id.reminder_text,
            R.id.contact_delivery_text, R.id.delivery_man_phone_text, R.id.copy_text})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.phone_rl:
                Common.phoneDialog(this, "023-12345678");
                break;
            case R.id.order_tail_after_text://订单跟踪
                intent = new Intent(this, OrderTailAfterActivity.class);
                startActivity(intent);
                break;
            case R.id.cancel_order_text://取消订单
                Toast.makeText(this, "取消订单", Toast.LENGTH_SHORT).show();
                break;
            case R.id.reminder_text://催单
                Toast.makeText(this, "催单", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contact_delivery_text://联系骑手
                Common.dialPhone(this, deliveryManPhoneText.getText().toString().trim());
                break;
            case R.id.shop_name_layout://进入相应商店
                break;
            case R.id.delivery_man_phone_text://送货人电话
                Common.dialPhone(this, deliveryManPhoneText.getText().toString().trim());
                break;
            case R.id.copy_text://复制
                Common.copyText(this, orderNumberText.getText().toString().trim());
                break;
            case R.id.contact_text://联系商家
                Common.phoneDialog(this, "023-12345678");
                break;
        }
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
}
