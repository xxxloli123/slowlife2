package com.android.slowlife.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.slowlife.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ConstellationAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private List<Integer> list1;
    private int checkItemPosition = -1;
//    private int clickTemp = -2;//标识被选择的item
//    private int[] clickedList;//这个数组用来存放item的点击状态

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    public ConstellationAdapter(Context context, List<String> list, List<Integer> list1) {
        this.context = context;
        this.list = list;
        this.list1 = list1;
//        clickedList=new int[list.size()];
//        for (int i =0;i<list.size();i++){
//            clickedList[i]=0;      //初始化item点击状态的数组
//        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_constellation_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        fillValue(position, viewHolder);
        return convertView;
    }
    private void fillValue(int position, ViewHolder viewHolder) {
        viewHolder.text.setText(list.get(position));
        viewHolder.image.setImageResource(list1.get(position));
        //if (checkItemPosition != -1) {
        if (checkItemPosition == position) {
            viewHolder.text.setTextColor(context.getResources().getColor(R.color.red));
            viewHolder.layout.setBackgroundResource(R.drawable.sex_bgall);
//            if (clickedList[position]==0){
//                clickedList[position]=1;
//            }
        }else {
            viewHolder.text.setTextColor(context.getResources().getColor(R.color.text_color));
            viewHolder.layout.setBackgroundResource(R.drawable.address_bgall);
            //clickedList[position]=0;
        }
        //}
    }

    class ViewHolder {
        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.text)
        TextView text;
        @Bind(R.id.layout)
        LinearLayout layout;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
