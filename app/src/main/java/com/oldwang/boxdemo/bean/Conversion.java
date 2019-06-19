package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class Conversion implements Serializable{

    private String scores;
    private String cash;

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }
}
