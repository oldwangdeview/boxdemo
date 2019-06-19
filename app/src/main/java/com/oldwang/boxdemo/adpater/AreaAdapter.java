package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.NewsData;
import com.oldwang.boxdemo.bean.RegionData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.DateTools;

import java.util.List;

public class AreaAdapter extends BaseRecycleAdapter<RegionData> {

    ListOnclickLister mlister;

    public AreaAdapter(Context context, List<RegionData> datas) {
        super(context, datas, R.layout.item_area);
    }

    @Override
    protected void setData(RecycleViewHolder holder, RegionData item, final int position) {


        TextView tv_area_name = holder.getItemView(R.id.tv_area_name);

        if (!TextUtils.isEmpty(item.getFullname())) {
            tv_area_name.setText(item.getFullname());
        }

        if (!TextUtils.isEmpty(item.getRegionName())) {
            tv_area_name.setText(item.getRegionName());
        }
        tv_area_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mlister!=null){
                    mlister.onclick(view,position);
                }
            }
        });

    }

    public void setListClickLister(ListOnclickLister mlister){
        this.mlister = mlister;
    }

}
