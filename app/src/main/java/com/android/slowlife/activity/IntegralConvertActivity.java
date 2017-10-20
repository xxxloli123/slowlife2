package com.android.slowlife.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.UseRpkgAdapter;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.objectmodel.RPkg;
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
 * Created by Administrator on 2017/1/31 0031.
 */

public class IntegralConvertActivity extends BaseActivity
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        UseRpkgAdapter.OnClick<RPkg> {

    private RelativeLayout go_back;
    private ListView listView;
    private UseRpkgAdapter adapter;
    private SwipeRefreshLayout srl;
    private List<RPkg> list = new ArrayList<>();
    private OkHttpClient httpClient;

    private AMapLocation mapLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_integral_convert);
        initView();
        mapLocation=((MyApplication) getApplication()).getLocation();
        Log.e("定位",""+mapLocation);
        adapter = new UseRpkgAdapter(this, list, "pull");
        adapter.setListener(this);
        listView.setAdapter(adapter);
        httpClient = new OkHttpClient();
        initData();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        Intent intent = getIntent();
        listView = (ListView) findViewById(R.id.listview);
        srl = findView(R.id.srl);
        srl.setOnRefreshListener(this);
        srl.setRefreshing(true);
    }


    private void initData() {
//        type=PackageNumber,DayLanding,Unlimited
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type", "PackageNumber,DayLanding,Unlimited")
                .addFormDataPart("pro", mapLocation.getProvince())
                .addFormDataPart("city", mapLocation.getCity())
                .addFormDataPart("district", mapLocation.getDistrict())
                .build();
        Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.GETREDPACKETS))
                .post(requestBody)
                .tag(Config.GETREDPACKETS)
                .build();
        httpClient.newCall(request).enqueue(new Callback(this, srl));
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl://返回
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void click(int position, RPkg rPkg) {
        Info info = ((MyApplication) getApplication()).getInfo();
        if (info == null) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId())
                .addFormDataPart("redIds",rPkg.getId())
                .build();
        Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.PULLDOWNPKG))
                .tag(Config.REDPKG)
                .post(requestBody)
                .build();
        httpClient.newCall(request).enqueue(new Callback(this, null));
    }

    class Callback extends SimpleCallback {
        public Callback(Context context, SwipeRefreshLayout srl) {
            super(context, srl);
        }

        @Override
        public void onSuccess(String tag, JSONObject json) throws JSONException {
            switch (tag) {
                case Config.GETREDPACKETS:
                    JSONArray arr = json.getJSONArray("RedPackets");
                    Gson gson = new Gson();
                    list.clear();
                    System.out.println(arr.toString());
                    for (int i = 0; i < arr.length(); i++) {
                        final JSONObject item = arr.getJSONObject(i);
                        if (item.has("gainNumber"))
                            item.put("name", String.format("%s", item.getString("name")));
                        list.add(gson.fromJson(arr.getString(i), RPkg.class));
                    }
                    adapter.notifyDataSetChanged(list);
                    break;
                case Config.REDPKG:
                    Toast.makeText(IntegralConvertActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
