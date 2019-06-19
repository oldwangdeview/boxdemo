package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.List;

public class ScreenData implements Serializable{

    private String typeName;

    private boolean isArea;

    private List<ScreenChildData> screenChildDataList;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isArea() {
        return isArea;
    }

    public void setArea(boolean area) {
        isArea = area;
    }

    public List<ScreenChildData> getScreenChildDataList() {
        return screenChildDataList;
    }

    public void setScreenChildDataList(List<ScreenChildData> screenChildDataList) {
        this.screenChildDataList = screenChildDataList;
    }
}
