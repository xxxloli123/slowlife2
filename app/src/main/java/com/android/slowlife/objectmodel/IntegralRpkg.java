package com.android.slowlife.objectmodel;

/**
 * Created by sgrape on 2017/7/5.
 * e-mail: sgrape1153@gmail.com
 */

public class IntegralRpkg {

    /**
     * id : 2c9d5a815c66e726015c66f7963e0002
     * name : 积分兑换
     * cost : 5
     * createDate : 2017-06-02
     * startTime : 2017-06-02
     * endTime : 2017-06-30
     * status : Normal
     * status_value : 已发布
     * type : IntegralExchange
     * type_value : 积分兑换
     * registerStartTime : null
     * registerEndTime : null
     * packageNumber : null
     * integral : 500
     * userMaxNumber : 1
     * gainNumber : 50
     */

    private String id;
    private String name;
    private double cost;
    private String createDate;
    private String startTime;
    private String endTime;
    private String status;
    private String status_value;
    private String type;
    private String type_value;
    private String registerStartTime;
    private String registerEndTime;
    private String packageNumber;
    private int integral;
    private int userMaxNumber;
    private int gainNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_value() {
        return status_value;
    }

    public void setStatus_value(String status_value) {
        this.status_value = status_value;
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

    public String getRegisterStartTime() {
        return registerStartTime;
    }

    public void setRegisterStartTime(String registerStartTime) {
        this.registerStartTime = registerStartTime;
    }

    public String getRegisterEndTime() {
        return registerEndTime;
    }

    public void setRegisterEndTime(String registerEndTime) {
        this.registerEndTime = registerEndTime;
    }

    public String getPackageNumber() {
        return packageNumber;
    }

    public void setPackageNumber(String packageNumber) {
        this.packageNumber = packageNumber;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getUserMaxNumber() {
        return userMaxNumber;
    }

    public void setUserMaxNumber(int userMaxNumber) {
        this.userMaxNumber = userMaxNumber;
    }

    public int getGainNumber() {
        return gainNumber;
    }

    public void setGainNumber(int gainNumber) {
        this.gainNumber = gainNumber;
    }
}
