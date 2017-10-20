package com.android.slowlife;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.DisplayUtil;
import com.android.slowlife.database.impl.ProvinceDaoImpl;
import com.dialog.Dialog;

import java.util.List;

/**
 * Created by sgrape on 2017/7/29.
 * e-mail: sgrape1153@gmail.com
 */

public class AreaDialog extends Dialog {
    private ListView listview;
    private BaseAdapter adapter;
    private List<? extends Area> areas;
    private OnAreaSelectedCallback callback;

    public AreaDialog(@NonNull Context context, OnAreaSelectedCallback callback) {
        super(context, R.style.DialogStyle);
        setContentView(R.layout.dialog_area);
        this.adapter = new AreaAdapter(context);
        this.callback = callback;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        areas = ProvinceDaoImpl.getInstance(getContext()).getAll();
        listview = (ListView) findViewById(R.id.dialog_area_listview);
        listview.setAdapter(adapter);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(metrics.widthPixels / 3 * 2, metrics.heightPixels / 2);
        listview.setLayoutParams(lp);
        findViewById(R.id.dialog_area_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callback != null)
                            callback.onSelected(areas.get(listview.getCheckedItemPosition()), listview.getCheckedItemPosition());
                        dismiss();
                    }
                }
        );
    }

    public void show(int selectPosition) {
        show();
        listview.setItemChecked(selectPosition, true);
    }

    class AreaAdapter extends BaseAdapter {

        private Context context;
        private int pad;

        public AreaAdapter(Context context) {
            this.context = context;
            pad = DisplayUtil.dip2px(context, 8);
        }

        @Override
        public int getCount() {
            return areas == null ? 0 : areas.size();
        }

        @Override
        public Object getItem(int position) {
            return areas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CheckBox area;
            if (convertView == null) {
                area = new CheckBox(context);
                area.setPadding(pad, pad, pad, pad);
                area.setTextColor(Color.BLACK);
                area.setFocusableInTouchMode(false);
                area.setClickable(false);
                area.setFocusable(false);
                area.setButtonDrawable(null);
                area.setBackgroundResource(R.drawable.dialog_area_list_item_selector);
            } else area = (CheckBox) convertView;
            area.setTextColor(Color.BLACK);
            area.setText(areas.get(position).getName());
            return area;
        }

    }


    public static interface OnAreaSelectedCallback {
        public void onSelected(Area area, int position);
    }
}


