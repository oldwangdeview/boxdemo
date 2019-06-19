package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.List;

public class MyOrderData implements Serializable{

    //	0 可以售后申请 1 或者null 不可以
    private String canApply;
    //商品总数
    private String commodityNumTotal;
    //是否评价	1未评价2以评价
    private String isEvaluate;
    //是否查询待分享 不为空查询待分享
    private String isShare;
    private List<CommodityData> orderDetail;
    //订单号
    private String orderNo;
    //订单实付金额
    private String orderPriceRealPay;
    //订单状态(（01未付款02以付款03待发货04以发货05以收货06完结订单07取消订单）)
    private String orderStatus;
    //订单状态描述
    private String orderStatusDes;
    //订单类型 1普通订单
    private String orderType;
    //服务状态描述
    private String serviceStatusDes;
    //团购剩余时间（后端计算初始值）
    private String teamBuyEndTime;

    private String courierNo;//物流单号

    public String getCourierNo() {
        return courierNo;
    }

    public void setCourierNo(String courierNo) {
        this.courierNo = courierNo;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    private String serviceCode;

    public String getServiceNo() {
        return serviceCode;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceCode = serviceNo;
    }

    public String getCanApply() {
        return canApply;
    }

    public void setCanApply(String canApply) {
        this.canApply = canApply;
    }

    public String getCommodityNumTotal() {
        return commodityNumTotal;
    }

    public void setCommodityNumTotal(String commodityNumTotal) {
        this.commodityNumTotal = commodityNumTotal;
    }

    public String getIsEvaluate() {
        return isEvaluate;
    }

    public void setIsEvaluate(String isEvaluate) {
        this.isEvaluate = isEvaluate;
    }

    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
    }

    public List<CommodityData> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<CommodityData> orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderPriceRealPay() {
        return orderPriceRealPay;
    }

    public void setOrderPriceRealPay(String orderPriceRealPay) {
        this.orderPriceRealPay = orderPriceRealPay;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusDes() {
        return orderStatusDes;
    }

    public void setOrderStatusDes(String orderStatusDes) {
        this.orderStatusDes = orderStatusDes;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getServiceStatusDes() {
        return serviceStatusDes;
    }

    public void setServiceStatusDes(String serviceStatusDes) {
        this.serviceStatusDes = serviceStatusDes;
    }

    public String getTeamBuyEndTime() {
        return teamBuyEndTime;
    }

    public void setTeamBuyEndTime(String teamBuyEndTime) {
        this.teamBuyEndTime = teamBuyEndTime;
    }
}
