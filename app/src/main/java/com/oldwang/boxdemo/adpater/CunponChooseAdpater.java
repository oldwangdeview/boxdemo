package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.CouponData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.DateTools;

import java.util.List;

public class CunponChooseAdpater extends BaseRecycleAdapter<CouponData> {

    LinearLayout item_linlayout;
    TextView type_btn;
    ListOnclickLister mlister;
    int type;
    public CunponChooseAdpater(Context context, List<CouponData> datas, int type) {
        super(context, datas, R.layout.item_coupon);
        this.type = type;
    }

    @Override
    protected void setData(RecycleViewHolder holder, CouponData s, final int position) {
        item_linlayout = holder.getItemView(R.id.item_linlayout);
        type_btn = holder.getItemView(R.id.type_btn);

        TextView tv_rmb = holder.getItemView(R.id.tv_rmb);

       TextView tv_coupon_price = holder.getItemView(R.id.tv_coupon_price);
        TextView tv_use_price = holder.getItemView(R.id.tv_use_price);
        TextView tv_limit_itme = holder.getItemView(R.id.tv_limit_itme);
        TextView tv_coupon_name = holder.getItemView(R.id.tv_coupon_name);
//        ImageView iv_type = holder.getItemView(R.id.iv_type);


        //满减
        if (!TextUtils.isEmpty(s.getCouponType()) && s.getCouponType().equals("1") ){
            tv_coupon_price.setText(s.getCouponValue());
            tv_use_price.setVisibility(View.VISIBLE);
            tv_rmb.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(s.getCouponLimit())){
                tv_use_price.setText("满"+s.getCouponLimit() + "可用");
            }
        }else {
            tv_rmb.setVisibility(View.GONE);
            tv_coupon_price.setText(s.getCouponDiscount()+"折");
            tv_use_price.setVisibility(View.GONE);

        }

        if (!TextUtils.isEmpty(s.getCouponName())){
            tv_coupon_name.setText(s.getCouponName());
        }
        if (!TextUtils.isEmpty(s.getLimitDateEnd())){
            tv_limit_itme.setText("有限日期至:"+ DateTools.getFormat(Long.parseLong(s.getLimitDateEnd())));
        }else {
            tv_limit_itme.setVisibility(View.VISIBLE);
        }


        if (!TextUtils.isEmpty(id) && id.equals(s.getCouponGetId())){
            type_btn.setText("已选择");
            type_btn.setBackgroundResource(R.drawable.coupon_itembottom_red);
            type_btn.setTextColor(mContext.getResources().getColor(R.color.c_ffffff));
        }else {
            type_btn.setVisibility(View.GONE);
            type_btn.setText("立即使用");
            type_btn.setBackgroundResource(R.drawable.coupon_itembottom);
            type_btn.setTextColor(mContext.getResources().getColor(R.color.c_d52e21));
        }

        item_linlayout.setBackgroundResource(R.mipmap.mycoupon_itemback);




        type_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });


    }

    public void setListClickLister(ListOnclickLister mlister){
        this.mlister = mlister;
    }

    private String id;

    public void setId(String id) {
        this.id = id;
    }
}
