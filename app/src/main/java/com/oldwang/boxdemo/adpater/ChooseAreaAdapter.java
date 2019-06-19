package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.RegionData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;

import java.util.List;

public class ChooseAreaAdapter extends BaseRecycleAdapter<RegionData> {

    ListOnclickLister mlister;

    private String regionId = "";

    public ChooseAreaAdapter(Context context, List<RegionData> datas) {
        super(context, datas, R.layout.item_choose_area);
    }

    @Override
    protected void setData(RecycleViewHolder holder, RegionData item, final int position) {

        String areaId = item.getAreaId();

        if (TextUtils.isEmpty(areaId)){
            areaId = item.getRegionId();
        }

        TextView tv_area_name = holder.getItemView(R.id.tv_area_name);
        ImageView iv_image = holder.getItemView(R.id.iv_image);
        tv_area_name.setSingleLine(true);

        if (!TextUtils.isEmpty(regionId) && !TextUtils.isEmpty(areaId) && areaId.equals(regionId)){
            tv_area_name.setTextColor(mContext.getResources().getColor(R.color.c_FFD52E21));
            iv_image.setVisibility(View.VISIBLE);
        }else {
            tv_area_name.setTextColor(mContext.getResources().getColor(R.color.c_FF595959));
            iv_image.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.getFullname())) {
            tv_area_name.setText(item.getFullname());
        }

        if (!TextUtils.isEmpty(item.getRegionName())) {
            tv_area_name.setText(item.getRegionName());
        }
        tv_area_name.setOnClickListener(new View.OnClickListener() {
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

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
