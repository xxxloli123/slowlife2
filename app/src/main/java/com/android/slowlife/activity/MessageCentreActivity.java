package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.MessageCentreAdapter;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.objectmodel.MessgeEntity;
import com.android.slowlife.util.SimpleCallback;
import com.google.gson.Gson;
import com.interfaceconfig.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/1/25 0025.
 */

public class MessageCentreActivity extends BaseActivity implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    private MessageCentreAdapter adapter;
    @Bind(R.id.listview)
    ListView listView;
    @Bind(R.id.srl)
    SwipeRefreshLayout srl;
    private int page;
    private final String pageSize = "20";
    private OkHttpClient okHttpClient;
    private Info info;
    private List<MessgeEntity> msgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_centre);
        adapter = new MessageCentreAdapter(this, null);
        listView.setAdapter(adapter);
        srl.setOnRefreshListener(this);
        okHttpClient = new OkHttpClient();
        info = ((MyApplication) getApplication()).getInfo();
        if (info == null) {
            finish();
            return;
        }
        onRefresh();
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
        }
    }

    private void loadData() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId())
                .addFormDataPart("pageNum", String.valueOf(++page))
                .addFormDataPart("numPerPage", pageSize)
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(Config.Url.getUrl(Config.MESSAGE))
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    @Override
    public void onRefresh() {
        page = 0;
        loadData();
    }

    private Callback callback = new SimpleCallback() {
        @Override
        public void onSuccess(String tag, JSONObject json) throws JSONException {
            System.out.println(json);
            JSONArray arr = json.getJSONArray("listMessage");
            if (msgs == null) msgs = new ArrayList<>();
            if (page == 1) msgs.clear();
            Gson gson = new Gson();
            for (int i = 0; i < arr.length(); i++) {
                msgs.add(gson.fromJson(arr.getString(i), MessgeEntity.class));
            }
            adapter.notifyDataSetChanged(msgs);
            if (srl != null && srl.isRefreshing()) srl.setRefreshing(false);
        }

        @Override
        public void onFail(Call call) {
            if (srl.isRefreshing()) srl.setRefreshing(false);
            super.onFail(call);
        }

        @Override
        public void onFail(JSONObject json) throws JSONException {
            if (srl.isRefreshing()) srl.setRefreshing(false);
            super.onFail(json);
        }

        @Override
        public void onFailure(Call call, IOException e) {
            if (srl.isRefreshing()) srl.setRefreshing(false);
            super.onFailure(call, e);
        }
    };

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount + 5 > totalItemCount
                && totalItemCount % 20 == 0 && totalItemCount > 0) {
            loadData();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra(OrderDetailsActivity.ORDER_ID, msgs.get(position).getOrderId());
        startActivity(intent);
    }
}
