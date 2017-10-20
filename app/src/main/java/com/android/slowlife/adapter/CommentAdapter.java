package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.CommentEntity;
import com.android.slowlife.view.RatingBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/10 0010.
 */

public class CommentAdapter extends BaseAdapter {
    private Context mContext;
    private List<CommentEntity> list;
    private LayoutInflater inflater;
    private TradeNameAdapter adapter;

    public CommentAdapter(Context mComtext, List<CommentEntity> list) {
        this.mContext = mComtext;
        this.list = list;
        inflater = LayoutInflater.from(mComtext);
    }

    @Override
    public int getCount() {
        return list == null || list.size() == 0 ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_comment, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameText.setText(list.get(position).getName());
        holder.productRatingBar.setCountSelected(list.get(position).getRating());
        adapter=new TradeNameAdapter(mContext,getGridview());
        holder.gridView.setAdapter(adapter);
        return convertView;
    }
    /**
     * gridview添加数据
     * */
    private List<String> getGridview(){
        List<String> list=new ArrayList<String>();
        for(int i=0;i<2;i++){
            list.add("心心相印抽纸"+i);
        }
        return list;
    }
    static class ViewHolder {
        @Bind(R.id.head_image)
        ImageView headImage;
        @Bind(R.id.name_text)
        TextView nameText;
        @Bind(R.id.time_text)
        TextView timeText;
        @Bind(R.id.product_ratingBar)
        RatingBar productRatingBar;
        @Bind(R.id.assist)
        ImageView assist;
        @Bind(R.id.gridView)
        GridView gridView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
