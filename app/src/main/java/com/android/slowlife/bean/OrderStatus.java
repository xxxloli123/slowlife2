package com.android.slowlife.bean;

import android.text.TextUtils;

/**
 * Created by sgrape on 2017/5/22.
 * e-mail: sgrape1153@gmail.com
 */

public enum OrderStatus {
    // 配送类
    UnReceivedOrder("UnReceivedOrder", "待抢单", "等待抢单"),
    ReceivedOrder("ReceivedOrder", "待取货", "等待快递员取货"),
    CancelOrder("CancelOrder", "已取消"),
    GoodsDelivery("GoodsDelivery", "待送达"),
    Complete("Complete", "已完成"),
    UnPayed("UnPayed", "待付款"),
    Closed("Closed", "已关闭");
    private String status;
    private String str;
    private String describe;

    OrderStatus(String status, String str) {
        this(status, str, null);
    }

    OrderStatus(String status, String str, String describe) {
        this.str = str;
        this.status = status;
        if (TextUtils.isEmpty(describe)) this.describe = str;
        else this.describe = describe;
    }


    public static OrderStatus creat(String status) {
        if (UnReceivedOrder.equals(status)) return UnReceivedOrder;
        else if (ReceivedOrder.equals(status)) return ReceivedOrder;
        else if (CancelOrder.equals(status)) return CancelOrder;
        else if (GoodsDelivery.equals(status)) return GoodsDelivery;
        else if (Complete.equals(status)) return Complete;
        else if (Closed.equals(status)) return Closed;
        else if (UnPayed.equals(status)) return UnPayed;
        else throw new IllegalArgumentException("参数超出可选区    " + status);
    }

    public String getStatus() {
        return str;
    }

    @Override
    public String toString() {
        return "[status:" + status + ",msg:" + str + "]";
    }

    public boolean equals(CharSequence status) {
        return TextUtils.equals(status.toString().trim(), this.status.trim());
    }
    public String getDescribe(){
        return describe;
    }

}
