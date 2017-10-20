package com.android.slowlife.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/7 .
 */

public class OkHttpUtils {
    private static OkHttpUtils okHttpUtils = null;
    private static OkHttpClient mOkHttpClient = null;
    private Context context;

    public OkHttpUtils(Context context) {
        this.context = context;
    }

    public static OkHttpUtils getInstance(Context context) {
        if (null == okHttpUtils) {
            okHttpUtils = new OkHttpUtils(context);
            initOkHttpClient(context);
        }
        return okHttpUtils;
    }

    private static void initOkHttpClient(Context context) {
        File sdcache = context.getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        mOkHttpClient = builder.build();
    }

    /**
     * okhttp get请求
     */
    public Call getRequest(String url) {
        //2. 获取Request对象
        Request request = new Request.Builder()
                .url(url)
                .build();
        //3. 获取Call对象
        Call call = mOkHttpClient.newCall(request);
        //4. 获取Response对象, 通过接口回调方式返回Response
        return call;
    }

    /**
     * okhttp post请求
     */
    public Call postRequest(String url, Map<String, String> maps) {
        FormBody.Builder builder = new FormBody.Builder();
        if (maps != null) {
            for (String key : maps.keySet()) {
                builder.add(key, maps.get(key));
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        return call;
    }

    /**
     * 上传文件
     */
    public final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    private void postAsynFile() {
        File file = new File("/sdcard/wangshu.txt");
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }

    /**
     * 下载文件
     */
    private void downAsynFile() {
        String url = "http://img.my.csdn.net/uploads/201603/26/1458988468_5804.jpg";
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(new File("/sdcard/wangshu.jpg"));
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();
                } catch (IOException e) {
                    LogUtils.i("wangshu", "IOException");
                    e.printStackTrace();
                }

                LogUtils.i("wangshu", "文件下载成功");
            }
        });
    }

    /**
     * 多文件上传
     */
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private void sendMultipart() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "wangshu")
                .addFormDataPart("image", "wangshu.jpg",
                        RequestBody.create(MEDIA_TYPE_PNG, new File("/sdcard/wangshu.jpg")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...")
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.i("wangshu", response.body().string());
            }
        });
    }
}
