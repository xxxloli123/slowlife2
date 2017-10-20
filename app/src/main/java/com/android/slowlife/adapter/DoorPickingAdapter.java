package com.android.slowlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.slowlife.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/14 0014.
 */

public class DoorPickingAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private JSONArray arr;

    public DoorPickingAdapter(Context context, JSONArray arr) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.arr = arr;
    }

    public DoorPickingAdapter(Context context) {
        this(context, null);
    }


    @Override
    public int getCount() {
        if (arr != null) return arr.length();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        try {
            return arr.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_door_picking, null);
            vh = new ViewHolder(convertView);
        } else vh = (ViewHolder) convertView.getTag();
        convertView.setTag(vh);
        try {
            JSONObject json = arr.getJSONObject(position);
            vh.region.setText(json.getString("endDistrict"));
            vh.exceed.setText("¥" + json.getString("exceedingPrice"));
            vh.range.setText(json.getString("startPrice"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public void notifyDataSetChanged(JSONArray arr) {
        this.arr = arr;
        super.notifyDataSetChanged();
    }

    static class ViewHolder {
        @Bind(R.id.region)
        TextView region;
        @Bind(R.id.range)
        TextView range;
        @Bind(R.id.exceed)
        TextView exceed;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
