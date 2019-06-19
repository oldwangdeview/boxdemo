package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class LogisticsData implements Serializable{

    private String logisticsNote;
    private String logisticsStatus;
    private String logisticsTime;
    private boolean isFirst;
    private boolean isTop;

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public boolean isFirst() {
        return isFirst;
    }
    public void setFirst(boolean first) {
        isFirst = first;
    }

    public String getLogisticsNote() {
        return logisticsNote;
    }

    public void setLogisticsNote(String logisticsNote) {
        this.logisticsNote = logisticsNote;
    }

    public String getLogisticsStatus() {
        return logisticsStatus;
    }

    public void setLogisticsStatus(String logisticsStatus) {
        this.logisticsStatus = logisticsStatus;
    }

    public String getLogisticsTime() {
        return logisticsTime;
    }

    public void setLogisticsTime(String logisticsTime) {
        this.logisticsTime = logisticsTime;
    }
}
