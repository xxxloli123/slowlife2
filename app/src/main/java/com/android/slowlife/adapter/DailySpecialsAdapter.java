package com.android.slowlife.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.SpecialOfferProductEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/9 0009.
 */

public class DailySpecialsAdapter extends BaseAdapter {
    private List<SpecialOfferProductEntity> list;
    private Context mContext;
    private LayoutInflater inflater;

    public DailySpecialsAdapter(Context mContext, List<SpecialOfferProductEntity> list) {
        this.list = list;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size() == 0 || list == null ? 0 : list.size();
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
        ViewHolder holder=null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_daily_specials, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.originalPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
        holder.productName.setText(list.get(position).getName());
        holder.originalPrice.setText("￥"+list.get(position).getOriginalPrice());
        holder.specialOffer.setText("￥"+list.get(position).getSpecialOffer()+"");
        holder.discount.setText(list.get(position).getDiscount());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.product_image)
        ImageView productImage;
        @Bind(R.id.product_name)
        TextView productName;
        @Bind(R.id.special_offer)
        TextView specialOffer;
        @Bind(R.id.original_price)
        TextView originalPrice;
        @Bind(R.id.discount)
        TextView discount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
