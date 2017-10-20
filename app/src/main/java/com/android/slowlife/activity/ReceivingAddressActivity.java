package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.NearbyAddressAdapter;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.objectmodel.AddressEntity;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.view.MyListView;
import com.slowlife.map.interfaces.APSImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/11 0011.
 */

public class ReceivingAddressActivity extends BaseActivity {
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.add_receiving_address)
    TextView addReceivingAddress;
    @Bind(R.id.address_text)
    TextView addressText;
    @Bind(R.id.relocation_image)
    ImageView relocationImage;
    @Bind(R.id.relocation_text)
    TextView relocationText;
    @Bind(R.id.listview)
    MyListView listview;
    @Bind(R.id.more_address)
    LinearLayout moreAddress;
    private NearbyAddressAdapter nearbyAddressAdapter;
    private APSImpl aps;
    private JSONArray addrs;
    private boolean loadding;
    private OkHttpClient okHttpClient;
    private JSONObject street;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_receiving_address);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            try {
                String addrStr = savedInstanceState.getString("addrs");
                String streetStr = savedInstanceState.getString("street");
                addrs = new JSONArray(addrStr);
                street = new JSONObject(streetStr);
            } catch (JSONException e) {
            }
        }
        nearbyAddressAdapter = new NearbyAddressAdapter(this, null);
        listview.setAdapter(nearbyAddressAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                finish();
            }
        });
        okHttpClient = new OkHttpClient();
        if (addrs != null) {
            initData();
            return;
        }
        final AMapLocation point = ((MyApplication) getApplication()).getLocation();
        if (point == null) {
            aps = new APSImpl(this) {
                @Override
                public void onLocationChanged(AMapLocation amapLocation) {
                    if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                        ((MyApplication)getApplication()).setLocation(amapLocation);
                        loadAddr(amapLocation);
                        aps.onPause();
                    }
                }
            };
            aps.onCreate();
        } else
            loadAddr(point);
    }

    private void loadAddr(AMapLocation point) {
        if (addrs != null || loadding) return;
        loadding = true;
        StringBuffer sb = new StringBuffer();
        sb.append("http://restapi.amap.com/v3/geocode/regeo?key=48efc81c9340b7c4f795405f83d19d6f&location=")
                .append(point.getLongitude())
                .append(",")
                .append(point.getLatitude())
                .append("&radius=500&extensions=all&batch=true&roadlevel=0&poitype=");
        Request request = new Request.Builder()
                .url(sb.toString())
                .tag("all").build();
        okHttpClient.newCall(request).enqueue(ReceivingAddressActivity.this);
    }

    private void initData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onPause() {
        aps.onPause();
        super.onPause();
    }

    /**
     * listview测试数据
     */
    private List<AddressEntity> getAddressInformation() {
        List<AddressEntity> list = new ArrayList<AddressEntity>();
        return list;
    }

    @OnClick({R.id.back_rl, R.id.add_receiving_address, R.id.relocation_text, R.id.more_address})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.add_receiving_address://新增收货地址
                intent = new Intent(this, NewAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.relocation_text:
                break;
            case R.id.more_address://更多地址
                intent = new Intent(this, MoreAddressActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String tag = response.request().tag().toString();

        final String jsonStr = response.body().string();
        System.out.println(response.request().url());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    System.out.println(json);
                    if (json.getInt("status") == 1) {
                        addrs = json.getJSONArray("roads");
                        street = json.getJSONObject("addressComponent").getJSONObject("streetNumber");
                        loadding = false;
                        initData();
                    } else {
                        Toast.makeText(ReceivingAddressActivity.this, "定位失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(ReceivingAddressActivity.this, "解析数据失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (addrs != null) outState.putString("addrs", addrs.toString());
        if (street != null) outState.putString("street", street.toString());
        super.onSaveInstanceState(outState);
    }
}
