package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class CommentChilds implements Serializable{

    //父ID
    private String parentId;
    //评论内容
    private String recommendContent;
    //回复人
    private String recommendPerson;
    //回复时间
    private String recommendTime;
    //评论类型(1评价2回复)
    private String recommendType;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRecommendContent() {
        return recommendContent;
    }

    public void setRecommendContent(String recommendContent) {
        this.recommendContent = recommendContent;
    }

    public String getRecommendPerson() {
        return recommendPerson;
    }

    public void setRecommendPerson(String recommendPerson) {
        this.recommendPerson = recommendPerson;
    }

    public String getRecommendTime() {
        return recommendTime;
    }

    public void setRecommendTime(String recommendTime) {
        this.recommendTime = recommendTime;
    }

    public String getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(String recommendType) {
        this.recommendType = recommendType;
    }
}
