package com.oldwang.boxdemo.event;

import com.oldwang.boxdemo.bean.Attribute;

public class AttributeEvent {

    private int tag;

    private Attribute attribute;

    public AttributeEvent(int tag, Attribute attribute) {
        this.tag = tag;
        this.attribute = attribute;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }
}
