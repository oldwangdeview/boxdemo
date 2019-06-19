package com.oldwang.boxdemo.adpater;

import android.content.Context;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.help.RecycleViewHolder;

import java.util.List;

public class VideoFindAdpater extends BaseRecycleAdapter<String> {
    public VideoFindAdpater(Context context, List<String> datas) {
        super(context, datas, R.layout.item_videofind);
    }

    @Override
    protected void setData(RecycleViewHolder holder, String s, int position) {

    }
}
