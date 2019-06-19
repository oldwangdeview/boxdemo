package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.BaseDetailActivity;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.VenueData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.List;

public class Home_XLadpater extends BaseRecycleAdapter<VenueData> {
    public Home_XLadpater(Context context, List<VenueData> datas) {
        super(context, datas, R.layout.item_homegoods);
    }

    @Override
    protected void setData(RecycleViewHolder holder, final VenueData item, int position) {

        ImageView itemView = holder.getItemView(R.id.image);
        if (!TextUtils.isEmpty(item.getVenueUrl())){
            UIUtils.loadImageView(mContext,item.getVenueUrl(),itemView);
        }

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseDetailActivity.startactivity(mContext,item.getVenueId());

            }
        });
    }
}
