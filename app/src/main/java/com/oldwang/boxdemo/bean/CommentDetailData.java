package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.List;

public class CommentDetailData implements Serializable{

    private String id;
    private String userHead;
    //1视频2快讯
    private int type;
    private String userName;
    private String time;
    private String content;
    private List<CommentRepayInfo> boxingVideoCommentRepay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<CommentRepayInfo> getBoxingVideoCommentRepay() {
        return boxingVideoCommentRepay;
    }

    public void setBoxingVideoCommentRepay(List<CommentRepayInfo> boxingVideoCommentRepay) {
        this.boxingVideoCommentRepay = boxingVideoCommentRepay;
    }
}
