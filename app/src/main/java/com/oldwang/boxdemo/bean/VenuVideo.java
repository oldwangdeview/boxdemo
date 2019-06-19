package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class VenuVideo implements Serializable{

    private String desc;//描述
    private String venuVideo;//场馆视频
    private String venueDevice;//场馆设备图片
    private String venuePic;//场馆图片
    private String venueTeaching;//场馆师资图片
    private String previewPicPath;

    public String getPreviewPicPath() {
        return previewPicPath;
    }

    public void setPreviewPicPath(String previewPicPath) {
        this.previewPicPath = previewPicPath;
    }

    public String getVenuePic() {
        return venuePic;
    }

    public void setVenuePic(String venuePic) {
        this.venuePic = venuePic;
    }

    public String getVenueTeaching() {
        return venueTeaching;
    }

    public void setVenueTeaching(String venueTeaching) {
        this.venueTeaching = venueTeaching;
    }

    public String getVenueDevice() {
        return venueDevice;
    }

    public void setVenueDevice(String venueDevice) {
        this.venueDevice = venueDevice;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVenuVideo() {
        return venuVideo;
    }

    public void setVenuVideo(String venuVideo) {
        this.venuVideo = venuVideo;
    }
}
