package com.oldwang.boxdemo.bean;

import java.io.Serializable;

/**
 * Created by oldwang on 2019/1/2 0002.
 */


public class StatusCode<T> implements Serializable{

    private int status;// 状态码  状态码状态 1成功
    private String message;// 状态码详细值
    private String msg;// 状态码详细值
    private T data;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StatusCode{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
