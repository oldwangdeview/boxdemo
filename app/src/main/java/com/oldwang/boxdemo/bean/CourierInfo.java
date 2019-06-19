package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class CourierInfo implements Serializable {

    //快递公司ID
    private String courierId;
    //快递公司
    private String courierName;
    //运费
    private String freightPrice;

    //可用积分(所有商品可使用积分的和)
    private String canUseSore;
    //总积分(用户可用积分)
    private String totalScore;

    //是否可以送达（1送达0不可送达）
    private String isSend;

    public String getCourierId() {
        return courierId;
    }


    public String getCanUseSore() {
        return canUseSore;
    }

    public void setCanUseSore(String canUseSore) {
        this.canUseSore = canUseSore;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public void setCourierId(String courierId) {
        this.courierId = courierId;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getFreightPrice() {
        return freightPrice;
    }

    public void setFreightPrice(String freightPrice) {
        this.freightPrice = freightPrice;
    }

    public String getIsSend() {
        return isSend;
    }

    public void setIsSend(String isSend) {
        this.isSend = isSend;
    }
}



