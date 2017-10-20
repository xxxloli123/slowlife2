package com.android.slowlife.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.objectmodel.RPkg;

import java.util.List;

/**
 * Created by sgrape on 2017/6/9.
 * e-mail: sgrape1153@gmail.com
 */

public class UseRpkgAdapter extends Baseadapter<RPkg> {
    private String txt;
    private String type;
    private int redIc;
    private int itemR;
    private OnClick<RPkg> listener;
    private boolean selectAble;

    public UseRpkgAdapter(Context context, List<RPkg> data, String type) {
        super(context, data);
        this.type = type;
        if (redIc <= 0 || itemR <= 0) {
            switch (type) {
                case "Used": {
                    redIc = R.drawable.rpkg_used;
                    itemR = R.drawable.rpkg_used_right;
                    break;
                }
                case "pull":
                case "UnUsed":
                    redIc = R.drawable.rpkg_unuse;
                    break;
                case "Expired":
                    redIc = R.drawable.rpkg_expired;
                    itemR = R.drawable.rpkg_expired_right;
                    break;
            }
        }
    }

    @Override
    protected View getView(View view, int position) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder(view);
            if (selectAble) {
                view.setEnabled(false);
                view.setFocusable(false);
                view.setFocusableInTouchMode(false);
                holder.checkbox.setVisibility(View.VISIBLE);
                if (TextUtils.equals(type, "pull")) {
                    holder.yes_use.setVisibility(View.VISIBLE);
                    holder.yes_use.setText("立即领取");
                }
            }
            view.setTag(holder);
        }
        RPkg rPkg = list.get(position);
        ImageSpan imageSpan = new ImageSpan(view.getContext(), redIc);
        SpannableString spanString = new SpannableString(rPkg.getName());
        spanString.setSpan(imageSpan, 0, 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.position = position;
        holder.is_use.setBackgroundResource(redIc);
        holder.phone.setText(spanString);
        holder.price.setText(String.valueOf(rPkg.getCost()));
        holder.time.setText(String.format("%s - %s", rPkg.getStartTime(), rPkg.getEndTime()));
        return view;
    }

    @Override
    protected int getLayoutId(int position) {
        return R.layout.item_use_red_packet;
    }

    class ViewHolder {
        private TextView availability;
        private TextView time;
        private TextView phone;
        private TextView price;
        private TextView yes_use;
        private ImageView no_use;
        private LinearLayout is_use;
        private TextView is_use_text;
        private int position;
        private View checkbox;

        public ViewHolder(View view) {
            availability = (TextView) view.findViewById(R.id.availability_text);
            time = (TextView) view.findViewById(R.id.time_text);
            price = (TextView) view.findViewById(R.id.price_text);
            phone = (TextView) view.findViewById(R.id.phone_text);
            checkbox = view.findViewById(R.id.checkbox_layout);
            yes_use = (TextView) view.findViewById(R.id.yes_use_text);
            yes_use.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exec(position);
                }
            });
            if (TextUtils.equals("UnUsed", type)) {
            } else if (TextUtils.equals("Used", type)
                    || TextUtils.equals("Expired", type)) {
                yes_use.setBackgroundResource(itemR);
                yes_use.setVisibility(View.GONE);
                yes_use.setText(txt);
            } else if (TextUtils.equals("pull", type)) {
                yes_use.setVisibility(View.VISIBLE);
                yes_use.setText("立即领取");
            }
            no_use = (ImageView) view.findViewById(R.id.no_use_image);
            is_use = (LinearLayout) view.findViewById(R.id.is_use_layout);
        }
    }

    private void exec(int position) {
        if (listener != null) {
            listener.click(position, list.get(position));
        }
    }

    public void setSelectAble(boolean selectAble) {
        this.selectAble = selectAble;
    }

    public void setListener(OnClick<RPkg> listener) {
        this.listener = listener;
    }

    public static interface OnClick<T> {
        void click(int position, T t);
    }
}
