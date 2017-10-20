package com.android.slowlife.objectmodel;

import java.io.Serializable;

public class DishEntity  implements Serializable {

    private String dishName;
    private double dishPrice;
    private int dishAmount;
    private int dishRemain;

    public DishEntity(String dishName, double dishPrice, int dishAmount){
        this.dishName = dishName;
        this.dishPrice = dishPrice;
        this.dishAmount = dishAmount;
        this.dishRemain = dishAmount;
    }


    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public double getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(double dishPrice) {
        this.dishPrice = dishPrice;
    }

    public int getDishAmount() {
        return dishAmount;
    }

    public void setDishAmount(int dishAmount) {
        this.dishAmount = dishAmount;
    }

    public int getDishRemain() {
        return dishRemain;
    }

    public void setDishRemain(int dishRemain) {
        this.dishRemain = dishRemain;
    }

    public int hashCode(){
        int code = this.dishName.hashCode()+(int)this.dishPrice;
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==this)return true;

        return obj instanceof DishEntity &&
                this.dishName.equals(((DishEntity)obj).dishName) &&
                this.dishPrice ==  ((DishEntity)obj).dishPrice &&
                this.dishAmount == ((DishEntity)obj).dishAmount &&
                this.dishRemain == ((DishEntity)obj).dishRemain;
    }
}
