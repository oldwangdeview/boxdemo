package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.WithdrawalData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;

import java.util.List;

public class GetCashAdpater extends BaseRecycleAdapter<WithdrawalData> {
    int type;
    TextView text_type;

    private ListOnclickLister listOnclickLister;

    public GetCashAdpater(Context context, List<WithdrawalData> datas,int type) {
        super(context, datas, R.layout.item_getcash);
        this.type = type;
    }

    @Override
    protected void setData(RecycleViewHolder holder, WithdrawalData s, final int position) {


        LinearLayout ll_item = holder.getItemView(R.id.ll_item);
        TextView tv_money = holder.getItemView(R.id.tv_money);
        TextView tv_time = holder.getItemView(R.id.tv_time);

        if (!TextUtils.isEmpty(s.getWithdrawalAmount() )){
            tv_money.setText("提现金额："+s.getWithdrawalAmount());
        }
        if (!TextUtils.isEmpty(s.getCreateTime() )){
            tv_time.setText(s.getCreateTime());
        }


        text_type = holder.getItemView(R.id.text_type);
        switch (type){
            case 0:
                text_type.setText("待审核");
                text_type.setTextColor(mContext.getResources().getColor(R.color.c_FDA100));
                break;
            case 1:
                text_type.setText("审核失败");
                text_type.setTextColor(mContext.getResources().getColor(R.color.c_FDA100));

                break;
            case 2:
                text_type.setText("已提现");
                text_type.setTextColor(mContext.getResources().getColor(R.color.c_159C3B));

                break;
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
