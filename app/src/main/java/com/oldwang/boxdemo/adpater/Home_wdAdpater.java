package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.VideoData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.List;

public class Home_wdAdpater extends BaseRecycleAdapter<VideoData> {

    private ListOnclickLister listOnclickLister;

    public void setListOnclickLister(ListOnclickLister listOnclickLister) {
        this.listOnclickLister = listOnclickLister;
    }

    public Home_wdAdpater(Context context, List<VideoData> datas) {

        super(context, datas, R.layout.item_homegoods);
    }

    @Override
    protected void setData(RecycleViewHolder holder, VideoData item, final int position) {

        ImageView itemView = holder.getItemView(R.id.palyimage);
        itemView.setVisibility(View.VISIBLE);
        ImageView image = holder.getItemView(R.id.image);
        if (!TextUtils.isEmpty(item.getBoxingVideoImg())) {
            UIUtils.loadVideoView(mContext,item.getBoxingVideoImg(),image);
        }
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOnclickLister.onclick(view,position);
            }
        });
    }
}
