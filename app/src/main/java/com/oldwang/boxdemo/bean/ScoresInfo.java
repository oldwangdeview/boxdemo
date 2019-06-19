package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class ScoresInfo implements Serializable{

    private Conversion conversion;

    public Conversion getConversion() {
        return conversion;
    }

    public void setConversion(Conversion conversion) {
        this.conversion = conversion;
    }
}
