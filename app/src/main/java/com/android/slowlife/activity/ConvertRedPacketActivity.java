package com.android.slowlife.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.Baseadapter;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.objectmodel.IntegralRpkg;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.SimpleCallback;
import com.google.gson.Gson;
import com.interfaceconfig.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/1/25 0025.
 */

public class ConvertRedPacketActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout go_back;
    private ListView listView;
    private Adapter adapter;
    private TextView integral;
    private AMapLocation mapLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_convert_red_packet);
        initView();
        loadAccountInfo();
        mapLocation=((MyApplication) getApplication()).getLocation();
        adapter = new Adapter();
        listView.setAdapter(adapter);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type", "IntegralExchange")
                .addFormDataPart("pro", mapLocation.getProvince())
                .addFormDataPart("city", mapLocation.getCity())
                .addFormDataPart("district", mapLocation.getDistrict())
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(Config.Url.getUrl(Config.GETREDPACKETS))
                .build();
        new OkHttpClient().newCall(request).enqueue(new SimpleCallback(this) {
            @Override
            public void onSuccess(String tag, JSONObject json) throws JSONException {
                JSONArray arr = json.getJSONArray("RedPackets");
                Gson gson = new Gson();
                List<IntegralRpkg> bean = new ArrayList<IntegralRpkg>();
                for (int i = 0; i < arr.length(); i++)
                    bean.add(gson.fromJson(arr.getString(i), IntegralRpkg.class));
                adapter.notifyDataSetChanged(bean);
            }

        });
    }

    private void loadAccountInfo() {
        final Info info = ((MyApplication) getApplication()).getInfo();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId())
                .build();
        Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.ACCOUNT))
                .post(requestBody).build();
        new OkHttpClient().newCall(request).enqueue(new SimpleCallback(this) {
            @Override
            public void onSuccess(String tag, JSONObject json) throws JSONException {
                int tem = json.getInt("integral");
                integral.setText(String.valueOf(tem));     // 积分
            }

        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.convert_listview);
        integral = (TextView) findViewById(R.id.integral);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
        }
    }

    /**
     * dialog窗口
     *
     * @param bean
     */
    private void delgeteDialog(final IntegralRpkg bean) {
        Info info = ((MyApplication) getApplication()).getInfo();
        if (info == null) {
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
        new Dia(this,bean).show();
    }


    class Adapter extends Baseadapter<IntegralRpkg> {
        public Adapter() {
            super(ConvertRedPacketActivity.this);
        }

        @Override
        protected View getView(View view, int position) {
            ViewHolder holder = (ViewHolder) view.getTag();
            if (holder == null) {
                holder = new ViewHolder(view);
                view.setTag(holder);
            }
            IntegralRpkg bean = list.get(position);
            holder.position = position;
            holder.price.setText(String.valueOf(bean.getCost()));
            holder.time.setText(String.format("%s - %s", bean.getStartTime(), bean.getEndTime()));
            holder.phone.setText(String.format("%d积分兑换", bean.getIntegral()));
            return view;
        }

        @Override
        protected int getLayoutId(int position) {
            return R.layout.item_use_red_packet;
        }

        class ViewHolder {
            private TextView availability;
            private TextView time;
            private TextView phone;
            private TextView price;
            private TextView yes_use;
            private ImageView no_use;
            private LinearLayout is_use;
            private TextView is_use_text;
            private int position;

            public ViewHolder(View view) {
                availability = (TextView) view.findViewById(R.id.availability_text);
                time = (TextView) view.findViewById(R.id.time_text);
                price = (TextView) view.findViewById(R.id.price_text);
                phone = (TextView) view.findViewById(R.id.phone_text);
                yes_use = (TextView) view.findViewById(R.id.yes_use_text);
                yes_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delgeteDialog(list.get(position));
                    }
                });
                yes_use.setText("立即兑换");
                yes_use.setVisibility(View.VISIBLE);
                no_use = (ImageView) view.findViewById(R.id.no_use_image);
                is_use = (LinearLayout) view.findViewById(R.id.is_use_layout);
                is_use.setBackgroundResource(R.drawable.rpkg_unuse);
            }
        }


    }

    private void exec(IntegralRpkg bean) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", ((MyApplication) getApplication()).getInfo().getId())
                .addFormDataPart("redPacketsId", bean.getId())
                .build();
        Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.EXCHANGEREDPACKETS))
                .post(requestBody)
                .build();
        new OkHttpClient().newCall(request).enqueue(new SimpleCallback(ConvertRedPacketActivity.this) {
            @Override
            public void onSuccess(String tag, JSONObject json) throws JSONException {
                Toast.makeText(ConvertRedPacketActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                loadAccountInfo();
            }
        });
    }


    class Dia extends Dialog {
        private IntegralRpkg bean;

        public Dia(@NonNull Context context, IntegralRpkg bean) {
            super(context,R.style.DialogStyle);
            this.bean = bean;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_convert_red_packet);
            Button mYes = (Button) findViewById(R.id.sure_bt);
            Button mNo = (Button) findViewById(R.id.cancel_bt);
            TextView text = (TextView) findViewById(R.id.covert_text);
            View view = findViewById(R.id.view);
            if (Integer.parseInt(integral.getText().toString().trim()) < bean.getIntegral()) {//积分数小于兑换积分时
                text.setText("当前积分不足，不能兑换红包");
                mYes.setVisibility(View.GONE);
                mNo.setText("知道了");
                view.setVisibility(View.GONE);
            }
            mYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exec(bean);
                    dismiss();
                }
            });
            mNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }
}
