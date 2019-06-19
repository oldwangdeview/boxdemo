package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class RequestBean implements Serializable{

    private String param;
    private Security security;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }
}
