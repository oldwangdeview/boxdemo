package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class Attribute implements Serializable{

    //规格颜色
    private String attributeColor;
    //规格ID
    private String attributeId;
    //规格材质
    private String attributeQuality;
    //规格大小
    private String attributeSize;
    //商品ID
    private String commodityId;
    private String deductScore;
    private String salePrice;
    private String samePrice;
    //库存数量
    private String stockNum;

    //商品图片
    private String picUrl;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    private boolean isChoos;

    public boolean isChoos() {
        return isChoos;
    }

    public void setChoos(boolean choos) {
        isChoos = choos;
    }

    public String getAttributeColor() {
        return attributeColor;
    }

    public void setAttributeColor(String attributeColor) {
        this.attributeColor = attributeColor;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
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

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getDeductScore() {
        return deductScore;
    }

    public void setDeductScore(String deductScore) {
        this.deductScore = deductScore;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getSamePrice() {
        return samePrice;
    }

    public void setSamePrice(String samePrice) {
        this.samePrice = samePrice;
    }

    public String getStockNum() {
        return stockNum;
    }

    public void setStockNum(String stockNum) {
        this.stockNum = stockNum;
    }
}
