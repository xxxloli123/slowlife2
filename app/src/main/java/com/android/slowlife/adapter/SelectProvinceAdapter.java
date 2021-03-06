package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.slowlife.R;
import com.android.slowlife.view.MyRadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/2/13 0013.
 */

public class SelectProvinceAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private JSONArray areas;

    public SelectProvinceAdapter(Context mContext, JSONArray areas) {
        this.areas = areas;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return areas == null ? 0 : areas.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return areas.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_select_province, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            final JSONObject addr = areas.getJSONObject(position);
            holder.radio.setText(String.format("%s %s",addr.getString("endDistrict"),addr.getString("endStreet")));
        } catch (JSONException e) {
        }
        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.radio)
        MyRadioButton radio;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
