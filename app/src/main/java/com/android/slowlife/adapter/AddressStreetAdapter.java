package com.android.slowlife.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.AddressStreetEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */

public class AddressStreetAdapter extends BaseAdapter {
    private List<AddressStreetEntity.TarifflistBean> customers;
    Context context;

    public AddressStreetAdapter(Context context, List<AddressStreetEntity.TarifflistBean> customers){
        this.customers = customers;
        this.context = context;
    }

    @Override
    public int getCount() {
        return (customers==null)?0:customers.size();
    }

    @Override
    public Object getItem(int position) {
        return customers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public class ViewHolder{
        TextView textViewItem01;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AddressStreetEntity.TarifflistBean customer = (AddressStreetEntity.TarifflistBean)getItem(position);
        ViewHolder viewHolder = null;
        if(convertView==null){
            Log.d("MyBaseAdapter", "新建convertView,position="+position);
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.address_street_list, null);

            viewHolder = new ViewHolder();

            viewHolder.textViewItem01 = (TextView)convertView.findViewById(
                    R.id.tv_address_street);





            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
            Log.d("MyBaseAdapter", "旧的convertView,position="+position);
        }

        viewHolder.textViewItem01.setText(customer.getStartStreet());





        return convertView;
    }

}