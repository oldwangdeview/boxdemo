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
import com.oldwang.boxdemo.bean.MyCommentGoodsBean;
import com.oldwang.boxdemo.bean.ScoreData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyCommentGoodsAdpater extends BaseRecycleAdapter<MyCommentGoodsBean> {

    public ListOnclickLister mlister;

     HashMap<Integer,Boolean> clickdata = new HashMap<>();
    MyGridView mygridview;
    ImageView choice_image;

    ImageView goods_image;
    TextView goods_name;
    TextView goods_price;

    MyGridView mygridview2;

    ImageView iv_head;
    TextView tv_name;

    TextView tv_content;

    public MyCommentGoodsAdpater(Context context, List<MyCommentGoodsBean> datas) {
        super(context, datas, R.layout.item_comment);
    }

    @Override
    protected void setData(RecycleViewHolder holder, MyCommentGoodsBean s, final int position) {

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



        holder.getItemView(R.id.goods_detail_layout).setVisibility(View.VISIBLE);

        goods_image = holder.getItemView(R.id.goods_image);
        goods_name = holder.getItemView(R.id.goods_name);
        goods_price = holder.getItemView(R.id.goods_price);
        mygridview2 = holder.getItemView(R.id.mygridview2);
        mygridview2.setVisibility(View.VISIBLE);
        iv_head = holder.getItemView(R.id.iv_head);
        tv_name = holder.getItemView(R.id.tv_name);
        tv_content = holder.getItemView(R.id.tv_content);




        if(s.getPicUrl()!=null&&s.getPicUrl().size()>0){
            CommentImageAdpater madpater = new CommentImageAdpater(mContext,s.getPicUrl());
            mygridview2.setAdapter(madpater);
        }

        if(!TextUtils.isEmpty(s.getMemberLogo())){
            UIUtils.loadImageView(mContext,s.getMemberLogo(),iv_head);
        }

        if(!TextUtils.isEmpty(s.getMemberName())){
            tv_name.setText(s.getMemberName());
        }

        if(!TextUtils.isEmpty(s.getRecommendContent())){
            tv_content.setText(s.getRecommendContent());
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



    public List<MyCommentGoodsBean> GetChoiceData(){
        List<MyCommentGoodsBean> choicelist = new ArrayList<>();

        for(int i =0;i<mDatas.size();i++){
            if(clickdata.get(i)!=null&&clickdata.get(i)){
                choicelist.add(mDatas.get(i));
            }
        }
        return  choicelist;
    }


}
