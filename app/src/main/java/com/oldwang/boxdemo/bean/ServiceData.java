package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class ServiceData implements Serializable{

    //折扣
    private String discount;
    //场馆ID
    private String venueId;
    //项目ID
    private String venueProjectId;
    //项目名称
    private String venueProjectName;
    //项目价格
    private String venueProjectPrice;


    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getVenueProjectId() {
        return venueProjectId;
    }

    public void setVenueProjectId(String venueProjectId) {
        this.venueProjectId = venueProjectId;
    }

    public String getVenueProjectName() {
        return venueProjectName;
    }

    public void setVenueProjectName(String venueProjectName) {
        this.venueProjectName = venueProjectName;
    }

    public String getVenueProjectPrice() {
        return venueProjectPrice;
    }

    public void setVenueProjectPrice(String venueProjectPrice) {
        this.venueProjectPrice = venueProjectPrice;
    }
}
