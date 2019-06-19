package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.LogisticsData;
import com.oldwang.boxdemo.bean.RegionData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;

import java.util.List;

/***
 * 物流信息
 */
public class ExpressAdapter extends BaseRecycleAdapter<LogisticsData> {

    ListOnclickLister mlister;

    public ExpressAdapter(Context context, List<LogisticsData> datas) {
        super(context, datas, R.layout.item_express);
    }

    @Override
    protected void setData(RecycleViewHolder holder, LogisticsData item, final int position) {

        TextView tv_address = holder.getItemView(R.id.tv_address);

        TextView tv_name_two = holder.getItemView(R.id.tv_name_two);
        TextView tv_time_two = holder.getItemView(R.id.tv_time_two);

        TextView tv_time_three = holder.getItemView(R.id.tv_time_three);
        TextView tv_name_three = holder.getItemView(R.id.tv_name_three);

        LinearLayout ll_one = holder.getItemView(R.id.ll_one);
        LinearLayout ll_two = holder.getItemView(R.id.ll_two);
        LinearLayout ll_three = holder.getItemView(R.id.ll_three);
        if (item.isFirst()){
            ll_one.setVisibility(View.VISIBLE);
            ll_two.setVisibility(View.GONE);
            ll_three.setVisibility(View.GONE);
        }else if (item.isTop()){
            ll_one.setVisibility(View.GONE);
            ll_two.setVisibility(View.VISIBLE);
            ll_three.setVisibility(View.GONE);
        }else {
            ll_one.setVisibility(View.GONE);
            ll_two.setVisibility(View.GONE);
            ll_three.setVisibility(View.VISIBLE);
        }


        if (!TextUtils.isEmpty(item.getLogisticsNote())){
            tv_address.setText(item.getLogisticsNote());
            tv_name_two.setText(item.getLogisticsNote());
            tv_name_three.setText(item.getLogisticsNote());
        }
        if (!TextUtils.isEmpty(item.getLogisticsTime())){
            tv_time_two.setText(item.getLogisticsTime());
            tv_time_three.setText(item.getLogisticsTime());
        }


    }

    public void setListClickLister(ListOnclickLister mlister){
        this.mlister = mlister;
    }

}
