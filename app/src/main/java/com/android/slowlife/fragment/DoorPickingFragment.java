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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.slowlife.adapter.DoorPickingAdapter;
import com.android.slowlife.adapter.SelectProvinceAdapter;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.objectmodel.AddressEntity;
import com.android.slowlife.util.SimpleCallback;
import com.android.slowlife.view.EditTextChange;
import com.dialog.LoadDialog;
import com.google.gson.Gson;
import com.interfaceconfig.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * TODO 同城快递 => 上门取件
 */

public class DoorPickingFragment extends BaseFragment implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    @Bind(R.id.delivery_address)
    TextView deliveryAddress;
    @Bind(R.id.delivery_address_rl)
    RelativeLayout deliveryAddressRl;
    @Bind(R.id.select_area)
    TextView selectArea;
    @Bind(R.id.select_area_rl)
    RelativeLayout selectAreaRl;
    @Bind(R.id.detailed_address)
    EditTextChange detailedAddress;
    @Bind(R.id.voice_layout)
    RelativeLayout voiceLayout;
    @Bind(R.id.consignee_phone)
    EditText consigneePhone;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.urgent_rl)
    RelativeLayout urgentRl;
    @Bind(R.id.minus)
    ImageView minus;
    @Bind(R.id.cargo_weight)
    TextView cargoWeight;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.cargo_unit)
    TextView cargoUnit;
    @Bind(R.id.cargo_weight_rl)
    RelativeLayout cargoWeightRl;
    @Bind(R.id.tableLayout)
    LinearLayout tableLayout;
    @Bind(R.id.textview)
    TextView textView;
    private DoorPickingAdapter adapter;
    private Info info;
    private AddressEntity address;
    private JSONObject json;
    private JSONArray areas;
    private LoadDialog dialog;
    private int selectedPosition = -1;
    private View sureBt;
    private JSONObject addr;
    private int weight = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_door_picking, null);
        dialog = new LoadDialog(view.getContext());
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new DoorPickingAdapter(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        info = ((MyApplication) getContext().getApplicationContext()).getInfo();
        if (info == null) {
            return;
        }
        if (address != null) return;

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId()).build();
        final Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.ADDRLIST)).tag(Config.ADDRLIST).post(requestBody).build();
        new OkHttpClient().newCall(request).enqueue(new Callback(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.delivery_address_rl, R.id.select_area_rl, R.id.voice_layout, R.id.minus, R.id.add, R.id.checkbox})
    public void onClick(View view) {

        double price = 0;
        switch (view.getId()) {
            case R.id.delivery_address_rl://发货地址
                Intent intent = new Intent(getContext(), ReceiptAddressActivity.class);
                intent.putExtra(ReceiptAddressActivity.SELECT_ADDRESS, true);
                intent.putExtra(ReceiptAddressActivity.SELECTED_ITEM, address == null ? null : address.getId());
                startActivityForResult(intent, 13333);
                break;
            case R.id.select_area_rl://选择区域
                if (areas == null) showAddrs();
                else selectProvinceDialog();
                break;
            case R.id.voice_layout://语音
                Toast.makeText(getActivity(), "语音", Toast.LENGTH_SHORT).show();
                break;
            case R.id.minus:
                if (weight > 5) {
                    weight--;
                }
                cargoWeight.setText(weight + "");
                imputedPrice();
                break;
            case R.id.add://加
                weight++;
                cargoWeight.setText(weight + "");
                imputedPrice();
                break;
            case R.id.sure_bt://我要寄件
                sureBt = view;
                submit();
                break;
            case R.id.agreement:    //   服务协议
                break;
            case R.id.checkbox:
                if (checkbox.isChecked()) {
                }
                break;
        }
    }

    @OnClick({R.id.video})
    public void video() {

    }

    private void submit() {
        if (isEmpty(info.getPhone())) {
            Toast.makeText(getContext(), "请先绑定手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (address == null) {
                Toast.makeText(getContext(), "请选择发货地址", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!((CityExpressActivity) getActivity()).isChecked()) {
                Toast.makeText(getContext(), "请阅读服务协议", Toast.LENGTH_SHORT).show();
                return;
            }
            String detaAddr = detailedAddress.getText().toString().trim();
            String phone = consigneePhone.getText().toString().trim();
            final JSONObject params = new JSONObject();
            params.put("createUserId", info.getId());
            params.put("createUserName", address.getPersonname());
            params.put("createUserPhone", address.getPersonphone());
            params.put("startPro", address.getPro());
            params.put("startCity", address.getCity());
            params.put("startDistrict", address.getDistrict());
            params.put("startStreet", address.getStreet());
            params.put("startHouseNumber", address.getHouseNumber());
            params.put("startLng", String.valueOf(address.getLng()));
            params.put("startLat", String.valueOf(address.getLat()));
            params.put("weight", weight);
            if (selectedPosition != -1) {
                JSONObject json = this.json.getJSONArray("tariff").getJSONObject(selectedPosition);
                params.put("endPro", json.getString("endPro"));
                params.put("endCity", json.getString("endCity"));
                params.put("endDistrict", json.getString("endDistrict"));
                params.put("endStreet", json.getString("endStreet"));
                params.put("userChoiceCommpanyId", json.getString("expressCompanyId"));
                params.put("userChoiceCommpanyName", json.getString("expressCompanyName"));
                params.put("userChoiceCommpanyCode", "ZSHHD");
            } else {
                if (this.json.getJSONArray("tariff").length() == 0) {
                    Toast.makeText(getContext(), "暂未覆盖该区域", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject json = this.json.getJSONArray("tariff").getJSONObject(0);
                params.put("endPro", address.getPro());
                params.put("endCity", address.getCity());
                params.put("endDistrict", address.getDistrict());
                params.put("endStreet", (addr == null) ? "" : addr.getString("endStreet"));
                params.put("userChoiceCommpanyId", json.getString("expressCompanyId"));
                params.put("userChoiceCommpanyName", json.getString("expressCompanyName"));
                params.put("userChoiceCommpanyCode", "ZSHHD");
            }
            params.put("endHouseNumber", detaAddr == null ? "" : detaAddr);
            params.put("endLng", "0");
            params.put("endLat", "0");
            params.put("receiverName", "");
            params.put("receiverPhone", phone);
            params.put("urgent", checkbox.isChecked() ? "yes" : "no");
            final String costStr = this.json.getJSONObject("urgent").getString("urgentCost");
            float cost = 0.0f;
            try {
                cost = Float.valueOf(costStr);
            } catch (Exception e) {
            }
            params.put("urgentFee", cost);
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

    /**
     * 获取常用地址
     */
    private void showAddrs() {
        if (address == null) {
            Toast.makeText(getContext(), "请先选择发货地址", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show();
        if (json != null) return;
    }

    /**
     * 选择区域dialog窗口
     */
    private void selectProvinceDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View mDialog = inflater.inflate(R.layout.dialog_select_area, null);
        final Dialog areaDialog = new AlertDialog.Builder(getActivity(), R.style.DialogStyle).create();
        View delete = mDialog.findViewById(R.id.back_rl);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaDialog.dismiss();
            }
        });
        final SelectProvinceAdapter adapter = new SelectProvinceAdapter(getActivity(), areas);
        ListView listView = (ListView) mDialog.findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(false);
        areaDialog.show();
        areaDialog.setContentView(mDialog);
        if (selectedPosition != -1) listView.setItemChecked(selectedPosition, true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                areaDialog.dismiss();
                try {
                    selectedPosition = position;
                    addr = areas.getJSONObject(position);
                    selectArea.setText(String.format("%s %s", addr.getString("endDistrict"), addr.getString("endStreet")));
                    imputedPrice();
                } catch (JSONException e) {
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case 13333:         //  发货地址
                address = data.getParcelableExtra(ReceiptAddressActivity.SELECTED_ADDRESS);
                if (address != null) {
                    getInfo(address);
                    Log.e("定位", "" + address.getLng() + "," + address.getLat());
                    imputedPrice();
                }
        }
    }

    private void init() {
        try {
            if (tableLayout.getChildCount() > 1)
                tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
            Context context = selectArea.getContext();
            int pad = DisplayUtil.dip2px(context, 6);
            int hei = DisplayUtil.dip2px(context, 30);
            int width = tableLayout.getWidth();

            for (int i = 0; i < areas.length(); i++) {
                final JSONObject a = areas.getJSONObject(i);
                final LinearLayout tableRow = new LinearLayout(context);
                final TextView area = new TextView(context);
                final TextView ps = new TextView(context);
                final TextView pe = new TextView(context);
                final View l1 = new View(context);
                final View l2 = new View(context);
                final View l3 = new View(context);
                area.setTextSize(12);
                ps.setTextSize(12);
                pe.setTextSize(12);
                tableRow.setOrientation(LinearLayout.HORIZONTAL);
                tableRow.setGravity(Gravity.CENTER_VERTICAL);
                l1.setBackgroundResource(R.color.line_color);
                l2.setBackgroundResource(R.color.line_color);
                l3.setBackgroundResource(R.color.line_color);
                ps.setPadding(pad, 0, pad, 0);
                pe.setPadding(pad, 0, pad, 0);
                area.setPadding(pad, 0, pad, 0);
                tableLayout.addView(l3, -1, 2);
                tableLayout.addView(tableRow, new LinearLayout.LayoutParams(-1, hei));
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
            getView().requestLayout();
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }


    /**
     * 查询符合的手机号码
     *
     * @param str
     */
    private static String checkCellphone(String str) {

        // 将给定的正则表达式编译到模式中
        Pattern pattern = Pattern.compile("(1[3-9])\\d{9}");
        // 创建匹配给定输入与此模式的匹配器。
        Matcher matcher = pattern.matcher(str);

        // 查找字符串中是否有符合的子字符串d
        while (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    @OnClick(R.id.discern_bt)
    public void onClick() {
        consigneePhone.setText(checkCellphone(detailedAddress.getText().toString()));
    }

    class Callback extends SimpleCallback {
        Callback(Context context) {
            super(context);
        }

        @Override
        public void onSuccess(String tag, JSONObject jsonObject) throws JSONException {
            switch (tag) {
                case Config.CREATEORDER:
                    //  创建订单
                    if (getActivity() == null)
                        Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                    else new MsgDialog(getActivity()).show();
                    break;
                case Config.AREAINFO:
                    //  获取区域信息
                    json = jsonObject;
                    areas = jsonObject.getJSONArray("tariff");
                    init();
                    break;
                case Config.ADDRLIST:
                    JSONArray arr = jsonObject.getJSONArray("address");
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
                        ((CityExpressActivity) act).setPrice(jsonObject.getString("cost"));
                    }
                    break;
            }
            if (sureBt != null) sureBt.setEnabled(true);
        }

        @Override
        public void onFail(Call call) {
            dialog.dismiss();
            Toast.makeText(getContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
            if (sureBt != null) sureBt.setEnabled(true);
        }

        @Override
        public void onFailure(Call call, IOException e) {
            super.onFailure(call, e);
            if (sureBt != null) sureBt.setEnabled(true);
        }

        @Override
        public void onFail(JSONObject json) throws JSONException {
            super.onFail(json);
            if (sureBt != null) sureBt.setEnabled(true);
        }
    }

    private void getInfo(AddressEntity address) {
        this.address = address;
        if (deliveryAddress != null)
            deliveryAddress.setText(address.getDistrict() + " " + address.getStreet());
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

    @Override
    public void onResume() {
        super.onResume();
        if (deliveryAddress != null && address != null)
            deliveryAddress.setText(address.getDistrict() + " " + address.getStreet());
    }

    private void imputedPrice() {
        if (address == null) return;
        if (selectedPosition == -1) return;
        if (isEmpty(selectArea.getText())) return;
        //:type [CityWide(同城配送), Intercity(城际配送)], startPro[起点省], startCity[起点市], startDistrict[起点区县],expressCompanyId[用户选择的快递公司id], endStreet[终点街道], endPro[终点省], weight[重量]
        try {
            JSONObject json = this.json.getJSONArray("tariff").getJSONObject(selectedPosition);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("type", "CityWide")
                    .addFormDataPart("startPro", address.getPro())
                    .addFormDataPart("startCity", address.getCity())
                    .addFormDataPart("startDistrict", address.getDistrict())
                    .addFormDataPart("startStreet", address.getStreet())
                    .addFormDataPart("expressCompanyId", areas.getJSONObject(0).getString("expressCompanyId"))
                    .addFormDataPart("endStreet", json.getString("endStreet"))
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
