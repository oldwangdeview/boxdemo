package com.oldwang.boxdemo.event;

public class JumpTeachers {


    //1名师2战将
    private int type;
    private String id;


    public JumpTeachers(int type, String id) {
        this.type = type;
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
