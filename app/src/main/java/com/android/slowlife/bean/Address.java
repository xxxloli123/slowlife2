package com.android.slowlife.bean;

import java.io.Serializable;

/**
 * 地址信息
 */

public class Address implements Serializable {

    private static final long serialVersionUID = 7629095029419646379L;

    private String id;

    /**
     * 省
     */
    private String pro;

    /**
     * 市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 地址的详细部分
     */
    private String street;

    /**
     * 纬度
     */
    private double lat;

    /**
     * 经度
     */
    private double lng;


    /**
     * 门牌号详情
     */
    private String houseNumber;


    /**
     * 类型 0：家   1：公司 2：其他
     */
    private int type = 0;

    /**
     * 地址使用频率
     */
    private int count = 0;

    /**
     * 状态 0=正常状态 1=被删除状态
     */
    private int addtype = 0;

    /**
     * 收货人
     */
    private String personname;

    /**
     * 收货人电话
     */
    private String personphone;


    public Address() {
        this.type = 0;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }


    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public int getAddtype() {
        return addtype;
    }

    public void setAddtype(int addtype) {
        this.addtype = addtype;
    }


    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }


    public String getPersonphone() {
        return personphone;
    }

    public void setPersonphone(String personphone) {
        this.personphone = personphone;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }


    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }


}
