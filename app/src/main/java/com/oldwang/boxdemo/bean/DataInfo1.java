package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.List;

public class DataInfo1 implements Serializable{


    ListData<TeamData> regionData;

    ListData<MyOrderData> orderData;

    ListData<CommodityData> commodityData;

    CourierInfo courierInfo;

    CourierInfo orderScoreInfo;

    OrderInfo orderInfo;

    SignInfo signInfo;

    ListData<NewsData> newsData;
    NewsInfo newsInfo;


    VideoData videoInfo;
    ListData<CommodityTeamBuyData> data;

    List<LogisticsData> logisticsData;
    ListData<MyCommentGoodsBean> commodityCommentData;
    ListData<MyCommentVenueBean> venueCommentData;
    ListData<MyCommentNewsBean> newsCommentData;
    ListData<NewsData> boxingVideoCommentData;

    public ListData<MyCommentNewsBean> getNewsCommentData() {
        return newsCommentData;
    }

    public void setNewsCommentData(ListData<MyCommentNewsBean> newsCommentData) {
        this.newsCommentData = newsCommentData;
    }

    public ListData<MyCommentVenueBean> getVenueCommentData() {
        return venueCommentData;
    }

    public void setVenueCommentData(ListData<MyCommentVenueBean> venueCommentData) {
        this.venueCommentData = venueCommentData;
    }

    public ListData<MyCommentGoodsBean> getCommodityCommentData() {
        return commodityCommentData;
    }

    public void setCommodityCommentData(ListData<MyCommentGoodsBean> commodityCommentData) {
        this.commodityCommentData = commodityCommentData;
    }

    private String mainId;
    private String recommendId;

    public String getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(String recommendId) {
        this.recommendId = recommendId;
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public List<LogisticsData> getLogisticsData() {
        return logisticsData;
    }

    public void setLogisticsData(List<LogisticsData> logisticsData) {
        this.logisticsData = logisticsData;
    }

    public ListData<CommodityTeamBuyData> getData() {
        return data;
    }

    public void setData(ListData<CommodityTeamBuyData> data) {
        this.data = data;
    }

    public ListData<NewsData> getBoxingVideoCommentData() {
        return boxingVideoCommentData;
    }

    public void setBoxingVideoCommentData(ListData<NewsData> boxingVideoCommentData) {
        this.boxingVideoCommentData = boxingVideoCommentData;
    }

    public VideoData getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(VideoData videoInfo) {
        this.videoInfo = videoInfo;
    }

    public ListData<NewsData> getNewsData() {
        return newsData;
    }

    public void setNewsData(ListData<NewsData> newsData) {
        this.newsData = newsData;
    }

    public NewsInfo getNewsInfo() {
        return newsInfo;
    }

    public void setNewsInfo(NewsInfo newsInfo) {
        this.newsInfo = newsInfo;
    }

    public ListData<MyOrderData> getOrderData() {
        return orderData;
    }

    public void setOrderData(ListData<MyOrderData> orderData) {
        this.orderData = orderData;
    }

    public ListData<CommodityData> getCommodityData() {
        return commodityData;
    }

    public void setCommodityData(ListData<CommodityData> commodityData) {
        this.commodityData = commodityData;
    }

    public SignInfo getSignInfo() {
        return signInfo;
    }

    public void setSignInfo(SignInfo signInfo) {
        this.signInfo = signInfo;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public CourierInfo getOrderScoreInfo() {
        return orderScoreInfo;
    }

    public void setOrderScoreInfo(CourierInfo orderScoreInfo) {
        this.orderScoreInfo = orderScoreInfo;
    }

    public ListData<TeamData> getRegionData() {
        return regionData;
    }

    public void setRegionData(ListData<TeamData> regionData) {
        this.regionData = regionData;
    }

    public CourierInfo getCourierInfo() {
        return courierInfo;
    }

    public void setCourierInfo(CourierInfo courierInfo) {
        this.courierInfo = courierInfo;
    }
}
