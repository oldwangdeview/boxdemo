package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.VenueData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.List;

public class TrainingBaseAdpater extends BaseRecycleAdapter<VenueData> {
    private ListOnclickLister mlister;
    public TrainingBaseAdpater(Context context, List<VenueData> datas) {
        super(context, datas, R.layout.item_trainingbase);
    }

    @Override
    protected void setData(RecycleViewHolder holder, VenueData s, final int position) {

        ImageView iv_image = holder.getItemView(R.id.iv_image);
        TextView tv_name = holder.getItemView(R.id.tv_name);
        TextView tv_distance = holder.getItemView(R.id.tv_distance);
        TextView tv_detail = holder.getItemView(R.id.tv_detail);
        TextView tv_address = holder.getItemView(R.id.tv_address);
        TextView tv_phone = holder.getItemView(R.id.tv_phone);

        TextView tv_subscribe = holder.getItemView(R.id.tv_subscribe);
        LinearLayout ll_item = holder.getItemView(R.id.ll_item);




        if (!TextUtils.isEmpty(s.getVenuePicUrl())){
            UIUtils.loadImageView(mContext,s.getVenuePicUrl(),iv_image);
        }

        if (!TextUtils.isEmpty(s.getVenueName())){
            tv_name.setText(s.getVenueName());
        }
        if (!TextUtils.isEmpty(s.getDistance())){
            tv_distance.setText(s.getDistance());
        }
        if (!TextUtils.isEmpty(s.getVenueTypeName())){
            tv_detail.setText(s.getVenueTypeName());
        }
        if (!TextUtils.isEmpty(s.getVenueAddress())){
            tv_address.setText(s.getVenueAddress());
        }
        if (!TextUtils.isEmpty(s.getVenuePhone())){
            tv_phone.setText(s.getVenuePhone());
        }

        ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position );
                }
            }
        });

        tv_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position );
                }
            }
        });
    }


    public void setlistonclickLister(ListOnclickLister mlister){
        this.mlister = mlister;

    }
}
