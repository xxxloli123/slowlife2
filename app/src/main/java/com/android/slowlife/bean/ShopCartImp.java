package com.android.slowlife.bean;

import android.view.View;

import java.io.Serializable;

public interface ShopCartImp  extends Serializable {
    void add(View view, int postion);
    void remove(View view, int postion);
}
