package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class AttributeColorBean implements Serializable {
    private String colorName;

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
}
