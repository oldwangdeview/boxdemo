package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.CouponData;
import com.oldwang.boxdemo.help.RecycleViewHolder;

import java.util.List;

public class MainCouponAdpater extends BaseRecycleAdapter<CouponData> {

    TextView tv_text1;
    TextView tv_text2;
    TextView tv_text3;
    TextView tv_text4;
    public MainCouponAdpater(Context context, List<CouponData> datas) {
        super(context, datas, R.layout.item_maindialogcoupon);
    }

    @Override
    protected void setData(RecycleViewHolder holder, CouponData s, int position) {

        tv_text1 = holder.getItemView(R.id.tv_text1);
        tv_text2 = holder.getItemView(R.id.tv_text2);


        if(!TextUtils.isEmpty(s.getCouponValue())){
            tv_text1.setText("¥ " +s.getCouponValue());
         }

         if(!TextUtils.isEmpty(s.getCouponLimit())){
            tv_text2.setVisibility(View.VISIBLE);
            tv_text2.setText(s.getCouponLimit());
         }else{
            tv_text2.setVisibility(View.GONE);
         }


         if(!TextUtils.isEmpty(s.getCouponDiscount())){
            tv_text1.setText(s.getCouponDiscount()+" 折");
         }

         if(!TextUtils.isEmpty(s.getCouponCommodityType())){

         }
    }
}
