package com.android.slowlife.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.slowlife.R;
import com.android.slowlife.activity.LoginActivity;
import com.android.slowlife.adapter.Baseadapter;
import com.android.slowlife.adapter.UseRpkgAdapter;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.objectmodel.RPkg;
import com.google.gson.Gson;
import com.interfaceconfig.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by sgrape on 2017/6/5.
 * e-mail: sgrape1153@gmail.com
 */
@SuppressLint("ValidFragment")
public class UseRedPkgFrag extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private String type;
    protected Baseadapter adapter;
    protected ArrayList<RPkg> data;
    protected SwipeRefreshLayout srl;
    protected OkHttpClient okHttpClient;
    private Handler handler;
    private Callback httpCallback;
    private ListView listView;

    public UseRedPkgFrag(String status) {
        Bundle bundle = new Bundle();
        bundle.putString("arg", status);
        this.type = status;
        setArguments(bundle);
    }

    public UseRedPkgFrag() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        srl = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_use_red_packet, null);
        srl.setOnRefreshListener(this);
        return srl;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getString("arg");
        listView = (ListView) view.findViewById(R.id.red_packet_listview);
        if (adapter == null) {
            adapter = new UseRpkgAdapter(view.getContext(), data, type);
            listView.setAdapter(adapter);
        }
        if (listView.getAdapter() == null) listView.setAdapter(adapter);
        if (okHttpClient == null) okHttpClient = new OkHttpClient();
        if (handler == null) handler = new Handler();
        if (httpCallback == null) httpCallback = new HttpCallback();
        if (data == null) {
            data = getArguments().getParcelableArrayList("pkgs");
            adapter.notifyDataSetChanged(data);
            if (data == null) initData();
        }
    }


    protected void initData() {
        if (((MyApplication) getContext().getApplicationContext()).getInfo() == null) {
            return;
        }
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", ((MyApplication) getContext().getApplicationContext()).getInfo().getId())
                .addFormDataPart("type", type).build();
        Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.REDPKG))
                .post(requestBody)
                .tag(type)
                .build();
        okHttpClient.newCall(request).enqueue(httpCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getArguments().putParcelableArrayList("pkgs", data);
    }

    private void exec(int position) {

    }

    @Override
    public void onRefresh() {
        initData();
    }

    class HttpCallback implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    if (srl.isRefreshing())
                        srl.setRefreshing(false);
                }
            });
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            final int code = response.code();
            final String result = response.body().string();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    UseRedPkgFrag.this.onResponse(code, result);
                }
            });
        }
    }


    protected void onResponse(int responseCode, String result) {
        if (responseCode != 200) {
            Toast.makeText(getContext(), "服务器错误", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            JSONArray arr = new JSONObject(result).getJSONArray("RedPackets");
            Gson gson = new Gson();
            if (data == null) data = new ArrayList<>();
            data.clear();
            System.out.println(arr.toString());
            for (int i = 0; i < arr.length(); i++) {
                final JSONObject item = arr.getJSONObject(i);
                if (item.has("gainNumber"))
                    item.put("name", String.format("%s (库存%s)", item.getString("name"), item.getString("gainNumber")));
                data.add(gson.fromJson(arr.getString(i), RPkg.class));
            }
            adapter.notifyDataSetChanged(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        srl.setRefreshing(false);
    }

    @Override
    public Context getContext() {
        if (super.getContext() != null) return super.getContext();
        if (super.getActivity() != null) return super.getActivity();
        if (getView() != null) return getView().getContext();
        return super.getContext();
    }

}
