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
import com.oldwang.boxdemo.bean.ScoreData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.DateTools;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyVideoAdpater extends BaseRecycleAdapter<ScoreData> {

    public ListOnclickLister mlister;
    private ImageView mChoiceimage;

    private static HashMap<Integer, Boolean> clickdata = new HashMap<>();

    public MyVideoAdpater(Context context, List<ScoreData> datas) {
        super(context, datas, R.layout.item_myvedio);
    }

    @Override
    protected void setData(RecycleViewHolder holder, ScoreData s, final int position) {


        ImageView iv_image = holder.getItemView(R.id.iv_image);
        TextView tv_name = holder.getItemView(R.id.tv_name);
        TextView tv_time = holder.getItemView(R.id.tv_time);
        TextView tv_play_count = holder.getItemView(R.id.tv_play_count);
        TextView tv_comment_count = holder.getItemView(R.id.tv_comment_count);
        TextView tv_good_count = holder.getItemView(R.id.tv_good_count);
        TextView tv_bad_count = holder.getItemView(R.id.tv_bad_count);

        LinearLayout ll_item = holder.getItemView(R.id.ll_item);

        if (!TextUtils.isEmpty(s.getPicUrl())){
            UIUtils.loadImageView(mContext,s.getPicUrl(),iv_image);
        }

        if (!TextUtils.isEmpty(s.getPublishTitle())){
            tv_name.setText(s.getPublishTitle());
        }
        if (!TextUtils.isEmpty(s.getCreateTime())){
            tv_time.setText("发布时间： "+ s.getCreateTime());
        }

        if (!TextUtils.isEmpty(s.getPlayCount())){
            tv_play_count.setText(s.getPlayCount());
        }
        if (!TextUtils.isEmpty(s.getRepayCount())){
            tv_comment_count.setText(s.getRepayCount());
        }

        if (!TextUtils.isEmpty(s.getBadCount())){
            tv_bad_count.setText(s.getBadCount());
        }
        if (!TextUtils.isEmpty(s.getGoodCount())){
            tv_good_count.setText(s.getGoodCount());
        }

            mChoiceimage = holder.getItemView(R.id.choice_image);
        if(clickdata.get(position)==null){
            clickdata.put(position,false);
        }
        if (clickdata.get(position)) {
            mChoiceimage.setImageResource(R.mipmap.addresslist_choice);
        } else {
            mChoiceimage.setImageResource(R.mipmap.addresslist_unchoice);
        }
        holder.getItemView(R.id.choice_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickdata.put(position, !clickdata.get(position));
                notifyDataSetChanged();
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });



        ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlister.onclick(view,position);
            }
        });

    }


    public void setAllDataClickType(boolean type) {
        for (int i = 0; i < mDatas.size(); i++) {
            clickdata.put(i, type);
        }
        notifyDataSetChanged();
    }


    public List<ScoreData> getClickdata() {
        List<ScoreData> listdata = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            if (clickdata.get(i)) {
                listdata.add(mDatas.get(i));
            }
        }

        return listdata;
    }

    public void setlistonclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }


    public boolean GetAllDataChoiceType(){
        for(int i=0;i<mDatas.size();i++){
            if(clickdata.get(i)==null||!clickdata.get(i)){
                return  false;
            }
        }
        return true;
    }

    public List<ScoreData> GetChoiceData(){
        List<ScoreData> choicelist = new ArrayList<>();

        for(int i =0;i<mDatas.size();i++){
            if(clickdata.get(i)){
                choicelist.add(mDatas.get(i));
            }
        }
        return  choicelist;
    }





}
