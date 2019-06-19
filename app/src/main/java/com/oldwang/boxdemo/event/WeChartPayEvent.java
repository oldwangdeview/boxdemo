package com.oldwang.boxdemo.event;

public class WeChartPayEvent {

    private int errCode;
    public WeChartPayEvent(int errCode) {
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }
}
