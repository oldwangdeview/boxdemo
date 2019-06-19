package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.oldwang.boxdemo.activity.GoodsDetailActivity;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.CommodityData;
import com.oldwang.boxdemo.help.RecycleViewHolder;

import java.util.List;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.util.UIUtils;

public class Home_goodsAdpater extends BaseRecycleAdapter<CommodityData> {

    public Home_goodsAdpater(Context context, List<CommodityData> datas) {
        super(context, datas, R.layout.item_homegoods);
    }

    @Override
    protected void setData(RecycleViewHolder holder, final CommodityData item, int position) {

        ImageView itemView = holder.getItemView(R.id.image);
        if (!TextUtils.isEmpty(item.getCommodityUrl())) {
            UIUtils.loadImageView(mContext,item.getCommodityUrl(),itemView);
        }
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodsDetailActivity.startactivity(mContext,false,item.getCommodityId(),null);
            }
        });
    }
}
