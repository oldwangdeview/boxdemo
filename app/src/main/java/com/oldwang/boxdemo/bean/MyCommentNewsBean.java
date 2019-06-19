package com.oldwang.boxdemo.bean;

import java.io.Serializable;

/**
 *     "createTime":"评论时间",
 *                     "memeberLogo":"会员LOGO",
 *                     "memeberName":"会员名称",
 *                     "newsCommentContent":"评论内容",
 *                     "newsCommentId":"评论ID",
 *                     "newsId":"新闻ID"
 */

public class MyCommentNewsBean implements Serializable {

    private String createTime;
    private String memeberLogo;
    private String memeberName;
    private String newsCommentContent;
    private String newsCommentId;
    private String newsId;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMemeberLogo() {
        return memeberLogo;
    }

    public void setMemeberLogo(String memeberLogo) {
        this.memeberLogo = memeberLogo;
    }

    public String getMemeberName() {
        return memeberName;
    }

    public void setMemeberName(String memeberName) {
        this.memeberName = memeberName;
    }

    public String getNewsCommentContent() {
        return newsCommentContent;
    }

    public void setNewsCommentContent(String newsCommentContent) {
        this.newsCommentContent = newsCommentContent;
    }

    public String getNewsCommentId() {
        return newsCommentId;
    }

    public void setNewsCommentId(String newsCommentId) {
        this.newsCommentId = newsCommentId;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }
}
