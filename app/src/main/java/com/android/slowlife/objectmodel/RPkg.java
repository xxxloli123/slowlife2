package com.android.slowlife.objectmodel;

import android.os.Parcel;

import com.util.ParcelableUtil;

/**
 * Created by sgrape on 2017/6/5.
 * e-mail: sgrape1153@gmail.com
 */

public class RPkg extends ParcelableUtil {
    public static final Creator CREATOR = new Creator() {
        @Override
        public RPkg createFromParcel(Parcel source) {
            return new RPkg(source);
        }

        @Override
        public RPkg[] newArray(int size) {
            return new RPkg[size];
        }
    };
    /**
     * id : 402881f05c6c9f9a015c6ca3555f0001
     * redPacketId : 402881f05c6c9f9a015c6ca307760000
     * userId : 402881ed5bcdaa4f015bcdabe3c10001
     * phone : 15333333333
     * name : 新用户
     * cost : 5
     * gainDate : 2017-06-03
     * startTime : 2017-06-01
     * endTime : 2017-06-03
     * type : Expired
     * type_value : 已过期
     */

    private String id;
    private String redPacketId;
    private String userId;
    private String phone;
    private String name;
    private double cost;
    private String gainDate;
    private String startTime;
    private String endTime;
    private String type;
    private String type_value;

    public RPkg() {
    }

    private RPkg(Parcel source) {
        super(source);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(String redPacketId) {
        this.redPacketId = redPacketId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getGainDate() {
        return gainDate;
    }

    public void setGainDate(String gainDate) {
        this.gainDate = gainDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_value() {
        return type_value;
    }

    public void setType_value(String type_value) {
        this.type_value = type_value;
    }

}
