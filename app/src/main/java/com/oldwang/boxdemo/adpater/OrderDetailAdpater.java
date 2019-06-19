package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.AfterSaleActivity;
import com.oldwang.boxdemo.activity.GoodsDetailActivity;
import com.oldwang.boxdemo.activity.OrderEvaluateActivity;
import com.oldwang.boxdemo.bean.CommodityData;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailAdpater extends BaseAdapter {
    Context mContext;
    List<CommodityData> listdata = new ArrayList<>();
    private String orderNo;

    private boolean isOrderList ;

    public void setOrderList(boolean orderList) {
        isOrderList = orderList;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public OrderDetailAdpater(Context mContext, List<CommodityData> listdata) {
        this.mContext = mContext;
        this.listdata = listdata;
    }



    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = UIUtils.inflate(mContext, R.layout.item_orderdetail);
        }

        ImageView iv_image = convertView.findViewById(R.id.iv_image);
        TextView orderdetail_text_pintuan = convertView.findViewById(R.id.orderdetail_text_pintuan);

        LinearLayout ll_item = convertView.findViewById(R.id.ll_item);

        TextView tv_name = convertView.findViewById(R.id.tv_name);
        TextView tv_goods_price = convertView.findViewById(R.id.tv_goods_price);
        TextView tv_goods_count = convertView.findViewById(R.id.tv_goods_count);

        TextView tv_attribute = convertView.findViewById(R.id.tv_attribute);
        TextView btn_one = convertView.findViewById(R.id.btn_one);
        TextView btn_two = convertView.findViewById(R.id.btn_two);



        final CommodityData commodityData = listdata.get(position);


        if (tag == 3) {
            if (!TextUtils.isEmpty(commodityData.getIsService()) && commodityData.getIsService().equals("0")){
                btn_one.setVisibility(View.VISIBLE);
            }else {
                btn_one.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(commodityData.getIsEvaluate()) && commodityData.getIsEvaluate().equals("0")){
                btn_two.setVisibility(View.VISIBLE);
            }else {
                btn_two.setVisibility(View.GONE);
            }
        }

        if (!TextUtils.isEmpty(orderType) && !orderType.equals("1")) {
            orderdetail_text_pintuan.setVisibility(View.VISIBLE);
        } else {
            orderdetail_text_pintuan.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(commodityData.getCommodityPicUrl())) {
            UIUtils.loadImageView(mContext, commodityData.getCommodityPicUrl(), iv_image);
        }

        if (!TextUtils.isEmpty(commodityData.getCommodityName())) {
            tv_name.setText(commodityData.getCommodityName());
        }
        if (!TextUtils.isEmpty(commodityData.getPriceUnit())) {
            tv_goods_price.setText("¥ " + commodityData.getPriceUnit());
        }
        if (!TextUtils.isEmpty(commodityData.getCommodityNum())) {
            tv_goods_count.setText("x" + commodityData.getCommodityNum());
        }

        String attribute = "";

        if (!TextUtils.isEmpty(commodityData.getAttributeColor())) {
            attribute = "颜色：" + commodityData.getAttributeColor() + " ";
        }
        if (!TextUtils.isEmpty(commodityData.getAttributeQuality())) {
            attribute += "材质：" + commodityData.getAttributeQuality() + " ";
        }
        if (!TextUtils.isEmpty(commodityData.getAttributeSize())) {
            attribute += "型号：" + commodityData.getAttributeSize();
        }
        tv_attribute.setText(attribute);


        convertView.findViewById(R.id.btn_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AfterSaleActivity.startactivity(mContext,commodityData,orderNo);
            }
        });

        convertView.findViewById(R.id.btn_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderEvaluateActivity.startactivity(mContext,commodityData.getCommodityId(),orderNo,commodityData.getCommodityPicUrl());
            }
        });

        if (!isOrderList) {
            ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isPinTuan = false;
                    if (!TextUtils.isEmpty(orderType) && !orderType.equals("1")) {
                        isPinTuan = true;
                    }
                    GoodsDetailActivity.startactivity(mContext, isPinTuan, commodityData.getCommodityId(), null);
                }
            });
        }



        return convertView;
    }

    //订单类型 1普通订单
    private String orderType;

    private int tag;

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
