package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.OrderDetail;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.List;

public class GoodsOrderAdpater extends BaseRecycleAdapter<OrderDetail> {
    private ListOnclickLister mlister;

    public GoodsOrderAdpater(Context context, List<OrderDetail> datas ){
        super(context, datas, R.layout.goods_order_item);
    }

    @Override
    protected void setData(RecycleViewHolder holder, OrderDetail s, final int position) {

        ImageView iv_goods_image = holder.getItemView(R.id.iv_goods_image);
        TextView tv_goods_name = holder.getItemView(R.id.tv_goods_name);
        TextView tv_goods_count = holder.getItemView(R.id.tv_goods_count);
        TextView tv_attribute = holder.getItemView(R.id.tv_attribute);
        TextView tv_total = holder.getItemView(R.id.tv_total);

        if (!TextUtils.isEmpty(s.getFilePath())){
            UIUtils.loadImageView(mContext,s.getFilePath(),iv_goods_image);
        }

        if (!TextUtils.isEmpty(s.getCommodityPicUrl())){
            UIUtils.loadImageView(mContext,s.getCommodityPicUrl(),iv_goods_image);
        }

        String attribute = "";

        if (!TextUtils.isEmpty(s.getAttributeColor())){
            attribute = "颜色："+s.getAttributeColor() +" ";
        }
        if (!TextUtils.isEmpty(s.getAttributeQuality())){
            attribute += "材质："+s.getAttributeQuality() +" ";
        }
        if (!TextUtils.isEmpty(s.getAttributeSize())){
            attribute += "型号："+s.getAttributeSize();
        }
        tv_attribute.setText(attribute);

        if (!TextUtils.isEmpty(s.getCommodityName())){
            tv_goods_name.setText(s.getCommodityName());
        }
        if (!TextUtils.isEmpty(s.getCommodityNum())){
            tv_goods_count.setText("x"+s.getCommodityNum());
        }
        if (!TextUtils.isEmpty(s.getTotalPrice())){
            tv_total.setText(" ¥"+s.getTotalPrice());
        }

    }


    public void setonclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
