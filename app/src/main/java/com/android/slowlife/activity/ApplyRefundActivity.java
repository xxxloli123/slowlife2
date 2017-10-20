package com.android.slowlife.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.Common;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/7 0007.
 */

public class ApplyRefundActivity extends BaseActivity {
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.chargeback_layout)
    RelativeLayout chargebackLayout;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.online_refund_layout)
    RelativeLayout onlineRefundLayout;
    @Bind(R.id.text1)
    TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_apply_refund);
        ButterKnife.bind(this);
        String text1 = text.getText().toString().trim();
        setTextviewColor(text1, "24小时内");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * 设置txtview中部分字体的颜色
     */
    private void setTextviewColor(String text, String hint) {
        int fstart = text.indexOf(hint);
        int fend = fstart + hint.length();
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(Color.RED), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        TextView tvColor = (TextView) findViewById(R.id.text);
        tvColor.setText(style);
    }

    @OnClick({R.id.back_rl, R.id.chargeback_layout, R.id.online_refund_layout, R.id.text1})
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.chargeback_layout:
                Common.phoneDialog(this,"023-12345678");
                break;
            case R.id.online_refund_layout:
                intent=new Intent(this,ApplyRefundActivity1.class);
                startActivity(intent);
                finish();
                break;
            case R.id.text1:
                Common.dialPhone(this,text1.getText().toString().trim());
                break;
        }
    }
}
