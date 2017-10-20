package com.android.slowlife.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.ConfirmOrderAdapter;
import com.android.slowlife.adapter.PopwindowsConfirmOrderAdapter;
import com.android.slowlife.objectmodel.ConfirmOrderEntity;
import com.android.slowlife.objectmodel.ServiceTimeEntity;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.view.MyListView;
import com.android.slowlife.view.ShSwitchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/15 0015.
 */

public class ConfirmOrderActivity extends BaseActivity {
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.name_phone_text)
    TextView namePhoneText;
    @Bind(R.id.consignee)
    RelativeLayout consignee;
    public static TextView time;
    @Bind(R.id.service_time)
    RelativeLayout serviceTime;
    @Bind(R.id.listview)
    MyListView listview;
    @Bind(R.id.switch_view)
    ShSwitchView switchView;
    @Bind(R.id.red_packet_price)
    TextView redPacketPrice;
    @Bind(R.id.red_packet)
    RelativeLayout redPacket;
    @Bind(R.id.voucher_price)
    TextView voucherPrice;
    @Bind(R.id.voucher)
    RelativeLayout voucher;
    @Bind(R.id.distribution_cost_price)
    TextView distributionCostPrice;
    @Bind(R.id.distribution_cost)
    RelativeLayout distributionCost;
    @Bind(R.id.order)
    TextView order;
    @Bind(R.id.discount)
    TextView discount;
    @Bind(R.id.to_paid_price)
    TextView toPaidPrice;
    @Bind(R.id.to_paid)
    RelativeLayout toPaid;
    @Bind(R.id.leaving_message)
    EditText leavingMessage;
    @Bind(R.id.to_paid_price1)
    TextView toPaidPrice1;
    @Bind(R.id.discount1)
    TextView discount1;
    @Bind(R.id.submit)
    TextView submit;
    @Bind(R.id.business)
    RelativeLayout business;
    @Bind(R.id.address)
    TextView address;
    private ConfirmOrderAdapter adapter;
    public static PopupWindow pop;
    public static LinearLayout popwindows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        time = (TextView) findViewById(R.id.time);
        adapter = new ConfirmOrderAdapter(this, initData());
        listview.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.business, R.id.back_rl, R.id.consignee, R.id.service_time, R.id.red_packet, R.id.voucher, R.id.submit})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.consignee://收货地址
                intent = new Intent(this, SelectReceiptAddressActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.service_time://送达时间
                popwindowsshow();
                break;
            case R.id.red_packet://红包
                intent = new Intent(this, UseRedPacketActivity.class);
                startActivity(intent);
                break;
            case R.id.voucher://商家代金券
                break;
            case R.id.submit://提交订单
                intent = new Intent(this, OrderPaymentActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.business://进入商店
                intent = new Intent(this, DetailActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 给listview添加数据
     */
    private List<ConfirmOrderEntity> initData() {
        List<ConfirmOrderEntity> list = new ArrayList<ConfirmOrderEntity>();
        ConfirmOrderEntity entity = null;
        for (int i = 0; i < 3; i++) {
            entity = new ConfirmOrderEntity();
            entity.setName("Rubatime 韩国潮女大学生简约个性韩版潮流创意手表情侣一对时尚原宿复古男韩国手表女学生韩版简约时尚潮流" + i);
            entity.setNumber(1 + i);
            entity.setPrice(128.00 + i);
            list.add(entity);
        }
        return list;
    }

    /**
     * 弹出窗口(选择送达时间)
     */
    private void popwindowsshow() {
        PopwindowsConfirmOrderAdapter adapter = null;
        pop = new PopupWindow(this);
        View view = getLayoutInflater().inflate(R.layout.popwindows_confirm_order, null);
        popwindows = (LinearLayout) view.findViewById(R.id.popwindows);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        ListView listView = (ListView) view.findViewById(R.id.listview);
        adapter = new PopwindowsConfirmOrderAdapter(this, selectServiceTime());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pop.dismiss();
                popwindows.clearAnimation();
            }
        });
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 选择送达时间
     */
    private List<ServiceTimeEntity> selectServiceTime() {
        String date[] = {"今日（周四）", "17号（周五）", "18号（周六）", "19号（周日）", "20号（周一）", "21号（周二）"};
        String time[] = {"尽快送达|预计13:20", "14:20", "15:30", "16:40", "17:50", "18:30"};
        List<ServiceTimeEntity> list = new ArrayList<ServiceTimeEntity>();
        ServiceTimeEntity entity = null;
        for (int i = 0; i < date.length; i++) {
            entity = new ServiceTimeEntity();
            entity.setDate(date[i]);
            entity.setTime(time[i]);
            entity.setDistribution_cost(i);
            list.add(entity);
        }
        return list;
    }

    /**
     * 接收回传过来的值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            String name = data.getStringExtra("name");
            String phone = data.getStringExtra("phone");
            String address1 = data.getStringExtra("address");
            namePhoneText.setText("收货人：" + name + " " + phone);
            address.setText("收货地址："+address1);
        }
    }
}
