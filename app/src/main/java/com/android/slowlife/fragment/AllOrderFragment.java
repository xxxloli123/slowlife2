package com.android.slowlife.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.slowlife.BaseFragment;
import com.android.slowlife.R;
import com.android.slowlife.activity.OrderDetailsActivity;
import com.android.slowlife.adapter.OrdersAdapter;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.objectmodel.OrdersEntity;
import com.android.slowlife.util.SimpleCallback;
import com.google.gson.Gson;
import com.interfaceconfig.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/1/19 .
 */
public class AllOrderFragment extends BaseFragment implements AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    protected View rootView;
    @Bind(R.id.orderList)
    ListView listView;
    @Bind(R.id.srl)
    SwipeRefreshLayout srl;
    protected OrdersAdapter ordersAdapter;
    protected List<OrdersEntity> list;
    protected int page = 0;
    protected int pageSize = 20;
    protected Info info;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order, container, false);
        srl = (SwipeRefreshLayout) rootView.findViewById(R.id.srl);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        srl = (SwipeRefreshLayout) rootView.findViewById(R.id.srl);
        srl.setOnRefreshListener(this);
        initPagers();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && rootView != null) {
            onRefresh();
        }
    }

    protected void loadData() {
        info = ((MyApplication) getContext().getApplicationContext()).getInfo();
        if (info == null) {
            if (ordersAdapter.getCount() != 0)
                ordersAdapter.notifyDataSetChanged(null);
            if (srl != null && srl.isRefreshing()) srl.setRefreshing(false);
            return;
        }
        Bundle bundle = getArguments();
        String paramName = bundle.getString("paramName");
        String param = bundle.getString(paramName);
        String type = "CityWide,Intercity";
        String status = "UnReceivedOrder,ReceivedOrder,GoodsDelivery,CancelOrder,Complete,UnPayed";
        if (TextUtils.equals(paramName, "type")) {
            type = param;
        } else if (TextUtils.equals(paramName, "status")) status = param;


        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId())
                .addFormDataPart("type", type)
                .addFormDataPart("pageNum", String.valueOf(++page))
                .addFormDataPart("status", status)
                .addFormDataPart("numPerPage", String.valueOf(pageSize)).build();
        Request request = new Request.Builder().url(Config.Url.getUrl(Config.ORDERLIST)).tag(Config.ORDERLIST).post(requestBody).build();
        new OkHttpClient().newCall(request).enqueue(new Callback(getContext(), srl));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (rootView != null && getUserVisibleHint())
            onRefresh();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        rootView = null;
    }

    /**
     * 根据选择的不同界面选择不同tabs
     */
    protected void initPagers() {
        ordersAdapter = new OrdersAdapter(getActivity(), list);
        listView.setAdapter(ordersAdapter);
        listView.setOnItemClickListener(this);
    }

    public static AllOrderFragment newInstance(String pName, String params) {
        Bundle args = new Bundle();
        args.putString(pName, params);
        args.putString("paramName", pName);
        AllOrderFragment fragment = new AllOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OrdersEntity oe = list.get(position);
        Intent intent = new Intent(getContext(), OrderDetailsActivity.class);
        intent.putExtra(OrderDetailsActivity.ORDER, oe);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        page = 0;
        loadData();
    }

    class Callback extends SimpleCallback {
        public Callback(Context context, SwipeRefreshLayout srl) {
            super(context, srl);
        }

        @Override
        public void onSuccess(String tag, JSONObject jsonObject) throws JSONException {
            switch (tag) {
                case Config.ORDERLIST:
                    JSONArray os = jsonObject.getJSONObject("ordersInfo").getJSONArray("aaData");
                    Gson gson = new Gson();
                    if (list == null) list = new ArrayList<>();
                    if (page == 1) list.clear();
                    for (int i = 0; i < os.length(); i++)
                        list.add(gson.fromJson(os.getJSONObject(i).toString(), OrdersEntity.class));
                    ordersAdapter.notifyDataSetChanged(list);
                    break;
            }
        }


        @Override
        public void onFail(JSONObject json) throws JSONException {
            super.onFail(json);
            if (srl != null && srl.isRefreshing()) srl.setRefreshing(false);
        }
    }

    @Override
    public Context getContext() {
        if (getView() != null) return getView().getContext();
        if (super.getContext() != null) return super.getContext();
        if (getActivity() != null) return getActivity();
        if (srl != null) return srl.getContext();
        return null;
    }
}
