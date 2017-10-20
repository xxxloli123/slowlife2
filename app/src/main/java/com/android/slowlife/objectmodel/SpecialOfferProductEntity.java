package com.android.slowlife.objectmodel;

/**
 * Created by Administrator on 2017/2/9 0009.
 */

public class SpecialOfferProductEntity {
    private Integer productUrl;
    private String name;
    private double specialOffer;
    private double originalPrice;
    private String discount;

    public Integer getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(Integer productUrl) {
        this.productUrl = productUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public double getSpecialOffer() {
        return specialOffer;
    }

    public void setSpecialOffer(double specialOffer) {
        this.specialOffer = specialOffer;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }
}
