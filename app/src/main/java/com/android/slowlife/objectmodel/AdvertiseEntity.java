package com.android.slowlife.objectmodel;

import java.util.List;

/**
 * Created by Administrator on 2017/8/29.
 */

public class AdvertiseEntity {
    /**
     * message : 获取成功
     * listadv : [{"id":"402880e75e26ce3a015e272a8d900007","sort":22,"advImg":"ee6a6ac4-96b1-4ff5-ad75-59e2bc46acfd.jpg","advUrl":"","status":"Normal","status_value":"已发布","createDate":"2017-08-28 12:47:34","days":20,"details":""},{"id":"402880e75e26ce3a015e272aaa330008","sort":22,"advImg":"e1401b72-8b02-4a02-943f-f76802e7e985.jpg","advUrl":"","status":"Normal","status_value":"已发布","createDate":"2017-08-28 12:47:41","days":20,"details":""},{"id":"402880e75e26ce3a015e272aba160009","sort":22,"advImg":"997872be-1ec5-4444-b7bf-616799327fb8.jpg","advUrl":"","status":"Normal","status_value":"已发布","createDate":"2017-08-28 12:47:45","days":20,"details":""},{"id":"402880e75e26ce3a015e272ac8d2000a","sort":22,"advImg":"0e9c59d2-4b36-4657-8911-f5361836719b.jpg","advUrl":"","status":"Normal","status_value":"已发布","createDate":"2017-08-28 12:47:49","days":20,"details":""}]
     * statusCode : 200
     */

    private String message;
    private int statusCode;
    private List<ListadvBean> listadv;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<ListadvBean> getListadv() {
        return listadv;
    }

    public void setListadv(List<ListadvBean> listadv) {
        this.listadv = listadv;
    }

    public static class ListadvBean {
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
}
