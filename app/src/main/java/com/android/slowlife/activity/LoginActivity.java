package com.android.slowlife.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.LoginNavBarAdapter;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.util.SimpleCallback;
import com.android.slowlife.view.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.interfaceconfig.Config;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.SocializeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/1/21 0021.
 * 登陆页面
 */

public class LoginActivity extends BaseActivity {
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mPager;
    private ProgressDialog dialog;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.login_tabs);
        mPager = (ViewPager) findViewById(R.id.login_pager);
        TabsInformation();
        okHttpClient = new OkHttpClient();
    }

    /**
     * 根据选择的不同界面选择不同tabs
     */
    private void TabsInformation() {
        ArrayList<String> list = new ArrayList<>();
        list.add("账号密码");
        list.add("短信密码");
        mPager.setAdapter(new LoginNavBarAdapter(getSupportFragmentManager(), list));
        mPagerSlidingTabStrip.setViewPager(mPager);
        mPager.setCurrentItem(0);
    }

    /**
     * 登录
     *
     * @param phone
     * @param pwd
     */
    public void doLogin(String phone, String pwd) {

    }

    public void signed() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    public void onClick(View v) {
        if (dialog == null) dialog = new ProgressDialog(this);
        dialog.setMessage("请稍后");
        dialog.show();
        switch (v.getId()) {
            case R.id.wechat_image: {
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, infoL);
                break;
            }
            case R.id.qq_image: {
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, infoL);
                break;
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }


    UMAuthListener infoL = new UMAuthListener() {

        @Override
        public void onStart(SHARE_MEDIA share_media) {
            SocializeUtils.safeCloseDialog(dialog);
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Toast.makeText(LoginActivity.this, "获取成功", Toast.LENGTH_SHORT).show();
            String openId = map.get("openid");
            String name = map.get("name");
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("openId", openId)
                    .addFormDataPart("nickelname", name)
                    .addFormDataPart("type", share_media.equals(SHARE_MEDIA.QQ) ? "qq" : "wx")
                    .addFormDataPart("phoneType", "Android")
                    .addFormDataPart("token", ((MyApplication) getApplication()).getToken())
                    .build();
            Request request = new Request.Builder().post(requestBody)
                    .url(Config.Url.getUrl(Config.SIGNIN)).build();
            okHttpClient.newCall(request).enqueue(callback);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Toast.makeText(LoginActivity.this, "获取信息出错", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(LoginActivity.this, "已取消", Toast.LENGTH_SHORT).show();
        }
    };


    Callback callback = new SimpleCallback(this) {

        @Override
        public void onSuccess(String tag, JSONObject json) throws JSONException {
            Toast.makeText(LoginActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
            Info info = new Gson().fromJson(json.getJSONObject("user").toString(), Info.class);
            ((MyApplication) LoginActivity.this.getApplication()).setInfo(info);
            if (info.getPhone() == null) {
                Intent intent = new Intent(LoginActivity.this, BoundPhoneActivity.class);
                intent.putExtra("showPwd", false);
                startActivity(intent);
                finish();
                return;
            }
            (LoginActivity.this).signed();
        }
    };
}
