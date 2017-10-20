package com.android.slowlife.objectmodel;

import java.io.Serializable;
import java.sql.Time;

/**
 * Created by Administrator on 2017/1/31 0031.
 */

public class IntegralConvertShopEntity  implements Serializable {
    private Integer shopHeadUrl;
    private String name;
    private String integral;
    private String tag="0";//0不是会员  1是会员
    private Time time;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getShopHeadUrl() {
        return shopHeadUrl;
    }

    public void setShopHeadUrl(Integer shopHeadUrl) {
        this.shopHeadUrl = shopHeadUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }
}
