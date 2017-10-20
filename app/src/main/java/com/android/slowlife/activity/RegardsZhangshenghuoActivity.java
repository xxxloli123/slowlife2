package com.android.slowlife.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.util.CacheActivity;
import com.interfaceconfig.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/25 0025.
 */

public class RegardsZhangshenghuoActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout go_back;
    private TextView version;
    private int versionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_regards_zhangshenhuo);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        version = findView(R.id.version);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            version.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        findView(R.id.update).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl://返回
                finish();
                break;
            case R.id.update:
                update(this, versionCode);
                break;
        }
    }

    public static int getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void update(final Context context, final int versionCode) {
        RequestBody requestBody=new MultipartBody.Builder()
                .addFormDataPart("type","android")
                .build();
        Request req = new Request.Builder()
                .tag("")
                .post(requestBody)
                .url(Config.Url.getUrl(Config.UPDATE)).build();
        new OkHttpClient().newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    int ver = json.getJSONObject("appVersion").getInt("version");
                    if (ver > versionCode) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //创建下载任务,downloadUrl就是下载链接
                                DownloadManager.Request request = new DownloadManager.Request(
                                        Uri.parse(Config.Url.getUrl("/slowlife/share/appdownload?type=android")));
                                //指定下载路径和下载文件名
                                request.setDestinationInExternalPublicDir("/sdcard/Android/" + context.getPackageName(), "惠递.apk");
                                request.setDescription("惠递新版本下载");
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setMimeType("application/vnd.android.package-archive");
                                // 设置为可被媒体扫描器找到
                                request.allowScanningByMediaScanner();
                                // 设置为可见和可管理
                                request.setVisibleInDownloadsUi(true);
                                //获取下载管理器
                                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                                //将下载任务加入下载队列，否则不会进行下载
                                downloadManager.enqueue(request);
                            }
                        });
                    } else {
                        Toast.makeText(context, "已安装最新版", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private static Handler handler = new Handler();
}
