package com.android.slowlife;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.android.slowlife.app.MyApplication;

import butterknife.Bind;

/**
 * Created by sgrape on 2017/5/4.
 */

public abstract class MapActivity extends BaseActivity implements LocationSource,
        AMapLocationListener {
    @Bind(com.slowlife.map.R.id.mapView)
    MapView mapView;
    protected AMap aMap;
    protected UiSettings uiSettings;
    protected OnLocationChangedListener mListener;
    protected AMapLocationClient mlocationClient;
    private Circle circle;
    // /声明mLocationOption对象
    protected AMapLocationClientOption mLocationOption;
    private Marker marker;
    private boolean first = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (mapView == null)
            mapView = findView(com.slowlife.map.R.id.mapView);
        mapView.onCreate(savedInstanceState);
        initMap();
        init();
    }

    /**
     * 初始化地图
     */
    private void initMap() {
        if (aMap == null) {
            if (mapView == null) return;
            aMap = mapView.getMap();
            uiSettings = aMap.getUiSettings();
        }
        mlocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mlocationClient.startLocation();
    }

    @Override
    protected void init() {
        AMapLocation point = getIntent().getParcelableExtra("latlonPoint");
        if (point == null)
            point = ((MyApplication) getApplication()).getLocation();
        if (point != null) {
            CameraPosition cameraPosition = new CameraPosition(new LatLng(point.getLatitude(), point.getLongitude()), 18, 30, 30);
            AMapOptions aOptions = new AMapOptions();
            aOptions.camera(cameraPosition);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            aMap.animateCamera(cameraUpdate);
            circle = aMap.addCircle(new CircleOptions()
                    .center(new LatLng(point.getLatitude(), point.getLongitude()))
                    .strokeWidth(0)
                    .fillColor(Color.argb(50, 8, 147, 255)));
            marker = aMap.addMarker(new MarkerOptions().position(new LatLng(point.getLatitude(), point.getLongitude()))
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }

    /**
     * 获取布局ID
     *
     * @return
     */
    protected abstract
    @LayoutRes
    int getLayoutId();

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
        deactivate();
        first = false;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null)
            mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) mapView.onDestroy();
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && amapLocation.getErrorCode() == 0) {
            if (mListener != null)
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            //定位成功回调信息，设置相关消息
//            amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//            amapLocation.getLatitude();//获取纬度
//            amapLocation.getLongitude();//获取经度
//            amapLocation.getAccuracy();//获取精度信息
            // latitude=29.589898#longitude=106.480093
            LatLng latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
            if (aMap == null) return;

            if (marker == null)
                marker = aMap.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            else marker.setPosition(latLng);
            // 定位精度
            float accuracy = amapLocation.getAccuracy();
            float perPixel = aMap.getScalePerPixel();

            if (!first) {
                aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 18, 30, 30)));
                first = true;
            }
            if (circle == null) {
                circle = aMap.addCircle(new CircleOptions().center(latLng)
                        .radius(accuracy)
                        .strokeWidth(0)
                        .fillColor(Color.argb(50, 8, 147, 255)));
            } else {
                circle.setCenter(latLng);
                circle.setRadius(accuracy);
            }
            ((MyApplication) getApplication()).setLocation(amapLocation);
            System.out.println(accuracy / perPixel + "\t\t\t" + accuracy);
        } else {
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
            Log.e("AmapError", "location Error, ErrCode:"
                    + amapLocation.getErrorCode() + ", errInfo:"
                    + amapLocation.getErrorInfo());
        }
    }
}
