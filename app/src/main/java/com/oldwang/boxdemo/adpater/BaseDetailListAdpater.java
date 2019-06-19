package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.List;

/**
 */
public class BaseDetailListAdpater extends BaseRecycleAdapter<String> {

    public BaseDetailListAdpater(Context context, List<String> datas) {
        super(context, datas, R.layout.item_homegoods);
    }

    @Override
    protected void setData(RecycleViewHolder holder, String s, final int position) {


        ImageView itemView = holder.getItemView(R.id.image);
        ImageView bofang = holder.getItemView(R.id.palyimage);

        if (!TextUtils.isEmpty(s)){
            UIUtils.loadImageView(mContext,s,itemView);
        }

        if (isShowVideo){
            bofang.setVisibility(View.VISIBLE);
        }else {
            bofang.setVisibility(View.GONE);
        }
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listOnclickLister != null){
                    listOnclickLister.onclick(view,position);
                }
            }
        });

    }


    public void setShowVideo(boolean showVideo) {
        isShowVideo = showVideo;
    }

    boolean isShowVideo = false;

    private ListOnclickLister listOnclickLister;

    public void setListOnclickLister(ListOnclickLister listOnclickLister) {
        this.listOnclickLister = listOnclickLister;
    }
}
