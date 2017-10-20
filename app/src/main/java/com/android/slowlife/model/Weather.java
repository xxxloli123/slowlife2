package com.android.slowlife.model;

import android.os.Parcel;

import com.util.ParcelableUtil;

/**
 * Created by sgrape on 2017/5/21.
 * e-mail: sgrape1153@gmail.com
 */

public class Weather extends ParcelableUtil {

    /**
     * province : 重庆
     * city : 江北区
     * adcode : 500105
     * weather : 多云
     * temperature : 23         // 温度
     * winddirection : 西北
     * windpower : 4
     * humidity : 93
     * reporttime : 2017-05-21 15:00:00
     */

    private String province;
    private String city;
    private String adcode;
    private String weather;
    private String temperature;
    private String winddirection;
    private String windpower;
    private String humidity;
    private String reporttime;

    public Weather() {

    }

    private Weather(Parcel in) {
        super(in);
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    // 温度
    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWinddirection() {
        return winddirection;
    }

    public void setWinddirection(String winddirection) {
        this.winddirection = winddirection;
    }

    public String getWindpower() {
        return windpower;
    }

    public void setWindpower(String windpower) {
        this.windpower = windpower;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getReporttime() {
        return reporttime;
    }

    public void setReporttime(String reporttime) {
        this.reporttime = reporttime;
    }
}
