package com.android.slowlife.objectmodel;

/**
 * Created by Administrator on 2017/1/22 0022.
 * 商家信息
 */

public class MerchantEntity {
    /**
     * 商家头像
     * */
    private Integer headPortrait;
    /**
     * 商店名称
     * */
    private String name;
    /**
     * 商店地址
     * */
    private String address;
    /**
     * 商店评分
     * */
    private double grade;
    /**
     * 月售额
     * */
    private String monthlySalesAmount;
    /**
     * 起送价格
     * */
    private String  priceSending;
    /**
     * 配送费
     * */
    private String deliveryCost;
    /**
     * 距离
     * */
    private String distance;
    /**
     * 分钟
     * */
    private int time;

    public Integer getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(Integer headPortrait) {
        this.headPortrait = headPortrait;
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

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getMonthlySalesAmount() {
        return monthlySalesAmount;
    }

    public void setMonthlySalesAmount(String monthlySalesAmount) {
        this.monthlySalesAmount = monthlySalesAmount;
    }

    public String getPriceSending() {
        return priceSending;
    }

    public void setPriceSending(String priceSending) {
        this.priceSending = priceSending;
    }

    public String getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(String deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
