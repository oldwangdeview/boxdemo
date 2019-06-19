package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.MainPath_ListBean;
import com.oldwang.boxdemo.bean.RegionData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;

import java.util.List;

public class ChooseCityAdapter extends BaseRecycleAdapter<MainPath_ListBean> {

    ListOnclickLister mlister;

    public ChooseCityAdapter(Context context, List<MainPath_ListBean> datas) {
        super(context, datas, R.layout.item_choose_city);
    }

    @Override
    protected void setData(RecycleViewHolder holder, MainPath_ListBean item, final int position) {

        TextView tv_city_name = holder.getItemView(R.id.tv_city_name);
        ImageView iv_delete = holder.getItemView(R.id.iv_delete);

        tv_city_name.setText(item.fullname);

        if (item.isItmeChoose){
            tv_city_name.setBackground(mContext.getResources().getDrawable(R.drawable.city_choose_shape));
            tv_city_name.setTextColor(Color.parseColor("#FFD52E21"));
            iv_delete.setVisibility(View.VISIBLE);
        }else {
            tv_city_name.setBackground(mContext.getResources().getDrawable(R.drawable.city_noraml_shape));
            tv_city_name.setTextColor(Color.parseColor("#FF262626"));
            iv_delete.setVisibility(View.GONE);
        }


        tv_city_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mlister!=null){
                    mlister.onclick(view,position);
                }
            }
        });
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mlister!=null){
                    mlister.onclick(view,position);
                }
            }
        });
    }

    public void setListClickLister(ListOnclickLister mlister){
        this.mlister = mlister;
    }

}
