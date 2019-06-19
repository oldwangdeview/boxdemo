package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.List;

public class CommodityTeamBuyData implements Serializable{


    /**品牌ID*/
    private String brandId;

    /**品牌名称	*/
    private String brandName;

    /**	商品描述*/
    private String commodityDescription;


    /**商品ID*/
    private String commodityId;


    //商品信息类型 1普通商品2拼团商品
    private String commodityType;

    /**商品 图片列表*/
    private List<String> commodityImgList;

//    /**	预留字段*/
//    private List<String> commodityImgsDetail;

    /**商品名称*/
    private String commodityName;

    /**团购人数*/
    private String peopleNum;


    /**挂牌价*/
    private String salePrice;

    /**普通价*/
    private String samePrice;

    /**销售量*/
    private String sellCount	;

    /**上架状态	1上架0 未上架*/
    private String shelfStatus;

    /**状态描述	*/
    private String shelfStatusDes;

    /**库存	*/
    private String stockNum;

    /**团购价*/
    private String teamBuyPrice;

    /**评论数量*/
    private String commentCount;

    /**商品描述*/
    private String commodity_description;

    private NewCommodity newCommodity;

    private String endTime;

    //拼团的编码
    private String teamBuyCode;

    //拼团id
    private String teamBuyId;

    private String commodityUrl;

    //是否收藏0未收藏1已收藏
    private String isFavorite;

    private String score;
    private String commodityImgsDetail;

    public String getCommodityImgsDetail() {
        return commodityImgsDetail;
    }

    public void setCommodityImgsDetail(String commodityImgsDetail) {
        this.commodityImgsDetail = commodityImgsDetail;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getCommodityUrl() {
        return commodityUrl;
    }

    public void setCommodityUrl(String commodityUrl) {
        this.commodityUrl = commodityUrl;
    }

    public String getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    public String getTeamBuyId() {
        return teamBuyId;
    }

    public void setTeamBuyId(String teamBuyId) {
        this.teamBuyId = teamBuyId;
    }

    public String getTeamBuyCode() {
        return teamBuyCode;
    }

    public void setTeamBuyCode(String teamBuyCode) {
        this.teamBuyCode = teamBuyCode;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    private List<TeamBuyMembers> teamBuyMembers;

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getCommodity_description() {
        return commodity_description;
    }

    public void setCommodity_description(String commodity_description) {
        this.commodity_description = commodity_description;
    }

    public NewCommodity getNewCommodity() {
        return newCommodity;
    }

    public void setNewCommodity(NewCommodity newCommodity) {
        this.newCommodity = newCommodity;
    }

    public List<TeamBuyMembers> getTeamBuyMembers() {
        return teamBuyMembers;
    }

    public void setTeamBuyMembers(List<TeamBuyMembers> teamBuyMembers) {
        this.teamBuyMembers = teamBuyMembers;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCommodityDescription() {
        return commodityDescription;
    }

    public void setCommodityDescription(String commodityDescription) {
        this.commodityDescription = commodityDescription;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public List<String> getCommodityImgList() {
        return commodityImgList;
    }

    public void setCommodityImgList(List<String> commodityImgList) {
        this.commodityImgList = commodityImgList;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(String peopleNum) {
        this.peopleNum = peopleNum;
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

    public String getSellCount() {
        return sellCount;
    }

    public void setSellCount(String sellCount) {
        this.sellCount = sellCount;
    }

    public String getShelfStatus() {
        return shelfStatus;
    }

    public void setShelfStatus(String shelfStatus) {
        this.shelfStatus = shelfStatus;
    }

    public String getShelfStatusDes() {
        return shelfStatusDes;
    }

    public void setShelfStatusDes(String shelfStatusDes) {
        this.shelfStatusDes = shelfStatusDes;
    }

    public String getStockNum() {
        return stockNum;
    }

    public void setStockNum(String stockNum) {
        this.stockNum = stockNum;
    }

    public String getTeamBuyPrice() {
        return teamBuyPrice;
    }

    public void setTeamBuyPrice(String teamBuyPrice) {
        this.teamBuyPrice = teamBuyPrice;
    }
}
