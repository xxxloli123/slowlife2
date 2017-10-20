package com.android.slowlife;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.android.slowlife.activity.GuideTrueActivity;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.fragment.AllOrderFragment;
import com.android.slowlife.fragment.HomeFragment;
import com.android.slowlife.fragment.MyFragment;
import com.android.slowlife.fragment.OrderFragment;
import com.android.slowlife.fragment.WOrderFrag;
import com.android.slowlife.model.Weather;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.SimpleCallback;
import com.google.gson.Gson;
import com.interfaceconfig.Config;
import com.slowlife.map.interfaces.APSImpl;
import com.slowlife.map.interfaces.APSInterface;
import com.tencent.android.tpush.XGPushClickedResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements APSInterface.OnApsChanged {
    @Bind(R.id.menus_frame)
    FrameLayout menusFrame;
    @Bind(R.id.index)
    RadioButton index;
    @Bind(R.id.all)
    RadioButton all;
    @Bind(R.id.order)
    RadioButton order;
    @Bind(R.id.my)
    RadioButton my;
    @Bind(R.id.group)
    RadioGroup group;

    private int id;
    private Button selectButton;
    private RadioButton indexs, finds, orders, mys;
    private RadioButton[] rButton;

    private final static String TAG_KEY = "TAG_KEY";
    private final static String HOME = "index";
    private final static String ALL = "all";
    private final static String ORDER = "order";
    private final static String MY = "my";
    private APSImpl apsi;
    private ImageView leadView;


    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            button((Button) v);
        }
    };

    private HomeFragment homeFragment;
    private OrderFragment allOrderFragment;
    private AllOrderFragment orderFragment;
    private MyFragment myFragment;
    private boolean isFirst = true;
    private long timeMillis;
    private AMapLocation mapLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView(savedInstanceState);
        apsi = new APSImpl(this);
        apsi.onCreate();
        apsi.addListener(this);
        if (savedInstanceState != null) {
            isFirst = savedInstanceState.getBoolean("isFirst");
        }
        MyApplication app = (MyApplication) getApplication();
        mapLocation = ((MyApplication) getApplication()).getLocation();
        if (getIntent().getBooleanExtra("rpkg", false) && app.getInfo()
                != null && savedInstanceState == null) {
            isFirst = false;
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("type", "RegisterDate")
                    .addFormDataPart("pro", mapLocation.getProvince())
                    .addFormDataPart("city", mapLocation.getCity())
                    .addFormDataPart("district", mapLocation.getDistrict())
                    .build();
            Request request = new Request.Builder()
                    .url(Config.Url.getUrl(Config.GETREDPACKETS))
                    .post(requestBody)
                    .tag(Config.GETREDPACKETS)
                    .build();
            new OkHttpClient().newCall(request).enqueue(new SimpleCallback(this) {
                @Override
                public void onSuccess(String tag, JSONObject json) throws JSONException {
                    JSONArray arr = json.getJSONArray("RedPackets");
                    showRpkgDialog(arr);
                }
            });
        }
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle == null) return;
        Set<String> keys = bundle.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            final String k = iterator.next();
            System.out.println("key = " + k + "\t\t\t" + bundle.get(k));
        }

        XGPushClickedResult result = (XGPushClickedResult) bundle.getSerializable("tag.tpush.NOTIFIC");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication app = (MyApplication) getApplication();
        if (app.getLocation() == null)
            apsi.onCreate();
        else {
            if (homeFragment != null) {
                homeFragment.setAddrChanged();
            }
        }
//        isFirst();
    }

    @Override
    protected void onPause() {
        super.onPause();
        apsi.onPause();
    }

    /*
       onSaveInstanceState方法会在什么时候被执行，有这么几种情况：
    1、当用户按下HOME键时。
    这是显而易见的，系统不知道你按下HOME后要运行多少其他的程序，自然也不知道activity A是否会被销毁，故系统会调用onSaveInstanceState，让用户有机会保存某些非永久性的数据。以下几种情况的分析都遵循该原则
    2、长按HOME键，选择运行其他的程序时。
    3、按下电源按键（关闭屏幕显示）时。
    4、从activity A中启动一个新的activity时。
    5、屏幕方向切换时，例如从竖屏切换到横屏时。
    onSaveInstanceState的调用遵循一个重要原则，即当系统“未经你许可”时销毁了你的activity，则onSaveInstanceState会被系统调用，这是系统的责任，因为它必须要提供一个机会让你保存你的数据
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(TAG_KEY, getIndex());
        outState.putBoolean("isFirst", isFirst);
        super.onSaveInstanceState(outState);
    }

    private int getIndex() {
        int index = -1;
        for (int i = 0; i < rButton.length; i++) {
            if (selectButton == rButton[i]) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 初始化控件
     */
    private void initView(Bundle savedInstanceState) {
        index.setOnClickListener(onClickListener);
        all.setOnClickListener(onClickListener);
        order.setOnClickListener(onClickListener);
        my.setOnClickListener(onClickListener);
        rButton = new RadioButton[]{index, all, order, my};

        int index = 0;
        if (savedInstanceState != null) {
            index = savedInstanceState.getInt(TAG_KEY, 0);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                index = bundle.getInt(TAG_KEY, 0);
            }
        }

        if (index < 0 || index > rButton.length - 1) {
            index = 0;
        }
        button(rButton[index]);
    }

    /**
     * 点击时需要切换相应的页面
     *
     * @param b 当前需要传入的按钮
     */
    private void button(Button b) {
        if (selectButton != null && selectButton == b) {
            return;
        }
        selectButton = b;
        Bundle bundle = new Bundle();
        if (selectButton == index) {
            if (homeFragment == null) {
                homeFragment = addFragment(HomeFragment.class, HOME, bundle);
            }
            changeFragment(homeFragment);
        } else if (selectButton == all) {
            if (allOrderFragment == null) {
                allOrderFragment = addFragment(OrderFragment.class, ALL, bundle);
            }
            changeFragment(allOrderFragment);
        } else if (selectButton == order) {
            if (orderFragment == null) {
                orderFragment = addFragment(WOrderFrag.class, ORDER, bundle);
            }
            changeFragment(orderFragment);
        } else if (selectButton == my) {
            if (myFragment == null) {
                myFragment = addFragment(MyFragment.class, MY, bundle);
            }
            changeFragment(myFragment);
        }
    }

    /**
     * 添加管理fragment 并返回
     *
     * @param fragmentClass 传入的fragment类
     * @param tag           fragment标识
     * @param bundle
     * @return
     */
    private <T> T addFragment(Class<? extends Fragment> fragmentClass, String tag, Bundle bundle) {
        //Fragment 管理者
        FragmentManager manager = getSupportFragmentManager();
        /*
         * FragmentTransaction方法对应Fragment生命周期方法：
		1. add() : onAttach -> onCreate -> onCreateView -> onActivityCreated -> onStart -> onResume
		2. remove() : onPause -> onStop -> onDestoyView -> onDestory -> onDetach
		3. attach() :  onCreateView -> onActivityCreated -> onStart -> onResume
		4. detach() : onPause -> onStop -> onDestoryView
		5. show() : 没有
		6. hide() : 没有
		 *
		 */
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = Fragment.instantiate(this, fragmentClass.getName(), bundle);
            if (bundle != null) {
                bundle.putString("fragment", fragmentClass.getName());
                fragment.setArguments(bundle);
            }
            transaction.add(R.id.menus_frame, fragment, tag);
            transaction.commit();
        }
        return (T) fragment;
    }

    /**
     * 切换fragment
     *
     * @param fragment 传入当前切换的fragment
     */
    private void changeFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment homeFragment = manager.findFragmentByTag(HOME);
        if (homeFragment != null && homeFragment != fragment) {
            transaction.hide(homeFragment);
            homeFragment.setUserVisibleHint(false);
        }

        Fragment findFragment = manager.findFragmentByTag(ALL);
        if (findFragment != null && findFragment != fragment) {
            transaction.detach(findFragment);
            findFragment.setUserVisibleHint(false);
        }

        Fragment orderFragment = manager.findFragmentByTag(ORDER);
        if (orderFragment != null && orderFragment != fragment) {
            transaction.detach(orderFragment);
            orderFragment.setUserVisibleHint(false);
        }

        Fragment myFragment = manager.findFragmentByTag(MY);
        if (myFragment != null && myFragment != fragment) {
            transaction.detach(myFragment);
            myFragment.setUserVisibleHint(false);
        }

        if (fragment != null) {
            if (fragment != homeFragment && fragment.isDetached()) {
                transaction.attach(fragment);
            } else if (fragment == homeFragment && fragment.isHidden()) {
                transaction.show(fragment);
            }
            fragment.setUserVisibleHint(true);
        }
        transaction.commit();
    }

    @Override
    public void onChanged(AMapLocation location) {
        if (TextUtils.isEmpty(location.getAdCode())) return;
        MyApplication app = (MyApplication) getApplication();
//        app.setAddress(address);
        app.setLocation(location);
        apsi.onPause();
    }


    @Override
    public void Fail() {

    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        final String jsonStr = response.body().string();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final JSONObject json = new JSONObject(jsonStr);
                    switch (response.request().tag().toString()) {
                        case Config.WEATHER:
                            if (json.getInt("status") == 1) {
                                Weather weather = new Gson().fromJson(json.getJSONArray("lives").getString(0), Weather.class);
                                MyApplication app = (MyApplication) getApplication();
                                app.setWeather(weather);
                                if (homeFragment != null) {
//                                    homeFragment.setWeather(weather);
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "获取天气失败", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onSuccess(Call call, Response response, JSONObject json) throws JSONException {
        super.onSuccess(call, response, json);
    }


    private void showRpkgDialog(final JSONArray arr) throws JSONException {
        if (arr.length() == 0) return;
        final Info info = ((MyApplication) getApplication()).getInfo();
        if (info == null) return;
        double c = 0;
        for (int i = 0; i < arr.length(); i++) {
            final JSONObject item = arr.getJSONObject(i);
            c += item.getDouble("cost");
        }
        final Dialog dialog = new Dialog(MainActivity.this);
        Window win = dialog.getWindow();
        win.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        dialog.setContentView(R.layout.rpkg_dialog);
        TextView total = (TextView) dialog.findViewById(R.id.total);
        final StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arr.length(); i++)
            stringBuffer.append(arr.getJSONObject(i).getString("id"))
                    .append(",");
        dialog.findViewById(R.id.pulldown).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("userId", info.getId())
                                .addFormDataPart("redIds", stringBuffer.substring(0, stringBuffer.length() - 1))
                                .addFormDataPart("str", "str")
                                .build();
                        Request request = new Request.Builder()
                                .url(Config.Url.getUrl(Config.PULLDOWNPKG))
                                .post(requestBody)
                                .build();
                        new OkHttpClient().newCall(request)
                                .enqueue(new SimpleCallback(MainActivity.this) {
                                    @Override
                                    public void onSuccess(String tag, JSONObject json) throws JSONException {
                                        Toast.makeText(MainActivity.this, json.getString("message"), 0).show();
                                    }
                                });
                    }
                });
        dialog.findViewById(R.id.close).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }
        );
        total.setText("¥" + c);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


//    private void isFirst() {
//        File file = new File(getFilesDir(), "slowfile.a");
//        boolean isExists = file.exists();
//        if (!isExists) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if (!isExists) lead();
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (leadView != null) {
                ViewGroup decorview = findView(R.id.root);
                decorview.removeView(leadView);
                leadView = null;
                return true;
            }
            if (System.currentTimeMillis() - timeMillis >= 2000) {
                Toast.makeText(this, "再次点击退出程序", Toast.LENGTH_SHORT).show();
                timeMillis = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     * */
    private void lead() {
        /**
         * 获取状态栏高度——方法1
         * */
        int statusBarHeight = 0;
//获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        final ImageView leadView = new ImageView(this);
        leadView.setPadding(0, statusBarHeight, 0, 0);
        leadView.setImageResource(R.drawable.lead_image);
        leadView.setScaleType(ImageView.ScaleType.FIT_XY);
        leadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        ViewGroup rootview = findView(R.id.root);
        rootview.addView(leadView);

        this.leadView = leadView;
    }
}
