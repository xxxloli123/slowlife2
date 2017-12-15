package com.android.slowlife.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DisplayUtil;
import com.amap.api.services.cloud.CloudItem;
import com.android.slowlife.Area;
import com.android.slowlife.AreaDialog.OnAreaSelectedCallback;
import com.android.slowlife.BaseActivity;
import com.android.slowlife.BuildConfig;
import com.android.slowlife.DoneDialog;
import com.android.slowlife.MsgDialog;
import com.android.slowlife.R;
import com.android.slowlife.adapter.DoorPickingAdapter;
import com.android.slowlife.adapter.SelectExpressAdapter;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.objectmodel.AddressEntity;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.SimpleCallback;
import com.android.slowlife.util.ToastUtil;
import com.android.slowlife.view.MyListView;
import com.dialog.LoadDialog;
import com.google.gson.Gson;
import com.interfaceconfig.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;
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
    @Bind(R.id.commodity_type)
    TextView commodityType;
    @Bind(R.id.img)
    ImageView img;
    private SharedPreferences sp;
    private DoorPickingAdapter adapter;
    private int touchSlop;
    private DisplayMetrics display;
    private LoadDialog loadDialog;
    private Info info;
    private JSONObject json;
    private JSONArray areas;
    private CloudItem cloud;
    private AddressEntity address, endAddress;
    private int selectedPosition = -1;
    private JSONArray companyList;
    private String mImagePath = Environment.getExternalStorageDirectory()+"/meta/";
    private File camera, clipping;
    private static final int CROP_CODE = 3;
    private RequestBody fileRequest1;

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
    }

    private void submit()  {
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
        //IMG 的
        FileInputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (clipping!=null){
                fis = new FileInputStream(clipping);
                int len;
                byte[] buf = new byte[512];
                while ((len = fis.read(buf)) > 0) {
                    baos.write(buf, 0, len);
                    baos.flush();
                }
                byte[] fileData = baos.toByteArray();
                fis.close();
                baos.close();
                fileRequest1 = RequestBody.create(MediaType.parse("application/octet-stream"), fileData);
            }
//            else fileRequest1= RequestBody.create(MediaType.parse("application/octet-stream"),"");
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
            params.put("weight", cargoWeight.getText());
            params.put("userChoiceCommpanyId", companyList.getJSONObject(selectedPosition).getString("id"));
            params.put("userChoiceCommpanyName", companyList.getJSONObject(selectedPosition).getString("name"));
            params.put("userChoiceCommpanyCode", companyList.getJSONObject(selectedPosition).getString("companyCode"));
//            params.put("endHouseNumber", detaAddr);
            params.put("endLng", "0");
            params.put("endLat", "0");
            params.put("receiverName", (endAddress != null) ? endAddress.getPersonname() : "");
            params.put("receiverPhone", (endAddress != null) ? endAddress.getPersonphone() : "");
            params.put("endPro", (endAddress != null) ? endAddress.getPro() : "");
            params.put("endCity", (endAddress != null) ? endAddress.getCity() : "");
            params.put("endDistrict", (endAddress != null) ? endAddress.getDistrict() : "");
            params.put("endStreet", "");
            params.put("endHouseNumber", (endAddress != null) ? endAddress.getHouseNumber() : "");
            params.put("type", "Intercity");
            //商品类型
            params.put("goodsName", commodityType.getText().toString());
            new DoneDialog(this).setMessage("确定创建订单吗?\n费用由快递员上门后收取").setListener(
                    new DoneDialog.DialogButtonClickListener() {
                        @Override
                        public void done(Dialog dialog, Object tag) {
                            dialog.dismiss();
//                            参数：[ordeStr, appVersion, pictures]
//                            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                                    .addFormDataPart("ordeStr", params.toString())
//                                    .addFormDataPart("appVersion", "2.0")
//                                    .addFormDataPart("pictures", (clipping==null)?"":clipping.getName(),
//                                            fileRequest1)
//                                    .build();
                            MultipartBody.Builder builder=new MultipartBody.Builder().setType(MultipartBody.FORM);
                            builder.addFormDataPart("ordeStr", params.toString())
                                    .addFormDataPart("appVersion", "20171214");
                            if (clipping!=null)builder.addFormDataPart("pictures", clipping.getName(),
                                    fileRequest1);
                            Request request = new Request.Builder().post(builder.build())
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
                    if (endAddress != null) {
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
                if (endAddress == null) {
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
                if (endAddress == null) {
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
                intent = new Intent(this, ReceivingInfoActivity.class);
                if (endAddress!=null)intent.putExtra("Address", endAddress);
                startActivityForResult(intent, 2333);
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
                        Toast.makeText(this, "数据读取失败", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 2333:
                endAddress = data.getParcelableExtra("Address");
                if (endAddress == null) {
                    ToastUtil.show(this, "数据读取失败");
                    selectArea.setText("数据读取失败");
                    return;
                }
                selectArea.setText(endAddress.getPersonname() + " " + endAddress.getPersonphone() + "　" + endAddress.getHouseNumber());
                break;
            case 2:
                Uri uri = data.getData();
                if (uri == null) {
                    Toast.makeText(this, "选择图片文件读取出错", Toast.LENGTH_LONG).show();
                    return;
                }
                startImageZoom(uri);
                break;
            case 1:
                startImageZoom(FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID  +
                        ".provider", camera));
                break;
            case CROP_CODE:
                if (data != null) {
                    try {
                        String fils = clipping.getAbsolutePath();
                        Bitmap bitmap = BitmapFactory.decodeFile(fils);
                        compressPicture(fils,clipping.getAbsoluteFile());
                        img.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(this, "图片文件剪裁出错", Toast.LENGTH_LONG).show();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startImageZoom(Uri uri) {
        clipping = new File(getExternalCacheDir(), UUID.randomUUID().toString() + ".png");
        //构建隐式Intent来启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //设置数据uri和类型为图片类型
        intent.setDataAndType(uri, "image/*");
        //显示View为可裁剪的
        intent.putExtra("crop", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(clipping));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, CROP_CODE);
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

    @OnClick({R.id.commodity_type, R.id.upload_front})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.commodity_type:
                selectCommodityType();
                break;
            case R.id.upload_front:
                popwindowsshow();
                break;
        }
    }

    /**
     * 弹出窗口
     */
    private void popwindowsshow() {
        final PopupWindow pop = new PopupWindow(this);
        View view = getLayoutInflater().inflate(R.layout.popwindows_head, null);
        final LinearLayout popwindows = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pop.dismiss();
                popwindows.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//拍照
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (it.resolveActivity(getPackageManager()) != null) {
                    File path = new File(mImagePath);
                    if (!path.exists()) {
                        path.mkdir();
                    }
                    camera = new File(mImagePath, UUID.randomUUID().toString()+".jpg");
                    Uri photoUri = FileProvider.getUriForFile(OutsideExpressActivity.this,
                            getPackageName() + ".provider", camera);

                    it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    it.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(it, 1);
                }
                pop.dismiss();
                popwindows.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//从相册中选择
                Intent local =new  Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                local.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(local, 2);
                pop.dismiss();
                popwindows.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                popwindows.clearAnimation();
            }
        });
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 商品类型
     */
    public void selectCommodityType() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_commodity_type, null);
        final Dialog selectCommodityTypeDialog = new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog).create();
        CheckBox commodity = (CheckBox) view.findViewById(R.id.commodity);
        CheckBox digital_products = (CheckBox) view.findViewById(R.id.digital_products);
        CheckBox food = (CheckBox) view.findViewById(R.id.food);
        CheckBox file = (CheckBox) view.findViewById(R.id.file);
        CheckBox clothes = (CheckBox) view.findViewById(R.id.clothes);
        CheckBox rests = (CheckBox) view.findViewById(R.id.rests);
        selectCommodityTypeDialog.show();
        selectCommodityTypeDialog.setContentView(view);
        View delete = view.findViewById(R.id.back_rl);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCommodityTypeDialog.dismiss();
            }
        });
        commodity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodityType.setText("日用品");
                selectCommodityTypeDialog.dismiss();
            }
        });
        digital_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodityType.setText("数码产品");
                selectCommodityTypeDialog.dismiss();
            }
        });
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodityType.setText("食品");
                selectCommodityTypeDialog.dismiss();
            }
        });
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodityType.setText("文件");
                selectCommodityTypeDialog.dismiss();
            }
        });
        clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodityType.setText("衣物");
                selectCommodityTypeDialog.dismiss();
            }
        });
        rests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodityType.setText("其他");
                selectCommodityTypeDialog.dismiss();
            }
        });
    }

    /**
     * 按照图片尺寸压缩：
     */
    public static void compressPicture(String srcPath, File desPath) {
        FileOutputStream fos = null;
        BitmapFactory.Options op = new BitmapFactory.Options();

        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        op.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, op);
        op.inJustDecodeBounds = false;
        if (op.outWidth == 200f) return;
        // 缩放图片的尺寸
        float w = op.outWidth;
        float h = op.outHeight;
        float hh = 400f;//
        float ww = 400f;//
        // 最长宽度或高度1024
        float be = 1.0f;
//        if (w > h || w > ww) {
//            be = (float) (w / ww);
//        } else if (w < h || h > hh) {
//            be = (float) (h / hh);
//        }
        if (h > hh && w > ww) be = (float) (w / ww);
        if (be <= 0) {
            be = 1.0f;
        }
        op.inSampleSize = (int) be;// 设置缩放比例,这个数字越大,图片大小越小.
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, op);
        int desWidth = (int) (w / be);
        int desHeight = (int) (h / be);
        bitmap = Bitmap.createScaledBitmap(bitmap, desWidth, desHeight, true);
        try {
            fos = new FileOutputStream(desPath);
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
                    if (rule!=null) rule.setText(jsonObject.getString("rule"));
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

    private void imputedPrice() {
        if (address == null) return;
        if (selectedPosition == -1) return;
        if (isEmpty(selectArea.getText())) return;
        if (endAddress == null) {
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
                    .addFormDataPart("endPro", endAddress.getPro())
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
