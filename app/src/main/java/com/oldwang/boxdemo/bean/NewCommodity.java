package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class NewCommodity implements Serializable{

    //新品ID
    private String commodityId;
    //新品图片链接
    private String commodityImgsDetail;
    //新品名称
    private String commodityName;

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityImgsDetail() {
        return commodityImgsDetail;
    }

    public void setCommodityImgsDetail(String commodityImgsDetail) {
        this.commodityImgsDetail = commodityImgsDetail;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }
}
