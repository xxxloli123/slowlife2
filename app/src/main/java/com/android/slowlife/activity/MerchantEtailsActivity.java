package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.BaseActivity1;
import com.android.slowlife.R;
import com.android.slowlife.adapter.BusinessQualificationAdapter;
import com.android.slowlife.adapter.CampaignAdapter;
import com.android.slowlife.adapter.CommentAdapter;
import com.android.slowlife.adapter.OutdoorSceneAdapter;
import com.android.slowlife.objectmodel.CampaignEntity;
import com.android.slowlife.objectmodel.CommentEntity;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.view.CustomRadioGroup;
import com.android.slowlife.view.MyGridView;
import com.android.slowlife.view.MyListView;
import com.android.slowlife.view.RatingBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/10 0010.
 */

public class MerchantEtailsActivity extends BaseActivity1 {
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.img_icon)
    ImageView imgIcon;
    @Bind(R.id.right_dish_name)
    TextView rightDishName;
    @Bind(R.id.upto_amount_text)
    TextView uptoAmountText;
    @Bind(R.id.minute)
    TextView minute;
    @Bind(R.id.distance)
    TextView distance;
    @Bind(R.id.distribution_cost)
    TextView distributionCost;
    @Bind(R.id.notice)
    TextView notice;
    @Bind(R.id.score)
    TextView score;
    @Bind(R.id.ranking)
    TextView ranking;
    @Bind(R.id.comment)
    TextView comment;
    @Bind(R.id.comments)
    RelativeLayout comments;
    @Bind(R.id.customRadioGroup)
    CustomRadioGroup customRadioGroup;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.view_comments)
    LinearLayout viewComments;
    @Bind(R.id.view_more1)
    TextView viewMore1;
    @Bind(R.id.listview1)
    MyListView listview1;
    @Bind(R.id.gridView)
    MyGridView gridView;
    @Bind(R.id.gridView1)
    MyGridView gridView1;
    @Bind(R.id.business_information)
    TextView businessInformation;
    @Bind(R.id.category)
    TextView category;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.report_merchant)
    RelativeLayout reportMerchant;
    @Bind(R.id.rate_appraise_score)
    RatingBar rateAppraiseScore;
    @Bind(R.id.merchant_sale_text)
    TextView merchantSaleText;
    private CommentAdapter commentAdapter;
    private CampaignAdapter campaignAdapter;
    private OutdoorSceneAdapter outdoorSceneAdapter;
    private BusinessQualificationAdapter businessQualificationAdapter;
    private String[] labStr = {"送货快", "服务不错", "很优惠", "包装精美", "日期新鲜", "品类齐全"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_merchant_etails);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化数据
     */
    private void initView() {
        commentAdapter = new CommentAdapter(this, getComment());
        listview.setAdapter(commentAdapter);
        campaignAdapter = new CampaignAdapter(this, getCampaign());
        listview1.setAdapter(campaignAdapter);
        outdoorSceneAdapter = new OutdoorSceneAdapter(this, getOutdoorScene());
        gridView.setAdapter(outdoorSceneAdapter);
        businessQualificationAdapter = new BusinessQualificationAdapter(this, getBusinessQualification());
        gridView1.setAdapter(businessQualificationAdapter);
        for (int i = 0; i < labStr.length; i++) {
            RadioButton radioButton = (RadioButton) this.getLayoutInflater().inflate(R.layout.item_merchant_etails_addlab_rb, null);
            radioButton.setText(labStr[i] + "(10)");
            customRadioGroup.addView(radioButton);
        }
    }

    /**
     * 评论测试数据
     */
    private List<CommentEntity> getComment() {
        List<CommentEntity> list = new ArrayList<CommentEntity>();
        CommentEntity entity = null;
        for (int i = 0; i < 10; i++) {
            entity = new CommentEntity();
            entity.setName("1234567" + i);
            entity.setRating(i + 1);
            if (i > 5) {
                entity.setRating(1);
            }
            list.add(entity);
        }
        return list;
    }

    /**
     * 活动与服务测试数据
     */
    private List<CampaignEntity> getCampaign() {
        List<CampaignEntity> list = new ArrayList<CampaignEntity>();
        CampaignEntity entity = null;
        for (int i = 0; i < 3; i++) {
            entity = new CampaignEntity();
            entity.setName("新用户下单立减10元（不与其他活动同享）");
            entity.setTag("0");
            if (i == 1) {
                entity.setTag("1");
                entity.setName("满60减5（不与其他活动同享）");
            }
            if (i == 2) {
                entity.setTag("2");
                entity.setName("情人节专项套餐");
            }
            list.add(entity);
        }
        return list;
    }

    /**
     * 商家实景测试数据
     */
    private List<String> getOutdoorScene() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            list.add("大堂" + i + "(1张)");
        }
        return list;
    }

    /**
     * 商家实景测试数据
     */
    private List<Integer> getBusinessQualification() {
        int[] id = {R.drawable.add, R.drawable.test61, R.drawable.test62};
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < id.length; i++) {
            list.add(id[i]);
        }
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.back_rl, R.id.view_comments, R.id.comments, R.id.report_merchant})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.view_comments://查看全部评论
                intent = new Intent(this, CommodityEvaluationActivity.class);
                startActivity(intent);
                break;
            case R.id.comments://查看评论
                intent = new Intent(this, CommodityEvaluationActivity.class);
                startActivity(intent);
                break;
            case R.id.report_merchant://举报商家
                intent = new Intent(this, ReportMerchantActivity.class);
                startActivity(intent);
                break;
        }
    }
}
