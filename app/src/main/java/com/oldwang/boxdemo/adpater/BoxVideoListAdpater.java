package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.MemberInfo;
import com.oldwang.boxdemo.bean.VideoData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.List;

public class BoxVideoListAdpater extends BaseRecycleAdapter<VideoData> {

    private ListOnclickLister listOnclickLister;

    public BoxVideoListAdpater(Context context, List<VideoData> datas) {
        super(context, datas, R.layout.item_boxvideo);
    }

    @Override
    protected void setData(RecycleViewHolder holder, VideoData s, final int position) {

        ImageView iv_image  = holder.getItemView(R.id.iv_image);
        ImageView iv_member_head =  holder.getItemView(R.id.iv_member_head);
        TextView tv_name =  holder.getItemView(R.id.tv_name);
        TextView tv_member_name =  holder.getItemView(R.id.tv_member_name);
        TextView tv_follow =  holder.getItemView(R.id.tv_follow);
        ImageView iv_follow =  holder.getItemView(R.id.iv_follow);
        TextView tv_play_count =  holder.getItemView(R.id.tv_play_count);
        TextView tv_comment_count =  holder.getItemView(R.id.tv_comment_count);
        TextView tv_collect_count =  holder.getItemView(R.id.tv_collect_count);


        if (!TextUtils.isEmpty(s.getBoxingVideoImg())){
            UIUtils.loadImageView(mContext,s.getBoxingVideoImg(),iv_image);
        }

        if (!TextUtils.isEmpty(s.getIsAttention()) && s.getIsAttention().equals("1")){
            tv_follow.setText("已关注");
            tv_follow.setTextColor(Color.parseColor("#FF8C8C8C"));
            iv_follow.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.boxvideo_gz));
        }else {
            tv_follow.setTextColor(Color.parseColor("#FFD52E21"));
            tv_follow.setText("关注");
            iv_follow.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_add));
        }

        MemberInfo memberInfo = s.getMemberInfo();
        if (memberInfo != null){
            if (!TextUtils.isEmpty(memberInfo.getMemberHeadLogo())){
                UIUtils.loadImageView(mContext, memberInfo.getMemberHeadLogo(),iv_member_head);
            }
            if (!TextUtils.isEmpty(memberInfo.getMemberNickname())){
                tv_member_name.setText(memberInfo.getMemberNickname());
            }
        }

        if (!TextUtils.isEmpty(s.getBoxingVideoTitle())){
            tv_name.setText(s.getBoxingVideoTitle());
        }
        if (!TextUtils.isEmpty(s.getBoxingVideoPlayCount())){
            tv_play_count.setText(s.getBoxingVideoPlayCount());
        }
        if (!TextUtils.isEmpty(s.getBoxingVideoCommentCount())){
            tv_comment_count.setText(s.getBoxingVideoCommentCount());
        }
        if (!TextUtils.isEmpty(s.getBoxingFavoriteCount())){
            tv_collect_count.setText(s.getBoxingFavoriteCount());
        }

        tv_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOnclickLister.onclick(view,position);
            }
        });

        holder.getItemView(R.id.ll_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOnclickLister.onclick(view,position);
            }
        });
    }

    public void setListOnclickLister(ListOnclickLister listOnclickLister) {
        this.listOnclickLister = listOnclickLister;
    }
}
