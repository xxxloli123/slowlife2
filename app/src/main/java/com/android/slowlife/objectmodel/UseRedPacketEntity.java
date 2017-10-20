package com.android.slowlife.objectmodel;

import java.sql.Time;

/**
 * Created by Administrator on 2017/1/25 0025.
 */

public class UseRedPacketEntity {
    private String price;
    private String availability;
    private String phone;
    private Time startTime;
    private Time endTime;
    private String tag;//0已过期 1可以使用 2已使用

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
