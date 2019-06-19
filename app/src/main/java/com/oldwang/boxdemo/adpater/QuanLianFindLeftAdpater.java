package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.bean.CommodityTypeData;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuanLianFindLeftAdpater extends BaseAdapter {
    List<CommodityTypeData> listdata = new ArrayList<>();
    Context mContext;
    ListOnclickLister mlister;
    public QuanLianFindLeftAdpater(Context mContext, List<CommodityTypeData> listdata){

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=UIUtils.inflate(mContext, R.layout.item_quanlianfindleft);
        }
        TextView text = convertView.findViewById(R.id.text);

        CommodityTypeData commodityTypeData = listdata.get(position);

        if (!TextUtils.isEmpty(commodityTypeData.getCommodityTypeName())){
            text.setText(commodityTypeData.getCommodityTypeName());
        }

        if(commodityTypeData.isChoose()){
            text.setTextColor(mContext.getResources().getColor(R.color.c_d52e21));
            text .setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            convertView.setBackgroundResource(R.color.c_ffffff);

        }else{
            text.setTextColor(mContext.getResources().getColor(R.color.c_262626));
            text.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            convertView.setBackgroundResource(R.color.c_F8F8F8);
        }
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });
        return convertView;
    }


    public void SetSeletePosition(int position){
        for(int i =0;i<listdata.size();i++){
            if (i == position){
                listdata.get(i).setChoose(true);
            }else {
                listdata.get(i).setChoose(false);
            }
        }
        notifyDataSetChanged();
    }

    public void setListClickLister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
