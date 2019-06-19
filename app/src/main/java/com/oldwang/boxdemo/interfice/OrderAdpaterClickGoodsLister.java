package com.oldwang.boxdemo.interfice;

import android.view.View;

public interface OrderAdpaterClickGoodsLister  {
    /**
     *
     * @param fposition 父适配器点击下标
     * @param view 点击的view
     * @param zposition 子适配器点击下标
     */
    public void click(int fposition, View view,int zposition);

}
