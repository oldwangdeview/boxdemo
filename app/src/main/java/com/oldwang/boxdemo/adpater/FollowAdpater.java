package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.MemberRelationData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.List;

public class FollowAdpater extends BaseRecycleAdapter<MemberRelationData> {



    private ListOnclickLister listOnclickLister;

    public void setListOnclickLister(ListOnclickLister listOnclickLister) {
        this.listOnclickLister = listOnclickLister;
    }

    public FollowAdpater(Context context, List<MemberRelationData> datas) {
        super(context, datas, R.layout.item_followuser);
    }

    @Override
    protected void setData(RecycleViewHolder holder, MemberRelationData s, final int position) {


        ImageView headImageView = holder.getItemView(R.id.iv_head);
        TextView tv_name = holder.getItemView(R.id.tv_name);
        TextView tv_attention = holder.getItemView(R.id.tv_attention);
        TextView btn_chat = holder.getItemView(R.id.btn_chat);

        //关注状态（0-我关注的 1-被关注的 2-互相关注）
        String relationState = s.getRelationState();

        if (!TextUtils.isEmpty(relationState) && relationState.equals("2")){
            btn_chat.setVisibility(View.VISIBLE);
        }else {
            btn_chat.setVisibility(View.GONE);
        }

        if (s.getMemberInfo() != null){
            if (!TextUtils.isEmpty(s.getMemberInfo().getMemberHeadurl())){
                UIUtils.loadImageView(mContext,s.getMemberInfo().getMemberHeadurl(),headImageView);
            }
            if (!TextUtils.isEmpty(s.getMemberInfo().getMemberNickname())){
                tv_name.setText(s.getMemberInfo().getMemberNickname());
            }
        }

        if (!s.isNotAttention()){
            tv_attention.setText("已关注");
            tv_attention.setTextColor(Color.parseColor("#FF8C8C8C"));
        }else {
            tv_attention.setTextColor(Color.parseColor("#FFD52E21"));
            tv_attention.setText("+ 关注");
        }

        tv_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOnclickLister.onclick(view,position);
            }
        });
        btn_chat.setOnClickListener(new View.OnClickListener() {
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
}
