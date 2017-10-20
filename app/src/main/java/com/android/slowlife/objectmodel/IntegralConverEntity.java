package com.android.slowlife.objectmodel;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/23 0023.
 */

public class IntegralConverEntity  implements Serializable {
    private Integer integralUrl;
    private String integralSection;

    public Integer getIntegralUrl() {
        return integralUrl;
    }

    public void setIntegralUrl(Integer integralUrl) {
        this.integralUrl = integralUrl;
    }

    public String getIntegralSection() {
        return integralSection;
    }

    public void setIntegralSection(String integralSection) {
        this.integralSection = integralSection;
    }
}
