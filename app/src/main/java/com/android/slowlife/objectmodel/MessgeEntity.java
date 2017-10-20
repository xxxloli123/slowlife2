package com.android.slowlife.objectmodel;

/**
 * Created by Administrator on 2017/1/25 0025.
 */

public class MessgeEntity {
    /**
     * id : 402881f05cab7752015cab7942bd0001
     * orderId : 402881f05cab04af015cab1a8f9c0000
     * messagetitle : 订单已处理
     * messagecontent : 您的订单已被快递员接单
     * createDate : 2017-06-15 19:17:50
     * userId : 402881ed5bcdaa4f015bcdabe3c10001
     */

    private String id;
    private String orderId;
    private String messagetitle;
    private String messagecontent;
    private String createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMessagetitle() {
        return messagetitle;
    }

    public void setMessagetitle(String messagetitle) {
        this.messagetitle = messagetitle;
    }

    public String getMessagecontent() {
        return messagecontent;
    }

    public void setMessagecontent(String messagecontent) {
        this.messagecontent = messagecontent;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
