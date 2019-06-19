package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.ServiceData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;

import java.util.List;

public class ChooseSpaceTypeAdapter extends BaseRecycleAdapter<ServiceData> {

    ListOnclickLister mlister;

    private String typeId = "";

    public ChooseSpaceTypeAdapter(Context context, List<ServiceData> datas) {
        super(context, datas, R.layout.item_choose_area);
    }

    @Override
    protected void setData(RecycleViewHolder holder, ServiceData item, final int position) {

        String venueProjectId = item.getVenueProjectId();


        TextView tv_area_name = holder.getItemView(R.id.tv_area_name);
        ImageView iv_image = holder.getItemView(R.id.iv_image);

        if (!TextUtils.isEmpty(typeId) && !TextUtils.isEmpty(venueProjectId) && venueProjectId.equals(typeId)){
            tv_area_name.setTextColor(mContext.getResources().getColor(R.color.c_FFD52E21));
            iv_image.setVisibility(View.VISIBLE);
        }else {
            tv_area_name.setTextColor(mContext.getResources().getColor(R.color.c_FF595959));
            iv_image.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.getVenueProjectName())) {
            tv_area_name.setText(item.getVenueProjectName());
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

    public void setTypeId(String regionId) {
        this.typeId = regionId;
    }
}
