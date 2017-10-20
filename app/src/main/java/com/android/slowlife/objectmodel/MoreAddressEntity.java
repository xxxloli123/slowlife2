package com.android.slowlife.objectmodel;

/**
 * Created by Administrator on 2017/2/11 0011.
 */

public class MoreAddressEntity {
    private String tag;//0当前 1不是当前
    private String name;
    private String address;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
