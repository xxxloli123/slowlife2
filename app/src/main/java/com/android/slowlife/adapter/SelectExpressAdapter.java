package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.android.slowlife.R;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/14 0014.
 */

public class SelectExpressAdapter extends BaseAdapter {
    private JSONArray arr;
    private LayoutInflater inflater;

    public SelectExpressAdapter(Context mContext, JSONArray list) {
        this.arr = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return arr == null ? 0 : arr.length();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        RadioButton radioButton = null;
        try {
            if (convertView == null) {
                radioButton = (RadioButton) inflater.inflate(R.layout.item_select_express, null);
            } else {
                radioButton = (RadioButton) convertView;
            }
            radioButton.setText(arr.getJSONObject(position).getString("name"));
        } catch (JSONException e) {
        }
        return radioButton;
    }

    static class ViewHolder {
        @Bind(R.id.radio)
        RadioButton radio;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
