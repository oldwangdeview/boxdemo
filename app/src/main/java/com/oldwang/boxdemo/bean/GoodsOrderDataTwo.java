package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.List;

public class GoodsOrderDataTwo implements Serializable{


    private AddressData address;
    //可用积分
    private String canUseScore;
    //商品总金额
    private String commodityNumTotal;
    //可用优惠券个人数
    private String couponCount;
    private String memberId;
    private OrderDetail orderDetail;
    //"订单状态(01未付款02以付款03待发货04以发货05以收货06完结订单07取消订单)",
    private String orderStatus;
    //订单类型 1普通订单
    private String orderType;
    private String orderTypeName;

    public AddressData getAddress() {
        return address;
    }

    public void setAddress(AddressData address) {
        this.address = address;
    }

    public String getCanUseScore() {
        return canUseScore;
    }

    public void setCanUseScore(String canUseScore) {
        this.canUseScore = canUseScore;
    }

    public String getCommodityNumTotal() {
        return commodityNumTotal;
    }

    public void setCommodityNumTotal(String commodityNumTotal) {
        this.commodityNumTotal = commodityNumTotal;
    }

    public String getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(String couponCount) {
        this.couponCount = couponCount;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }
}
