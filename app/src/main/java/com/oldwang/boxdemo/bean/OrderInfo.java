package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.List;

public class OrderInfo implements Serializable{

    private String orderNo;
    private String orderPayPrice;
    private String sign;


    private List<CommodityData> commoditys;

    private String courierName;
    private String orderStatus;
    private String receivingAddress;
    private String courierNo;//物流单号
    private String logisticsStatus;
    private boolean isPinTuan;

    public boolean isPinTuan() {
        return isPinTuan;
    }

    public void setPinTuan(boolean pinTuan) {
        isPinTuan = pinTuan;
    }

    public String getLogisticsStatus() {
        return logisticsStatus;
    }

    public void setLogisticsStatus(String logisticsStatus) {
        this.logisticsStatus = logisticsStatus;
    }

    public String getCourierNo() {
        return courierNo;
    }

    public void setCourierNo(String courierNo) {
        this.courierNo = courierNo;
    }

    public List<CommodityData> getCommoditys() {
        return commoditys;
    }

    public void setCommoditys(List<CommodityData> commoditys) {
        this.commoditys = commoditys;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getReceivingAddress() {
        return receivingAddress;
    }

    public void setReceivingAddress(String receivingAddress) {
        this.receivingAddress = receivingAddress;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderPayPrice() {
        return orderPayPrice;
    }

    public void setOrderPayPrice(String orderPayPrice) {
        this.orderPayPrice = orderPayPrice;
    }
}
