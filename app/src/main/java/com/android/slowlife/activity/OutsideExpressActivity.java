package com.android.slowlife.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DisplayUtil;
import com.amap.api.services.cloud.CloudItem;
import com.android.slowlife.Area;
import com.android.slowlife.AreaDialog.OnAreaSelectedCallback;
import com.android.slowlife.BaseActivity;
import com.android.slowlife.DoneDialog;
import com.android.slowlife.MsgDialog;
import com.android.slowlife.R;
import com.android.slowlife.adapter.DoorPickingAdapter;
import com.android.slowlife.adapter.SelectExpressAdapter;
import com.android.slowlife.adressselectorlib.AddressSelector;
import com.android.slowlife.adressselectorlib.CityInterface;
import com.android.slowlife.adressselectorlib.OnItemClickListener;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.objectmodel.AddressEntity;
import com.android.slowlife.objectmodel.City;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.SimpleCallback;
import com.android.slowlife.view.MyListView;
import com.dialog.LoadDialog;
import com.google.gson.Gson;
import com.interfaceconfig.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/2/13 0013.
 */

public class OutsideExpressActivity extends BaseActivity implements OnAreaSelectedCallback {
    @Bind(R.id.price2)
    TextView price;
    @Bind(R.id.select_addr)
    TextView select_addr;
    @Bind(R.id.select_area)
    TextView selectArea;
    @Bind(R.id.cargo_weight)
    TextView cargoWeight;
    @Bind(R.id.express_company)
    TextView expressCompany;
    @Bind(R.id.express)
    TextView express;
    @Bind(R.id.listview)
    MyListView listview;
    @Bind(R.id.layout)
    LinearLayout layout;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.sure_bt)
    Button sureBt;
    @Bind(R.id.appointment)
    ImageView appointment;
    @Bind(R.id.tableLayout)
    LinearLayout tableLayout;
    @Bind(R.id.textview)
    TextView textView;
    @Bind(R.id.rule)
    TextView rule;
    @Bind(R.id.consignee_name)
    EditText consigneeName;
    @Bind(R.id.consignee_phone)
    EditText consigneePhone;
    @Bind(R.id.detailed_address)
    EditText detailedAddress;
    @Bind(R.id.bredPacket)
    TextView bredPacket;
    private SharedPreferences sp;
    private DoorPickingAdapter adapter;
    private int touchSlop;
    private DisplayMetrics display;
    private LoadDialog loadDialog;
    private Info info;
    private JSONObject json;
    private JSONArray areas;
    private CloudItem cloud;
    private AddressEntity address;
    private int selectedPosition = -1;
    private JSONArray companyList;
    //选择地区
    private ArrayList<City> cities1 = new ArrayList<>();
    private ArrayList<City> cities2 = new ArrayList<>();
    private ArrayList<City> cities3 = new ArrayList<>();
    private AddressSelector addressSelector;
    private JSONArray proArray, cityArray, districtArray;
    private JSONObject proObject, ctiyObject, districtObject;

    public OutsideExpressActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_outside_express);
        ButterKnife.bind(this);
        touchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
//        suspensionIcon();
        adapter = new DoorPickingAdapter(this);
        listview.setAdapter(adapter);
        loadDialog = new LoadDialog(this);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type", "Intercity").build();
        Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.GETRULE))
                .tag(Config.GETRULE)
                .post(requestBody).build();
        new OkHttpClient().newCall(request).enqueue(new Callback(this));
        info = ((MyApplication) getApplication()).getInfo();
        loadData();
        if (info == null) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        if (address != null) return;
        RequestBody requestBody1 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId()).build();
        final Request request1 = new Request.Builder()
                .url(Config.Url.getUrl(Config.ADDRLIST)).tag(Config.ADDRLIST).post(requestBody1).build();
        new OkHttpClient().newCall(request1).enqueue(new Callback(this));
    }

    private void loadData() {
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
//                String.format("预估金额:%s元", jsonObject.getString("cost"))
                bredPacket.setText(String.format("当前红包有:%s个", json.getString("RedPacketsNumber")));     // 红包
            }
        });

        RequestBody requestBody2 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("pid", "")
                .addFormDataPart("cid", "").build();
        Request request2 = new Request.Builder().url(Config.Url.getUrl(Config.APPGETAREA)).post(requestBody2).build();
        new OkHttpClient().newCall(request2).enqueue(new SimpleCallback(OutsideExpressActivity.this) {
            @Override
            public void onSuccess(String tag, JSONObject json) throws JSONException {
                proArray = json.getJSONArray("Pro");
                for (int i = 0; i < proArray.length(); i++) {
                    JSONObject jsonObject = proArray.getJSONObject(i);
                    City city = new City();
                    city.setId(jsonObject.getString("id"));
                    city.setName(jsonObject.getString("name"));
                    cities1.add(city);
                    Log.d("MyBaseAdapter", "区域=" + cities1);
                }
            }
        });
    }

    private void submit() {
        info = ((MyApplication) getApplication()).getInfo();
        if (info == null) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isEmpty(info.getPhone())) {
            Toast.makeText(this, "请先绑定手机号", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (address == null) {
//            Toast.makeText(this, "请选择收货地址", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (selectedPosition == -1) {
            Toast.makeText(this, "请选择快递公司", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
//            JSONObject json = this.json.getJSONArray("tariff").getJSONObject(selectedPosition);
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
            if (!isEmpty(selectArea.getText()))
                params.put("endPro", selectArea.getText());
            params.put("weight", cargoWeight.getText());
            params.put("userChoiceCommpanyId", companyList.getJSONObject(selectedPosition).getString("id"));
            params.put("userChoiceCommpanyName", companyList.getJSONObject(selectedPosition).getString("name"));
            params.put("userChoiceCommpanyCode", companyList.getJSONObject(selectedPosition).getString("companyCode"));
//            params.put("endHouseNumber", detaAddr);
            params.put("endLng", "0");
            params.put("endLat", "0");
            params.put("receiverName", consigneeName.getText());
            params.put("receiverPhone", consigneePhone.getText());
            params.put("endPro", (proObject != null) ? proObject.getString("name") : "");
            params.put("endCity", (ctiyObject != null) ? ctiyObject.getString("name") : "");
            params.put("endDistrict", (districtObject != null) ? districtObject.getString("name") : "");
            params.put("endStreet", "");
            params.put("endHouseNumber", detailedAddress.getText());
            params.put("type", "Intercity");
            new DoneDialog(this).setMessage("确定创建订单吗?\n费用由快递员上门后收取").setListener(
                    new DoneDialog.DialogButtonClickListener() {
                        @Override
                        public void done(Dialog dialog, Object tag) {
                            dialog.dismiss();
                            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("ordeStr", params.toString())
                                    .addFormDataPart("appVersion", "2.0")
                                    .build();
                            Request request = new Request.Builder().post(requestBody)
                                    .url(Config.Url.getUrl(Config.CREATEORDER))
                                    .tag(Config.CREATEORDER)
                                    .build();
                            new OkHttpClient().newCall(request).enqueue(new Callback(OutsideExpressActivity.this));
                            sureBt.setEnabled(false);
                        }

                        @Override
                        public void cancel(Dialog dialog, Object tag) {
                            dialog.dismiss();
                        }
                    }
            ).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置图标可拖动
     */
    private void suspensionIcon() {
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        int lastx = sp.getInt("lastx", 0);
        int lasty = sp.getInt("lasty", 0);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) appointment.getLayoutParams();
        params.leftMargin = lastx;
        params.topMargin = lasty;
        appointment.setLayoutParams(params);
        appointment.setOnTouchListener(new BtnTouchListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * 选择区域dialog窗口
     */
    private void selectProvinceDialog() {
        View view = LayoutInflater.from(OutsideExpressActivity.this).inflate(R.layout.dialog_select_area1, null);
        final Dialog selectAreaDialog = new AlertDialog.Builder(this, R.style.DialogStyle).create();
        addressSelector = (AddressSelector) view.findViewById(R.id.address);
        addressSelector.setTabAmount(3);
        addressSelector.setCities(cities1);
        View delete = view.findViewById(R.id.back_rl);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAreaDialog.dismiss();
            }
        });
        selectAreaDialog.show();
        selectAreaDialog.setContentView(view);
        addressSelector.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void itemClick(final AddressSelector addressSelector, CityInterface city, int tabPosition, int position) {
                switch (tabPosition) {
                    case 0:
                        try {
                            proObject = proArray.getJSONObject(position);
                            RequestBody requestBody2 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("pid", proObject.getString("id"))
                                    .addFormDataPart("cid", "").build();
                            Request request = new Request.Builder().url(Config.Url.getUrl(Config.APPGETAREA)).post(requestBody2).build();
                            new OkHttpClient().newCall(request).enqueue(new SimpleCallback(OutsideExpressActivity.this) {
                                @Override
                                public void onSuccess(String tag, JSONObject json) throws JSONException {
                                    cityArray = json.getJSONArray("City");
                                    for (int i = 0; i < cityArray.length(); i++) {
                                        JSONObject jsonObject = cityArray.getJSONObject(i);
                                        City city = new City();
                                        city.setId(jsonObject.getString("id"));
                                        city.setName(jsonObject.getString("name"));
                                        cities2.add(city);
                                        addressSelector.setCities(cities2);
                                        Log.d("MyBaseAdapter", "区域=" + cities1);
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cities2 = new ArrayList<>();
                        break;
                    case 1:
                        try {
                            ctiyObject = cityArray.getJSONObject(position);
                            RequestBody requestBody2 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("pid", "")
                                    .addFormDataPart("cid", ctiyObject.getString("id")).build();
                            Request request = new Request.Builder().url(Config.Url.getUrl(Config.APPGETAREA)).post(requestBody2).build();
                            new OkHttpClient().newCall(request).enqueue(new SimpleCallback(OutsideExpressActivity.this) {
                                @Override
                                public void onSuccess(String tag, JSONObject json) throws JSONException {
                                    districtArray = json.getJSONArray("District");
                                    for (int i = 0; i < districtArray.length(); i++) {
                                        JSONObject jsonObject = districtArray.getJSONObject(i);
                                        City city = new City();
                                        city.setId(jsonObject.getString("id"));
                                        city.setName(jsonObject.getString("name"));
                                        cities3.add(city);
                                        addressSelector.setCities(cities3);
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cities3 = new ArrayList<>();
                        break;
                    case 2:
                        try {
                            if (companyList != null) {
                                imputedPrice();
                            }
                            districtObject = districtArray.getJSONObject(position);
                            selectArea.setText(proObject.getString("name") + "  " + ctiyObject.getString("name") + "  " + districtObject.getString("name"));
                            selectAreaDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
        addressSelector.setOnTabSelectedListener(new AddressSelector.OnTabSelectedListener() {
            @Override
            public void onTabSelected(AddressSelector addressSelector, AddressSelector.Tab tab) {
                switch (tab.getIndex()) {
                    case 0:
                        addressSelector.setCities(cities1);
                        break;
                    case 1:
                        addressSelector.setCities(cities2);
                        break;
                    case 2:
                        addressSelector.setCities(cities3);
                        break;
                }
            }

            @Override
            public void onTabReselected(AddressSelector addressSelector, AddressSelector.Tab tab) {

            }
        });
    }

    /**
     * 选择快递dialog窗口
     */
    private void selectExpress() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View mDialog = inflater.inflate(R.layout.dialog_select_express, null);
        final Dialog areaDialog = new AlertDialog.Builder(this).create();
        View delete = mDialog.findViewById(R.id.back_rl);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaDialog.dismiss();
            }
        });
        SelectExpressAdapter adapter = new SelectExpressAdapter(this, companyList);
        final GridView gridView = (GridView) mDialog.findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        if (selectedPosition != -1) gridView.setItemChecked(selectedPosition, true);
        areaDialog.show();
        areaDialog.getWindow().setContentView(mDialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            areaDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getColor(R.color.transparent)));
        } else
            areaDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                areaDialog.dismiss();
                try {
                    selectedPosition = position;
                    expressCompany.setText(companyList.getJSONObject(position).getString("name"));
                    textView.setText(companyList.getJSONObject(position).getString("name") + "资费标准");
                    loadP();
                    if (proObject != null) {
                        imputedPrice();
                    }
                } catch (JSONException e) {
                }
            }
        });
    }

    @OnClick({R.id.back_rl, R.id.minus, R.id.add, R.id.express_rl, R.id.sure_bt, R.id.appointment,
            R.id.select_area_rl, R.id.select_addr_rl})
    public void onClick(View view) {
        Intent intent = null;
        String weightStr = cargoWeight.getText().toString().trim();//获取货物重量
        int weight = Integer.valueOf(weightStr).intValue();//字符串转换成整型
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.rule://规则
                intent = new Intent(this, ExpressRuleActivity.class);
                startActivity(intent);
                break;
            case R.id.delivery_address_rl://收货地址

                break;
            case R.id.minus://减
                if (proObject == null) {
                    Toast.makeText(OutsideExpressActivity.this, "请选择送货区域", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (companyList == null) {
                    Toast.makeText(OutsideExpressActivity.this, "请选择快递公司", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (weight > 1) {
                    weight--;
                }
                cargoWeight.setText(weight + "");
                imputedPrice();
                break;
            case R.id.add://加
                if (proObject == null) {
                    Toast.makeText(OutsideExpressActivity.this, "请选择送货区域", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (companyList == null) {
                    Toast.makeText(OutsideExpressActivity.this, "请选择快递公司", Toast.LENGTH_SHORT).show();
                    return;
                }
                weight++;
                cargoWeight.setText(weight + "");
                imputedPrice();
                break;
            case R.id.express_rl://快递公司
                getExpressDate();
                break;
            case R.id.sure_bt://我要寄件
                submit();
                break;
            case R.id.appointment://预约时间
                intent = new Intent(this, UseRedPacketActivity.class);
                startActivity(intent);
                break;
            case R.id.select_addr_rl:       //  发货地址
                intent = new Intent(this, ReceiptAddressActivity.class);
                intent.putExtra(ReceiptAddressActivity.SELECT_ADDRESS, true);
                intent.putExtra(ReceiptAddressActivity.SELECTED_ITEM, address == null ? null : address.getId());
                startActivityForResult(intent, 10003);
                break;
            case R.id.select_area_rl:       //  收货区域
//                new AreaDialog(this, this).show();
                selectProvinceDialog();
                break;
            case R.id.agreement:    //   服务协议
                intent = new Intent(this, HelpActivity.class);
                intent.putExtra(HelpActivity.URI, "app/appGeneral/serviceContract.html");
                startActivity(intent);
                break;
        }
    }

    private void getExpressDate() {
        if (address == null) {
            Toast.makeText(this, "请选择发货地址", Toast.LENGTH_SHORT).show();
            return;
        }
        loadDialog.show();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("pro", address.getPro())
                .addFormDataPart("city", address.getCity())
                .addFormDataPart("district", address.getDistrict())
                .addFormDataPart("street", address.getStreet())
                .build();
        Request request = new Request.Builder().url(Config.Url.getUrl(Config.EXPRESSCOMPANY)).tag(Config.EXPRESSCOMPANY)
                .post(requestBody).build();
        new OkHttpClient().newCall(request).enqueue(new Callback(this));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case 10003:         //  发货地址
                address = data.getParcelableExtra(ReceiptAddressActivity.SELECTED_ADDRESS);
                if (address != null) {
                    try {
                        loadP();
                    } catch (JSONException e) {
                        Toast.makeText(this, "解析数据失败", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadP() throws JSONException {
        if (address == null) return;
        select_addr.setText(address.getDistrict() + " " + address.getStreet());
        if (selectedPosition == -1 || address == null) return;
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId())
                .addFormDataPart("pro", address.getPro())
                .addFormDataPart("city", address.getCity())
                .addFormDataPart("district", address.getDistrict())
                .addFormDataPart("street", address.getStreet())
                .addFormDataPart("type", "Intercity")
                .addFormDataPart("expressCompanyId", companyList.getJSONObject(selectedPosition).getString("id"))
                .build();
        Request request = new Request.Builder().url(Config.Url.getUrl(Config.AREAINFO))
                .tag(Config.AREAINFO).post(requestBody).build();
        new OkHttpClient().newCall(request).enqueue(new Callback(this));
    }

    @Override
    public void onSelected(Area area, int position) {
        selectArea.setText(area.getName());
    }

    class Callback extends SimpleCallback {
        public Callback(Context context) {
            super(context);
        }

        @Override
        public void onSuccess(String tag, JSONObject jsonObject) throws JSONException {
            switch (tag) {
                case Config.EXPRESSCOMPANY:
                    companyList = jsonObject.getJSONArray("company");
                    loadDialog.dismiss();
                    selectExpress();
                    break;
                case Config.CREATEORDER:
                    new MsgDialog(OutsideExpressActivity.this).show();
                    break;
                case Config.AREAINFO:
                    json = jsonObject;
                    areas = json.getJSONArray("group");
                    Log.e("错误group", "" + json + jsonObject);
                    initP();
                    break;
                case Config.ADDRLIST:
                    JSONArray arr = jsonObject.getJSONArray("address");
                    for (int i = 0; i < arr.length(); i++) {
                        final JSONObject addr = arr.getJSONObject(i);
                        if (TextUtils.equals("yes", addr.getString("defaultAddress"))) {
                            address = new Gson().fromJson(addr.toString(), AddressEntity.class);
                            loadP();
                            break;
                        }
                    }
                    break;
                case Config.GETRULE:
                    rule.setText(jsonObject.getString("rule"));
                    break;
                case Config.BUDGETCOST:
                    price.setText(String.format("预估金额:%s元", jsonObject.getString("cost")));
                    break;
            }
            sureBt.setEnabled(true);
        }
    }


    @Override
    protected void onFail(Call call, IOException e) {
        super.onFail(call, e);
        sureBt.setEnabled(true);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        super.onFailure(call, e);
        sureBt.setEnabled(true);
    }

    private void initP() {
        try {
            if (tableLayout.getChildCount() > 1)
                tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
            Context context = this;
            int pad = DisplayUtil.dip2px(context, 6);
            int hei = DisplayUtil.dip2px(context, 30);
            int width = tableLayout.getWidth();
            for (int i = 0; i < areas.length(); i++) {
                final JSONObject a = areas.getJSONObject(i);
                final LinearLayout tableRow = new LinearLayout(context);
                tableRow.setOrientation(LinearLayout.HORIZONTAL);
                tableRow.setGravity(Gravity.CENTER_VERTICAL);
                tableRow.setMinimumHeight(hei);
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
                tableLayout.addView(tableRow, new LinearLayout.LayoutParams(-1, -2));
                l1.setBackgroundResource(R.color.line_color);
                l2.setBackgroundResource(R.color.line_color);
                ps.setPadding(pad, 0, pad, 0);
                pe.setPadding(pad, 0, pad, 0);
                area.setPadding(pad, 0, pad, 0);
                tableRow.addView(area, new LinearLayout.LayoutParams((int) (width / 9 * 3.5), -2));
                tableRow.addView(l1, new LinearLayout.LayoutParams(2, -1));
                tableRow.addView(ps, new LinearLayout.LayoutParams((int) (width / 9 * 2), -2));
                tableRow.addView(l2, new LinearLayout.LayoutParams(2, -1));
                tableRow.addView(pe, new LinearLayout.LayoutParams((int) (width / 9 * 3.5), -2));
                area.setText(a.getString("expressCompanyName"));
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

    class BtnTouchListener implements View.OnTouchListener {
        PointF down = new PointF();
        boolean intercept = false;


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    down.x = event.getX();
                    down.y = event.getY();
                    intercept = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    final int left = (int) (event.getX() - down.x);
                    final int top = (int) (event.getY() - down.y);
                    if (Math.abs(left) > touchSlop || Math.abs(top) > touchSlop || intercept) {
                        intercept = true;
                        int l = appointment.getLeft();
                        int r = appointment.getRight();
                        int t = appointment.getTop();
                        int b = appointment.getBottom();
                        int newt = t + top;
                        int newb = b + top;
                        int newl = l + left;
                        int newr = r + left;
                        if ((newl < 0) || (newt < 0)
                                || (newr > display.widthPixels)
                                || (newb > display.heightPixels)) {
                            break;
                        }
                        // 更新iv在屏幕的位置.
                        appointment.layout(newl, newt, newr, newb);
                    } else intercept = false;
                    return intercept;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            return intercept;
        }
    }

    private void imputedPrice() {
        if (address == null) return;
        if (selectedPosition == -1) return;
        if (isEmpty(selectArea.getText())) return;
        if (proObject == null) {
            Toast.makeText(OutsideExpressActivity.this, "请选择送货区域", Toast.LENGTH_SHORT).show();
            return;
        }
        if (companyList == null) {
            Toast.makeText(OutsideExpressActivity.this, "请选择快递公司", Toast.LENGTH_SHORT).show();
            return;
        }
        //:type [CityWide(同城配送), Intercity(城际配送)], startPro[起点省], startCity[起点市], startDistrict[起点区县],expressCompanyId[用户选择的快递公司id], endStreet[终点街道], endPro[终点省], weight[重量]
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("type", "Intercity")
                    .addFormDataPart("startPro", address.getPro())
                    .addFormDataPart("startCity", address.getCity())
                    .addFormDataPart("startDistrict", address.getDistrict())
                    .addFormDataPart("startStreet", address.getStreet())
                    .addFormDataPart("expressCompanyId", companyList.getJSONObject(selectedPosition).getString("id"))
                    .addFormDataPart("endPro", proObject.getString("name"))
                    .addFormDataPart("weight", cargoWeight.getText().toString())
                    .addFormDataPart("userId", info.getId())
                    .build();
            Request request = new Request.Builder()
                    .url(Config.Url.getUrl(Config.BUDGETCOST))
                    .tag(Config.BUDGETCOST)
                    .post(requestBody).build();
            new OkHttpClient().newCall(request).enqueue(new Callback(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
