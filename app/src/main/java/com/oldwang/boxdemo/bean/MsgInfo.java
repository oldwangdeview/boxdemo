package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class MsgInfo implements Serializable{

    private String ReadCount;

    public String getReadCount() {
        return ReadCount;
    }

    public void setReadCount(String readCount) {
        ReadCount = readCount;
    }
}
