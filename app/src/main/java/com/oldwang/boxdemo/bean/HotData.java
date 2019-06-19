package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.List;

public class HotData implements Serializable{

    private  List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
