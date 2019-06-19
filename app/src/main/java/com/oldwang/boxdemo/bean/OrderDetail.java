package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class OrderDetail implements Serializable{


    private String attributeId;
    private String commodityId;
    private String commodityName;
    private String commodityNum;
    private String commodityPrice;
    private String totalPrice;
    private String filePath;
    private String attributeColor;
    private String attributeQuality;
    private String attributeSize;
    private String teamBuyPrice;
    private String commodityPicUrl;

    public String getCommodityPicUrl() {
        return commodityPicUrl;
    }

    public void setCommodityPicUrl(String commodityPicUrl) {
        this.commodityPicUrl = commodityPicUrl;
    }

    public String getTeamBuyPrice() {
        return teamBuyPrice;
    }

    public void setTeamBuyPrice(String teamBuyPrice) {
        this.teamBuyPrice = teamBuyPrice;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAttributeColor() {
        return attributeColor;
    }

    public void setAttributeColor(String attributeColor) {
        this.attributeColor = attributeColor;
    }

    public String getAttributeQuality() {
        return attributeQuality;
    }

    public void setAttributeQuality(String attributeQuality) {
        this.attributeQuality = attributeQuality;
    }

    public String getAttributeSize() {
        return attributeSize;
    }

    public void setAttributeSize(String attributeSize) {
        this.attributeSize = attributeSize;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityNum() {
        return commodityNum;
    }

    public void setCommodityNum(String commodityNum) {
        this.commodityNum = commodityNum;
    }

    public String getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(String commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
