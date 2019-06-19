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
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.DateTools;

import java.util.List;

public class Home_other_adpater1 extends BaseRecycleAdapter<NewsData> {
    RecyclerView recyclerview1;

    private ListOnclickLister listOnclickLister;

    public void setListOnclickLister(ListOnclickLister listOnclickLister) {
        this.listOnclickLister = listOnclickLister;
    }

    public Home_other_adpater1(Context context, List<NewsData> datas) {
        super(context, datas, R.layout.item_other1);
    }

    @Override
    protected void setData(RecycleViewHolder holder, NewsData item, final int position) {


        TextView tv_title = holder.getItemView(R.id.tv_title);
        TextView tv_commentCount = holder.getItemView(R.id.tv_commentCount);
        TextView tv_goodCount = holder.getItemView(R.id.tv_goodCount);
        TextView tv_badCount = holder.getItemView(R.id.tv_badCount);
        TextView tv_time = holder.getItemView(R.id.tv_time);

        if (!TextUtils.isEmpty(item.getNewsTitle())) {
            tv_title.setText(item.getNewsTitle());
        }
        if (!TextUtils.isEmpty(item.getCommentCount())) {
            tv_commentCount.setText(item.getCommentCount());
        }
        if (!TextUtils.isEmpty(item.getGoodCount())) {
            tv_goodCount.setText(item.getGoodCount());
        }
        if (!TextUtils.isEmpty(item.getBadCount())) {
            tv_badCount.setText(item.getBadCount());
        }
        if (!TextUtils.isEmpty(item.getCreateTime())) {
            tv_time.setText(DateTools.getFormat(Long.parseLong(item.getCreateTime())));
        }


        recyclerview1 = holder.getItemView(R.id.recyclerview);
        LinearLayoutManager ms = new LinearLayoutManager(mContext);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview1.setLayoutManager(ms);
        recyclerview1.setItemAnimator(new DefaultItemAnimator());
        HomeImageAdpater mgoodsadpater = new HomeImageAdpater(mContext, item.getNewsPicUrl());
        recyclerview1.setAdapter(mgoodsadpater);
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOnclickLister.onclick(view,position);
            }
        });

    }
}
