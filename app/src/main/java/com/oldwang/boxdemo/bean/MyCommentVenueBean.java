package com.oldwang.boxdemo.bean;

import java.io.Serializable;

/**
 *       "list":[
 *                 {
 *                     "commentContent":"内容",
 *                     "commentGrade":"评论分数",
 *                     "commentId":"评论ID",
 *                     "commentTime":"评论时间",
 *                     "memberHeadLogo":"评论人头像",
 *                     "memberName":"评论人名称",
 *                     "venueId":"场馆ID",
 *                     "venuePic":"场馆图片"
 *                 }
 */
public class MyCommentVenueBean implements Serializable {
    private String commentContent;
    private String commentGrade;
    private String commentId;
    private String commentTime;
    private String memberHeadLogo;
    private String memberName;
    private String venueId;
    private String venuePic;

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentGrade() {
        return commentGrade;
    }

    public void setCommentGrade(String commentGrade) {
        this.commentGrade = commentGrade;
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

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getVenuePic() {
        return venuePic;
    }

    public void setVenuePic(String venuePic) {
        this.venuePic = venuePic;
    }
}
