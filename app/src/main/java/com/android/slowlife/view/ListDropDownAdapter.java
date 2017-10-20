package com.android.slowlife.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.slowlife.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ListDropDownAdapter extends BaseAdapter {

    private Context context;
    private List<String> listText;
    private List<Integer> listIcon;
    private int checkItemPosition = -1;

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    public ListDropDownAdapter(Context context, List<String> listText, List<Integer> listIcon) {
        this.context = context;
        this.listText = listText;
        this.listIcon = listIcon;
    }

    @Override
    public int getCount() {
        return listText.size();
    }

    @Override
    public Object getItem(int position) {
        return listText.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_drop_down_sort, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        fillValue(position, viewHolder);
        return convertView;
    }

    private void fillValue(int position, ViewHolder viewHolder) {
        viewHolder.text.setText(listText.get(position));
        viewHolder.imgIcon.setImageResource(listIcon.get(position));
        if (checkItemPosition == position) {
            viewHolder.text.setTextColor(context.getResources().getColor(R.color.red));
            viewHolder.imgSelect.setImageResource(R.drawable.test75);
        } else {
            viewHolder.text.setTextColor(context.getResources().getColor(R.color.drop_down_unselected));
            viewHolder.imgSelect.setImageResource(R.color.white);
        }
    }


    class ViewHolder {
        @Bind(R.id.img_icon)
        ImageView imgIcon;
        @Bind(R.id.text)
        TextView text;
        @Bind(R.id.img_select)
        ImageView imgSelect;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
