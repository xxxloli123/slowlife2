package com.android.slowlife.objectmodel;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/23 0023.
 */

public class ConvertRecommendEntity  implements Serializable {
    private Integer integralUrl;
    private String name;
    private String credits;

    public Integer getIntegralUrl() {
        return integralUrl;
    }

    public void setIntegralUrl(Integer integralUrl) {
        this.integralUrl = integralUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }
}
