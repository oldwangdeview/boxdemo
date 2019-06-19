package com.oldwang.boxdemo.event;

public class CollectCheckEvent {

    private int postion;

    private boolean isCheckAll;

    public CollectCheckEvent(int postion, boolean isCheckAll) {
        this.postion = postion;
        this.isCheckAll = isCheckAll;
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    public boolean isCheckAll() {
        return isCheckAll;
    }

    public void setCheckAll(boolean checkAll) {
        isCheckAll = checkAll;
    }
}
