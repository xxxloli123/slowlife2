package com.android.slowlife.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.OKhttp.OkHttp;
import com.android.slowlife.OKhttp.OkHttpCallback;
import com.android.slowlife.R;
import com.android.slowlife.adressselectorlib.AddressSelector;
import com.android.slowlife.adressselectorlib.CityInterface;
import com.android.slowlife.adressselectorlib.OnItemClickListener;
import com.android.slowlife.objectmodel.AddressEntity;
import com.android.slowlife.objectmodel.City;
import com.android.slowlife.util.SimpleCallback;
import com.android.slowlife.util.ToastUtil;
import com.android.slowlife.view.EditTextChange;
import com.interfaceconfig.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ReceivingInfoActivity extends BaseActivity implements OkHttpCallback.Impl,EditTextChange.IClipCallback{

    @Bind(R.id.name_edit)
    EditText nameEdit;
    @Bind(R.id.phone_edit)
    EditText phoneEdit;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.addr_des)
    EditText addrDes;
    @Bind(R.id.discern_et)
    EditTextChange discernEt;

    //选择地区
    private ArrayList<City> cities1 = new ArrayList<>();
    private ArrayList<City> cities2 = new ArrayList<>();
    private ArrayList<City> cities3 = new ArrayList<>();
    private AddressSelector addressSelector;
    private JSONObject proObject,districtObject,ctiyObject;
    private JSONArray proArray, cityArray, districtArray;
    private AddressEntity getAdd=new AddressEntity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving_info);
        ButterKnife.bind(this);
        if (getIntent().getParcelableExtra("Address")!=null){
            getAdd=getIntent().getParcelableExtra("Address");
            address .setText(getAdd.getPro() + "  " + getAdd.getCity() + "  "+getAdd.getDistrict());
            nameEdit.setText(getAdd.getPersonname());
            phoneEdit.setText(getAdd.getPersonphone());
            addrDes.setText(getAdd.getHouseNumber());
        }
        load();
    }

    private void load() {
        Map<String, Object> params = new HashMap<>();
        params.put("pid", "");
        params.put("cid", "");
        OkHttp.Call(Config.Url.getUrl(Config.APPGETAREA), params,this);
    }

    @OnClick({R.id.back_rl, R.id.address, R.id.ensure_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.address:
                selectDialog();
                break;
            case R.id.ensure_bt:
                if (getAdd.getPro()==null||getAdd.getCity()==null||getAdd.getDistrict()==null){
                    ToastUtil.show(this,"请选择省市区");
                    return;
                }
                if (isEmpty(nameEdit.getText())){
                    ToastUtil.show(this,"请填写名字");
                    return;
                }
                if (isEmpty(phoneEdit.getText())){
                    ToastUtil.show(this,"请填写电话");
                    return;
                }
                if (isEmpty(addrDes.getText())){
                    ToastUtil.show(this,"请填写地址");
                    return;
                }
                Intent intent = new Intent();
                AddressEntity ae = new AddressEntity();
                ae.setPro(getAdd.getPro());
                ae.setCity(getAdd.getCity());
                ae.setDistrict(getAdd.getDistrict());
                ae.setPersonname(nameEdit.getText().toString());
                ae.setPersonphone(phoneEdit.getText().toString());
                ae.setHouseNumber(addrDes.getText().toString());
                intent.putExtra("Address", ae);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }

    /**
     * 选择区域dialog窗口
     */
    private void selectDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_area1, null);
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
                            getAdd.setPro(proObject.getString("name"));
                            RequestBody requestBody2 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("pid", proObject.getString("id"))
                                    .addFormDataPart("cid", "").build();
                            Request request = new Request.Builder().url(Config.Url.getUrl(Config.APPGETAREA)).post(requestBody2).build();
                            new OkHttpClient().newCall(request).enqueue(new SimpleCallback(ReceivingInfoActivity.this) {
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
                            getAdd.setCity(ctiyObject.getString("name"));
                            RequestBody requestBody2 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("pid", "")
                                    .addFormDataPart("cid", ctiyObject.getString("id")).build();
                            Request request = new Request.Builder().url(Config.Url.getUrl(Config.APPGETAREA)).post(requestBody2).build();
                            new OkHttpClient().newCall(request).enqueue(new SimpleCallback(ReceivingInfoActivity.this) {
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
                            districtObject = districtArray.getJSONObject(position);
                            getAdd.setDistrict(districtObject.getString("name"));
                            address .setText(proObject.getString("name") + "  " +
                                    ctiyObject.getString("name") + "  " + districtObject.getString("name"));
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

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        if (tag.equals(Config.APPGETAREA)){
            proArray = json.getJSONArray("Pro");
            for (int i = 0; i < proArray.length(); i++) {
                JSONObject jsonObject = proArray.getJSONObject(i);
                City city = new City();
                city.setId(jsonObject.getString("id"));
                city.setName(jsonObject.getString("name"));
                cities1.add(city);
            }
        }else if (tag.equals(Config.Discern_Address)){
//                    "city": "温州市",
//                    "phone": "15868025640",
//                    "district": "瓯海区",
//                    "name": "徐翠芳",
//                    "pro": "浙江省",
//                    "detailedaddress": "郭溪街道塘下隔岸路110号 徐翠芳 15868025640"
            getAdd=new AddressEntity();
            address.setText(json.getJSONObject("map").getString("pro")+"   "+
                    json.getJSONObject("map").getString("city")+"   "+json.getJSONObject("map").
                    getString("district"));

            getAdd.setPro(json.getJSONObject("map").getString("pro"));
            getAdd.setCity(json.getJSONObject("map").getString("city"));
            getAdd.setDistrict(json.getJSONObject("map").getString("district"));

            nameEdit.setText(json.getJSONObject("map").getString("name"));
            phoneEdit.setText(json.getJSONObject("map").getString("phone"));
            addrDes.setText(json.getJSONObject("map").getString("detailedaddress"));
        }
    }

    @Override
    public void fail(Object tag, int code, JSONObject json) throws JSONException {
    }

    @NonNull
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onCut(Object o) {
    }

    @Override
    public void onCopy(Object o) {
    }

    @Override
    public void onPaste(Object o) {
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("address", discernEt.getText().toString());
                OkHttp.Call(Config.Url.getUrl(Config.Discern_Address), params,ReceivingInfoActivity.this);
            }
        },100);
    }
}
