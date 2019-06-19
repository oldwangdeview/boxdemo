package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.List;

public class NewsData implements Serializable{


    private String badCount;
    private String commentCount;
    private String createTime;
    private String goodCount;
    private String newsContent;
    private String newsId;
    private String newsOnline;
    private List<String> newsPicUrl;
    private String newsTitle;

    private String memberName;
    private String newsCommentDetail;
    private String commentRepayCount;
    private String memberLogo;
    private String memberId;
    private String newsCommentId;
    private String isPraise;


    /**********************/


    private String boxingVideoCommentDetail;
    private String boxingVideoCommentId;
    private List<CommentRepayInfo> boxingVideoCommentRepay;
    private String boxingVideoCommentUpCount;
    private String boxingVideoId;
    private String memberHeadLogo;
    private String boxingVideoCommentRepayDetail;
    private String newsComment;
    private String newsCommentRepayDetail;

    private String boxingVideoPic;

    public String getBoxingVideoPic() {
        return boxingVideoPic;
    }

    public void setBoxingVideoPic(String boxingVideoPic) {
        this.boxingVideoPic = boxingVideoPic;
    }

    public String getNewsCommentRepayDetail() {
        return newsCommentRepayDetail;
    }

    public void setNewsCommentRepayDetail(String newsCommentRepayDetail) {
        this.newsCommentRepayDetail = newsCommentRepayDetail;
    }

    public String getNewsComment() {
        return newsComment;
    }

    public void setNewsComment(String newsComment) {
        this.newsComment = newsComment;
    }

    public String getBoxingVideoCommentRepayDetail() {
        return boxingVideoCommentRepayDetail;
    }

    public void setBoxingVideoCommentRepayDetail(String boxingVideoCommentRepayDetail) {
        this.boxingVideoCommentRepayDetail = boxingVideoCommentRepayDetail;
    }

    public String getBoxingVideoCommentDetail() {
        return boxingVideoCommentDetail;
    }

    public void setBoxingVideoCommentDetail(String boxingVideoCommentDetail) {
        this.boxingVideoCommentDetail = boxingVideoCommentDetail;
    }

    public String getBoxingVideoCommentId() {
        return boxingVideoCommentId;
    }

    public void setBoxingVideoCommentId(String boxingVideoCommentId) {
        this.boxingVideoCommentId = boxingVideoCommentId;
    }

    public List<CommentRepayInfo> getBoxingVideoCommentRepay() {
        return boxingVideoCommentRepay;
    }

    public void setBoxingVideoCommentRepay(List<CommentRepayInfo> boxingVideoCommentRepay) {
        this.boxingVideoCommentRepay = boxingVideoCommentRepay;
    }

    public String getBoxingVideoCommentUpCount() {
        return boxingVideoCommentUpCount;
    }

    public void setBoxingVideoCommentUpCount(String boxingVideoCommentUpCount) {
        this.boxingVideoCommentUpCount = boxingVideoCommentUpCount;
    }

    public String getBoxingVideoId() {
        return boxingVideoId;
    }

    public void setBoxingVideoId(String boxingVideoId) {
        this.boxingVideoId = boxingVideoId;
    }

    public String getMemberHeadLogo() {
        return memberHeadLogo;
    }

    public void setMemberHeadLogo(String memberHeadLogo) {
        this.memberHeadLogo = memberHeadLogo;
    }

    public String getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(String isPraise) {
        this.isPraise = isPraise;
    }

    public String getNewsCommentId() {
        return newsCommentId;
    }

    public void setNewsCommentId(String newsCommentId) {
        this.newsCommentId = newsCommentId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    private CommentRepayInfo commentRepayInfo;

    public CommentRepayInfo getCommentRepayInfo() {
        return commentRepayInfo;
    }

    public void setCommentRepayInfo(CommentRepayInfo commentRepayInfo) {
        this.commentRepayInfo = commentRepayInfo;
    }

    public String getNewsCommentDetail() {
        return newsCommentDetail;
    }

    public void setNewsCommentDetail(String newsCommentDetail) {
        this.newsCommentDetail = newsCommentDetail;
    }

    public String getCommentRepayCount() {
        return commentRepayCount;
    }

    public void setCommentRepayCount(String commentRepayCount) {
        this.commentRepayCount = commentRepayCount;
    }

    public String getMemberLogo() {
        return memberLogo;
    }

    public void setMemberLogo(String memberLogo) {
        this.memberLogo = memberLogo;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

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

    public String getNewsOnline() {
        return newsOnline;
    }

    public void setNewsOnline(String newsOnline) {
        this.newsOnline = newsOnline;
    }

    public List<String> getNewsPicUrl() {
        return newsPicUrl;
    }

    public void setNewsPicUrl(List<String> newsPicUrl) {
        this.newsPicUrl = newsPicUrl;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }
}
