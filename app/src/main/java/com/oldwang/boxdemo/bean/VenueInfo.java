package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.List;

public class VenueInfo implements Serializable{

    private String businessTime;//营业时间
    private String commentGradeTotal;//场馆评价总数
    private List<String> devices;
    private String phone;//场馆电话
    private List<ServiceData> serviceData;
    private List<VenuVideo> venuVideoUrls;
    private String venueDetailedAddress;//场馆地址
    private List<VenuVideo> venueDevicesUrls;
    private String venueId;
    private String venueIntroduce;//介绍
    private String venueName;//场馆名称
    private List<VenuVideo> venuePicUrls;
    private List<VenuVideo> venueTeachingUrls;
    private String venueTypeId;//场馆类型ID
    private String venueTypeName;//场馆类型名称

    private String venueDetailPic;
    private String venueCommentCount;//评价数量
    private List<VenuVideo> venueNotices;//公告信息


    public String getVenueDetailPic() {
        return venueDetailPic;
    }

    public void setVenueDetailPic(String venueDetailPic) {
        this.venueDetailPic = venueDetailPic;
    }

    public List<VenuVideo> getVenueNotices() {
        return venueNotices;
    }

    public void setVenueNotices(List<VenuVideo> venueNotices) {
        this.venueNotices = venueNotices;
    }


    public String getVenueCommentCount() {
        return venueCommentCount;
    }

    public void setVenueCommentCount(String venueCommentCount) {
        this.venueCommentCount = venueCommentCount;
    }

    public String getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(String businessTime) {
        this.businessTime = businessTime;
    }

    public String getCommentGradeTotal() {
        return commentGradeTotal;
    }

    public void setCommentGradeTotal(String commentGradeTotal) {
        this.commentGradeTotal = commentGradeTotal;
    }

    public List<String> getDevices() {
        return devices;
    }

    public void setDevices(List<String> devices) {
        this.devices = devices;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<ServiceData> getServiceData() {
        return serviceData;
    }

    public void setServiceData(List<ServiceData> serviceData) {
        this.serviceData = serviceData;
    }

    public List<VenuVideo> getVenuVideoUrls() {
        return venuVideoUrls;
    }

    public void setVenuVideoUrls(List<VenuVideo> venuVideoUrls) {
        this.venuVideoUrls = venuVideoUrls;
    }

    public String getVenueDetailedAddress() {
        return venueDetailedAddress;
    }

    public void setVenueDetailedAddress(String venueDetailedAddress) {
        this.venueDetailedAddress = venueDetailedAddress;
    }

    public List<VenuVideo> getVenueDevicesUrls() {
        return venueDevicesUrls;
    }

    public void setVenueDevicesUrls(List<VenuVideo> venueDevicesUrls) {
        this.venueDevicesUrls = venueDevicesUrls;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getVenueIntroduce() {
        return venueIntroduce;
    }

    public void setVenueIntroduce(String venueIntroduce) {
        this.venueIntroduce = venueIntroduce;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public List<VenuVideo> getVenuePicUrls() {
        return venuePicUrls;
    }

    public void setVenuePicUrls(List<VenuVideo> venuePicUrls) {
        this.venuePicUrls = venuePicUrls;
    }

    public List<VenuVideo> getVenueTeachingUrls() {
        return venueTeachingUrls;
    }

    public void setVenueTeachingUrls(List<VenuVideo> venueTeachingUrls) {
        this.venueTeachingUrls = venueTeachingUrls;
    }

    public String getVenueTypeId() {
        return venueTypeId;
    }

    public void setVenueTypeId(String venueTypeId) {
        this.venueTypeId = venueTypeId;
    }

    public String getVenueTypeName() {
        return venueTypeName;
    }

    public void setVenueTypeName(String venueTypeName) {
        this.venueTypeName = venueTypeName;
    }
}
