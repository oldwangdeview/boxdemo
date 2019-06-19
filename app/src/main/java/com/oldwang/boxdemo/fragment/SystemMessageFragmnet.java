package com.oldwang.boxdemo.fragment;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.MessageAdpater;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.YRecycleview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SystemMessageFragmnet extends BaseFragment {

    @BindView(R.id.recyclerview)
    YRecycleview recyclerview;

    private List<String> mlistdata = new ArrayList<>();
    private MessageAdpater madpater ;

    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_yrecycleview);
    }

    @Override
    protected void initData() {
        super.initData();
    }


}
