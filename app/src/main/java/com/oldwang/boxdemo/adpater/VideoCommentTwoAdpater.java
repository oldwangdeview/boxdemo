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

public class VideoCommentTwoAdpater extends BaseRecycleAdapter<NewsData> {
    ListOnclickLister mlister;


    public VideoCommentTwoAdpater(Context context, List<NewsData> datas) {
        super(context, datas, R.layout.item_newscomment);
    }

    @Override
    protected void setData(RecycleViewHolder holder, NewsData s, final int position) {


        ImageView iv_head = holder.getItemView(R.id.iv_head);
        TextView tv_name = holder.getItemView(R.id.tv_name);
        TextView tv_time = holder.getItemView(R.id.tv_time);
        TextView tv_content = holder.getItemView(R.id.tv_content);

        ImageView tv_zan = holder.getItemView(R.id.tv_zan);
        tv_zan.setVisibility(View.GONE);


        LinearLayout ll_comment = holder.getItemView(R.id.ll_comment);
        LinearLayout ll_prise = holder.getItemView(R.id.ll_prise);
        LinearLayout ll_repay = holder.getItemView(R.id.ll_repay);

        ll_repay.setVisibility(View.GONE);
        ll_prise.setVisibility(View.GONE);
        ll_comment.setVisibility(View.GONE);


        if (!TextUtils.isEmpty(s.getMemberHeadLogo())){
            UIUtils.loadImageView(mContext,s.getMemberHeadLogo(),iv_head);
        }


        if (!TextUtils.isEmpty(s.getMemberName())){
            tv_name.setText(s.getMemberName());
        }

        if (!TextUtils.isEmpty(s.getCreateTime())){
            if (!s.getCreateTime().contains(":")){
                tv_time.setText(DateTools.getFormat(Long.parseLong(s.getCreateTime())));
            }else {
                tv_time.setText(s.getCreateTime());
            }
        }


        if (!TextUtils.isEmpty(s.getBoxingVideoCommentRepayDetail())){
            tv_content.setText(s.getBoxingVideoCommentRepayDetail());
        }else {
            if (!TextUtils.isEmpty(s.getNewsCommentRepayDetail())){
                tv_content.setText(s.getNewsCommentRepayDetail());
            }
        }


    }




    public void setlistonclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
