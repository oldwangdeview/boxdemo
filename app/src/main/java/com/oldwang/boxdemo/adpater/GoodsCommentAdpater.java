package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.CommentChilds;
import com.oldwang.boxdemo.bean.CommodityChildCommentData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.util.DateTools;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

public class GoodsCommentAdpater extends BaseRecycleAdapter<CommodityChildCommentData> {

    MyGridView myGridView;

    public GoodsCommentAdpater(Context context, List<CommodityChildCommentData> datas) {
        super(context, datas, R.layout.item_comment);
    }

    @Override
    protected void setData(RecycleViewHolder holder, CommodityChildCommentData commodityChildCommentData, int position) {

        ImageView iv_head = holder.getItemView(R.id.iv_head);
        TextView tv_name = holder.getItemView(R.id.tv_name);
        TextView tv_attributeName = holder.getItemView(R.id.tv_attributeName);
        TextView tv_content = holder.getItemView(R.id.tv_content);
        LinearLayout news_detail_layout = holder.getItemView(R.id.news_detail_layout);
        TextView news_content = holder.getItemView(R.id.news_content);


        if (commodityChildCommentData != null) {
            List<CommentChilds> childs = commodityChildCommentData.getChilds();
            if (childs != null && childs.size() > 0){
                CommentChilds commentChilds = childs.get(0);
                if (commentChilds != null && !TextUtils.isEmpty(commentChilds.getRecommendContent())){

                    String recommendPerson = commentChilds.getRecommendPerson();
                    if (TextUtils.isEmpty(recommendPerson)){
                        recommendPerson = "平台回复：";
                    }else{
                        recommendPerson  += "：";
                    }


                    news_content.setText(recommendPerson+commentChilds.getRecommendContent());
                }

                news_detail_layout.setVisibility(View.VISIBLE);
            }else {
                news_detail_layout.setVisibility(View.GONE);
            }


            if (!TextUtils.isEmpty(commodityChildCommentData.getMemberLogo())) {
                UIUtils.loadImageView(mContext, commodityChildCommentData.getMemberLogo(), iv_head);
            }
            if (!TextUtils.isEmpty(commodityChildCommentData.getMemberNickname())) {
                tv_name.setText(commodityChildCommentData.getMemberNickname());
            }

            String name = "";

            if (!TextUtils.isEmpty(commodityChildCommentData.getCreateTime())) {
                name = DateTools.getFormat(Long.parseLong(commodityChildCommentData.getCreateTime())) +"     ";
            }
            if (!TextUtils.isEmpty(commodityChildCommentData.getAttributeName())) {
                name += commodityChildCommentData.getAttributeName();
            }
            tv_attributeName.setText(name);


            if (!TextUtils.isEmpty(commodityChildCommentData.getRecommendContent())) {
                tv_content.setText(commodityChildCommentData.getRecommendContent());
            }

            List<String> commodityUrl = commodityChildCommentData.getCommodityUrl();

            if (commodityUrl != null){
                MyGridView mygridview = holder.getItemView(R.id.mygridview2);
                CommentImageAdpater madapter = new CommentImageAdpater(mContext, commodityUrl);
                mygridview.setAdapter(madapter);
            }
        }
        

        
        holder.getItemView(R.id.choice_image).setVisibility(View.GONE);



    }
}
