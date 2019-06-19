package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyCommentGoodsBean implements Serializable {

    private String attributeId;
    private String attributeName;
    private String commodityCommentId;
    private String commodityId;
    private String commodityName;
    private String commodityUrl;
    private String createTime;
    private String memberLogo;
    private String memberName;
    private String recommendContent;
    private String recommendScore;
    private String recommendType;
    private String salePrice;
    private String samePrice;

    //子评论信息
    private List<CommentChilds> childs = new ArrayList<>();
    //图片
    private List<String> picUrl = new ArrayList<>();

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getCommodityCommentId() {
        return commodityCommentId;
    }

    public void setCommodityCommentId(String commodityCommentId) {
        this.commodityCommentId = commodityCommentId;
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

    public String getCommodityUrl() {
        return commodityUrl;
    }

    public void setCommodityUrl(String commodityUrl) {
        this.commodityUrl = commodityUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(String recommendType) {
        this.recommendType = recommendType;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getSamePrice() {
        return samePrice;
    }

    public void setSamePrice(String samePrice) {
        this.samePrice = samePrice;
    }

    public List<CommentChilds> getChilds() {
        return childs;
    }

    public void setChilds(List<CommentChilds> childs) {
        this.childs = childs;
    }

    public List<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(List<String> picUrl) {
        this.picUrl = picUrl;
    }
}
