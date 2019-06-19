package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.CommodityTeamBuyData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.List;

public class QuanLianListDataAdpater extends BaseRecycleAdapter<CommodityTeamBuyData> {

    private boolean isPingTuan = true;

    ListOnclickLister mlister;

    public QuanLianListDataAdpater(Context context, List<CommodityTeamBuyData> datas) {
        super(context, datas, R.layout.item_quanlianlistdata);
    }

    @Override
    protected void setData(RecycleViewHolder holder, CommodityTeamBuyData s, final int position) {
        TextView tv_tag = holder.getItemView(R.id.tv_tag);

        ImageView iv_goods_image = holder.getItemView(R.id.iv_goods_image);
        TextView tv_goods_name = holder.getItemView(R.id.tv_goods_name);
        TextView tv_now_pirce = holder.getItemView(R.id.tv_now_pirce);

        TextView tv_price = holder.getItemView(R.id.tv_price);
        String commodityType = s.getCommodityType();

        if (!TextUtils.isEmpty(commodityType) && commodityType.equals("2")) {
            isPingTuan = true;
        } else if (!TextUtils.isEmpty(commodityType) && commodityType.equals("1")) {
            isPingTuan = false;
        }

        if (isPingTuan) {
            tv_tag.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(s.getTeamBuyPrice())) {
                tv_now_pirce.setText("¥" + s.getTeamBuyPrice());
            }
        } else {
            tv_tag.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(s.getSamePrice())) {
                tv_now_pirce.setText("¥" + s.getSamePrice());
            }
        }

        List<String> commodityImgList = s.getCommodityImgList();

//        if (isPingTuan){
//            if (commodityImgList != null && commodityImgList.size() > 0){
//                String imageUrl = commodityImgList.get(0);
//                if (!TextUtils.isEmpty(imageUrl)){
//                    UIUtils.loadImageView(mContext,imageUrl,iv_goods_image);
//                }
//            }
//        }else {
//            if (!TextUtils.isEmpty(s.getCommodityUrl())){
//                UIUtils.loadImageView(mContext,s.getCommodityUrl(),iv_goods_image);
//            }
//        }

        if (commodityImgList != null && commodityImgList.size() > 0) {
            String imageUrl = commodityImgList.get(0);
            if (!TextUtils.isEmpty(imageUrl)) {
                UIUtils.loadImageView(mContext, imageUrl, iv_goods_image);
            }
        } else if (!TextUtils.isEmpty(s.getCommodityUrl())) {
            UIUtils.loadImageView(mContext, s.getCommodityUrl(), iv_goods_image);
        }

        if (!TextUtils.isEmpty(s.getCommodityName())) {
            tv_goods_name.setText(s.getCommodityName());
        }
        if (!TextUtils.isEmpty(s.getSamePrice())) {
            tv_price.setText("¥" + s.getSalePrice());
        }


        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlister != null) {
                    mlister.onclick(v, position);
                }
            }
        });
    }

    public void setListonClickLister(ListOnclickLister mlister) {
        this.mlister = mlister;
    }

    public void setPingTuan(boolean pingTuan) {
        isPingTuan = pingTuan;
    }
}
