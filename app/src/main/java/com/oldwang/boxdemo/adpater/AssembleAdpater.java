package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.discussionavatarview.DiscussionAvatarView;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.CommodityTeamBuyData;
import com.oldwang.boxdemo.bean.TeamBuyMembers;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.Api;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class AssembleAdpater extends BaseRecycleAdapter<CommodityTeamBuyData> {
    View mview;
    DiscussionAvatarView daview;
    ListOnclickLister mlister;
    public AssembleAdpater(Context context, List<CommodityTeamBuyData> datas) {
        super(context, datas, R.layout.item_assemble);
    }

    @Override
    protected void setData(RecycleViewHolder holder, CommodityTeamBuyData s, final int position) {

         ImageView iv_goods_image = holder.getItemView(R.id.iv_goods_image);
        TextView tv_name = holder.getItemView(R.id.tv_name);
        TextView tv_time = holder.getItemView(R.id.tv_time);
        TextView tv_now_pirce = holder.getItemView(R.id.tv_now_pirce);
        TextView tv_price = holder.getItemView(R.id.tv_price);

        if(!TextUtils.isEmpty(s.getCommodityName())){
            tv_name.setText(s.getCommodityName());
        }
        if(!TextUtils.isEmpty(s.getTeamBuyPrice())){
            tv_now_pirce.setText("¥ "+s.getTeamBuyPrice());
        }
        if(!TextUtils.isEmpty(s.getSalePrice())){
            tv_price.setText("¥ "+s.getSalePrice());
        }
        List<String> commodityImgList = s.getCommodityImgList();

        if (commodityImgList != null && commodityImgList.size() > 0){
            UIUtils.loadImageView(mContext,commodityImgList.get(0),iv_goods_image);
        }



        mview = holder.getItemView(R.id.view);
        daview = holder.getItemView(R.id.daview);
        ArrayList<String> imagetest = new ArrayList<>();

        List<TeamBuyMembers> teamBuyMembers = s.getTeamBuyMembers();

        for (int i = 0; i < teamBuyMembers.size(); i++) {
            TeamBuyMembers teamBuyMember = teamBuyMembers.get(i);
            String imagUrl = teamBuyMember.getMemberHeadurl();
            if (!imagUrl.startsWith("http")){
                imagUrl = Api.imageUrl + imagUrl;
            }
            imagetest.add(imagUrl);
            //最多8个
            if(i == 7){
                break;
            }
        }


        daview.initDatas(imagetest);
        if(position==0){
            mview.setVisibility(View.VISIBLE);
        }else{
            mview.setVisibility(View.GONE);
        }

        holder.getItemView(R.id.text_addassemble).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });


    }

    public void setListOnclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
