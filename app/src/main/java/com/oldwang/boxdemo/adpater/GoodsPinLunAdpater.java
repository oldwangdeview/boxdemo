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
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.CommentChilds;
import com.oldwang.boxdemo.bean.CommodityChildCommentData;
import com.oldwang.boxdemo.util.DateTools;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

public class GoodsPinLunAdpater extends BaseAdapter {
    Context mContext;
    List<CommodityChildCommentData> listdata;

    public GoodsPinLunAdpater(Context mCOntext, List<CommodityChildCommentData> listdata) {
        this.mContext = mCOntext;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = UIUtils.inflate(mContext, R.layout.item_comment);

        }
        ImageView iv_head = convertView.findViewById(R.id.iv_head);
        TextView tv_name = convertView.findViewById(R.id.tv_name);
        TextView tv_attributeName = convertView.findViewById(R.id.tv_attributeName);
        TextView tv_content = convertView.findViewById(R.id.tv_content);
        LinearLayout news_detail_layout = convertView.findViewById(R.id.news_detail_layout);
        TextView news_content = convertView.findViewById(R.id.news_content);


        CommodityChildCommentData commodityChildCommentData = listdata.get(position);

        if (commodityChildCommentData != null) {

             List<CommentChilds> childs = commodityChildCommentData.getChilds();
            if (childs != null && childs.size() > 0){
                CommentChilds commentChilds = childs.get(0);
                if (commentChilds != null && !TextUtils.isEmpty(commentChilds.getRecommendContent())){
                    news_content.setText("平台回复："+commentChilds.getRecommendContent());
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
                MyGridView mygridview = convertView.findViewById(R.id.mygridview2);
                CommentImageAdpater madapter = new CommentImageAdpater(mContext, commodityUrl);
                mygridview.setAdapter(madapter);
            }
        }


        convertView.findViewById(R.id.choice_image).setVisibility(View.GONE);

        return convertView;
    }
}
