package com.android.slowlife.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.DoneDialog;
import com.android.slowlife.R;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.SaveUtils;
import com.android.slowlife.util.SimpleCallback;
import com.android.slowlife.view.RoundDrawable;
import com.dialog.LoadDialog;
import com.interfaceconfig.Config;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.SocializeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Administrator on 2017/1/25 0025.
 * <p>
 * <p>
 * TODO  账号信息
 */

public class AccountInformationActivity extends BaseActivity implements View.OnClickListener, UMAuthListener
        , DoneDialog.DialogButtonClickListener<SHARE_MEDIA> {
    private RelativeLayout go_back, headRl, nameRl, phoneRl, wechatRl, qqRl, noSetRl;
    private ImageView head;
    private TextView name, phone, wechat, qq, noSet;
    private Bitmap bitmap, bitmap1, bmp;
    private String srcPath;//图片保存在sdcard中的位置
    private File file = null, myCaptureFile = null;
    private Info info;
    private File imgPath;
    private LoadDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_account_information);
        info = ((MyApplication) getApplication()).getInfo();
        if (info == null) {
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        initView();
        initDate();
        UMShareAPI.get(this).fetchAuthResultWithBundle(this, savedInstanceState, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                SocializeUtils.safeShowDialog(dialog);
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                Toast.makeText(getApplicationContext(), "onRestoreInstanceState Authorize succeed", Toast.LENGTH_SHORT).show();
                SocializeUtils.safeCloseDialog(dialog);
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                Toast.makeText(getApplicationContext(), "onRestoreInstanceState Authorize onError", Toast.LENGTH_SHORT).show();
                SocializeUtils.safeCloseDialog(dialog);
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                Toast.makeText(getApplicationContext(), "onRestoreInstanceState Authorize onCancel", Toast.LENGTH_SHORT).show();
                SocializeUtils.safeCloseDialog(dialog);
            }
        });
    }

    private void initDate() {
        phone.setText(info.getPhone());
        bitmap = info.getHead();
        if (bitmap != null) head.setImageDrawable(new RoundDrawable(bitmap));
    }

    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        headRl = (RelativeLayout) findViewById(R.id.head_rl);
        headRl.setOnClickListener(this);
        head = (ImageView) findViewById(R.id.head);
        nameRl = (RelativeLayout) findViewById(R.id.name_rl);
        nameRl.setOnClickListener(this);
        name = (TextView) findViewById(R.id.name);
        phoneRl = (RelativeLayout) findViewById(R.id.phone_rl);
        phoneRl.setOnClickListener(this);
        phone = (TextView) findViewById(R.id.phone);
        wechatRl = (RelativeLayout) findViewById(R.id.wechat_rl);
        wechatRl.setOnClickListener(this);
        wechat = (TextView) findViewById(R.id.wechat);
        qqRl = (RelativeLayout) findViewById(R.id.qq_rl);
        qqRl.setOnClickListener(this);
        qq = (TextView) findViewById(R.id.qq);
        noSetRl = (RelativeLayout) findViewById(R.id.no_set_rl);
        noSetRl.setOnClickListener(this);
        noSet = (TextView) findViewById(R.id.no_set);
        Bitmap cache = SaveUtils.loadHead(this);
        if (cache != null) head.setImageDrawable(new RoundDrawable(cache));
        qq.setTextColor(getResources().getColor(R.color.hint_text_color));
        wechat.setTextColor(getResources().getColor(R.color.hint_text_color));
        if (isEmpty(info.getQqOpenId())) {
            qq.setTextColor(Color.RED);
            qq.setText("未绑定");
        } else {
            qq.setText("已绑定");
        }
        if (isEmpty(info.getWxOpenId())) {
            wechat.setTextColor(Color.RED);
            wechat.setText("未绑定");
        } else wechat.setText("已绑定");
        if (!isEmpty(info.getPhone())) phone.setText(info.getPhone());
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl://返回
                finish();
                break;
            case R.id.head_rl://头像
                popwindowsshow();
                break;
            case R.id.name_rl://用户名         昵称
//                intent = new Intent(this, AccountMessageActivity.class);
                intent = new Intent(this, NickActivity.class);
                startActivity(intent);
                break;
            case R.id.phone_rl://绑定电话
                if (isEmpty(info.getPhone())) {
                    if (info.getPhone() == null) {
                        intent = new Intent(this, BoundPhoneActivity.class);
                        intent.putExtra("showPwd", false);
                        startActivity(intent);
                        finish();
                        return;
                    }
                } else {
                    intent = new Intent(this, ChangePhoneActivity.class);
                    imgPath = new File(getExternalCacheDir(), UUID.randomUUID().toString() + ".png");
                    intent.putExtra("phone", phone.getText().toString().trim());
                    startActivityForResult(intent, 102);
                }
                break;
            case R.id.wechat_rl://微信
                if (!isEmpty(info.getWxOpenId())) {
                    new DoneDialog<SHARE_MEDIA>(this).setMessage("是否确认取消绑定微信?")
                            .setTag(SHARE_MEDIA.WEIXIN)
                            .setListener(this).show();
                } else {
                    UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.WEIXIN, this);
                }
                break;
            case R.id.qq_rl://QQ
                if (!isEmpty(info.getQqOpenId())) {
                    new DoneDialog<SHARE_MEDIA>(this).setMessage("是否确认取消绑定QQ?")
                            .setTag(SHARE_MEDIA.QQ)
                            .setListener(this).show();
                } else
                    UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.QQ, this);
                break;
            case R.id.no_set_rl://登录密码
                intent = new Intent(this, LoginPasswordActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 弹出窗口
     */
    private void popwindowsshow() {
        final PopupWindow pop = new PopupWindow(this);
        View view = getLayoutInflater().inflate(R.layout.item_popwindows_head, null);
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
                it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgPath)); //指定图片输出地址
                startActivityForResult(it, 1);
                pop.dismiss();
                popwindows.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//从相册中选择
                Intent local = new Intent();
                local.setType("image/*");
                local.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(local, 105);
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
     * 拍照上传
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case 1:
                if (!imgPath.exists()) {
                    Toast.makeText(this, "读取文件出错", Toast.LENGTH_SHORT).show();
                    return;
                }
                bitmap = BitmapFactory.decodeFile(imgPath.getAbsolutePath());
                Intent i = new Intent(this, ImageDisposeActivity.class);
                i.putExtra(ImageDisposeActivity.BITMAPPATH, imgPath);
                startActivityForResult(i, 99);
                break;
            case 2:             // 上传头像
                uploadImg(data);
                break;
            case 102:
                info = ((MyApplication) getApplication()).getInfo();
                phone.setText(info.getPhone());
                break;
            case 105:
                Uri uri = data.getData();
                if (uri == null) {
                    Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                    return;
                }
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                String path = null;
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    if (cursor.moveToFirst() && columnIndex >= 0)
                        path = cursor.getString(columnIndex);
                    cursor.close();
                }
                if (path == null) {
                    System.out.println(uri.getPath());
                    try {
                        File dir = new File(getCacheDir(), "tem.png");
                        Bitmap img = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        img.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(dir));
                        path = dir.getAbsolutePath();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(AccountInformationActivity.this, ImageDisposeActivity.class);
                intent.putExtra(ImageDisposeActivity.BITMAPPATH, path);
                startActivityForResult(intent, 2);
                break;
            default:
                break;
        }
    }

    // 上传头像
    private void uploadImg(Intent data) {
        bitmap = data.getParcelableExtra(ImageDisposeActivity.RESULT_IMG);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        Request.Builder builder = new Request.Builder().url(Config.Url.getUrl(Config.UPLOADFILE)).tag(Config.UPLOADFILE);
        RequestBody fileRequest = RequestBody.create(MediaType.parse("application/octet-stream"), baos.toByteArray());
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId())
                .addFormDataPart("type", "0")
                .addFormDataPart("pictures", info.getId() + ".png", fileRequest).build();

        builder.method("POST", requestBody);
        new OkHttpClient().newCall(builder.build()).enqueue(this);
    }


    /**
     * 把bitmap转成圆形
     */
    public Bitmap toRoundBitmap() {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int r = 0;
        //取最短边做边长
        if (width < height) {
            r = width;
        } else {
            r = height;
        }
        //构建一个bitmap
        Bitmap backgroundBm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //new一个Canvas，在backgroundBmp上画图
        Canvas canvas = new Canvas(backgroundBm);
        Paint p = new Paint();
        //设置边缘光滑，去掉锯齿
        p.setAntiAlias(true);
        RectF rect = new RectF(0, 0, r, r);
        //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
        //且都等于r/2时，画出来的圆角矩形就是圆形
        canvas.drawRoundRect(rect, r / 2, r / 2, p);
        //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(bitmap, null, rect, p);
        return backgroundBm;
    }


    @Override
    protected void onSuccess(Call call, Response response, JSONObject json) throws JSONException {
        switch (response.request().tag().toString()) {
            case Config.UPLOADFILE:
                Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
                head.setImageBitmap(toRoundBitmap());
                MyApplication app = (MyApplication) getApplication();
                info.setHead(bitmap);
                info.setHeaderImg(json.getJSONObject("user").getString("headerImg"));
                System.out.println(app.getInfo().getHeaderImg());
                SaveUtils.saveHead(this, bitmap);
                break;
        }
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

    @Override
    public void onStart(SHARE_MEDIA platform) {
        SocializeUtils.safeShowDialog(dialog);
    }

    @Override
    protected void onResume() {
        info = ((MyApplication) getApplication()).getInfo();
        name.setText(info.getNickName());
        super.onResume();
    }

    @Override
    public void onComplete(final SHARE_MEDIA platform, int action, final Map<String, String> data) {
        SocializeUtils.safeCloseDialog(dialog);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId())
                .addFormDataPart("type", platform.equals(SHARE_MEDIA.QQ) ? "qq" : "wx")
                .addFormDataPart("openId", data.get("openid"))
                .build();
        Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.BINDACCOUNT))
                .post(requestBody)
                .build();
        new OkHttpClient().newCall(request).enqueue(new SimpleCallback(this) {
            @Override
            public void onSuccess(String tag, JSONObject json) throws JSONException {
                Toast.makeText(AccountInformationActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                if (platform.equals(SHARE_MEDIA.QQ)) {
                    qq.setTextColor(getResources().getColor(R.color.hint_text_color));
                    qq.setText("已绑定");
                    info.setQqOpenId(data.get("openid"));
                } else {
                    wechat.setTextColor(getResources().getColor(R.color.hint_text_color));
                    wechat.setText("已绑定");
                    info.setWxOpenId(data.get("openid"));
                }
                ((MyApplication) getApplication()).setInfo(info);
            }
        });
    }

    @Override
    public void onError(SHARE_MEDIA platform, int action, Throwable t) {
        SocializeUtils.safeCloseDialog(dialog);
        Toast.makeText(this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancel(SHARE_MEDIA platform, int action) {
        SocializeUtils.safeCloseDialog(dialog);
        Toast.makeText(this, "取消了", Toast.LENGTH_LONG).show();
    }


    private void unBind(final SHARE_MEDIA share_media) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId())
                .addFormDataPart("type", share_media.equals(SHARE_MEDIA.QQ) ? "qq" : "wx")
                .build();
        Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.UNBINDACCOUNT))
                .post(requestBody)
                .build();
        new OkHttpClient().newCall(request).enqueue(new SimpleCallback(this) {
            @Override
            public void onSuccess(String tag, JSONObject json) throws JSONException {
                Toast.makeText(AccountInformationActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                if (share_media.equals(SHARE_MEDIA.QQ)) {
                    qq.setTextColor(Color.RED);
                    qq.setText("未绑定");
                    info.setQqOpenId("");
                } else {
                    wechat.setTextColor(Color.RED);
                    wechat.setText("未绑定");
                    info.setWxOpenId("");
                }
                ((MyApplication) getApplication()).setInfo(info);
            }
        });
    }

    @Override
    public void done(Dialog dialog, SHARE_MEDIA tag) {
        dialog.dismiss();
        unBind(tag);
    }

    @Override
    public void cancel(Dialog dialog, SHARE_MEDIA tag) {
        dialog.dismiss();
    }
}
