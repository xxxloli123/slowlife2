package com.android.slowlife.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.objectmodel.OrdersEntity;
import com.android.slowlife.util.LogoConfig;
import com.android.slowlife.util.SimpleCallback;
import com.google.gson.Gson;
import com.interfaceconfig.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.android.slowlife.bean.OrderStatus.Complete;
import static com.android.slowlife.bean.OrderStatus.GoodsDelivery;
import static com.android.slowlife.bean.OrderStatus.ReceivedOrder;
import static com.android.slowlife.bean.OrderStatus.UnPayed;
import static com.android.slowlife.bean.OrderStatus.UnReceivedOrder;

public class OrderDetailsActivity extends BaseActivity {
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.top_layout)
    RelativeLayout topLayout;
    @Bind(R.id.order_status)
    TextView orderStatus;
    @Bind(R.id.pay)
    TextView pay;
    @Bind(R.id.notify)
    TextView notify;
    @Bind(R.id.tel)
    TextView tel;
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.evaluate)
    TextView evaluate;
    @Bind(R.id.header1)
    LinearLayout header1;
    @Bind(R.id.user_head)
    ImageView userHead;
    @Bind(R.id.courierName)
    TextView courierName;
    @Bind(R.id.starbar)
    AppCompatRatingBar starbar;
    @Bind(R.id.call)
    TextView call;
    @Bind(R.id.info)
    RelativeLayout info;
    @Bind(R.id.anonymity)
    TextView anonymity;
    @Bind(R.id.evaluate_Starbar)
    AppCompatRatingBar evaluateStarbar;
    @Bind(R.id.evaluate_ver)
    TextView evaluateVer;
    @Bind(R.id.header2)
    LinearLayout header2;
    @Bind(R.id.img1)
    ImageView img1;
    @Bind(R.id.expresscompany)
    TextView expresscompany;
    @Bind(R.id.expresscompany1)
    TextView expresscompany1;
    @Bind(R.id.isUrgent)
    TextView isUrgent;
    @Bind(R.id.textView1)
    TextView textView1;
    @Bind(R.id.userName)
    TextView userName;
    @Bind(R.id.userPhone)
    TextView userPhone;
    @Bind(R.id.startAddr)
    TextView startAddr;
    @Bind(R.id.recipients)
    TextView recipients;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.urgent_price)
    TextView urgentPrice;
    @Bind(R.id.total_price)
    TextView totalPrice;
    @Bind(R.id.order_num)
    TextView orderNum;
    @Bind(R.id.sTime)
    TextView sTime;
    @Bind(R.id.gTime)
    TextView gTime;
    @Bind(R.id.require)
    TextView require;
    @Bind(R.id.consume)
    TextView consume;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.time_layout)
    LinearLayout timeLayout;
    @Bind(R.id.city)
    View city;
    @Bind(R.id.expresscompanyphone)
    TextView expresscompanyphone;
    @Bind(R.id.recipientsPhone)
    TextView recipientsPhone;
    @Bind(R.id.endAddr)
    TextView endAddr;
    @Bind(R.id.recipients_layout)
    LinearLayout recipientsLayout;
    @Bind(R.id.price_layout)
    LinearLayout priceLayout;
    @Bind(R.id.total_layout)
    LinearLayout totalLayout;
    @Bind(R.id.require_layout)
    TableRow requireLayout;
    @Bind(R.id.consume_layout)
    TableRow consumeLayout;
    @Bind(R.id.gTime_layout)
    TableRow gTimeLayout;
    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.imageLayout)
    RelativeLayout imageLayout;
    @Bind(R.id.ordertype)
    TextView type;
    @Bind(R.id.weight)
    TextView weight;
    @Bind(R.id.logisticsNumber)
    TextView logisticsNumber;


    private OrdersEntity oe;
    public static final String ORDER = "order";
    public static final String ORDER_ID = "order_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        oe = getIntent().getParcelableExtra(ORDER);
        String orderId = getIntent().getStringExtra(ORDER_ID);
        if (oe == null && isEmpty(orderId)) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (oe != null) init();
        else getOrder(orderId);
    }

    private void getOrder(String orderId) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", orderId)
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(Config.Url.getUrl(Config.ORDER_DETAILS))
                .build();
        new OkHttpClient().newCall(request).enqueue(new SimpleCallback() {
            @Override
            public void onSuccess(String tag, JSONObject json) throws JSONException {
                oe = new Gson().fromJson(json.getString("order"), OrdersEntity.class);
                init();
            }
        });
    }

    protected void init() {
        orderStatus.setText(oe.getOrderStatus().getDescribe());
        switch (oe.getOrderStatus()) {
            case UnPayed:
                pay.setVisibility(View.VISIBLE);
                tel.setVisibility(View.VISIBLE);
                if (TextUtils.equals(oe.getType(), "Intercity"))
                    imageLayout.setVisibility(View.VISIBLE);
                recipientsLayout.setVisibility(View.VISIBLE);
                break;
            case ReceivedOrder:
                tel.setVisibility(View.VISIBLE);
            case UnReceivedOrder:
                if (!oe.getOrderStatus().equals(ReceivedOrder)) {
                    cancel.setVisibility(View.VISIBLE);
                    priceLayout.setVisibility(View.GONE);
                }
                totalLayout.setVisibility(View.GONE);
                gTimeLayout.setVisibility(View.GONE);
                if (oe.getOrderStatus().equals(UnReceivedOrder)) {
                    consumeLayout.setVisibility(View.GONE);
                }
                if (TextUtils.equals(oe.getType(), "Intercity"))
                    recipientsLayout.setVisibility(View.GONE);
                break;
            case GoodsDelivery:
                tel.setVisibility(View.VISIBLE);
                totalLayout.setVisibility(View.VISIBLE);
//                gTimeLayout.setVisibility(View.GONE);
                consumeLayout.setVisibility(View.GONE);
                recipientsLayout.setVisibility(View.VISIBLE);
                break;
            case CancelOrder:
                priceLayout.setVisibility(View.GONE);
                totalLayout.setVisibility(View.GONE);
                gTimeLayout.setVisibility(View.GONE);
                consumeLayout.setVisibility(View.GONE);
                if (TextUtils.equals(oe.getType(), "Intercity"))
                    recipientsLayout.setVisibility(View.GONE);
                break;
            case Complete:
                if (oe.getEvaluate().toString().equals("no")){
                    evaluate.setVisibility(View.VISIBLE);
                }

                Log.d("sfad","p评价"+oe.getEvaluate().toString());

                break;
        }
        if (TextUtils.equals(oe.getType(), "Intercity")) {
//            city.setVisibility(View.VISIBLE);
            type.setText("保\n价\n费");
            expresscompany.setText(oe.getUserChoiceCommpanyName());
            logisticsNumber.setText(oe.getLogisticsNumber());
            urgentPrice.setText("¥" + oe.getInsuredFee());
            img1.setImageResource(LogoConfig.getLogoRes(oe.getUserChoiceCommpanyCode()));
        } else {
            expresscompany1.setText(oe.getUserChoiceCommpanyName());
            urgentPrice.setText("¥" + oe.getUrgentFee());
        }
        if (TextUtils.equals(oe.getUrgent(), "yes"))
            isUrgent.setVisibility(View.VISIBLE);
        userName.setText(oe.getCreateUserName());
        userPhone.setText(oe.getCreateUserPhone());
        startAddr.setText(String.format("%s%s%s%s%s", oe.getStartPro(), oe.getStartPro(),
                oe.getStartDistrict(), oe.getStartStreet(), oe.getStartHouseNumber()).replace("null", ""));
        recipients.setText(oe.getReceiverName());                 // 收件人
        recipientsPhone.setText(oe.getReceiverPhone());            // 电话
        endAddr.setText(String.format("%s%s%s%s%s", oe.getEndPro(), oe.getEndCity(), oe.getEndDistrict(),
                oe.getEndStreet(), oe.getEndHouseNumber()).replace("null", ""));
        price.setText("¥" + oe.getCost());
        totalPrice.setText("¥" + (oe.getCost() + oe.getUrgentFee() + oe.getInsuredFee()));
        orderNum.setText(oe.getOrderNumber());
        sTime.setText(oe.getCreateDate());      // 建单时间
        require.setText("2小时内");                    // 时效
        weight.setText(oe.getWeight() + "kg");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (oe.getPostmanPickupDate() != null)
                gTime.setText(format.format(Long.valueOf(oe.getPostmanPickupDate())));                      // 送达时间
        }catch (Exception e){}
        try {
            // 取件消耗
            if (!isEmpty(oe.getPostmanPickupDate())
                    && !isEmpty(oe.getCreateDate())) {
                long et = format.parse(oe.getPostmanPickupDate()).getTime();
                long st = format.parse(oe.getCreateDate()).getTime();
                long t = (et - st);
                int h = (int) (t / 60 / 60 / 1000);
                int m = (int) ((t % (60 * 60 * 1000)) / 60 / 1000);
                consume.setText(String.format("%d小时%d分钟", h, m));
            } else
                consumeLayout.setVisibility(View.GONE);
        } catch (ParseException e) {
            consumeLayout.setVisibility(View.GONE);
        }
        loadImg();
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.pay:                      // 付款
                pay();
                break;
            case R.id.notify:                   // 催单
                break;
            case R.id.tel:                      // 联系快递员
            case R.id.call:
                call();
                break;
            case R.id.cancel:                   // 取消订单
                cancel();
                break;
            case R.id.evaluate:                 // 评价快递员
                evaluate();
                break;
        }
    }

    /**
     * 评价快递员
     */
    private void evaluate() {
        Intent intent = new Intent(this, CourierEvaluateActivity.class);
        intent.putExtra(OrderDetailsActivity.ORDER, oe);
        startActivity(intent);
    }

    private void cancel() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("orderId", oe.getId())
                .addFormDataPart("type", "Cancel")
                .addFormDataPart("userId", ((MyApplication) getApplication()).getInfo().getId())
                .build();
        Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.DELORDER))
                .tag(Config.DELORDER)
                .post(requestBody)
                .build();
        new OkHttpClient().newCall(request).enqueue(callback);
    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + oe.getPostmanPhone());
        intent.setData(data);
        startActivity(intent);
    }

    private void pay() {
        Intent intent = new Intent(this, PayActivity.class);
        ArrayList orders = new ArrayList();
        orders.add(oe);
        intent.putExtra("orders", orders);
        startActivity(intent);
    }

    private Callback callback = new SimpleCallback() {
        @Override
        public void onSuccess(String tag, JSONObject json) throws JSONException {
            switch (tag) {
                case Config.DELORDER:
                    Toast.makeText(OrderDetailsActivity.this, "已取消", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }

    };


    private void loadImg() {
        if (oe.getType().equals("Intercity")
                && (oe.getOrderStatus().equals(GoodsDelivery)
                || oe.getOrderStatus().equals(Complete)
                || oe.getOrderStatus().equals(UnPayed))
                && !isEmpty(oe.getTradeImg())) {
            final Request request = new Request.Builder().url(Config.IMG + oe.getTradeImg()).build();
            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(OrderDetailsActivity.this, "加载交易快照失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final Bitmap img = BitmapFactory.decodeStream(response.body().byteStream());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageLayout.setVisibility(View.VISIBLE);
                            image.setImageBitmap(img);
                        }
                    });
                }
            });
        }
    }
}
