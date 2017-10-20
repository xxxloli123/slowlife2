package com.android.slowlife.objectmodel;

/**
 * Created by Administrator on 2017/1/20 .
 */
public class ShopEntity {
    //商户的属性
    public String shopFav;
    public String shopName;
    public String shopIcon;
    public String standby1;
    // 暂无数据属性
    private boolean isNoData = false;
    private int height;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isNoData() {
        return isNoData;
    }

    public void setNoData(boolean noData) {
        isNoData = noData;
    }
}
