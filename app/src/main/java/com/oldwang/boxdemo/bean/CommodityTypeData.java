package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommodityTypeData implements Serializable{

    private String commodityTypeId;
    private String commodityTypeName;
    private String commodityTypeCode;
    private String commodityTypeImg;
    private List<CommodityTypeData> childs = new ArrayList<>();
    private boolean isChoose;

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public String getCommodityTypeCode() {
        return commodityTypeCode;
    }

    public void setCommodityTypeCode(String commodityTypeCode) {
        this.commodityTypeCode = commodityTypeCode;
    }

    public String getCommodityTypeImg() {
        return commodityTypeImg;
    }

    public void setCommodityTypeImg(String commodityTypeImg) {
        this.commodityTypeImg = commodityTypeImg;
    }

    public List<CommodityTypeData> getChilds() {
        return childs;
    }

    public void setChilds(List<CommodityTypeData> childs) {
        this.childs = childs;
    }

    public String getCommodityTypeId() {
        return commodityTypeId;
    }

    public void setCommodityTypeId(String commodityTypeId) {
        this.commodityTypeId = commodityTypeId;
    }

    public String getCommodityTypeName() {
        return commodityTypeName;
    }

    public void setCommodityTypeName(String commodityTypeName) {
        this.commodityTypeName = commodityTypeName;
    }

}
