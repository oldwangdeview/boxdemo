package com.oldwang.boxdemo.event;

public class UpdateAreaEvent {

    private int tag;
    private String id;

    public UpdateAreaEvent(int tag, String id) {
        this.tag = tag;
        this.id = id;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
