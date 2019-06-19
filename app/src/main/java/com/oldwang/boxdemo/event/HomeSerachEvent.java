package com.oldwang.boxdemo.event;

public class HomeSerachEvent {


    private String content;

    public HomeSerachEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
