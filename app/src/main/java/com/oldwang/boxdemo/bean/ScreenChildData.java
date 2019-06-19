package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class ScreenChildData implements Serializable{


    private String id;

    private String name;

    private boolean isCheck;

    //0省1市2县3区
    private int tag;

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
