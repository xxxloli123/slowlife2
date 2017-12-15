package com.android.slowlife.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DisplayUtil;
import com.android.slowlife.BaseFragment;
import com.android.slowlife.DoneDialog;
import com.android.slowlife.MsgDialog;
import com.android.slowlife.R;
import com.android.slowlife.activity.CityExpressActivity;
import com.android.slowlife.activity.ReceiptAddressActivity;
import com.android.slowlife.activity.StartAddressActivity;
import com.android.slowlife.adapter.DoorPickingAdapter;
import com.android.slowlife.adapter.SelectProvinceAdapter;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.objectmodel.AddressEntity;
import com.android.slowlife.util.SimpleCallback;
import com.google.gson.Gson;
import com.interfaceconfig.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Administrator on 2017/3/14 0014.
 */

public class PickupHomeFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.cargo_weight)
    TextView cargoWeight;
    @Bind(R.id.delivery_address)
    TextView deliveryAddress;
    @Bind(R.id.delivery_address_rl)
    RelativeLayout deliveryAddressRl;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.urgent_rl)
    RelativeLayout urgentRl;
    @Bind(R.id.tableLayout)
    LinearLayout tableLayout;
    @Bind(R.id.start_address)
    TextView startAddress;
    @Bind(R.id.start_address_rl)
    RelativeLayout startAddressRl;
    private DoorPickingAdapter adapter;
    private Info info;
    private JSONObject json;
    private JSONArray areas;
    private AddressEntity addressFQ;
    private AddressEntity addressEntity;
    private View sureBt;
    private int weight = 5;
    private Map<String,AddressEntity> addressEntityMap=new HashMap<String,AddressEntity>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pickup_home, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new DoorPickingAdapter(view.getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        info = ((MyApplication) getContext().getApplicationContext()).getInfo();
        if (info == null) {
            return;
        }
        if (addressFQ != null) return;

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId()).build();
        final Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.ADDRLIST)).tag(Config.ADDRLIST).post(requestBody).build();
        new OkHttpClient().newCall(request).enqueue(new Callback(getContext()));
    }
//    /**
//     * 选择区域dialog窗口
//     */
//    private void selectProvinceDialog() {
//        if (json == null) {
//            Toast.makeText(getContext(), "请选选择收货地址", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        LinearLayout mDialog = (LinearLayout) inflater.inflate(R.layout.dialog_select_area, null);
//        final Dialog dialog = new AlertDialog.Builder(getActivity(), R.style.DialogStyle).create();
//        RelativeLayout delete = (RelativeLayout) mDialog.findViewById(R.id.back_rl);
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
////        TextView price = (TextView) mDialog.findViewById(R.id.price_text);
//        final SelectProvinceAdapter adapter = new SelectProvinceAdapter(getActivity(), areas);
//        ListView listView = (ListView) mDialog.findViewById(R.id.listview);
//        listView.setAdapter(adapter);
//        listView.setItemsCanFocus(false);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                try {
//                    dialog.dismiss();
//                    selectedPosition = position;
//                    JSONObject addr = areas.getJSONObject(position);
//                    selectArea.setText(String.format("%s %s", addr.getString("endDistrict"), addr.getString("endStreet")));
//                    imputedPrice();
//                } catch (JSONException e) {
//                }
//            }
//        });
//        dialog.show();
//        if (selectedPosition != -1) listView.setItemChecked(selectedPosition, true);
//        dialog.getWindow().setContentView(mDialog);
//    }

    @OnClick({R.id.delivery_address_rl, R.id.minus, R.id.add,R.id.start_address_rl})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.voice_layout://语音
                Toast.makeText(getActivity(), "语音", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delivery_address_rl://收货地址
                if (addressEntity!=null){}
                else {
                    Toast.makeText(getContext(), "请先选择发货地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), ReceiptAddressActivity.class);
                intent.putExtra(ReceiptAddressActivity.SELECT_ADDRESS, true);
                intent.putExtra(ReceiptAddressActivity.SELECTED_ITEM, addressFQ == null ? null : addressFQ.getId());
                startActivityForResult(intent, 10003);
                break;
            case R.id.sure_bt://我要寄件
                sureBt = view;
                submit();
                break;
            case R.id.minus://减
                if (weight > 5) {
                    weight--;
                    Log.e("重量",""+weight);
                }
                cargoWeight.setText(weight + "");
                imputedPrice();
                break;
            case R.id.add://加
                weight++;
                cargoWeight.setText(weight + "");
                Log.e("重量",""+weight);
                imputedPrice();
                break;
            case R.id.start_address_rl:

                Intent startIntent = new Intent(getContext(), ReceiptAddressActivity.class);
                //intent代入值
                startIntent.putExtra(ReceiptAddressActivity.START_ADDRESS, true);
                startIntent.putExtra(ReceiptAddressActivity.SELECTED_ITEM, addressEntity == null ? null : addressEntity.getId());
                startActivityForResult(startIntent, 23333);
        }
    }

    private void submit() {
        if (isEmpty(info.getPhone())) {
            Toast.makeText(getContext(), "请先绑定手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (addressFQ != null) {

            }else {
                Toast.makeText(getContext(), "请选择收货地址", Toast.LENGTH_SHORT).show();
                return;
            }

            addressEntity=addressEntityMap.get("start");
            if (addressEntity!=null) {

            }else {
                Toast.makeText(getContext(), "请选择发货地址", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!((CityExpressActivity) getActivity()).isChecked()) {
                Toast.makeText(getContext(), "请阅读服务协议", Toast.LENGTH_SHORT).show();
                return;
            }
            final JSONObject params = new JSONObject();
            JSONObject json = this.json.getJSONArray("tariff").getJSONObject(0);
            params.put("startPro", addressEntity.getPro());
            params.put("startCity", addressEntity.getCity());
            params.put("startDistrict", addressEntity.getDistrict());
            params.put("startStreet", addressEntity.getStreet());
            params.put("startHouseNumber", addressEntity.getHouseNumber());
            params.put("startLng", String.valueOf(addressEntity.getLng()));
            params.put("startLat", String.valueOf(addressEntity.getLat()));
            params.put("userChoiceCommpanyId", json.getString("expressCompanyId"));
            params.put("userChoiceCommpanyCode", "ZSHHD");
            params.put("userChoiceCommpanyName", json.getString("expressCompanyName"));
            params.put("createUserId", info.getId());
            params.put("createUserName", addressEntity.getPersonname());
            params.put("createUserPhone", addressEntity.getPersonphone());
            params.put("receiverName", addressFQ.getPersonname());
            params.put("receiverPhone", addressFQ.getPersonphone());
            params.put("endPro", addressFQ.getPro());
            params.put("endCity", addressFQ.getCity());
            params.put("endDistrict", addressFQ.getDistrict());
            params.put("endStreet", addressFQ.getStreet());
            params.put("endHouseNumber", addressFQ.getHouseNumber());
            params.put("endLng", String.valueOf(addressFQ.getLng()));
            params.put("endtLat", String.valueOf(addressFQ.getLat()));
            params.put("urgent", checkbox.isChecked() ? "yes" : "no");
            params.put("weight",weight);
            final String costStr = this.json.getJSONObject("urgent").getString("urgentCost");
            float cost = 0.0f;
            try {
                cost = Float.valueOf(costStr);
            } catch (Exception e) {
            }
            params.put("urgentFee", checkbox.isChecked() ? cost : 0);
            params.put("type", "CityWide");
            new DoneDialog(getContext()).setMessage("确定创建订单吗?\n费用由快递员上门后收取").setListener(
                    new DoneDialog.DialogButtonClickListener() {
                        @Override
                        public void done(Dialog dialog, Object tag) {
                            dialog.dismiss();
                            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("ordeStr", params.toString())
                                    .addFormDataPart("appVersion", "20171214")
                                    .build();
                            Request request = new Request.Builder().post(requestBody)
                                    .url(Config.Url.getUrl(Config.CREATEORDER))
                                    .tag(Config.CREATEORDER)
                                    .build();
                            new OkHttpClient().newCall(request).enqueue(new Callback(getContext()));
                            if (sureBt != null) sureBt.setEnabled(false);
                        }

                        @Override
                        public void cancel(Dialog dialog, Object tag) {
                            dialog.dismiss();
                        }
                    }).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (tableLayout != null) {
            int height = 0;
            for (int i = 0; i < tableLayout.getChildCount(); i++) {
                final View child = tableLayout.getChildAt(i);
                height += child.getMeasuredHeight();
            }
            if (tableLayout.getMeasuredHeight() != height) {
                tableLayout.measure(tableLayout.getMeasuredWidth(), height);
                tableLayout.requestLayout();
            }
        }
    }


    private void init() {
        try {
            if (tableLayout.getChildCount() > 1)
                tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
            Context context = getActivity();
            areas = json.getJSONArray("tariff");
            int pad = DisplayUtil.dip2px(context, 6);
            int hei = DisplayUtil.dip2px(context, 30);
            int width = tableLayout.getWidth();
            for (int i = 0; i < areas.length(); i++) {
                final JSONObject a = areas.getJSONObject(i);
                final LinearLayout tableRow = new LinearLayout(context);
                tableRow.setOrientation(LinearLayout.HORIZONTAL);
                tableRow.setGravity(Gravity.CENTER_VERTICAL);
                final TextView area = new TextView(context);
                final TextView ps = new TextView(context);
                final TextView pe = new TextView(context);
                final View l1 = new View(context);
                final View l2 = new View(context);
                final View l3 = new View(context);
                area.setTextSize(12);
                ps.setTextSize(12);
                pe.setTextSize(12);
                l3.setBackgroundResource(R.color.line_color);
                tableLayout.addView(l3, new LinearLayout.LayoutParams(-1, 2));
                tableLayout.addView(tableRow, new LinearLayout.LayoutParams(-1, hei));
                l1.setBackgroundResource(R.color.line_color);
                l2.setBackgroundResource(R.color.line_color);
                ps.setPadding(pad, 0, pad, 0);
                pe.setPadding(pad, 0, pad, 0);
                area.setPadding(pad, 0, pad, 0);
                tableRow.setLayoutParams(new LinearLayout.LayoutParams(tableLayout.getMeasuredWidth(), hei));
                tableRow.addView(area, new LinearLayout.LayoutParams((int) (width / 9 * 3.5), -2));
                tableRow.addView(l1, new LinearLayout.LayoutParams(2, -1));
                tableRow.addView(ps, new LinearLayout.LayoutParams((int) (width / 9 * 2), -2));
                tableRow.addView(l2, new LinearLayout.LayoutParams(2, -1));
                tableRow.addView(pe, new LinearLayout.LayoutParams((int) (width / 9 * 3.5), -2));
                area.setText(a.getString("endStreet"));
                ps.setText(a.getString("startPrice"));
                pe.setText(a.getString("exceedingPrice"));
            }
            tableLayout.requestLayout();
            final String costStr = json.getJSONObject("urgent").getString("urgentCost");
            float cost = 0.0f;
            try {
                cost = Float.valueOf(costStr);
            } catch (Exception e) {
            }
            checkbox.setText(String.format("加急%s元", cost));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case 10003:         //  收货地址
                addressFQ = data.getParcelableExtra(ReceiptAddressActivity.SELECTED_ADDRESS);
                if (addressFQ != null) {
                    deliveryAddress.setText(addressFQ.getDistrict() + " " + addressFQ.getStreet());

//                    Log.e("收货","*"+((addressEntity!=null)?addressEntity.getStreet():""));
//                    Log.e("收货","FQ"+addressFQ.getStreet());
                    if (addressEntity!=null){
                        imputedPrice();
                    }
                }
            case 23333:         //  发货地址

                addressEntity = data.getParcelableExtra(ReceiptAddressActivity.STARTED_ADDRESS);
                if (addressEntity != null) {
                    addressEntityMap.put("start",addressEntity);
                    startAddress.setText(addressEntity.getDistrict()+" "+addressEntity.getStreet());
//                    Log.e("发货2","FQ"+((addressFQ!=null)?addressFQ.getStreet():""));
//                    Log.e("发货2",""+addressEntity.getStreet());
                    getInfo(addressEntity);
                    if (addressFQ!=null){
                        imputedPrice();
                    }
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getInfo(AddressEntity address) {

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId())
                .addFormDataPart("pro", address.getPro())
                .addFormDataPart("city", address.getCity())
                .addFormDataPart("district", address.getDistrict())
                .addFormDataPart("street", address.getStreet())
                .addFormDataPart("type", "CityWide")
                .build();
        Request request = new Request.Builder().url(Config.Url.getUrl(Config.AREAINFO))
                .tag(Config.AREAINFO).post(requestBody).build();
        new OkHttpClient().newCall(request).enqueue(new Callback(getContext()));
    }


    class Callback extends SimpleCallback {
        Callback(Context context) {
            super(context);
        }

        @Override
        public void onSuccess(String tag, JSONObject json) throws JSONException {
            switch (tag) {
                case Config.AREAINFO: {
                    PickupHomeFragment.this.json = json;
                    areas = json.getJSONArray("tariff");
                    init();
                    break;
                }
                case Config.CREATEORDER:
                    if (getActivity() == null)
                        Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                    else new MsgDialog(getActivity()).show();
                    break;
                case Config.ADDRLIST:
                    JSONArray arr = json.getJSONArray("address");
                    for (int i = 0; i < arr.length(); i++) {
                        final JSONObject addr = arr.getJSONObject(i);
                        if (TextUtils.equals("yes", addr.getString("defaultAddress"))) {
                            getInfo(new Gson().fromJson(addr.toString(), AddressEntity.class));
                            break;
                        }
                    }
                    break;
                case Config.BUDGETCOST:
                    Activity act = getActivity();
                    if (act != null) {
                        ((CityExpressActivity) act).setPrice(json.getString("cost"));
                    }
                    break;
            }
            if (sureBt != null) sureBt.setEnabled(true);
        }

        @Override
        public void onFailure(Call call, IOException e) {
            super.onFailure(call, e);
            if (sureBt != null) sureBt.setEnabled(true);
        }

        @Override
        public void onFail(Call call) {
            super.onFail(call);
            if (sureBt != null) sureBt.setEnabled(true);
        }

        @Override
        public void onFail(JSONObject json) throws JSONException {
            super.onFail(json);
            if (sureBt != null) sureBt.setEnabled(true);
        }
    }

    private void imputedPrice() {
        if (addressFQ != null) {}
        else {
            return;
        }
        addressEntity=addressEntityMap.get("start");
        if (addressEntity != null){}
        else {
            return;
        }
        //:type [CityWide(同城配送), Intercity(城际配送)], startPro[起点省], startCity[起点市], startDistrict[起点区县],expressCompanyId[用户选择的快递公司id], endStreet[终点街道], endPro[终点省], weight[重量]
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("type", "CityWide")
                    .addFormDataPart("startPro", addressFQ.getPro())
                    .addFormDataPart("startCity", addressFQ.getCity())
                    .addFormDataPart("startDistrict", addressFQ.getDistrict())
                    .addFormDataPart("startStreet", addressEntity.getStreet())
                    .addFormDataPart("expressCompanyId", areas.getJSONObject(0).getString("expressCompanyId"))
                    .addFormDataPart("endStreet", addressFQ.getStreet())
                    .addFormDataPart("weight", cargoWeight.getText().toString())
                    .addFormDataPart("userId", info.getId())
                    .build();
            Request request = new Request.Builder()
                    .url(Config.Url.getUrl(Config.BUDGETCOST))
                    .tag(Config.BUDGETCOST)
                    .post(requestBody).build();
            new OkHttpClient().newCall(request).enqueue(new Callback(getContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}