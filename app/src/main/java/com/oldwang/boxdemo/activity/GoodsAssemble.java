package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseActivity;

/**
 * 拼团
 */
public class GoodsAssemble extends BaseActivity {
    @Override
    protected void initView() {
        setContentView(R.layout.activity_goodsassemble);
    }



    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,GoodsAssemble.class);
        mContext.startActivity(mIntent);
    }
}
