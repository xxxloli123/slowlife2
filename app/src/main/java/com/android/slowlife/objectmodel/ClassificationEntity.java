package com.android.slowlife.objectmodel;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public class ClassificationEntity  implements Serializable {
    private Integer url;
    private String name;
    private int number;

    public Integer getUrl() {
        return url;
    }

    public void setUrl(Integer url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
