package com.android.slowlife.objectmodel;

/**
 * Created by Administrator on 2017/9/3.
 */

public class Advertise {
    /**
     * id : 402880e75e26ce3a015e272a8d900007
     * sort : 22
     * advImg : ee6a6ac4-96b1-4ff5-ad75-59e2bc46acfd.jpg
     * advUrl :
     * status : Normal
     * status_value : 已发布
     * createDate : 2017-08-28 12:47:34
     * days : 20
     * details :
     */

    private String id;
    private int sort;
    private String advImg;
    private String advUrl;
    private String status;
    private String status_value;
    private String createDate;
    private int days;
    private String details;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getAdvImg() {
        return advImg;
    }

    public void setAdvImg(String advImg) {
        this.advImg = advImg;
    }

    public String getAdvUrl() {
        return advUrl;
    }

    public void setAdvUrl(String advUrl) {
        this.advUrl = advUrl;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
