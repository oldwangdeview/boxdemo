package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.ReportActivity;
import com.oldwang.boxdemo.activity.SubscribePlaceActivity;
import com.oldwang.boxdemo.activity.VenusPriseActivity;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.ScoreData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.PhotoUtils;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.List;

public class AppoinmentAdpater extends BaseRecycleAdapter<ScoreData> {

    private int type;
    TextView type_text;
    LinearLayout bottom_layout;

    public ListOnclickLister mlister;


    public AppoinmentAdpater(Context context, List<ScoreData> datas,int type) {
        super(context, datas, R.layout.item_appointment);
        this.type = type;
    }

    @Override
    protected void setData(RecycleViewHolder holder, ScoreData s, final int position) {
        type_text = holder.getItemView(R.id.type_text);
        bottom_layout = holder.getItemView(R.id.bottom_layout);
        bottom_layout.setVisibility(View.GONE);
       LinearLayout ll_item = holder.getItemView(R.id.ll_item);

        ImageView iv_image = holder.getItemView(R.id.iv_image);
        TextView tv_venueName = holder.getItemView(R.id.tv_venueName);
        TextView tv_venueProjectName = holder.getItemView(R.id.tv_venueProjectName);
        TextView tv_time = holder.getItemView(R.id.tv_time);
        TextView tv_report = holder.getItemView(R.id.tv_report);
        TextView tv_comment = holder.getItemView(R.id.tv_comment);


        if (!TextUtils.isEmpty(s.getVenuePicUrl())){
            UIUtils.loadImageView(mContext,s.getVenuePicUrl(),iv_image);
        }

        if (!TextUtils.isEmpty(s.getVenueName())){
            tv_venueName.setText(s.getVenueName());
        }
        if (!TextUtils.isEmpty(s.getVenueProjectName())){
            tv_venueProjectName.setText(s.getVenueProjectName());
        }

        String time = "";

        if (!TextUtils.isEmpty(s.getBookingBeginTime())){
            time = s.getBookingBeginTime();
        }
        if (!TextUtils.isEmpty(s.getBookingEndTime())){
            time += "-"+s.getBookingEndTime();
        }
        tv_time.setText(time);


        if (!TextUtils.isEmpty(s.getIsComment()) && s.getIsComment().equals("1")){
            tv_comment.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(s.getIsComplaint()) && s.getIsComplaint().equals("1")){
            tv_report.setVisibility(View.GONE);
        }

        /**举报**/
        tv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportActivity.startactivity(mContext, s.getVenueId(),0);
            }
        });

        /**评价**/
        tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VenusPriseActivity.startactivity(mContext,s.getBookingId(),s.getVenueId());
            }
        });

        //预约状态(0:预约中,1:预约成功,-1预约失败,3完成)
        switch (type){
            case 0:
                type_text.setText("商家确认中");
                type_text.setTextColor(mContext.getResources().getColor(R.color.c_FDA100));
                break;
            case 1:
                type_text.setText("预约成功");
                type_text.setTextColor(mContext.getResources().getColor(R.color.c_159C3B));
                bottom_layout.setVisibility(View.VISIBLE);
                break;
            case -1:
                type_text.setText("预约失败");
                type_text.setTextColor(mContext.getResources().getColor(R.color.c_d52e21));
                break;
            case 3:
                type_text.setText("已完成");
                type_text.setTextColor(mContext.getResources().getColor(R.color.c_159C3B));
                break;
            case -3:
                type_text.setText("已失效");
                type_text.setTextColor(mContext.getResources().getColor(R.color.c_ff999999));
                break;
        }


        ll_item.setOnClickListener(new View.OnClickListener() {
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
