package com.oldwang.boxdemo.bean;

import java.io.Serializable;

/**
 * "area_id":1,
 * "parent_area_id":0,
 * "fullname":"北京市",
 * "area_type":0
 */
public class MainPath_ListBean implements Serializable {

    public String area_id;
    public String parent_area_id;
    public String fullname;
    public String area_type;
    public boolean isChoose;
    public boolean isItmeChoose;


}
