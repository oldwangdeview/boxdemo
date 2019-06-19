package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.NewsData;
import com.oldwang.boxdemo.bean.VenueCommentData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class BaseDetail_Bottom_gridviewAdpater3 extends BaseRecycleAdapter<VenueCommentData> {

    ListOnclickLister mlister;


    public BaseDetail_Bottom_gridviewAdpater3(Context context, List<VenueCommentData> datas) {
        super(context, datas, R.layout.item_basedetail_evaluate);
    }

    @Override
    protected void setData(RecycleViewHolder holder, VenueCommentData venueCommentData, int position) {


        ImageView iv_image = holder.getItemView(R.id.iv_image);
        TextView tv_name = holder.getItemView(R.id.tv_name);
        TextView tv_content = holder.getItemView(R.id.tv_content);
        TextView tv_time = holder.getItemView(R.id.tv_time);

        ImageView image_1 = holder.getItemView(R.id.image_1);
        ImageView image_2 = holder.getItemView(R.id.image_2);
        ImageView image_3 = holder.getItemView(R.id.image_3);
        ImageView image_4 = holder.getItemView(R.id.image_4);
        ImageView image_5 = holder.getItemView(R.id.image_5);

        if (!TextUtils.isEmpty(venueCommentData.getMemberHeadLogo())){
            UIUtils.loadImageView(mContext,venueCommentData.getMemberHeadLogo(),iv_image);
        }

        if (!TextUtils.isEmpty(venueCommentData.getMemberName())){
            tv_name.setText(venueCommentData.getMemberName());
        }


        if (!TextUtils.isEmpty(venueCommentData.getCommentContent())){
            tv_content.setText(venueCommentData.getCommentContent());
        }
        if (!TextUtils.isEmpty(venueCommentData.getCommentTime())){
            tv_time.setText(venueCommentData.getCommentTime());
        }


        String venueStar = venueCommentData.getCommentGrade();

        if (TextUtils.isEmpty(venueStar)){
            venueStar = "0.0";
        }

        double star = Double.valueOf(venueStar);
        if (star < 1){
            image_1.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing));
            image_2.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing));
            image_3.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing));
            image_4.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing));
            image_5.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing));

        }else if (star < 2){
            image_1.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing2));
            image_2.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing));
            image_3.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing));
            image_4.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing));
            image_5.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing));
        }else if (star < 3){
            image_1.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing2));
            image_2.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing2));
            image_3.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing));
            image_4.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing));
            image_5.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing));
        }else if (star < 4){
            image_1.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing2));
            image_2.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing2));
            image_3.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing2));
            image_4.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing));
            image_5.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing));
        }else if (star < 5){
            image_1.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing2));
            image_2.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing2));
            image_3.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing2));
            image_4.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing2));
            image_5.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing));
        }else if (star < 6){
            image_1.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing2));
            image_2.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing2));
            image_3.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing2));
            image_4.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing2));
            image_5.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.xingxing2));
        }
    }

    public void setlistonclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }

}
