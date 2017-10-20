package com.android.slowlife.util;

/**
 * Created by Administrator on 2017/2/7 .
 */
public class MovieSubject {

    /**
     * msg : invalid_request_uri
     * code : 107
     * request : GET /v2/movie/
     */

    private String msg;
    private int code;
    private String request;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
