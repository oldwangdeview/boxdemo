package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class NoticeData implements Serializable{


    private String noticeId;
    private String noticeReleaseContent;
    private String noticeReleaseTime;

    private String noticeTitle;


    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeReleaseContent() {
        return noticeReleaseContent;
    }

    public void setNoticeReleaseContent(String noticeReleaseContent) {
        this.noticeReleaseContent = noticeReleaseContent;
    }

    public String getNoticeReleaseTime() {
        return noticeReleaseTime;
    }

    public void setNoticeReleaseTime(String noticeReleaseTime) {
        this.noticeReleaseTime = noticeReleaseTime;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }
}
