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
 * Created by Administrator on 2017/2/11 0011.
 */

public class NearbyAddressAdapter extends BaseAdapter {
    private JSONArray arr;
    private LayoutInflater inflater;

    public NearbyAddressAdapter(Context mContext, JSONArray arr) {
        inflater = LayoutInflater.from(mContext);
        this.arr = arr;
    }

    @Override
    public int getCount() {
        return arr == null ? 0 : arr.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return arr.get(position);
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
        try {
            ViewHolder holder = null;
            JSONObject json = arr.getJSONObject(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_nearby_address, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.address.setText(json.getString("name"));
        } catch (JSONException e) {

        }
        return convertView;
    }

    public void notifyDataSetChanged(JSONArray arr) {
        this.arr = arr;
        super.notifyDataSetChanged();
    }

    static class ViewHolder {
        @Bind(R.id.address)
        TextView address;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
