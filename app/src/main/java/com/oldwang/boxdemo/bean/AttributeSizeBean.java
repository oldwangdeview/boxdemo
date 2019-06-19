package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class AttributeSizeBean implements Serializable {
    private String sizeName;

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }
}
