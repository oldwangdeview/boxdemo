package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.MyCommentVenueBean;
import com.oldwang.boxdemo.bean.NewsData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.LogUntil;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyCommentVideoAdpater extends BaseRecycleAdapter<NewsData> {

    public ListOnclickLister mlister;


     HashMap<Integer,Boolean> clickdata = new HashMap<>();

    ImageView choice_image;


    ImageView video_image;
    TextView video_content;



    ImageView iv_head;
    TextView tv_name;

    TextView tv_content;

    public MyCommentVideoAdpater(Context context, List<NewsData> datas) {
        super(context, datas, R.layout.item_comment);
    }

    @Override
    protected void setData(RecycleViewHolder holder, NewsData s, final int position) {
        choice_image = holder.getItemView(R.id.choice_image);
        if(clickdata.get(position)==null){
            clickdata.put(position,false);
        }
        if(clickdata.get(position)){
            choice_image.setImageResource(R.mipmap.addresslist_choice);
        }else{
            choice_image.setImageResource(R.mipmap.addresslist_unchoice);
        }
        choice_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickdata.put(position,!clickdata.get(position));
                notifyDataSetChanged();
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });


        holder.getItemView(R.id.venue_detail_layout).setVisibility(View.VISIBLE);
        video_image = holder.getItemView(R.id.video_image);
        video_content = holder.getItemView(R.id.video_content);

        iv_head = holder.getItemView(R.id.iv_head);
        tv_name = holder.getItemView(R.id.tv_name);
        tv_content = holder.getItemView(R.id.tv_content);



        if(!TextUtils.isEmpty(s.getMemberHeadLogo())){
            UIUtils.loadImageView(mContext,s.getMemberHeadLogo(),iv_head);
        }

        if(!TextUtils.isEmpty(s.getMemberName())){
            tv_name.setText(s.getMemberName());
        }

        if(!TextUtils.isEmpty(s.getBoxingVideoPic())){
            UIUtils.loadImageView(mContext,s.getBoxingVideoPic(),video_image);
        }


        if(!TextUtils.isEmpty(s.getBoxingVideoCommentDetail())){
            tv_content.setText(s.getBoxingVideoCommentDetail());
        }





    }



    public void setAddDataType(boolean type){
        for(int i =0;i<mDatas.size();i++){
            clickdata.put(i,type);
        }
        notifyDataSetChanged();
    }
    public void setlistonclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }


    public boolean GetAlldataChoiceType(){

        for (int i = 0; i < mDatas.size(); i++) {
            if(clickdata.get(i)==null||!clickdata.get(i)){
                return false;
            }
        }

        return true;
    }

    public List<NewsData> GetChoiceData(){
        List<NewsData> choicelist = new ArrayList<>();

        for(int i =0;i<mDatas.size();i++){
            if(clickdata.get(i)!=null&&clickdata.get(i)){
                choicelist.add(mDatas.get(i));
            }
        }
        return  choicelist;
    }



}
