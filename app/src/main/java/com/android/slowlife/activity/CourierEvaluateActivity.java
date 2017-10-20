package com.android.slowlife.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.objectmodel.OrdersEntity;
import com.android.slowlife.util.SimpleCallback;
import com.android.slowlife.view.RatingBar;
import com.google.gson.Gson;
import com.interfaceconfig.Config;
import com.slowlife.lib.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CourierEvaluateActivity extends BaseActivity  {
    @Bind(R.id.top_layout)
    RelativeLayout topLayout;
    @Bind(R.id.iv_head_courier)
    ImageView ivHeadCourier;
    @Bind(R.id.tv_courier_name_phone)
    TextView tvCourierNamePhone;
    @Bind(R.id.merchant_ratingBar)
    RatingBar merchantRatingBar;
    @Bind(R.id.rb_speed)
    RatingBar rbSpeed;
    @Bind(R.id.rb_serve)
    RatingBar rbServe;
    @Bind(R.id.cb_shk)
    CheckBox cbShk;
    @Bind(R.id.cb_zsdd)
    CheckBox cbZsdd;
    @Bind(R.id.cb_shsm)
    CheckBox cbShsm;
    @Bind(R.id.cb_fywz)
    CheckBox cbFywz;
    @Bind(R.id.cb_czzs)
    CheckBox cbCzzs;
    @Bind(R.id.cb_spbcwh)
    CheckBox cbSpbcwh;
    @Bind(R.id.et_xxnr)
    EditText etXxnr;
    @Bind(R.id.cb_nmpj)
    CheckBox cbNmpj;
    @Bind(R.id.back_kdypj)
    RelativeLayout backKdypj;
    @Bind(R.id.tv_fbpj)
    TextView tvFbpj;
    private OrdersEntity oe;
    public static final String ORDER = "order";
    public static final String ORDER_ID = "order_id";

    //评分
    private double speed, serve;
    private String SXNR;
    private String tag1, tag2, tag3, tag4, tag5, tag6 = "";//保存CheckBox选中的值
    private String anonymity, userNickName;
    private Info info;
    //总评分
    private double totalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_evaluate);
        ButterKnife.bind(this);
        info = ((MyApplication) getApplication()).getInfo();
        oe = getIntent().getParcelableExtra(ORDER);
        String orderId = getIntent().getStringExtra(ORDER_ID);
        if (oe == null && isEmpty(orderId)) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (oe != null) init();
        else getOrder(orderId);
        init();
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
        StringBuilder sb = new StringBuilder();
        sb.append(oe.getPostmanName())
                .append("  ")
                .append(oe.getPostmanPhone());
        tvCourierNamePhone.setText(sb.toString());
        merchantRatingBar.setCountSelected(5);
        merchantRatingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                int x = countSelected;
            }
        });
    }

    private void submit() {
        speed =rbSpeed.getCountSelected();
        serve=rbServe.getCountSelected();

//        Log.d("MyBaseAdapter", "建convertView,position="+String.valueOf((int) Math.ceil((speed+serve)/2)));
        getCheckBoxOnclick();
        SXNR = etXxnr.getText().toString();
        if (SXNR!=null&&!SXNR.equals("")){
        }else {
            SXNR="送货快，五星好评！！！";
        }
        try {
            JSONObject userJson = new JSONObject();
            userJson.put("orderId", oe.getId());
            userJson.put("userId", info.getId());
            userJson.put("name", info.getName());
            userJson.put("phone", info.getPhone());
            userJson.put("headerImg", info.getHeaderImg());
            userJson.put("grade", String.valueOf((int) Math.ceil((speed+serve)/2)));
            userJson.put("comment", SXNR);
            userJson.put("anonymity", anonymity);
            userJson.put("userNickName", userNickName);
            userJson.put("evaluateType", "express");
            userJson.put("typeId", oe.getPostmanId());
            userJson.put("typeName", oe.getPostmanName());
            userJson.put("tag1", tag1);
            userJson.put("tag2", tag2);
            userJson.put("tag3", tag3);
            userJson.put("tag4", tag4);
            userJson.put("tag5", tag5);
            userJson.put("tag6", tag6);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("evaluateStr", userJson.toString())
                    .build();
            Request request = new Request
                    .Builder().post(requestBody)
                    .url(Config.Url.getUrl(Config.EVALUATE))
                    .build();
            new OkHttpClient().newCall(request).enqueue(new SimpleCallback(this) {
                @Override
                public void onSuccess(String tag, JSONObject json) throws JSONException {
                    Toast.makeText(CourierEvaluateActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } catch (JSONException e) {
            Toast.makeText(this, "解析数据失败,请重试", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * checkbox点击事件
     */
    private void getCheckBoxOnclick() {
        tag1 = (cbShk.isChecked()) ? "1" : "0";
        tag2 = (cbZsdd.isChecked()) ? "1" : "0";
        tag3 = (cbShsm.isChecked()) ? "1" : "0";
        tag4 = (cbFywz.isChecked()) ? "1" : "0";
        tag5 = (cbCzzs.isChecked()) ? "1" : "0";
        tag6 = (cbSpbcwh.isChecked()) ? "1" : "0";
        if (cbNmpj.isChecked()) {
            anonymity = "no";
            userNickName = "匿名用户";
        } else {
            anonymity = "yes";
            userNickName = info.getNickName();
        }

    }

    //    @OnClick(R.id.back_kdypj)
//    public void onViewClicked() {
//        finish();
//    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_kdypj://返回
                finish();
                break;
            case R.id.tv_fbpj:
                submit();
                break;
        }
    }

}
