package com.oldwang.boxdemo.event;

public class CartCheckEvent {


    private boolean isCheckAll;

    public CartCheckEvent(boolean isCheckAll) {
        this.isCheckAll = isCheckAll;
    }


    public boolean isCheckAll() {
        return isCheckAll;
    }

    public void setCheckAll(boolean checkAll) {
        isCheckAll = checkAll;
    }
}
