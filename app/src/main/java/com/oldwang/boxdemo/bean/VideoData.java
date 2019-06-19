package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class VideoData implements Serializable{

    private String boxingVideoId;
    private String boxingVideoTitle;
    private String boxingVideoUrl;
    private String boxingVideoImg;
    private MemberInfo memberInfo;
    private String boxingFavoriteCount;//收藏数量
    private String boxingVideoCommentCount;//评论次数
    private String boxingVideoDownloadCount;//下载次数
    private String boxingVideoPlayCount;//播放次数
    private String boxingVideoShareCount;//分享次数
    private String createTime;//创建时间
    private String isAttention;

    private String isCommentUpCount;//1已点赞0没有点赞

    private String isCollection;//0否,1是

    private String memberHeadLogo;
    private String memberId;
    private String memberName;

    public String getMemberHeadLogo() {
        return memberHeadLogo;
    }

    public void setMemberHeadLogo(String memberHeadLogo) {
        this.memberHeadLogo = memberHeadLogo;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(String isCollection) {
        this.isCollection = isCollection;
    }

    public String getIsCommentUpCount() {
        return isCommentUpCount;
    }

    public void setIsCommentUpCount(String isCommentUpCount) {
        this.isCommentUpCount = isCommentUpCount;
    }

    public String getIsPraise() {
        return isCommentUpCount;
    }

    public void setIsPraise(String isPraise) {
        this.isCommentUpCount = isPraise;
    }

    public String getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(String isAttention) {
        this.isAttention = isAttention;
    }

    public MemberInfo getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }

    public String getBoxingFavoriteCount() {
        return boxingFavoriteCount;
    }

    public void setBoxingFavoriteCount(String boxingFavoriteCount) {
        this.boxingFavoriteCount = boxingFavoriteCount;
    }

    public String getBoxingVideoCommentCount() {
        return boxingVideoCommentCount;
    }

    public void setBoxingVideoCommentCount(String boxingVideoCommentCount) {
        this.boxingVideoCommentCount = boxingVideoCommentCount;
    }

    public String getBoxingVideoDownloadCount() {
        return boxingVideoDownloadCount;
    }

    public void setBoxingVideoDownloadCount(String boxingVideoDownloadCount) {
        this.boxingVideoDownloadCount = boxingVideoDownloadCount;
    }

    public String getBoxingVideoPlayCount() {
        return boxingVideoPlayCount;
    }

    public void setBoxingVideoPlayCount(String boxingVideoPlayCount) {
        this.boxingVideoPlayCount = boxingVideoPlayCount;
    }

    public String getBoxingVideoShareCount() {
        return boxingVideoShareCount;
    }

    public void setBoxingVideoShareCount(String boxingVideoShareCount) {
        this.boxingVideoShareCount = boxingVideoShareCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getBoxingVideoImg() {
        return boxingVideoImg;
    }

    public void setBoxingVideoImg(String boxingVideoImg) {
        this.boxingVideoImg = boxingVideoImg;
    }

    public String getBoxingVideoId() {
        return boxingVideoId;
    }

    public void setBoxingVideoId(String boxingVideoId) {
        this.boxingVideoId = boxingVideoId;
    }

    public String getBoxingVideoTitle() {
        return boxingVideoTitle;
    }

    public void setBoxingVideoTitle(String boxingVideoTitle) {
        this.boxingVideoTitle = boxingVideoTitle;
    }

    public String getBoxingVideoUrl() {
        return boxingVideoUrl;
    }

    public void setBoxingVideoUrl(String boxingVideoUrl) {
        this.boxingVideoUrl = boxingVideoUrl;
    }
}
