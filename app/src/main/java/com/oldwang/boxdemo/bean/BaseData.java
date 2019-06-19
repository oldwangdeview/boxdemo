package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.List;

public class BaseData implements Serializable{

    private String name;
    private String count;
    //0场馆1学校
    private int positon;

    private String newsBoxingSchoolId;
    private String newsMasterId;


    public String getNewsBoxingSchoolId() {
        return newsBoxingSchoolId;
    }

    public void setNewsBoxingSchoolId(String newsBoxingSchoolId) {
        this.newsBoxingSchoolId = newsBoxingSchoolId;
    }

    public String getNewsMasterId() {
        return newsMasterId;
    }

    public void setNewsMasterId(String newsMasterId) {
        this.newsMasterId = newsMasterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getPositon() {
        return positon;
    }

    public void setPositon(int positon) {
        this.positon = positon;
    }
}
