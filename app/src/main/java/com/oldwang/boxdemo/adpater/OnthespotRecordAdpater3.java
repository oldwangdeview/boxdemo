package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.SchoolData;
import com.oldwang.boxdemo.bean.VenueData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;

import java.util.List;

public class OnthespotRecordAdpater3 extends BaseRecycleAdapter<SchoolData> {

    private ListOnclickLister listOnclickLister;

    public OnthespotRecordAdpater3(Context context, List<SchoolData> datas) {
        super(context, datas, R.layout.item_other2);
    }

    @Override
    protected void setData(RecycleViewHolder holder, SchoolData s, final int position) {

        TextView tv_name = holder.getItemView(R.id.tv_name);
        TextView tv_count = holder.getItemView(R.id.tv_count);
        TextView position_data = holder.getItemView(R.id.position_data);
        LinearLayout ll_count = holder.getItemView(R.id.ll_count);
        LinearLayout ll_item = holder.getItemView(R.id.ll_item);


        ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOnclickLister.onclick(view,position);
            }
        });

        position_data.setText((position+1)+"");


        if (position == 0){
            position_data.setTextColor(mContext.getResources().getColor(R.color.c_F8504D));
            position_data.setBackgroundResource(R.mipmap.ranking_num1);
        }else if (position == 1){
            position_data.setTextColor(mContext.getResources().getColor(R.color.c_FED83F));
            position_data.setBackgroundResource(R.mipmap.ranking_num2);
        }else if (position == 2){
            position_data.setTextColor(mContext.getResources().getColor(R.color.c_FE6F3F));
            position_data.setBackgroundResource(R.mipmap.ranking_num3);
        }else {
            position_data.setTextColor(mContext.getResources().getColor(R.color.c_81878B));
            position_data.setBackgroundResource(R.mipmap.ranking_numimage);
        }

        if (!TextUtils.isEmpty(s.getNewsBoxingSchoolName())){
            tv_name.setText(s.getNewsBoxingSchoolName());
        }
        if (!TextUtils.isEmpty(s.getNewsBoxingSchoolPraiseCount())){
            tv_count.setText(s.getNewsBoxingSchoolPraiseCount());
        }

    }

    public void setlistonclicklister(ListOnclickLister listOnclickLister) {
        this.listOnclickLister = listOnclickLister;
    }
}
