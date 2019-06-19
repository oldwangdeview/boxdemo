package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HiostorySearchData implements Serializable{

    private String userPhone = "";
    private List<String> historyList = new ArrayList<>();

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public List<String> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<String> historyList) {
        this.historyList = historyList;
    }
}
