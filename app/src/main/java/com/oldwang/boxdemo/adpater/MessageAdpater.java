package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.ScoreData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.DateTools;

import java.util.List;

public class MessageAdpater extends BaseRecycleAdapter<ScoreData> {

    private ListOnclickLister listOnclickLister;

    public MessageAdpater(Context context, List<ScoreData> datas) {
        super(context, datas, R.layout.item_message);
    }

    @Override
    protected void setData(RecycleViewHolder holder, ScoreData scoreData, final int position) {

        LinearLayout ll_item = holder.getItemView(R.id.ll_item);
        View read_status = holder.getItemView(R.id.read_status);
        TextView tv_title = holder.getItemView(R.id.tv_title);
        TextView tv_time = holder.getItemView(R.id.tv_time);

        if (scoreData.getMessageIsRead() != null && scoreData.getMessageIsRead().equals("1")){
            read_status.setVisibility(View.GONE);
        }else {
            read_status.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(scoreData.getMessageTitle())){
            tv_title.setText(scoreData.getMessageTitle());
        }

        if (!TextUtils.isEmpty(scoreData.getCreateTime())){
            tv_time.setText(DateTools.getFormat(Long.parseLong(scoreData.getCreateTime()), "yyyy-MM-dd HH:mm"));
        }




        ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOnclickLister.onclick(view,position);
            }
        });

    }

    public void setlistonclicklister(ListOnclickLister listOnclickLister) {
        this.listOnclickLister = listOnclickLister;
    }
}
