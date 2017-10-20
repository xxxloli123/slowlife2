package com.android.slowlife.http;

import android.util.Log;

import com.android.slowlife.ApplicationTest;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by sgrape on 2017/4/28.
 */

public class TestOkHttp extends ApplicationTest {

    public void test() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url("http://www.baidu.com");
        requestBuilder.method("GET", null);
        Request request = requestBuilder.build();
        Call mcall = okHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                    Log.i("wangshu", "cache---" + str);
                } else {
                    response.body().string();
                    String str = response.networkResponse().toString();
                    Log.i("wangshu", "network---" + str);
                }
                System.out.println(Thread.currentThread().getName());
            }
        });
    }


    /**
     * 上传文件
     */
    public void upLoadFile() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url("http://192.168.1.109:8080/slowlife/appuser/uploaduserimgs");
        RequestBody fileRequest = RequestBody.create(MediaType.parse("application/octet-stream"), "");
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", "402881ed5bcdaa4f015bcdabe3c10001")
                .addFormDataPart("type", "0")
                .addFormDataPart("pictures", "head.png", fileRequest).build();

        builder.method("POST", requestBody);
    }
}
