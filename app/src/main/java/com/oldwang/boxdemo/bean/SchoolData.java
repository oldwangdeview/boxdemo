package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class SchoolData implements Serializable{

    private String isPraise;
    private String newsBoxingSchoolDetail;
    private String newsBoxingSchoolId;
    private String newsBoxingSchoolName;
    private String newsBoxingSchoolPraiseCount;
    private String ranking;


    public String getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(String isPraise) {
        this.isPraise = isPraise;
    }

    public String getNewsBoxingSchoolDetail() {
        return newsBoxingSchoolDetail;
    }

    public void setNewsBoxingSchoolDetail(String newsBoxingSchoolDetail) {
        this.newsBoxingSchoolDetail = newsBoxingSchoolDetail;
    }

    public String getNewsBoxingSchoolId() {
        return newsBoxingSchoolId;
    }

    public void setNewsBoxingSchoolId(String newsBoxingSchoolId) {
        this.newsBoxingSchoolId = newsBoxingSchoolId;
    }

    public String getNewsBoxingSchoolName() {
        return newsBoxingSchoolName;
    }

    public void setNewsBoxingSchoolName(String newsBoxingSchoolName) {
        this.newsBoxingSchoolName = newsBoxingSchoolName;
    }

    public String getNewsBoxingSchoolPraiseCount() {
        return newsBoxingSchoolPraiseCount;
    }

    public void setNewsBoxingSchoolPraiseCount(String newsBoxingSchoolPraiseCount) {
        this.newsBoxingSchoolPraiseCount = newsBoxingSchoolPraiseCount;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }
}
