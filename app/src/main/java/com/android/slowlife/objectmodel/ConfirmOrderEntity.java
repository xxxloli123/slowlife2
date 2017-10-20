package com.android.slowlife.objectmodel;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

public class ConfirmOrderEntity  implements Serializable {
    private String name;
    private Double price;
    private int number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
