package com.android.slowlife.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.Baseadapter;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.objectmodel.AddressEntity;
import com.google.gson.Gson;
import com.interfaceconfig.Config;
import com.slowlife.map.interfaces.APSImpl;
import com.slowlife.map.interfaces.APSInterface;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddrActivity extends BaseActivity implements APSInterface.OnApsChanged, CloudSearch.OnCloudSearchListener, Callback {
    @Bind(R.id.listview)
    ListView listView;
    private APSImpl aps;
    private CloudSearch mCloudSearch;
    private List<CloudItem> clouds = new ArrayList<>();
    private List<AddressEntity> addrs = new ArrayList<>();
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addr);
        ButterKnife.bind(this);
        final Info info = ((MyApplication) getApplication()).getInfo();
        if (info == null) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId()).build();
        final Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.ADDRLIST)).tag("get").post(requestBody).build();
        new OkHttpClient().newCall(request).enqueue(this);
        aps = new APSImpl(this);
        aps.addListener(this);
        aps.onCreate();
        mCloudSearch = new CloudSearch(this);// 初始化查询类
        mCloudSearch.setOnCloudSearchListener(this);// 设置回调函数
        adapter = new Adapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                if (position < addrs.size()) {
                    intent.putExtra("address", addrs.get(position));
                } else if (position > addrs.size() + 1) {
                    intent.putExtra("cloud", clouds.get(position - addrs.size() - 1));
                } else return;
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    protected void onSuccess(Call call, Response response, JSONObject json) throws JSONException {

        JSONArray arr = json.getJSONArray("address");
        addrs.clear();
        Gson gson = new Gson();
        for (int i = 0; i < arr.length(); i++) {
            addrs.add(gson.fromJson(arr.getString(i), AddressEntity.class));
        }
        adapter.notifyDataSetChanged();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {      //  搜索

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onChanged(AMapLocation location) {

        CloudSearch.SearchBound bound = new CloudSearch.SearchBound(location.getCity());// 输入city “全国”，为本表全部搜索。
        CloudSearch.Query mQuery = null;
        try {
            mQuery = new CloudSearch.Query("", "公园", bound);
            mCloudSearch.searchCloudAsyn(mQuery);
        } catch (AMapException e) {
        }


    }

    @Override
    public void Fail() {

    }

    @Override
    public void onCloudSearched(CloudResult cloudResult, int i) {
        clouds = cloudResult.getClouds();
    }

    @Override
    public void onCloudItemDetailSearched(CloudItemDetail cloudItemDetail, int i) {

    }


    class Adapter extends Baseadapter {

        @Override
        public int getCount() {
            return addrs.size() + clouds.size();
        }

        @Override
        public Object getItem(int position) {
            if (position == addrs.size()) return null;
            else if (position > addrs.size()) return clouds.get(position);
            else return addrs.get(position);
        }

        public Adapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AddrVH holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_addr_list, null);
                holder = new AddrVH();
                holder.pro = (TextView) convertView.findViewById(R.id.pro);
                holder.street = (TextView) convertView.findViewById(R.id.street);
                holder.addr = convertView.findViewById(R.id.addr);
                holder.other = convertView.findViewById(R.id.other);
                convertView.setTag(holder);
            } else {
                holder = (AddrVH) convertView.getTag();
            }
            if (position < addrs.size()) {
                AddressEntity addressEntity = addrs.get(position);
                holder.street.setText(addressEntity.getStreet());
                if (holder.addr.getVisibility() == View.GONE)
                    holder.addr.setVisibility(View.VISIBLE);
                if (holder.other.getVisibility() == View.VISIBLE)
                    holder.other.setVisibility(View.GONE);
                holder.pro.setText(addressEntity.getDistrict());
            } else if (position == addrs.size()) {
                if (holder.other.getVisibility() == View.GONE)
                    holder.other.setVisibility(View.VISIBLE);
                if (holder.addr.getVisibility() == View.VISIBLE)
                    holder.addr.setVisibility(View.GONE);
                if (convertView.isEnabled())
                    convertView.setEnabled(false);
            } else {
                if (holder.addr.getVisibility() == View.GONE)
                    holder.addr.setVisibility(View.VISIBLE);
                if (holder.other.getVisibility() == View.VISIBLE)
                    holder.other.setVisibility(View.GONE);
                if (!convertView.isEnabled()) convertView.setEnabled(true);
                CloudItem item = clouds.get(position - addrs.size() + 1);
                holder.street.setText(item.getTitle());
                holder.pro.setText(item.getDistance());
            }

            return convertView;
        }

        @Override
        protected View getView(View view, int position) {
            return null;
        }

        @Override
        protected int getLayoutId(int position) {
            return 0;
        }
    }

    class AddrVH {
        TextView street, pro;
        View other, addr;
    }


    @Override
    public void onFailure(final Call call, final IOException e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AddrActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                onFail(call, e);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        XGPushManager.onActivityStarted(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        aps.onPause();
        XGPushManager.onActivityStoped(this);
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        final String jsonStr = response.body().string();
        System.out.println(jsonStr);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    if (json.getInt("statusCode") == 200)
                        onSuccess(call, response, json);
                    else onFail(call, null);
                } catch (JSONException e) {
                    Toast.makeText(AddrActivity.this, "解析数据失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public void onFail(Call call, IOException e) {

    }

}
