package com.android.slowlife.bean;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by sgrape on 2017/5/2.
 */

public class Info implements Serializable {

    /**
     * id : 402881ed5bcd3758015bcd384fbf0001
     * jobId : null
     * name : null
     * phone : 15231255123
     * telePhone : null
     * password : c7a7e1a26fea659144ac7ca7e311c9e5
     * mailbox : null
     * type : General
     * type_value : 普通用户
     * status : Wait_audit
     * status_value : 待审核
     * registerDate : 2017-05-03 15:31:11
     * sex : Man
     * sex_value : 男
     * token : null
     * phoneType : Android
     * phoneType_value : 安卓手机
     * nickName : null
     * headerImg : null
     * idStartTime : null
     * idEndTime : null
     * brief : null
     * latestLoginDatetime : 2017-05-03 15:31:11
     * commpany_id : null
     * shop_id : null
     * onlineStatus : notonline
     * onlineStatus_value : 不在线
     * id2 : null
     * idnumber : null
     */

    private String id;
    private String jobId;
    private String name;
    private String phone;
    private String telePhone;
    private String password;
    private String mailbox;
    private String type;
    private String type_value;
    private String status;
    private String status_value;
    private String registerDate;
    private String sex;
    private String sex_value;
    private String token;
    private String phoneType;
    private String phoneType_value;
    private String nickName;
    private String headerImg;
    private String idStartTime;
    private String idEndTime;
    private String brief;
    private String latestLoginDatetime;
    private String commpany_id;
    private String shop_id;
    private String onlineStatus;
    private String onlineStatus_value;
    private String id2;
    private String idnumber;
    private Bitmap head;/**
     * qqOpenId
     */
    private String qqOpenId;

    /**
     * wxOpenId
     */
    private String wxOpenId;

    public Bitmap getHead() {
        return head;
    }

    public void setHead(Bitmap head) {
        this.head = head;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
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

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex_value() {
        return sex_value;
    }

    public void setSex_value(String sex_value) {
        this.sex_value = sex_value;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getPhoneType_value() {
        return phoneType_value;
    }

    public void setPhoneType_value(String phoneType_value) {
        this.phoneType_value = phoneType_value;
    }

    public String getNickName() {
        return TextUtils.isEmpty(nickName) ? "无名氏" : nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(String headerImg) {
        this.headerImg = headerImg;
    }

    public String getIdStartTime() {
        return idStartTime;
    }

    public void setIdStartTime(String idStartTime) {
        this.idStartTime = idStartTime;
    }

    public String getIdEndTime() {
        return idEndTime;
    }

    public void setIdEndTime(String idEndTime) {
        this.idEndTime = idEndTime;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getLatestLoginDatetime() {
        return latestLoginDatetime;
    }

    public void setLatestLoginDatetime(String latestLoginDatetime) {
        this.latestLoginDatetime = latestLoginDatetime;
    }

    public String getCommpany_id() {
        return commpany_id;
    }

    public void setCommpany_id(String commpany_id) {
        this.commpany_id = commpany_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getOnlineStatus_value() {
        return onlineStatus_value;
    }

    public void setOnlineStatus_value(String onlineStatus_value) {
        this.onlineStatus_value = onlineStatus_value;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }
}
