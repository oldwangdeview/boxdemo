package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class PictureSetData implements Serializable{

    private String pictureSetName;
    private String pictureUrl;
    private String sortNum;
    private String type;
    private String info;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPictureSetName() {
        return pictureSetName;
    }

    public void setPictureSetName(String pictureSetName) {
        this.pictureSetName = pictureSetName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getSortNum() {
        return sortNum;
    }

    public void setSortNum(String sortNum) {
        this.sortNum = sortNum;
    }
}
