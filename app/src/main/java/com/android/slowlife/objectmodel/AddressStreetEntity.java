package com.android.slowlife.objectmodel;

import java.util.List;

/**
 * Created by Administrator on 2017/8/21.
 */

public class AddressStreetEntity {

    /**
     * tarifflist : [{"startStreet":"江东"},{"startStreet":"马鞍"},{"startStreet":"城区"}]
     * message : 获取成功
     * statusCode : 200
     */

    private String message;
    private int statusCode;
    private List<TarifflistBean> tarifflist;

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

    public List<TarifflistBean> getTarifflist() {
        return tarifflist;
    }

    public void setTarifflist(List<TarifflistBean> tarifflist) {
        this.tarifflist = tarifflist;
    }

    public static class TarifflistBean {
        /**
         * startStreet : 江东
         */

        private String startStreet;

        public String getStartStreet() {
            return startStreet;
        }

        public void setStartStreet(String startStreet) {
            this.startStreet = startStreet;
        }
    }
}
