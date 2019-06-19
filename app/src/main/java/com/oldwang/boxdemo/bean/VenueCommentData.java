package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class VenueCommentData implements Serializable{

    private String commentContent;//评论信息

    private String commentId;//评论ID

    private String commentTime;//评论时间

    private String memberHeadLogo;//用户头像

    private String memberName;//用户名

    private String noticeReleaseTime;

    private String commentGrade;

    public String getCommentGrade() {
        return commentGrade;
    }

    public void setCommentGrade(String commentGrade) {
        this.commentGrade = commentGrade;
    }

    public String getNoticeReleaseTime() {
        return noticeReleaseTime;
    }

    public void setNoticeReleaseTime(String noticeReleaseTime) {
        this.noticeReleaseTime = noticeReleaseTime;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getMemberHeadLogo() {
        return memberHeadLogo;
    }

    public void setMemberHeadLogo(String memberHeadLogo) {
        this.memberHeadLogo = memberHeadLogo;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
