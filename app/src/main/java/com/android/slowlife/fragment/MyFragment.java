package com.android.slowlife.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.BaseFragment;
import com.android.slowlife.R;
import com.android.slowlife.activity.AccountInformationActivity;
import com.android.slowlife.activity.HelpActivity;
import com.android.slowlife.activity.IntegralShopActivity;
import com.android.slowlife.activity.LoginActivity;
import com.android.slowlife.activity.MessageCentreActivity;
import com.android.slowlife.activity.MyBalanceActivity;
import com.android.slowlife.activity.MyCollectActivity;
import com.android.slowlife.activity.MyIntegralActivity;
import com.android.slowlife.activity.ReceiptAddressActivity;
import com.android.slowlife.activity.RecommentdAwardActivity;
import com.android.slowlife.activity.SetActivity;
import com.android.slowlife.activity.UseRedPacketActivity;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.bean.Info;
import com.android.slowlife.util.Common;
import com.android.slowlife.util.SaveUtils;
import com.android.slowlife.util.SimpleCallback;
import com.android.slowlife.view.RoundDrawable;
import com.interfaceconfig.Config;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Administrator on 2017/1/19 .
 */
public class MyFragment extends BaseFragment {

    @Bind(R.id.message_image)
    ImageView messageImage;
    @Bind(R.id.message_layout)
    RelativeLayout messageLayout;
    @Bind(R.id.set_layout)
    RelativeLayout setLayout;
    @Bind(R.id.head_image)
    ImageView headImage;
    @Bind(R.id.name_text)
    TextView nameText;
    @Bind(R.id.phone_text)
    TextView phoneText;
    @Bind(R.id.balance_text)
    TextView balanceText;
    @Bind(R.id.balance_layout)
    LinearLayout balanceLayout;
    @Bind(R.id.bred_packet_text)
    TextView bredPacketText;
    @Bind(R.id.bred_packet_layout)
    LinearLayout bredPacketLayout;
    @Bind(R.id.integral_text)
    TextView integralText;
    @Bind(R.id.integral_layout)
    LinearLayout integralLayout;
    @Bind(R.id.address_layout)
    RelativeLayout addressLayout;
    @Bind(R.id.collection_layout)
    RelativeLayout collectionLayout;
    @Bind(R.id.money)
    TextView money;
    @Bind(R.id.recommended_prize_layout)
    RelativeLayout recommendedPrizeLayout;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.shop_layout)
    RelativeLayout shopLayout;
    @Bind(R.id.service_center_layout)
    RelativeLayout serviceCenterLayout;
    @Bind(R.id.grade_layout)
    RelativeLayout gradeLayout;
    private Info info;
    private String head = "";
    private  View rootview;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, rootview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            (rootview.findViewById(R.id.ll)).setPadding(0, Common.getStatusHeight(getActivity()), 0, 0);
        }
        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        Info tem = ((MyApplication) getContext().getApplicationContext()).getInfo();
        if (SaveUtils.loadHead(getContext()) != null)
            headImage.setImageDrawable(new RoundDrawable(SaveUtils.loadHead(getContext())));
        if (tem == null) {
            resetData();
            return;
        }

        if (this.info == null || tem.getHeaderImg() != this.info.getHeaderImg() || head != tem.getHeaderImg()) {
            this.info = tem;
            loadImg();
        }
        nameText.setText(info.getNickName());
        phoneText.setText(info.getPhone());
        String account = getArguments().getString("account_info");
        try {
            JSONObject accountInfo = new JSONObject(account);
            balanceText.setText(accountInfo.getString("balance"));      //  余额
            integralText.setText(accountInfo.getString("integral"));     // 积分
            bredPacketText.setText(accountInfo.getString("RedPacketsNumber"));     // 红包
        } catch (Exception e) {
        }
        if (rootview != null) loadAccountInfo();
    }


    /**
     * 加载账户余额红包积分信息
     */
    private void loadAccountInfo() {
        if (info==null|| TextUtils.isEmpty(info.getId()))return;
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId())
                .build();
        Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.ACCOUNT))
                .post(requestBody).build();
        new OkHttpClient().newCall(request).enqueue(new SimpleCallback(getContext()) {
            @Override
            public void onSuccess(String tag, JSONObject json) throws JSONException {
                getArguments().putString("account_info", json.toString());
                if (balanceText == null
                        || integralText == null
                        || bredPacketText == null) return;
                balanceText.setText(json.getString("balance"));      //  余额
                integralText.setText(json.getString("integral"));     // 积分
                bredPacketText.setText(json.getString("RedPacketsNumber"));     // 红包
            }

            @Override
            public void onFail(Call call) {
                super.onFail(call);

            }
        });
    }

    private void resetData() {
        info = null;
        nameText.setText("未登录");
        phoneText.setText("");
        balanceText.setText("0");
        integralText.setText("0");
        bredPacketText.setText("0");
        headImage.setImageResource(R.drawable.def_head_ico);
    }

    /**
     * 初始化控件
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (!isSigned()) doLogin();
        if (getUserVisibleHint()) setData();
    }

    public static MyFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.message_layout, R.id.account_rl, R.id.set_layout, R.id.head_image, R.id.balance_layout,
            R.id.bred_packet_layout, R.id.integral_layout, R.id.address_layout, R.id.collection_layout,
            R.id.recommended_prize_layout, R.id.shop_layout, R.id.service_center_layout, R.id.grade_layout})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.message_layout://消息
                intent = new Intent(getActivity(), MessageCentreActivity.class);
                starttNewPage(intent);
                break;
            case R.id.set_layout://设置
                intent = new Intent(getActivity(), SetActivity.class);
                startActivityForResult(intent, 101);
                break;
            case R.id.head_image://头像
                if (isSigned())
                    startActivity(new Intent(getContext(), AccountInformationActivity.class));
                else doLogin();
                break;
            case R.id.balance_layout://余额
                intent = new Intent(getActivity(), MyBalanceActivity.class);
                starttNewPage(intent);
                break;
            case R.id.bred_packet_layout://红包
                intent = new Intent(getActivity(), UseRedPacketActivity.class);
                startActivity(intent);
                break;
            case R.id.integral_layout://积分
                intent = new Intent(getActivity(), MyIntegralActivity.class);
                starttNewPage(intent);
                break;
            case R.id.address_layout:// 常用地址
                intent = new Intent(getActivity(), ReceiptAddressActivity.class);
//                starttNewPage(intent);
                startActivity(intent);
                break;
            case R.id.collection_layout://收藏
                intent = new Intent(getActivity(), MyCollectActivity.class);
                starttNewPage(intent);
                break;
            case R.id.recommended_prize_layout://推荐有奖
                intent = new Intent(getActivity(), RecommentdAwardActivity.class);
                startActivity(intent);
                break;
            case R.id.shop_layout://积分商城
                intent = new Intent(getActivity(), IntegralShopActivity.class);
                startActivity(intent);
                break;
            case R.id.service_center_layout://服务中心
//                intent = new Intent(getActivity(), ServiceCenterActivity.class);
                intent = new Intent(getContext(), HelpActivity.class);
                intent.putExtra(HelpActivity.URI,"app/appGeneral/serviceCenter.html");
                startActivity(intent);
                break;
            case R.id.grade_layout://欢迎评分
//                intent=new Intent(getActivity(), CouponCentreActiviy.class);
//                startActivity(intent);
                Toast.makeText(getActivity(), "欢迎评分", Toast.LENGTH_SHORT).show();
                break;
            case R.id.account_rl://登陆
                if (!isSigned()) doLogin();
                break;
        }
    }

    private boolean isSigned() {
        return (((MyApplication) getContext().getApplicationContext()).getInfo() != null);
    }

    private void doLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, 101);
    }

    private void starttNewPage(Intent intent) {
        if (info == null) doLogin();
        else startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            setData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && rootview != null) setData();
    }

    private void loadImg() {
        if (info==null||TextUtils.isEmpty(info.getHeaderImg()))return;
        final Request request = new Request.Builder().url(Config.HEAD + info.getHeaderImg()).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.obtainMessage(1).sendToTarget();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Bitmap img = BitmapFactory.decodeStream(response.body().byteStream());
                handler.obtainMessage(0, img).sendToTarget();
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    Toast.makeText(getContext(), "加载头像失败", Toast.LENGTH_SHORT).show();
                case 0:
                    if (msg.obj != null) {
                        Bitmap img = (Bitmap) msg.obj;
                        if (info != null)
                            info.setHead(img);
                        MyApplication app = ((MyApplication) getContext().getApplicationContext());
                        if (app.getInfo() != null) app.getInfo().setHead(img);
                        if (headImage != null)
                            headImage.setImageDrawable(new RoundDrawable((Bitmap) msg.obj));
                        SaveUtils.saveHead(getContext(), img);
                    }
//                    else
//                        Toast.makeText(getContext(), "加载头像失败", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootview = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(getActivity()).release();
    }

}
