package com.android.slowlife.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.util.CacheActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

public class OrderPaymentActivity extends BaseActivity {
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.price1)
    TextView price1;
    @Bind(R.id.order_number)
    TextView orderNumber;
    @Bind(R.id.order)
    RelativeLayout order;
    @Bind(R.id.alipay)
    RelativeLayout alipay;
    @Bind(R.id.weichat)
    RelativeLayout weichat;
    @Bind(R.id.wallet)
    RelativeLayout wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_order_payment);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.back_rl, R.id.order, R.id.alipay, R.id.weichat, R.id.wallet})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.order:
                break;
            case R.id.alipay:
                Toast.makeText(this,"支付宝",Toast.LENGTH_SHORT).show();
                break;
            case R.id.weichat:
                Toast.makeText(this,"微信",Toast.LENGTH_SHORT).show();
                break;
            case R.id.wallet:
                walletDialog();
                break;
        }
    }
    /**
     * 钱包dialog窗口
     */
    private void walletDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout mDialog = (LinearLayout) inflater.inflate(R.layout.dialog_balance_payment, null);
        final Dialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(mDialog);// 设置对话框的布局
        Button mYes = (Button) window.findViewById(R.id.button);
        TextView balance= (TextView) window.findViewById(R.id.balance);
        TextView alipay= (TextView) window.findViewById(R.id.alipay);
        EditText password= (EditText) window.findViewById(R.id.password);
        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
