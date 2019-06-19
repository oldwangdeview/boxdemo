package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class BoxingVideoCommentInfo implements Serializable{

    private String boxingVideoCommentDetail;//评论内容
    private String boxingVideoCommentId;//视频评论ID
    private String boxingVideoCommentUpCount;//好评数
    private String boxingVideoId;//视频id
    private String createTime;//评论时间
    private String memberHeadLogo;//回复会员头像
    private String memberId;//会员ID
    private String memberName;//会员名称
    private String newsComment;

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

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
}
