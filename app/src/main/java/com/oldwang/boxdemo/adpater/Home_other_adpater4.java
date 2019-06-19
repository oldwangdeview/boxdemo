package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.EvaluationData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.DateTools;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.List;

public class Home_other_adpater4 extends BaseRecycleAdapter<EvaluationData> {


    ListOnclickLister listOnclickLister;


    public void setListOnclickLister(ListOnclickLister listOnclickLister) {
        this.listOnclickLister = listOnclickLister;
    }

    public Home_other_adpater4(Context context, List<EvaluationData> datas) {
        super(context, datas, R.layout.item_other4);
    }

    @Override
    protected void setData(RecycleViewHolder holder, EvaluationData item, final int position) {

        ImageView iv_image = holder.getItemView(R.id.iv_image);
        TextView tv_name = holder.getItemView(R.id.tv_name);
        TextView tv_time = holder.getItemView(R.id.tv_time);
        TextView tv_score = holder.getItemView(R.id.tv_score);

        UIUtils.loadImageView(AbsSuperApplication.getContext(),item.getCommodityPicUrl(),iv_image);
        tv_name.setText(item.getEvaluationDetailsName());
        if (!TextUtils.isEmpty(item.getEvaluationTime())){
            tv_time.setText(DateTools.getFormat(Long.parseLong(item.getEvaluationTime())));
        }
        tv_score.setText(item.getEvaluationDetailsScore()+"分");
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOnclickLister.onclick(view,position);
            }
        });

    }
}
