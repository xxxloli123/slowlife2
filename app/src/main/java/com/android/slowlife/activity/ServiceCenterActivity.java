package com.android.slowlife.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.util.CacheActivity;

/**
 * Created by Administrator on 2017/1/22 0022.
 * 服务中心
 */

public class ServiceCenterActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout go_back, customer_service, expressage_problem, pay_problem, balance_problem, redPacket_problem, integral_problem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_service_center);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        go_back = (RelativeLayout) findViewById(R.id.back_rl);
        go_back.setOnClickListener(this);
        customer_service= (RelativeLayout) findViewById(R.id.customer_service_hotline_rl);
        customer_service.setOnClickListener(this);
        expressage_problem= (RelativeLayout) findViewById(R.id.expressage_problem_rl);
        expressage_problem.setOnClickListener(this);
        pay_problem= (RelativeLayout) findViewById(R.id.pay_problem_rl);
        pay_problem.setOnClickListener(this);
        balance_problem= (RelativeLayout) findViewById(R.id.balance_problem_rl);
        balance_problem.setOnClickListener(this);
        redPacket_problem= (RelativeLayout) findViewById(R.id.red_packet_problem_rl);
        redPacket_problem.setOnClickListener(this);
        integral_problem= (RelativeLayout) findViewById(R.id.integral_problem_rl);
        integral_problem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()) {
            case R.id.back_rl://返回
                finish();
                break;
            case R.id.customer_service_hotline_rl://客服热线
                String s="12345678910";
                intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+s));
                startActivity(intent);
                break;
            case R.id.expressage_problem_rl://快递问题
                intent=new Intent(this,IntegralProblemActivity.class);
                startActivity(intent);
                break;
            case R.id.pay_problem_rl://支付问题
                intent=new Intent(this,IntegralProblemActivity.class);
                startActivity(intent);
                break;
            case R.id.balance_problem_rl://余额问题
                intent=new Intent(this,BalanceProblemActivity.class);
                startActivity(intent);
                break;
            case R.id.red_packet_problem_rl://红包问题
                intent=new Intent(this,IntegralProblemActivity.class);
                startActivity(intent);
                break;
            case R.id.integral_problem_rl://积分问题
                intent=new Intent(this,IntegralProblemActivity.class);
                startActivity(intent);
                break;
        }
    }
}
