package com.android.slowlife.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.activity.LoginActivity;
import com.android.slowlife.activity.PayActivity;
import com.android.slowlife.adapter.OrdersAdapter;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.objectmodel.OrdersEntity;
import com.android.slowlife.util.Common;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sgrape on 2017/6/7.
 * e-mail: sgrape1153@gmail.com
 */

public class WOrderFrag extends AllOrderFragment {
    @Bind(R.id.selectAll)
    CheckBox checkBox;
    @Bind(R.id.total)
    TextView total;
    @Bind(R.id.pay)
    View pay;
    private boolean isFirst;

    public WOrderFrag() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isFirst = getArguments().getBoolean("isFirst");
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_payorder, container, false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                (rootView.findViewById(R.id.ll)).setPadding(0, Common.getStatusHeight(getActivity()), 0, 0);
            }
        }
        ButterKnife.bind(this, rootView);
        if (listView == null) listView = (ListView) rootView.findViewById(R.id.orderList);
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void setArguments(Bundle args) {
        args.putString("paramName", "status");
        args.putString("status", "UnPayed");
        super.setArguments(args);
    }

    @OnClick({R.id.pay, R.id.selectAll})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay:
                pay();
                break;
            case R.id.selectAll:
                for (int i = 0; i < listView.getCount(); i++) {
                    listView.setItemChecked(i, checkBox.isChecked());
                }
                checkT();
                if (listView.getCheckedItemCount() > 0)
                    pay.setEnabled(checkBox.isChecked());
                break;
        }
    }

    @Override
    protected void loadData() {
        info = ((MyApplication) getContext().getApplicationContext()).getInfo();
        if (!isFirst && info == null) {
            isFirst = true;
            getArguments().putBoolean("isFirst", isFirst);
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            srl.setRefreshing(false);
        }
        super.loadData();
    }

    private void pay() {
        ArrayList<OrdersEntity> orders = new ArrayList<>();
        SparseBooleanArray positions = listView.getCheckedItemPositions();
        for (int i = 0; i < list.size(); i++) {
            if (positions.get(i)) orders.add(list.get(i));
        }

        Intent intent = new Intent(getContext(), PayActivity.class);
        intent.putExtra("orders", orders);
        startActivity(intent);
    }

    @Override
    protected void initPagers() {
        ordersAdapter = new Adapter(getView().getContext(), list);
        listView.setAdapter(ordersAdapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listView.getCheckedItemCount() > 0) {
            if (!pay.isEnabled()) pay.setEnabled(true);
        } else {
            if (pay.isEnabled()) pay.setEnabled(false);
        }
        if (listView.getCount() == listView.getCheckedItemCount()) {
            checkBox.setChecked(true);
        } else {
            if (checkBox.isChecked()) checkBox.setChecked(false);
        }
        checkT();
    }

    private void checkT() {
        double total = 0.0d;
        SparseBooleanArray checkedPosition = listView.getCheckedItemPositions();
        for (int i = 0; i < ordersAdapter.getCount(); i++) {
            if (checkedPosition.get(i)) {
                final OrdersEntity oe = ordersAdapter.getItem(i);
                total += oe.getUserActualFee();
            }
        }
        DecimalFormat df = new DecimalFormat("0.00");
        this.total.setText(String.format("总价: %s", df.format(total)));
    }


    public static class Adapter extends OrdersAdapter {

        DecimalFormat df = new DecimalFormat("¥0.00");

        public Adapter(Context mContext, List<OrdersEntity> list) {
            this(mContext, list, true);
        }

        public Adapter(Context payActivity, List<OrdersEntity> orders, boolean b) {
            super(payActivity, orders, b);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder holder;
            OrdersEntity oe = list.get(position);
            if (view == null) {
                view = inflater.inflate(R.layout.item_worders, parent, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else holder = (ViewHolder) view.getTag();
            holder.name.setText(oe.getReceiverName());
            holder.weight.setText("物件重量: " + oe.getWeight() + "kg");
            holder.addr.setText(String.format("%s%s%s%s%s",
                    oe.getEndPro(), oe.getEndCity(), oe.getEndDistrict()
                    , oe.getEndStreet(), oe.getEndHouseNumber()));
            holder.price.setText(df.format(oe.getUserActualFee()));
            holder.time.setText("下单时间: " + oe.getCreateDate());
            if (showCb) {
                if (holder.checkbox.getVisibility() != View.VISIBLE)
                    holder.checkbox.setVisibility(View.VISIBLE);
            } else {
                if (holder.checkbox.getVisibility() == View.VISIBLE)
                    holder.checkbox.setVisibility(View.GONE);
            }
            return view;
        }
    }

    public static class ViewHolder {
        @Bind(R.id.name_text)
        TextView name;
        @Bind(R.id.item_order_addr)
        TextView addr;
        @Bind(R.id.item_order_weight)
        TextView weight;
        @Bind(R.id.item_order_time)
        TextView time;
        @Bind(R.id.item_order_price)
        TextView price;
        @Bind(R.id.checkbox)
        View checkbox;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
