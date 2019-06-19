package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.CommodityData;
import com.oldwang.boxdemo.bean.MyOrderData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.interfice.OrderAdpaterClickGoodsLister;
import com.oldwang.boxdemo.view.MyGridView;

import java.util.List;

public class OrderListAdpater extends BaseRecycleAdapter<MyOrderData> {
    MyGridView listview;

    private int tag = 0;

    public void setPostion(int postion) {
        this.tag = postion;
    }

    OrderAdpaterClickGoodsLister mlister;
    public OrderListAdpater(Context context, List<MyOrderData> datas) {
        super(context, datas, R.layout.item_order);
    }

    @Override
    protected void setData(RecycleViewHolder holder, MyOrderData s, final int position) {


       TextView tv_order_num = holder.getItemView(R.id.tv_order_num);
        TextView type = holder.getItemView(R.id.type);
        TextView tv_total_num = holder.getItemView(R.id.tv_total_num);
        TextView tv_total_price = holder.getItemView(R.id.tv_total_price);

        TextView btn_1 = holder.getItemView(R.id.btn_1);
        TextView btn_2 = holder.getItemView(R.id.btn_2);

        if (!TextUtils.isEmpty(s.getOrderNo())){
            tv_order_num.setText("订单编号： " + s.getOrderNo());
        }

        if (!TextUtils.isEmpty(s.getOrderPriceRealPay())){
            tv_total_price.setText("    合计：¥" + s.getOrderPriceRealPay());
        }
        if (!TextUtils.isEmpty(s.getCommodityNumTotal())){
            tv_total_num.setText("共" + s.getCommodityNumTotal()+"件商品");
        }


        List<CommodityData> orderDetail = s.getOrderDetail();

        btn_1.setVisibility(View.VISIBLE);
        btn_2.setVisibility(View.VISIBLE);

        switch (tag){
            case -1:
                type.setText("待分享");
                btn_1.setVisibility(View.GONE);
                btn_2.setText("去分享");
                break;
            case 0:
                type.setText("待付款");
                btn_1.setText("取消订单");
                btn_2.setText("付款");
                break;
            case 1:
                type.setText("待发货");
                btn_1.setVisibility(View.GONE);
                btn_2.setVisibility(View.GONE);
                break;
            case 2:
                type.setText("待收货");
                btn_1.setText("确认收货");
                btn_2.setText("查看物流");

                break;
            case 3:
                type.setText("待评价");
                btn_1.setVisibility(View.GONE);
                btn_2.setVisibility(View.GONE);
                break;
            case 4:
                type.setText("退款/售后中");
                btn_1.setVisibility(View.GONE);
                btn_2.setVisibility(View.GONE);
                break;
            case 5:
                type.setText("已完成");
                btn_1.setText("删除订单");
                btn_2.setVisibility(View.GONE);
                break;
        }



        listview = holder.getItemView(R.id.listview);


        OrderDetailAdpater madpater = new OrderDetailAdpater(mContext,orderDetail);
        madpater.setOrderNo(s.getOrderNo());
        madpater.setOrderType(s.getOrderType());
        madpater.setOrderList(true);
        madpater.setTag(tag);

        btn_1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(mlister!=null){
                     mlister.click(position,v,-1);
                 }
             }
         });
        listview.setAdapter(madpater);
        btn_2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(mlister!=null){
                     mlister.click(position,v,-1);
                 }
             }
         });

         holder.getView().setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(mlister!=null){
                     mlister.click(position,v,-1);
                 }
             }
         });
//        listview.setFocusable(false);
        listview.setClickable(false);// 屏蔽主动获得点击
        listview.setPressed(false);
        listview.setEnabled(false);
    }

    public void setOrderAdpaterClickGoodsLister(OrderAdpaterClickGoodsLister mlister){
        this.mlister = mlister;
    }




}
