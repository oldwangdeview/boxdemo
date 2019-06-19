package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.CommodityData;
import com.oldwang.boxdemo.event.MainJumpEvent;
import com.oldwang.boxdemo.help.RecycleViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class HomeAdptaer extends BaseRecycleAdapter<String> {
    private RecyclerView recyclerview1;
    private  List<CommodityData> commodityData;
    public HomeAdptaer(Context context, List<String> datas) {
        super(context, datas, R.layout.item_home);
    }

    public void setData( List<CommodityData> commodityData){
        this.commodityData = commodityData;
    }

    @Override
    protected void setData(RecycleViewHolder holder, String s, int position) {
        recyclerview1 = holder.getItemView(R.id.recyclerview1);
        LinearLayoutManager ms= new LinearLayoutManager(mContext);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview1.setLayoutManager(ms);
        recyclerview1.setItemAnimator(new DefaultItemAnimator());


        holder.getItemView(R.id.tv_goods_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MainJumpEvent(1));
            }
        });

        Home_goodsAdpater mgoodsadpater = new Home_goodsAdpater(mContext,commodityData);
        recyclerview1.setAdapter(mgoodsadpater);
    }
}
