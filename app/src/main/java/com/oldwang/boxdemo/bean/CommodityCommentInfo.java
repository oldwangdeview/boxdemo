package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class CommodityCommentInfo implements Serializable{

    private String badCount;
    private String goodCount;
    private String midCount;

    public String getBadCount() {
        return badCount;
    }

    public void setBadCount(String badCount) {
        this.badCount = badCount;
    }

    public String getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(String goodCount) {
        this.goodCount = goodCount;
    }

    public String getMidCount() {
        return midCount;
    }

    public void setMidCount(String midCount) {
        this.midCount = midCount;
    }
}
