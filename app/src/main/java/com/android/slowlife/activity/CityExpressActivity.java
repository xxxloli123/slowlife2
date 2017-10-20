package com.android.slowlife.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.ExpressNavBarAdapter;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.SimpleCallback;
import com.android.slowlife.view.AutoHeightViewPager;
import com.android.slowlife.view.MScrollView1;
import com.android.slowlife.view.PagerSlidingTabStrip;
import com.interfaceconfig.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/2/13 0013.
 * <p>
 * TODO 同城快递
 */

public class CityExpressActivity extends BaseActivity {
    @Bind(R.id.price2)
    TextView price;
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @Bind(R.id.pager)
    AutoHeightViewPager pager;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.agreement)
    TextView agreement;
    @Bind(R.id.linearLayout4)
    LinearLayout linearLayout4;
    @Bind(R.id.sure_bt)
    Button sureBt;
    @Bind(R.id.appointment)
    ImageView appointment;
    @Bind(R.id.tabs1)
    PagerSlidingTabStrip tabs1;
    @Bind(R.id.scrollView)
    MScrollView1 scrollView;
    @Bind(R.id.rule)
    TextView rule;
    @Bind(R.id.bredPacket)
    TextView bredPacket;
    private int touchSlop;
    private DisplayMetrics display;
    private ExpressNavBarAdapter adapter;
    private Info info;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_city_express);
        ButterKnife.bind(this);
        //initView();
        suspensionIcon();
        initPagers();
        View v = findViewById(R.id.tabs1);
        View v1 = findViewById(R.id.llayout);
        scrollView.setV1(v);
        scrollView.setV2(v1);
        touchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        info = ((MyApplication) getApplication()).getInfo();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type", "CityWide").build();
        Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.GETRULE))
                .tag(Config.GETRULE)
                .post(requestBody).build();
        new OkHttpClient().newCall(request).enqueue(new Callback());

        RequestBody requestBody2 = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId())
                .build();
        Request request2 = new Request.Builder()
                .url(Config.Url.getUrl(Config.ACCOUNT))
                .post(requestBody2).build();
        new OkHttpClient().newCall(request2).enqueue(new SimpleCallback(this) {
            @Override
            public void onSuccess(String tag, JSONObject json) throws JSONException {
//                String.format("预估金额:%s元", jsonObject.getString("cost"))
                bredPacket.setText(String.format("当前红包有:%s个", json.getString("RedPacketsNumber")));     // 红包
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (((MyApplication) getApplication()).getInfo() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
    }

    /**
     * 设置图标可拖动
     */
    private void suspensionIcon() {
        final SharedPreferences sp;
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        int lastx = sp.getInt("lastx", 0);
        int lasty = sp.getInt("lasty", 0);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) appointment.getLayoutParams();
        params.leftMargin = lastx;
        params.topMargin = lasty;
        appointment.setLayoutParams(params);
    }

    /**
     * 根据选择的不同界面选择不同tabs
     */
    private void initPagers() {
        ArrayList<String> list = new ArrayList<>();
        list.add("帮我送");
        list.add("帮我拿");
        adapter = new ExpressNavBarAdapter(getSupportFragmentManager(), list);

        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        tabs1.setViewPager(pager);
        appointment.setOnTouchListener(new BtnTouchListener());
    }

    public boolean isChecked() {
        return checkbox.isChecked();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.back_rl, R.id.sure_bt, R.id.appointment, R.id.agreement})
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.rule://规则
                intent = new Intent(this, ExpressRuleActivity.class);
                startActivity(intent);
                break;
            case R.id.sure_bt://我要寄件
                ((View.OnClickListener) ((ExpressNavBarAdapter) pager.getAdapter()).getItem(pager.getCurrentItem())).onClick(v);
                break;
            case R.id.appointment://预约时间
                intent = new Intent(this, UseRedPacketActivity.class);
                startActivity(intent);
                break;
            case R.id.agreement:    //   服务协议
                intent = new Intent(v.getContext(), HelpActivity.class);
                intent.putExtra(HelpActivity.URI, "app/appGeneral/serviceContract.html");
                startActivity(intent);
                break;
        }
    }

    public void setPrice(String price) {
        this.price.setText(String.format("预估金额:%s元", price));
    }


    class Callback extends SimpleCallback {

        @Override
        public void onSuccess(String tag, JSONObject jsonObject) throws JSONException {
            switch (tag) {
                case Config.AREAINFO:
                    break;
                case Config.GETRULE:
                    rule.setText(jsonObject.getString("rule"));
                    break;
            }
        }

    }

    class BtnTouchListener implements View.OnTouchListener {
        PointF down = new PointF();
        boolean intercept = false;


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    down.x = event.getX();
                    down.y = event.getY();
                    intercept = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    final int left = (int) (event.getX() - down.x);
                    final int top = (int) (event.getY() - down.y);
                    if (Math.abs(left) > touchSlop || Math.abs(top) > touchSlop || intercept) {
                        intercept = true;
                        int l = appointment.getLeft();
                        int r = appointment.getRight();
                        int t = appointment.getTop();
                        int b = appointment.getBottom();
                        int newt = t + top;
                        int newb = b + top;
                        int newl = l + left;
                        int newr = r + left;
                        if ((newl < 0) || (newt < 0)
                                || (newr > display.widthPixels)
                                || (newb > display.heightPixels)) {
                            break;
                        }
                        // 更新iv在屏幕的位置.
                        appointment.layout(newl, newt, newr, newb);
                    } else intercept = false;
                    return intercept;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            return intercept;
        }
    }
}
