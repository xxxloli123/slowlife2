package com.android.slowlife.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.BaseFragment;
import com.android.slowlife.R;
import com.android.slowlife.activity.CityExpressActivity;
import com.android.slowlife.activity.CodeActivity;
import com.android.slowlife.activity.IntegralConvertActivity;
import com.android.slowlife.activity.LoginActivity;
import com.android.slowlife.activity.OutsideExpressActivity;
import com.android.slowlife.activity.ReceivingAddressActivity;
import com.android.slowlife.activity.RecommentdAwardActivity;
import com.android.slowlife.activity.SearchOrderActivity;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.objectmodel.Advertise;
import com.android.slowlife.util.Common;
import com.android.slowlife.util.SimpleCallback;
import com.google.gson.Gson;
import com.google.zxing.client.android.CaptureActivity;
import com.interfaceconfig.Config;
import com.squareup.picasso.Picasso;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by sgrape on 2017/5/2.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.scan)
    View scan;
    //    @Bind(R.id.receiving_address)
//    TextView addr;
//    @Bind(R.id.fragment_home_temperature)
//    TextView temperature;
    @Bind(R.id.my_banner)
    MZBannerView myBanner;
//    @Bind(R.id.fragment_home_temperature)
//    TextView fragmentHomeTemperature;
    @Bind(R.id.share)
    ImageView share;
    @Bind(R.id.hrpkg)
    ImageView hrpkg;
    @Bind(R.id.ll)
    LinearLayout ll;
    //    @Bind(R.id.frag_home_weather)
//    TextView weather;
//    private Weather w;
    private boolean viewCreaed;
    private PopupWindow popupWindow;
//    private RegeocodeAddress address;
    private List<Advertise> advertises;


    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            (view.findViewById(R.id.ll)).setPadding(0, Common.getStatusHeight(getActivity()), 0, 0);
        }
//        if (address == null) setAddrChanged();
        ButterKnife.bind(this, view);
        getAadvertis();
        scan.setOnClickListener(this);
//        addr.setOnClickListener(this);
//        w = getArguments().getParcelable("weather");
//        if (w != null) setWeather(w);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCreaed = true;
//        setWeather(w);
    }

    @OnClick({R.id.fragment_express1, R.id.fragment_express,
            R.id.hrpkg, R.id.share, R.id.fragment_home_logo})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_express:         // 同城快递
                if (logined()) return;
                Intent express = new Intent(getActivity(), CityExpressActivity.class);
                startActivity(express);
                break;
            case R.id.fragment_express1:        // 区外快递
                if (logined()) return;
                Intent express1 = new Intent(getActivity(), OutsideExpressActivity.class);
                startActivity(express1);
                break;
            case R.id.scan://扫一扫                // 查快递
                startActivity(new Intent(getContext(), SearchOrderActivity.class));
//                popwindows();
                break;
            case R.id.receiving_address:
                if (logined()) return;
                Intent intent = new Intent(getActivity(), ReceivingAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_home_logo:
                try {
                    PackageManager packageManager = getActivity().getPackageManager();
                    Intent intent1 = new Intent();
                    intent1 =packageManager.getLaunchIntentForPackage("com.zsh.shopclient");
                    startActivity(intent1);
                }catch (Exception e){
                    Intent intent1 = new Intent();
                    intent1.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("http://www.zsh7.com/app/zsh/index.html");
                    intent1.setData(content_url);
                    startActivity(intent1);
                }
                //Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//                Toast.makeText(getContext(), "商城正在建设,敬请期待...", Toast.LENGTH_SHORT).show();
//                WxPay.Pay(getContext());
                break;
//            case R.id.fragment_home_temperature:
//                break;
            case R.id.hrpkg:
                if (logined()) return;
                startActivity(new Intent(getContext(), IntegralConvertActivity.class));
                break;
            case R.id.share:
                if (logined()) return;
                startActivity(new Intent(getContext(), RecommentdAwardActivity.class));
                break;
        }
    }


    /**
     * 弹出popwindows
     */
    private void popwindows() {
        if (popupWindow == null) {
            LayoutInflater mInflater = LayoutInflater.from(getActivity());
            View view = mInflater.inflate(R.layout.popwindows_scan, null);
            popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //扫一扫
            view.findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CaptureActivity.class);
                    startActivity(intent);
                }
            });
            //付款
            view.findViewById(R.id.payment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CodeActivity.class);
                    startActivity(intent);
                }
            });
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            // 允许点击外部消失
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            //设置popwindows弹窗在某控件下面
        }
        popupWindow.showAsDropDown(scan);
    }


    @Override
    public void onDestroyView() {
        Bundle bundle = getArguments();
//        bundle.putParcelable("weather", w);
        viewCreaed = false;
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private boolean logined() {
        Info info = ((MyApplication) getContext().getApplicationContext()).getInfo();
        if (info == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            return true;
        }
        return false;
    }

    public void setAddrChanged() {
        MyApplication app = (MyApplication) getContext().getApplicationContext();
//        RegeocodeAddress addr = app.getAddress();
//        if (addr != address && addr != null) {
//            address = addr;
//            this.addr.setText(address.getStreetNumber().getStreet());
//        }
    }

    /**
     * 获取电影列表
     */
    private void getAadvertis(){
        Request request = new Request.Builder().url(Config.Url.getUrl(Config.GETADVERTISEMENT)).build();
        //        new OkHttpClient().newCall(request).enqueue(new Callback(getContext());
        new OkHttpClient().newCall(request).enqueue(new SimpleCallback(getContext()) {
            @Override
            public void onSuccess(String tag, JSONObject json) throws JSONException {
                JSONArray arr = json.getJSONArray("listadv");
                advertises = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < arr.length(); i++) {
                    advertises.add(gson.fromJson(arr.getString(i), Advertise.class));
                }
                Log.e("图片",""+advertises.get(0).getAdvImg());
                setBanner(advertises);
            }
        });
    }

    private void setBanner(List<Advertise> advertises){
        if (myBanner==null)return;
        myBanner.setPages(advertises, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });

        myBanner.start();

    }

    @Override
    public void onDestroy() {
//        myBanner.pause();
        super.onDestroy();
    }

    public static class BannerViewHolder implements MZViewHolder<Advertise> {
        private ImageView mImageView;
        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.remote_banner_item,null);
            mImageView = (ImageView) view.findViewById(R.id.remote_item_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, Advertise data) {
            Log.e("zhouwei","current position:"+data);
            String halfUrl="http://www.zsh7.com/slowlife/img/advertisement/";
            Picasso.with(context).load(halfUrl+data.getAdvImg()).into(mImageView);
        }
    }
}
