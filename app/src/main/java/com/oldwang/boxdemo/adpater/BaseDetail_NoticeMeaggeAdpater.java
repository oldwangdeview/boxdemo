package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.bean.NoticeData;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class BaseDetail_NoticeMeaggeAdpater extends BaseAdapter {



    List<NoticeData> listdata = new ArrayList<>();
    Context mContext;
    public BaseDetail_NoticeMeaggeAdpater(Context mContext, List<NoticeData> listdata){
        this.mContext = mContext;
        this.listdata = listdata;
    }
    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView=UIUtils.inflate(mContext, R.layout.item_basedetail_noticedetail);
        }
        View mview = convertView.findViewById(R.id.view);
        TextView tv_name = convertView.findViewById(R.id.tv_name);
        TextView tv_time = convertView.findViewById(R.id.tv_time);

        NoticeData noticeData = listdata.get(position);

        if (!TextUtils.isEmpty(noticeData.getNoticeTitle())){
            tv_name.setText(noticeData.getNoticeTitle());
        }
        if (!TextUtils.isEmpty(noticeData.getNoticeReleaseTime())){
            tv_time.setText(noticeData.getNoticeReleaseTime());
        }


        if(position==listdata.size()-1){
            mview.setVisibility(View.GONE);
        }else {
            mview.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
