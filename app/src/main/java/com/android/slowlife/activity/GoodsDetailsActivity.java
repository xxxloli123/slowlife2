package com.android.slowlife.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.Common;
import com.android.slowlife.view.InfiniteLoopViewNet;

/**
 * Created by Administrator on 2017/1/31 0031.
 */

public class GoodsDetailsActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout go_back,addressRl;
    private TextView titleText,nameText,integralText,priceText,freightText,addressText;
    private InfiniteLoopViewNet picture;
    private Button ensure;
    /**
     * 轮播图测试数据
     * */
    private int[] image=new int[]{R.drawable.add,R.drawable.add};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_goods_details);
        initView();
        if(50000>=Integer.parseInt(integralText.getText().toString().trim())){//积分数小于兑换积分时
            ensure.setBackgroundResource(R.drawable.login3_corners_bgall);
        }else {
            ensure.setBackgroundResource(R.drawable.login2_corners_bgall);
        }
    }
    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        nameText= (TextView) findViewById(R.id.name_text);
        titleText= (TextView) findViewById(R.id.title_text);
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        if(Common.isNull(title)){
            titleText.setText("女士手表腕表石英表");
            nameText.setText("女士手表腕表石英表");
        }else{
            titleText.setText(title);
            nameText.setText(title);
        }
        picture = new InfiniteLoopViewNet(getApplicationContext());
        picture = (InfiniteLoopViewNet) findViewById(R.id.picture);
        picture.setImageLoopResources(image);
        integralText= (TextView) findViewById(R.id.integral_text);//积分数
        priceText= (TextView) findViewById(R.id.price_text);//商品价格
        priceText.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);//设置删除线
        freightText= (TextView) findViewById(R.id.freight_text);//邮费
        addressText= (TextView) findViewById(R.id.address_text);//收货地址
        addressRl= (RelativeLayout) findViewById(R.id.address_rl);
        addressRl.setOnClickListener(this);
        ensure= (Button) findViewById(R.id.ensure_bt);
        ensure.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_rl://返回
                finish();
                break;
            case R.id.address_rl://收货地址
                intent=new Intent(this,NewAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.ensure_bt:
                if(50000>=Integer.parseInt(integralText.getText().toString().trim())){//积分数小于兑换积分时
                    showDialog();
                }
                break;
        }
    }

    /**
     * dialog窗口
     * */
    private void showDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout mDialog = (LinearLayout) inflater.inflate(R.layout.dialog_convert_hint, null);
        final Dialog dialog = new AlertDialog.Builder(this).create();
        Button mYes = (Button) mDialog.findViewById(R.id.sure_bt);
        Button mNo = (Button) mDialog.findViewById(R.id.cancel_bt);
        TextView text= (TextView) mDialog.findViewById(R.id.covert_text);
        text.setText("确定要使用"+integralText.getText().toString().trim()+"积分进行抽奖");
        View view=mDialog.findViewById(R.id.view);
        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setContentView(mDialog);
    }
}
