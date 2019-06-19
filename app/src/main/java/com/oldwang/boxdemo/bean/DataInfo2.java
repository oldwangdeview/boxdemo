package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.List;

public class DataInfo2 implements Serializable{


    ListData<CommodityTeamBuyData> regionData;

    OrderInfoDetail orderInfo;




    public ListData<CommodityTeamBuyData> getRegionData() {
        return regionData;
    }

    public void setRegionData(ListData<CommodityTeamBuyData> regionData) {
        this.regionData = regionData;
    }

    public OrderInfoDetail getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfoDetail orderInfo) {
        this.orderInfo = orderInfo;
    }

    public ListData<CommodityTeamBuyData> getCommodityTeamBuyData() {
        return regionData;
    }

    public void setCommodityTeamBuyData(ListData<CommodityTeamBuyData> regionData) {
        this.regionData = regionData;
    }
}
