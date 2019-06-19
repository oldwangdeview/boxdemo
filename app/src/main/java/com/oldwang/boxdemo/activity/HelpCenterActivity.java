package com.oldwang.boxdemo.activity;

import android.support.v7.widget.RecyclerView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseActivity;

import butterknife.BindView;

public class HelpCenterActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_helpcenter);
    }

    public void setonview(){

    }
}
