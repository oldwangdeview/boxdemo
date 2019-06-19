package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.CommentRepayInfo;
import com.oldwang.boxdemo.bean.NewsData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.DateTools;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.List;

public class VideoCommentAdpater extends BaseRecycleAdapter<NewsData> {
    ListOnclickLister mlister;


    public VideoCommentAdpater(Context context, List<NewsData> datas) {
        super(context, datas, R.layout.item_newscomment);
    }

    @Override
    protected void setData(RecycleViewHolder holder, NewsData s, final int position) {


        ImageView iv_head = holder.getItemView(R.id.iv_head);
        TextView tv_name = holder.getItemView(R.id.tv_name);
        TextView tv_time = holder.getItemView(R.id.tv_time);
        TextView tv_comment_count = holder.getItemView(R.id.tv_comment_count);
        TextView tv_good_count = holder.getItemView(R.id.tv_good_count);
        TextView tv_content = holder.getItemView(R.id.tv_content);

        ImageView tv_zan = holder.getItemView(R.id.tv_zan);

        if (!TextUtils.isEmpty(s.getIsPraise()) && s.getIsPraise().equals("0")){
            tv_zan.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.alreay_prise_icon));
        }else {
            tv_zan.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.news_images_zan));
        }

        LinearLayout ll_comment = holder.getItemView(R.id.ll_comment);
        LinearLayout ll_prise = holder.getItemView(R.id.ll_prise);

        LinearLayout ll_repay = holder.getItemView(R.id.ll_repay);
        TextView tv_repay_content = holder.getItemView(R.id.tv_repay_content);
        TextView tv_total_repay = holder.getItemView(R.id.tv_total_repay);

        List<CommentRepayInfo> boxingVideoCommentRepay = s.getBoxingVideoCommentRepay();

        CommentRepayInfo commentRepayInfo = null;
        if (boxingVideoCommentRepay != null && boxingVideoCommentRepay.size() > 0){
            commentRepayInfo = boxingVideoCommentRepay.get(0);
        }

        if (commentRepayInfo != null ){
            String contnet = "";

            if (!TextUtils.isEmpty(commentRepayInfo.getRepayMember())){
                contnet = commentRepayInfo.getRepayMember() +": ";
            }
            if (!TextUtils.isEmpty(commentRepayInfo.getBoxingVideoCommentRepayDetail())){
                contnet += commentRepayInfo.getBoxingVideoCommentRepayDetail();
            }
            tv_repay_content.setText(contnet);

            tv_total_repay.setText("全部"+boxingVideoCommentRepay.size()+"条回复 >");
            tv_comment_count.setText(boxingVideoCommentRepay.size()+"");


            ll_repay.setVisibility(View.VISIBLE);
        }else {
            ll_repay.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(s.getMemberHeadLogo())){
            UIUtils.loadImageView(mContext,s.getMemberHeadLogo(),iv_head);
        }


        if (!TextUtils.isEmpty(s.getMemberName())){
            tv_name.setText(s.getMemberName());
        }

        if (!TextUtils.isEmpty(s.getCreateTime())){
            tv_time.setText(s.getCreateTime());
        }


        if (!TextUtils.isEmpty(s.getBoxingVideoCommentUpCount())){
            tv_good_count.setText(s.getBoxingVideoCommentUpCount());
        }

        if (!TextUtils.isEmpty(s.getBoxingVideoCommentDetail())){
            tv_content.setText(s.getBoxingVideoCommentDetail());
        }


        ll_repay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlister.onclick(view,position);
            }
        });

        ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlister.onclick(view,position);
            }
        });

        ll_prise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlister.onclick(view,position);
            }
        });
    }




    public void setlistonclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
