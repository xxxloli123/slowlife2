package com.android.slowlife.objectmodel;

import java.sql.Time;

/**
 * Created by Administrator on 2017/1/24 0024.
 */

public class OrderIntegralEntity {
    private Time time;
    private String integral;

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }
}
