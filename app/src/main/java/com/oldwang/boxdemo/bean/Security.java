package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class Security implements Serializable{

    private String sign;//签名
    private String token;//登录后获取token,登录后服务端返回给客户端。有时效限制。服务器端配置
    private String xx;//参数是否加密 0不加密 1加密

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getXx() {
        return xx;
    }

    public void setXx(String xx) {
        this.xx = xx;
    }
}
