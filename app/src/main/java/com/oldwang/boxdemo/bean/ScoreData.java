package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class ScoreData implements Serializable{



    /**踩赞次数**/
    private String badCount;

    /**发布时间**/
    private String createTime;

    /**点赞次数**/
    private String goodCount;

    /**视频图片URL**/
    private String picUrl;

    /**播放次数**/
    private String playCount;

    /**发布标题**/
    private String publishTitle;

    /**回复次数**/
    private String repayCount;

    /**视频ID**/
    private String videoId;

    /**开始时间	**/
    private String bookingBeginTime;

    /**结束时间	**/
    private String bookingEndTime;

    /**	预约ID**/
    private String bookingId;

    /**预约状态**/
    private String bookingStatus;

    /**会员ID**/
    private String memberId;

    /**场馆ID**/
    private String venueId;

    /**场馆名称**/
    private String venueName;

    /**场馆项目名称**/
    private String venueProjectName;

    private String venuePicUrl;

    private String withdrawalAmount;
    private String withdrawalScores;

    private String memberName;
    private String ranking;
    private String scoreTotal;

    private String favoriteName;
    private String favoriteId;
    private String favoriteObjectId;

    private String messageContent;
    private String messageIsRead;
    private String messageTitle;

    //商品售价/会员头像
    private String param1;
    //商品挂牌价/会员昵称
    private String param2;
    //视频预览图
    private String param3;

    private String msgInfoId;

    private boolean isCheck;

    private String isComment;

    private String isComplaint;

    public String getVenuePicUrl() {
        return venuePicUrl;
    }

    public void setVenuePicUrl(String venuePicUrl) {
        this.venuePicUrl = venuePicUrl;
    }

    public String getIsComment() {
        return isComment;
    }

    public void setIsComment(String isComment) {
        this.isComment = isComment;
    }

    public String getIsComplaint() {
        return isComplaint;
    }

    public void setIsComplaint(String isComplaint) {
        this.isComplaint = isComplaint;
    }

    public String getFavoriteObjectId() {
        return favoriteObjectId;
    }

    public void setFavoriteObjectId(String favoriteObjectId) {
        this.favoriteObjectId = favoriteObjectId;
    }

    public String getMsgInfoId() {
        return msgInfoId;
    }

    public void setMsgInfoId(String msgInfoId) {
        this.msgInfoId = msgInfoId;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }

    public String getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(String withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public String getWithdrawalScores() {
        return withdrawalScores;
    }

    public void setWithdrawalScores(String withdrawalScores) {
        this.withdrawalScores = withdrawalScores;
    }

    public String getBadCount() {
        return badCount;
    }

    public void setBadCount(String badCount) {
        this.badCount = badCount;
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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPlayCount() {
        return playCount;
    }

    public void setPlayCount(String playCount) {
        this.playCount = playCount;
    }

    public String getPublishTitle() {
        return publishTitle;
    }

    public void setPublishTitle(String publishTitle) {
        this.publishTitle = publishTitle;
    }

    public String getRepayCount() {
        return repayCount;
    }

    public void setRepayCount(String repayCount) {
        this.repayCount = repayCount;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getBookingBeginTime() {
        return bookingBeginTime;
    }

    public void setBookingBeginTime(String bookingBeginTime) {
        this.bookingBeginTime = bookingBeginTime;
    }

    public String getBookingEndTime() {
        return bookingEndTime;
    }

    public void setBookingEndTime(String bookingEndTime) {
        this.bookingEndTime = bookingEndTime;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueProjectName() {
        return venueProjectName;
    }

    public void setVenueProjectName(String venueProjectName) {
        this.venueProjectName = venueProjectName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getScoreTotal() {
        return scoreTotal;
    }

    public void setScoreTotal(String scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageIsRead() {
        return messageIsRead;
    }

    public void setMessageIsRead(String messageIsRead) {
        this.messageIsRead = messageIsRead;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }
}
