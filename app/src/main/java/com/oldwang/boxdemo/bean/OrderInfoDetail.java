package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.List;

public class OrderInfoDetail implements Serializable {

    private AddressData address;
    //商品总数
    private String commodityTotalNum;
    private List<CommodityData> commoditys; //商品集合
    private String couponPrice; //优惠券抵扣金额
    private String deliverReceiverTime; //收货时间
    private String memberId; //会员id
    private String orderNo; //订单编号
    private String orderPriceFreight; //运费
    private String orderPriceRealPay; //实付金额
    private String orderScoreUse; //使用积分
    private String orderStatus; //订单状态（01未付款02以付款03待发货04以发货05以收货06完结订单07取消订单）
    private String orderTime; //下单时间
    private String orderType; //订单类型（正常购买商品1，团购商品2）
    private String payPrice; //支付金额
    private String payTime; //支付时间
    private String scorePrice; //积分抵扣金额
    private String serviceType; //售后类型(1换货，2退款并退货，3仅退款)
    private String serviceTypeDes; //售后服务类型描述
    private String statusDes; //状态描述
    private String surplusCloseTime; //订单剩余闭关时间（未支付订单需要）
    private String teamBuyId; //团id
    private String teamBuyMemberId;//团员ID
    private String teamEndTime; //团购截止时间
    private String totalPrice; //商品总金额
    private String teamBuyEndTime; //订单剩余闭关时间（未支付订单需要）

    public String getTeamBuyEndTime() {
        return teamBuyEndTime;
    }

    public void setTeamBuyEndTime(String teamBuyEndTime) {
        this.teamBuyEndTime = teamBuyEndTime;
    }

    /***缺失字段**/
    private String deliverSendTime; //发货时间
    private String reason; //退款描述
    private String serviceIngType; //退款状态 1审核中 2审核通过 3退款完成
    private String courierNo;//物流单号

    private String serviceStatusDes;//退换货状态描述
    private String callPhone;//拨打电话
    private String serviceStatus;

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getServiceStatusDes() {
        return serviceStatusDes;
    }

    public void setServiceStatusDes(String serviceStatusDes) {
        this.serviceStatusDes = serviceStatusDes;
    }

    public String getCourierNo() {
        return courierNo;
    }

    public void setCourierNo(String courierNo) {
        this.courierNo = courierNo;
    }

    public String getCallPhone() {
        return callPhone;
    }

    public void setCallPhone(String callPhone) {
        this.callPhone = callPhone;
    }

    public void setDeliverSendTime(String deliverSendTime) {
        this.deliverSendTime = deliverSendTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getServiceIngType() {
        return serviceIngType;
    }

    public void setServiceIngType(String serviceIngType) {
        this.serviceIngType = serviceIngType;
    }

    public String getDeliverSendTime() {
        return deliverSendTime;
    }

    public AddressData getAddress() {
        return address;
    }

    public void setAddress(AddressData address) {
        this.address = address;
    }

    public String getCommodityTotalNum() {
        return commodityTotalNum;
    }

    public void setCommodityTotalNum(String commodityTotalNum) {
        this.commodityTotalNum = commodityTotalNum;
    }

    public List<CommodityData> getCommoditys() {
        return commoditys;
    }

    public void setCommoditys(List<CommodityData> commoditys) {
        this.commoditys = commoditys;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
    }

    public String getDeliverReceiverTime() {
        return deliverReceiverTime;
    }

    public void setDeliverReceiverTime(String deliverReceiverTime) {
        this.deliverReceiverTime = deliverReceiverTime;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderPriceFreight() {
        return orderPriceFreight;
    }

    public void setOrderPriceFreight(String orderPriceFreight) {
        this.orderPriceFreight = orderPriceFreight;
    }

    public String getOrderPriceRealPay() {
        return orderPriceRealPay;
    }

    public void setOrderPriceRealPay(String orderPriceRealPay) {
        this.orderPriceRealPay = orderPriceRealPay;
    }

    public String getOrderScoreUse() {
        return orderScoreUse;
    }

    public void setOrderScoreUse(String orderScoreUse) {
        this.orderScoreUse = orderScoreUse;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getScorePrice() {
        return scorePrice;
    }

    public void setScorePrice(String scorePrice) {
        this.scorePrice = scorePrice;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceTypeDes() {
        return serviceTypeDes;
    }

    public void setServiceTypeDes(String serviceTypeDes) {
        this.serviceTypeDes = serviceTypeDes;
    }

    public String getStatusDes() {
        return statusDes;
    }

    public void setStatusDes(String statusDes) {
        this.statusDes = statusDes;
    }

    public String getSurplusCloseTime() {
        return surplusCloseTime;
    }

    public void setSurplusCloseTime(String surplusCloseTime) {
        this.surplusCloseTime = surplusCloseTime;
    }

    public String getTeamBuyId() {
        return teamBuyId;
    }

    public void setTeamBuyId(String teamBuyId) {
        this.teamBuyId = teamBuyId;
    }

    public String getTeamBuyMemberId() {
        return teamBuyMemberId;
    }

    public void setTeamBuyMemberId(String teamBuyMemberId) {
        this.teamBuyMemberId = teamBuyMemberId;
    }

    public String getTeamEndTime() {
        return teamEndTime;
    }

    public void setTeamEndTime(String teamEndTime) {
        this.teamEndTime = teamEndTime;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
