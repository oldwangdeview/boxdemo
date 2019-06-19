package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.NoticeData;
import com.oldwang.boxdemo.bean.VenueCommentData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.List;

public class NoticeListAdapter extends BaseRecycleAdapter<NoticeData> {

    ListOnclickLister mlister;

    private List<NoticeData> datas;

    public NoticeListAdapter(Context context, List<NoticeData> datas) {
        super(context, datas, R.layout.item_basedetail_noticedetail);
        this.datas = datas;
    }

    @Override
    protected void setData(RecycleViewHolder holder, NoticeData noticeData, final int position) {


        View mview = holder.getItemView(R.id.view);
        TextView tv_name = holder.getItemView(R.id.tv_name);
        TextView tv_time = holder.getItemView(R.id.tv_time);

        if (!TextUtils.isEmpty(noticeData.getNoticeTitle())){
            tv_name.setText(noticeData.getNoticeTitle());
        }
        if (!TextUtils.isEmpty(noticeData.getNoticeReleaseTime())){
            tv_time.setText(noticeData.getNoticeReleaseTime());
        }

        if(position==datas.size()-1){
            mview.setVisibility(View.GONE);
        }else {
            mview.setVisibility(View.VISIBLE);
        }
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlister.onclick(view,position);
            }
        });


    }

    public void setlistonclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }

}
