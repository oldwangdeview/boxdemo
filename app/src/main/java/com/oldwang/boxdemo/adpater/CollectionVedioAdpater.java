package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.ScoreData;
import com.oldwang.boxdemo.event.CollectCheckEvent;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CollectionVedioAdpater extends BaseRecycleAdapter<ScoreData> {
    ListOnclickLister listOnclickLister;

    ImageView choice_image;
    public CollectionVedioAdpater(Context context, List<ScoreData> datas) {
        super(context, datas, R.layout.item_collectionvideo);
    }

    @Override
    protected void setData(RecycleViewHolder holder, final ScoreData s, final int position) {
        choice_image= holder.getItemView(R.id.choice_image);
        if (s.isCheck()) {
            choice_image.setImageResource(R.mipmap.addresslist_choice);
        }else{
            choice_image.setImageResource(R.mipmap.addresslist_unchoice);
        }
        choice_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.setCheck(!s.isCheck());

                boolean temp = true;
                for (ScoreData mData : mDatas) {

                    if (!mData.isCheck()){
                        temp = false;
                        break;
                    }
                }
                //子条目是否全部选中
                EventBus.getDefault().post(new CollectCheckEvent(0,temp));
                notifyDataSetChanged();
            }
        });

        ImageView iv_image = holder.getItemView(R.id.iv_head);
        TextView tv_name = holder.getItemView(R.id.tv_name);
        ImageView iv_member_head = holder.getItemView(R.id.iv_member_head);
        TextView tv_member_name = holder.getItemView(R.id.tv_member_name);

        if (!TextUtils.isEmpty(s.getParam3())){
            UIUtils.loadImageView(mContext,s.getParam3(),iv_image);
        }

        if (!TextUtils.isEmpty(s.getFavoriteName())) {
            tv_name.setText(s.getFavoriteName());
        }
        if (!TextUtils.isEmpty(s.getParam1())) {
            UIUtils.loadImageView(mContext,s.getParam1(),iv_member_head);
        }

        if (!TextUtils.isEmpty(s.getParam2())) {
            tv_member_name.setText(s.getParam2());
        }
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOnclickLister.onclick(view,position);
            }
        });
    }

    public void setAddDatetype(boolean isCheck){
        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).setCheck(isCheck);
        }
        notifyDataSetChanged();
    }

    public void DeletAllData(){
        Iterator<ScoreData> iterator = mDatas.iterator();

        while (iterator.hasNext()) {
            ScoreData value = iterator.next();
            if (value.isCheck()) {
                iterator.remove();
            }
        }
        notifyDataSetChanged();

    }

    public void setListOnclickLister(ListOnclickLister listOnclickLister) {
        this.listOnclickLister = listOnclickLister;
    }
}
