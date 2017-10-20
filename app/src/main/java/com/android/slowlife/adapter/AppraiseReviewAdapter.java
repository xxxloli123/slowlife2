package com.android.slowlife.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.view.CustomRadioGroup;
import com.android.slowlife.view.RatingBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/17 0017.
 */


public class AppraiseReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private TradeNameAdapter adapter;
    private Context context;
    private final static int TYPE_EVALUATES = 0;
    private final static int TYPE_LISTVIEW = 1;
    private boolean isFirst = true;
    private EvaluatesHolder evaluatesHolder = null;
    private ListviewHolder listviewHolder=null;
    private String[] labStr = {"全部", "黑色", "满意", "送货快", "商品不错", "不满意", "玫瑰金色", "金色", "银色"};

    public AppraiseReviewAdapter
            (Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_EVALUATES:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evaluates, null);
                holder = new EvaluatesHolder(view);
                break;
            case TYPE_LISTVIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appraise1, null);
                holder = new ListviewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_EVALUATES:
                if (evaluatesHolder == null) {
                    evaluatesHolder = (EvaluatesHolder) holder;
                    setSpacing(evaluatesHolder.customRadioGroup, 12, 8);
                    evaluatesHolder.customRadioGroup.setListener(new CustomRadioGroup.OnclickListener() {
                        @Override
                        public void OnText(String text) {

                        }
                    });
                    for (int i = 0; i < labStr.length; i++) {
                        RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.item_commodity_evaluation_addlab_rb, null);
                        radioButton.setText(labStr[i] + "(88)");
                        evaluatesHolder.customRadioGroup.addView(radioButton);
                    }
                }
                break;
            case TYPE_LISTVIEW:
                if(listviewHolder==null){
                    listviewHolder = (ListviewHolder) holder;
                    adapter = new TradeNameAdapter(context, getGridview());
                    listviewHolder.gridView.setAdapter(adapter);
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (position) {
            case 0:
                return TYPE_EVALUATES;
            case 1:
                return TYPE_LISTVIEW;
        }
        return TYPE_LISTVIEW;
    }

    /**
     * gridview添加数据
     */
    private List<String> getGridview() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 2; i++) {
            list.add("心心相印抽纸" + i);
        }
        return list;
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ListviewHolder extends RecyclerView.ViewHolder {
        ImageView imageIconHead;
        TextView txtName;
        RatingBar rateAppraiseScore;
        TextView tvTime;
        TextView tvAppraise;
        ImageView assist;
        GridView gridView;

        public ListviewHolder(View itemView) {
            super(itemView);
            imageIconHead = (ImageView) itemView.findViewById(R.id.image_icon_head);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            rateAppraiseScore = (RatingBar) itemView.findViewById(R.id.rate_appraise_score);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvAppraise = (TextView) itemView.findViewById(R.id.tv_appraise);
            assist = (ImageView) itemView.findViewById(R.id.assist);
            gridView = (GridView) itemView.findViewById(R.id.gridView);
        }

    }

    public static class EvaluatesHolder extends RecyclerView.ViewHolder {
        TextView tvTotalScore;
        TextView tvTotalPercent;
        RatingBar rateServiceAttitude;
        RatingBar rateGoodScore;
        CustomRadioGroup customRadioGroup;

        public EvaluatesHolder(View itemView) {
            super(itemView);
            tvTotalScore = (TextView) itemView.findViewById(R.id.tv_total_score);
            tvTotalPercent = (TextView) itemView.findViewById(R.id.tv_total_percent);
            rateServiceAttitude = (RatingBar) itemView.findViewById(R.id.rate_service_attitude);
            rateGoodScore = (RatingBar) itemView.findViewById(R.id.rate_good_score);
            customRadioGroup = (CustomRadioGroup) itemView.findViewById(R.id.customRadioGroup);
        }
    }

    private void setSpacing(CustomRadioGroup cg, int widthdp, int heightdp) {
        cg.setHorizontalSpacing(widthdp);
        cg.setVerticalSpacing(heightdp);
    }
}
