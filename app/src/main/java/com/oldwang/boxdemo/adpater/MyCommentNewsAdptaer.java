package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.MyCommentNewsBean;
import com.oldwang.boxdemo.bean.MyCommentVenueBean;
import com.oldwang.boxdemo.bean.NewsData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyCommentNewsAdptaer extends BaseRecycleAdapter<MyCommentNewsBean> {

    public ListOnclickLister mlister;


    HashMap<Integer,Boolean> clickdata = new HashMap<>();
    MyGridView mygridview;
    ImageView choice_image;


    TextView news_content;



    ImageView iv_head;
    TextView tv_name;

    TextView tv_content;

    public MyCommentNewsAdptaer(Context context, List<MyCommentNewsBean> datas) {
        super(context, datas, R.layout.item_comment);
    }

    @Override
    protected void setData(RecycleViewHolder holder, MyCommentNewsBean s, final int position) {
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


        holder.getItemView(R.id.news_detail_layout).setVisibility(View.VISIBLE);

        news_content = holder.getItemView(R.id.news_content);

        iv_head = holder.getItemView(R.id.iv_head);
        tv_name = holder.getItemView(R.id.tv_name);
        tv_content = holder.getItemView(R.id.tv_content);



        if(!TextUtils.isEmpty(s.getMemeberLogo())){
            UIUtils.loadImageView(mContext,s.getMemeberLogo(),iv_head);
        }

        if(!TextUtils.isEmpty(s.getMemeberName())){
            tv_name.setText(s.getMemeberName());
        }

        if(!TextUtils.isEmpty(s.getNewsCommentContent())){
            tv_content.setText(s.getNewsCommentContent());
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

    public List<MyCommentNewsBean> GetChoiceData(){
        List<MyCommentNewsBean> choicelist = new ArrayList<>();

        for(int i =0;i<mDatas.size();i++){
            if(clickdata.get(i)!=null&&clickdata.get(i)){
                choicelist.add(mDatas.get(i));
            }
        }
        return  choicelist;
    }

}
