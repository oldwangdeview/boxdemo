package com.oldwang.boxdemo.bean;

import java.io.Serializable;

/**
 *  "boxingVideoCommentDetail":"评论内容",
 *                     "boxingVideoCommentId":"视频评论ID",
 *                     "boxingVideoId":"视频ID",
 *                     "boxingVideoPic":"视频第一帧图",
 *                     "createTime":"创建时间",
 *                     "memberHeadLogo":"评论会员LOGO",
 *                     "memberName":"评论会员名"
 */
public class MyCommentVideoBean implements Serializable {

    private String boxingVideoCommentDetail;
    private String boxingVideoCommentId;
    private String boxingVideoId;
    private String createTime;
    private String memberHeadLogo;
    private String memberName;
}
