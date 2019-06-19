package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class CouponData implements Serializable{


    //折扣
    private String couponDiscount;
    //使用门槛金额(满减类)
    private String couponLimit;
    //状态描述
    private String couponUseStatusDes;
    //获取优惠券id
    private String couponGetId;
    //优惠券ID
    private String couponId;
    //优惠券名称
    private String couponName;
    //优惠券类型 1满减 2折扣
    private String couponType;
    //优惠券状态（0未领取1以领取3以使用）
    private String couponUseStatus;
    //截止日期
    private String limitDateEnd;

    //优惠券金额
    private String couponValue;

    private String couponCommodityType;

    public String getCouponCommodityType() {
        return couponCommodityType;
    }

    public void setCouponCommodityType(String couponCommodityType) {
        this.couponCommodityType = couponCommodityType;
    }

    public String getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(String couponValue) {
        this.couponValue = couponValue;
    }

    public String getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(String couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public String getCouponLimit() {
        return couponLimit;
    }

    public void setCouponLimit(String couponLimit) {
        this.couponLimit = couponLimit;
    }

    public String getCouponUseStatusDes() {
        return couponUseStatusDes;
    }

    public void setCouponUseStatusDes(String couponUseStatusDes) {
        this.couponUseStatusDes = couponUseStatusDes;
    }

    public String getCouponGetId() {
        return couponGetId;
    }

    public void setCouponGetId(String couponGetId) {
        this.couponGetId = couponGetId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getCouponUseStatus() {
        return couponUseStatus;
    }

    public void setCouponUseStatus(String couponUseStatus) {
        this.couponUseStatus = couponUseStatus;
    }

    public String getLimitDateEnd() {
        return limitDateEnd;
    }

    public void setLimitDateEnd(String limitDateEnd) {
        this.limitDateEnd = limitDateEnd;
    }
}
