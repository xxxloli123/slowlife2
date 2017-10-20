package com.android.slowlife.objectmodel;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/6 0006.
 */

public class CancelOrdersEntity  implements Serializable {
    private Integer imageUrl;
    private String name;
    private double price;
    private int num;

    public Integer getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Integer imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
