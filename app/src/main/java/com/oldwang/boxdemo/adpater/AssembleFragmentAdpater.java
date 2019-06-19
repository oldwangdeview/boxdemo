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

public class AssembleFragmentAdpater extends BaseRecycleAdapter<CommodityTeamBuyData> {


    ImageView iv_goods_image;
    TextView tv_tag;
    TextView tv_goods_name;
    TextView tv_now_pirce;
    TextView tv_pirce;
    ListOnclickLister mlister;

    int type = -1;

    public AssembleFragmentAdpater(Context context, List<CommodityTeamBuyData> datas,int type) {
        super(context, datas, R.layout.item_quanlianlistdata);
        this.type = type;
    }

    @Override
    protected void setData(RecycleViewHolder holder, CommodityTeamBuyData s, final int position) {

        iv_goods_image = holder.getItemView(R.id.iv_goods_image);
        tv_tag = holder.getItemView(R.id.tv_tag);
        tv_goods_name = holder.getItemView(R.id.tv_goods_name);
        tv_now_pirce = holder.getItemView(R.id.tv_now_pirce);
        tv_pirce = holder.getItemView(R.id.tv_price);

        if(s.getCommodityImgList()!=null&&s.getCommodityImgList().size()>0&&!TextUtils.isEmpty(s.getCommodityImgList().get(0))){
            UIUtils.loadImageView(mContext,s.getCommodityImgList().get(0),iv_goods_image);
        }
        tv_tag.setVisibility(type==1?View.GONE:type==2?View.VISIBLE:View.GONE);


        if(!TextUtils.isEmpty(s.getCommodityName())){
            tv_goods_name.setText(s.getCommodityName());
        }

        if(!TextUtils.isEmpty(s.getSalePrice())){
            tv_now_pirce.setText(s.getSalePrice());
        }
        if(!TextUtils.isEmpty(s.getSamePrice())){
            tv_pirce.setText(s.getSamePrice());
        }else{
            tv_pirce.setVisibility(View.GONE);
        }

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

    }


    public void setListOnclick(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
