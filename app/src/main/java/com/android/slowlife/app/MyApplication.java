package com.android.slowlife.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.android.slowlife.bean.Info;
import com.android.slowlife.model.Weather;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.SSS;
import com.android.slowlife.util.SaveUtils;
import com.google.gson.Gson;
import com.slowlife.xgpush.XgReceiver;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotifactionCallback;
import com.tencent.android.tpush.common.Constants;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.common.QueuedWork;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/1/19 .
 */
public class MyApplication extends Application {

    private Info info;
    public static Context mContext;
    private String token;
    private AMapLocation location;
    private Weather weather;

    @Override
    public void onCreate() {
        super.onCreate();
        {
//            Config.DEBUG = true;
            QueuedWork.isUseThreadPool = false;
            UMShareConfig config = new UMShareConfig();
            config.isNeedAuthOnGetUserInfo(true);
            UMShareAPI.get(this).setShareConfig(config);
            PlatformConfig.setWeixin("wxa16fd44d55d01e5c", "cfdefcdb3527c902fd99b15b322644be");
            PlatformConfig.setQQZone("1106119235", "xI1MBWSs8nur8wJI");
        }
        mContext = getApplicationContext();
        // 在主进程设置信鸽相关的内容
        if (isMainProcess()) {
            // 为保证弹出通知前一定调用本方法，需要在application的onCreate注册
            // 收到通知时，会调用本回调函数。
            // 相当于这个回调会拦截在信鸽的弹出通知之前被截取
            // 一般上针对需要获取通知内容、标题，设置通知点击的跳转逻辑等等
            XGPushManager.setNotifactionCallback(new XGPushNotifactionCallback() {

                @Override
                public void handleNotify(XGNotifaction xGNotifaction) {
                    Log.i("test", "处理信鸽通知：" + xGNotifaction);
                    // 获取标签、内容、自定义内容
                    String title = xGNotifaction.getTitle();
                    String content = xGNotifaction.getContent();
                    String customContent = xGNotifaction.getCustomContent();
                    XgReceiver.PushObserver.notifyPsuh("title=" + title +
                            "\ncontent=" + content +
                            "\ncustomContent=" + customContent);
                    // 其它的处理
                    // 如果还要弹出通知，可直接调用以下代码或自己创建Notifaction，否则，本通知将不会弹出在通知栏中。
                    xGNotifaction.doNotify();
                }
            });
        }

        // 注册接口
        XGPushManager.registerPush(getApplicationContext(), getInfo() == null ? "SlowLifeClient" :
                        (TextUtils.isEmpty(info.getPhone()) ? "SlowLifeClient" : info.getPhone()),
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        XGPushManager.setTag(getApplicationContext(), getPackageName());
                        token = data.toString();
                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.w(Constants.LogTag,
                                "+++ register push fail. token:" + data
                                        + ", errCode:" + errCode + ",msg:"
                                        + msg);
                    }
                });
        /**
         * 记录崩溃日志并处理崩溃的异常
         */
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
    }

    /**
     * 安全退出app
     */
    public static void edit() {
        CacheActivity.finishActivity();
        System.exit(0);
    }

    public String getToken() {
        return token == null ? "" : token;
    }

    public Info getInfo() {
        if (info == null) readUserInfo();
        return info;
    }

    public void updateInfoCache() {
        setInfo(info);
    }

    public void setInfo(Info info) {
        this.info = info;
        saveUserInfo(info);
        XGPushManager.registerPush(getApplicationContext(), getInfo() == null ? "SlowLifeClient" :
                        (TextUtils.isEmpty(info.getPhone()) ? "SlowLifeClient" : info.getPhone()),
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        XGPushManager.setTag(getApplicationContext(), getPackageName());
                        token = data.toString();
                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.w(Constants.LogTag,
                                "+++ register push fail. token:" + data
                                        + ", errCode:" + errCode + ",msg:"
                                        + msg);
                    }
                });
    }

    public boolean isMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    private void readUserInfo() {
        DataInputStream fis = null;
        ByteArrayOutputStream baos = null;
        File file = new File(getFilesDir(), "tem");
        try {
            long time = file.lastModified();
            long now = System.currentTimeMillis();
            if (time >= now || now - time > 10 * 24 * 60 * 60 * 1000) {
                file.delete();
                return;
            }
            fis = new DataInputStream(new FileInputStream(file));
            baos = new ByteArrayOutputStream();
            int len;
            byte[] buf = new byte[512];
            while ((len = fis.read(buf)) > 0) {
                baos.write(buf, 0, len);
                baos.flush();
            }
            String str = SSS.decrypt(baos.toString("UTF-8"), getPackageName());
            info = new Gson().fromJson(str, Info.class);
            if (info == null || TextUtils.isEmpty(info.getId()))
                info = null;
        } catch (Exception e) {
            file.delete();
        } finally {
            try {
                if (fis != null) fis.close();
                if (baos != null) baos.close();
            } catch (IOException e) {
            }
        }
    }

    private void saveUserInfo(Info info) {
        DataOutputStream fos = null;
        File file = new File(getFilesDir(), "tem");
        try {
            if (info == null) {
                if (file.exists()) file.delete();
                return;
            }
            if (file.exists()) file.delete();
            String jsonStr = new Gson().toJson(info);
            String tem = SSS.encrypt(jsonStr, getPackageName());
            fos = new DataOutputStream(new FileOutputStream(file));
            fos.write(tem.getBytes("UTF-8"));
            file.setLastModified(System.currentTimeMillis());
            SaveUtils.saveHead(this, info.getHead());
        } catch (Exception e) {
            file.delete();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
    }


    public AMapLocation getLocation() {
        return location;
    }

    public void setLocation(AMapLocation point) {
        this.location = point;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Weather getWeather() {
        return weather;
    }


}
