package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class AttributeQualityBean implements Serializable {
    private String qualityName;

    public String getQualityName() {
        return qualityName;
    }

    public void setQualityName(String qualityName) {
        this.qualityName = qualityName;
    }
}
