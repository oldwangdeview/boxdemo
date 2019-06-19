package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.List;

public class CommodityChildCommentData implements Serializable{


    //子评论信息
    private List<CommentChilds> childs;

    //图片
    private List<String> picUrl;
    //评论会员头像
    private String memberLogo;

    //评论会员昵称
    private String memberName;

    //规格名称
    private String attributeName;

    private String createTime;

    //商品ID
    private String commodityId;


    //商品名
    private String commodityName;

    //商品名
    private String parentId;

    //评论内容
    private String recommendContent;

    //评分
    private String recommendScore;

    private String memberNickname;

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<CommentChilds> getChilds() {
        return childs;
    }

    public void setChilds(List<CommentChilds> childs) {
        this.childs = childs;
    }

    public List<String> getCommodityUrl() {
        return picUrl;
    }

    public void setCommodityUrl(List<String> commodityUrl) {
        this.picUrl = commodityUrl;
    }

    public String getMemberLogo() {
        return memberLogo;
    }

    public void setMemberLogo(String memberLogo) {
        this.memberLogo = memberLogo;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRecommendContent() {
        return recommendContent;
    }

    public void setRecommendContent(String recommendContent) {
        this.recommendContent = recommendContent;
    }

    public String getRecommendScore() {
        return recommendScore;
    }

    public void setRecommendScore(String recommendScore) {
        this.recommendScore = recommendScore;
    }

}
