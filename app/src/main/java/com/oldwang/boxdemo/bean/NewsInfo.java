package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class NewsInfo implements Serializable{


    private String badCount;//踩赞数
    private String commentCount;//评论数
    private String commentRepayCount;//回复数量
    private String createTime;//创建时间
    private String goodCount;//点赞数
    private String isPraise;//1已点赞0没有点赞
    private String newsContent;//新闻内容
    private String newsId;//新闻ID
    private String newsTitle;//新闻标题


    public String getBadCount() {
        return badCount;
    }

    public void setBadCount(String badCount) {
        this.badCount = badCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getCommentRepayCount() {
        return commentRepayCount;
    }

    public void setCommentRepayCount(String commentRepayCount) {
        this.commentRepayCount = commentRepayCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(String goodCount) {
        this.goodCount = goodCount;
    }

    public String getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(String isPraise) {
        this.isPraise = isPraise;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }
}
