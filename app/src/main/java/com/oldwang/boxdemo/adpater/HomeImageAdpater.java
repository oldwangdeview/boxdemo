package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.CommodityData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.List;

public class HomeImageAdpater extends BaseRecycleAdapter<String> {

    public HomeImageAdpater(Context context, List<String> datas) {
        super(context, datas, R.layout.item_homegoods);
    }

    @Override
    protected void setData(RecycleViewHolder holder, String item, int position) {

        ImageView itemView = holder.getItemView(R.id.image);
        UIUtils.loadImageView(mContext,item,itemView);
    }
}
