package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.objectmodel.CancelOrdersEntity;
import com.android.slowlife.objectmodel.DiscountEntity;
import com.android.slowlife.objectmodel.OrdersEntity;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.Common;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/6 0006.
 */

public class CancelOrdersActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.phone_rl)
    RelativeLayout phoneRl;
    @Bind(R.id.top_layout)
    RelativeLayout topLayout;
    @Bind(R.id.image1)
    ImageView image1;
    @Bind(R.id.order_tail_after_text)
    TextView orderTailAfterText;
    @Bind(R.id.recur_text)
    TextView recurText;
    @Bind(R.id.transport_time_text)
    TextView transportTimeText;
    @Bind(R.id.name_phone_text)
    TextView namePhoneText;
    @Bind(R.id.address_text)
    TextView addressText;
    @Bind(R.id.mode_distribution_text)
    TextView modeDistributionText;
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
        setContentView(R.layout.activity_cancel_orders);
        ButterKnife.bind(this);
        oe = getIntent().getParcelableExtra("order");
        orderTailAfterText.setText(oe.getStatus_value());
        orderNumberText.setText(oe.getId());
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

    @OnClick({R.id.back_rl, R.id.phone_rl, R.id.recur_text, R.id.copy_text, R.id.order_tail_after_text})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.phone_rl:
                if (TextUtils.isEmpty(oe.getPostmanId())) {
                    Toast.makeText(this, "未获取到快递员信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String phone = oe.getPostmanPhone();
                if (!TextUtils.isEmpty(phone))
                    Common.phoneDialog(this, phone);
                else Toast.makeText(this, "快递员未填写手机号", Toast.LENGTH_SHORT).show();
                break;
            case R.id.recur_text://再来一单
                break;
            case R.id.shop_name_layout://进入相应商店
                intent = new Intent(this, DetailActivity.class);
                startActivity(intent);
                break;
            case R.id.contact_text://联系商家
                Common.phoneDialog(this, "12345678");
                break;
            case R.id.copy_text://复制
                Common.copyText(this, orderNumberText.getText().toString().trim());
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
