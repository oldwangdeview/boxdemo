package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class CommentRepayInfo implements Serializable{


    //回复ID
    private String commentRepayId;
    //回复内容
    private String repayContent;
    //回复数量
    private String repayCount;
    //回复人
    private String repayMember;

    private String emptyIdentifier;
    private String boxingVideoCommentId;
    private String boxingVideoCommentRepayDetail;
    private String boxingVideoCommentRepayId;
    private String createTime;


    public String getEmptyIdentifier() {
        return emptyIdentifier;
    }

    public void setEmptyIdentifier(String emptyIdentifier) {
        this.emptyIdentifier = emptyIdentifier;
    }

    public String getBoxingVideoCommentId() {
        return boxingVideoCommentId;
    }

    public void setBoxingVideoCommentId(String boxingVideoCommentId) {
        this.boxingVideoCommentId = boxingVideoCommentId;
    }

    public String getBoxingVideoCommentRepayDetail() {
        return boxingVideoCommentRepayDetail;
    }

    public void setBoxingVideoCommentRepayDetail(String boxingVideoCommentRepayDetail) {
        this.boxingVideoCommentRepayDetail = boxingVideoCommentRepayDetail;
    }

    public String getBoxingVideoCommentRepayId() {
        return boxingVideoCommentRepayId;
    }

    public void setBoxingVideoCommentRepayId(String boxingVideoCommentRepayId) {
        this.boxingVideoCommentRepayId = boxingVideoCommentRepayId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCommentRepayId() {
        return commentRepayId;
    }

    public void setCommentRepayId(String commentRepayId) {
        this.commentRepayId = commentRepayId;
    }

    public String getRepayContent() {
        return repayContent;
    }

    public void setRepayContent(String repayContent) {
        this.repayContent = repayContent;
    }

    public String getRepayCount() {
        return repayCount;
    }

    public void setRepayCount(String repayCount) {
        this.repayCount = repayCount;
    }

    public String getRepayMember() {
        return repayMember;
    }

    public void setRepayMember(String repayMember) {
        this.repayMember = repayMember;
    }
}
