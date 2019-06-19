package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.CourierData;
import com.oldwang.boxdemo.help.RecycleViewHolder;

import java.util.HashMap;
import java.util.List;

public class ChoiceLogisticsAdpater extends BaseRecycleAdapter<CourierData> {
    ImageView choice_image;
    HashMap<Integer,Boolean> clickdata = new HashMap<>();
    public ChoiceLogisticsAdpater(Context context, List<CourierData> datas) {
        super(context, datas, R.layout.item_logistics);
    }

    @Override
    protected void setData(RecycleViewHolder holder, CourierData s, final int position) {


        TextView tv_name = holder.getItemView(R.id.tv_name);
        TextView tv_price = holder.getItemView(R.id.tv_price);


        if (!TextUtils.isEmpty(s.getFreightPrice())){
            tv_price.setText("¥"+s.getFreightPrice());
        }else {
            tv_price.setText("¥"+0);

        }

        if (!TextUtils.isEmpty(s.getCourierName())){
            tv_name.setText(s.getCourierName());
        }


        choice_image = holder.getItemView(R.id.choice_image);
        if(clickdata.get(position)){
            choice_image.setImageResource(R.mipmap.addresslist_choice);
        }else{
            choice_image.setImageResource(R.mipmap.addresslist_unchoice);
        }


        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlldataType(false);
                clickdata.put(position,true);
            }
        });
    }


    public void setAlldataType(boolean type){
        for(int i =0;i<mDatas.size();i++){
            clickdata.put(i,type);
        }
        notifyDataSetChanged();
    }

    public CourierData getchoicedata(){
        for(int i =0;i<mDatas.size();i++){
            if(clickdata.get(i)){
                return mDatas.get(i);
            }
        }
        return null;
    }

}
