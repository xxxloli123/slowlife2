package com.android.slowlife.bean;

import android.text.TextUtils;

/**
 * Created by sgrape on 2017/5/23.
 * e-mail: sgrape1153@gmail.com
 */

public enum PayStatus {
    Payed("已付款"), UnPayed("未付款");
    private String status;

    PayStatus(String txt) {
        this.status = txt;
    }

    public static PayStatus getStatus(String t) {
        if (Payed.equals(t)) return Payed;
        else if (UnPayed.equals(t)) return UnPayed;
        else return null;
    }

    public String getStatus() {
        return status;
    }

    public boolean equals(CharSequence status) {
        return TextUtils.equals(status.toString().trim(), this.status.trim());
    }
}
