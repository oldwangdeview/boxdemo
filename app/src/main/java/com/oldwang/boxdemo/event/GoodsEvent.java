package com.oldwang.boxdemo.event;

public class GoodsEvent {

    //1单独购买2我要开团3加入购物车4立即购买
    private int tag;


    public GoodsEvent(int tag) {
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
