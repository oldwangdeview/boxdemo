package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.OrderData;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;

import java.util.List;

public class IncomeRecordAdpater extends BaseRecycleAdapter<OrderData> {


    ListOnclickLister mlister;


    public IncomeRecordAdpater(Context context, List<OrderData> datas) {
        super(context, datas, R.layout.item_incomerecord);
    }

    @Override
    protected void setData(RecycleViewHolder holder, OrderData s, final int position) {

        LinearLayout linearLayout = holder.getItemView(R.id.ll_item);

        TextView tv_commissionAmount = holder.getItemView(R.id.tv_commissionAmount);
        TextView tv_commissionTotalAmount = holder.getItemView(R.id.tv_commissionTotalAmount);
        TextView tv_createTime = holder.getItemView(R.id.tv_createTime);
        TextView tv_memberName = holder.getItemView(R.id.tv_memberName);
        TextView tv_memberLevel = holder.getItemView(R.id.tv_memberLevel);

        if (!TextUtils.isEmpty(s.getCommissionAmount())){
            tv_commissionAmount.setText(s.getCommissionAmount());
        }

        if (!TextUtils.isEmpty(s.getCommissionTotalAmount())){
            tv_commissionTotalAmount.setText(s.getCommissionTotalAmount());
        }

        if (!TextUtils.isEmpty(s.getCreateTime())){
            tv_createTime.setText(s.getCreateTime());
        }

        if (!TextUtils.isEmpty(s.getMemberName())){
            tv_memberName.setText(s.getMemberName());
        }

        if (!TextUtils.isEmpty(s.getMemberLevel())){
            tv_memberLevel.setText(s.getMemberLevel());
        }


        linearLayout.setOnClickListener(new View.OnClickListener() {
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
