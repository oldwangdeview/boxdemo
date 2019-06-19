package com.oldwang.boxdemo.event;

import com.oldwang.boxdemo.bean.BrandData;

public class BrantEvent {

    private BrandData brandData;
    public BrantEvent(BrandData brandData) {
        this.brandData = brandData;
    }

    public BrandData getBrandData() {
        return brandData;
    }
}
