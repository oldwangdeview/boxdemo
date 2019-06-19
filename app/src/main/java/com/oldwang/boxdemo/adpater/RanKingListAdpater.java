package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.ScoreData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;

import java.util.List;

public class RanKingListAdpater extends BaseRecycleAdapter<ScoreData> {


    private ListOnclickLister listOnclickLister;

    TextView position_data;
    public RanKingListAdpater(Context context, List<ScoreData> datas) {
        super(context, datas, R.layout.item_rankinglist);
    }

    @Override
    protected void setData(RecycleViewHolder holder, ScoreData s, final int position) {
        position_data = holder.getItemView(R.id.position_data);
        position_data.setText((position+1)+"");
        switch (position){
            case 0:
                position_data.setTextColor(mContext.getResources().getColor(R.color.c_F8504D));
                position_data.setBackgroundResource(R.mipmap.ranking_num1);
                break;
            case 1:
                position_data.setTextColor(mContext.getResources().getColor(R.color.c_FED83F));
                position_data.setBackgroundResource(R.mipmap.ranking_num2);
                break;
            case 2:
                position_data.setTextColor(mContext.getResources().getColor(R.color.c_FE6F3F));
                position_data.setBackgroundResource(R.mipmap.ranking_num3);
                break;
                default:
                    position_data.setTextColor(mContext.getResources().getColor(R.color.c_81878B));
                    position_data.setBackgroundResource(R.mipmap.ranking_numimage);
                    break;
        }

        TextView tv_name = holder.getItemView(R.id.tv_name);
        TextView tv_score = holder.getItemView(R.id.tv_score);

        if (!TextUtils.isEmpty(s.getMemberName())) {
            tv_name.setText(s.getMemberName());
        }
        if (!TextUtils.isEmpty(s.getScoreTotal())) {
            tv_score.setText(s.getScoreTotal());
        }


        LinearLayout ll_item = holder.getItemView(R.id.ll_item);

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
