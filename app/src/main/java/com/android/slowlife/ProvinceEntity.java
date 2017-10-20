package com.android.slowlife;

/**
 * Created by sgrape on 2017/7/2.
 * e-mail: sgrape1153@gmail.com
 */

public class ProvinceEntity implements Area{
    private int id;
    private String name;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
