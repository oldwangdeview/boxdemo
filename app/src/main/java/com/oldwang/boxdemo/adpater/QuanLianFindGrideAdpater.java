package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.AssembleListQuanlianActivity;
import com.oldwang.boxdemo.activity.GoodsDetailActivity;
import com.oldwang.boxdemo.bean.CommodityTypeData;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class QuanLianFindGrideAdpater extends BaseAdapter {
    Context mContext;
    List<CommodityTypeData> listdata = new ArrayList<>();
    private ListOnclickLister mlister;
    public QuanLianFindGrideAdpater(Context mContext, List<CommodityTypeData> listdata) {
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
        ViewHolder holder;

        if (convertView == null) {
            convertView = UIUtils.inflate(mContext, R.layout.item_quanlianfinddata_gride);
            holder = new ViewHolder();
            holder.iv_image = (ImageView) convertView
                    .findViewById(R.id.iv_image);
            holder.name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            holder.ll_item = (LinearLayout) convertView
                    .findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CommodityTypeData commodityTypeData = listdata.get(position);

        if (!TextUtils.isEmpty(commodityTypeData.getCommodityTypeName())) {
            holder.name.setText(commodityTypeData.getCommodityTypeName());
        }
        if (!TextUtils.isEmpty(commodityTypeData.getCommodityTypeImg())) {
            UIUtils.loadImageView(mContext, commodityTypeData.getCommodityTypeImg(), holder.iv_image);
        }

        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AssembleListQuanlianActivity.startactivity(mContext);
                if(mlister!=null){
                    mlister.onclick(view,position);
                }
            }
        });

        return convertView;
    }

    private class ViewHolder {
        LinearLayout ll_item;
        ImageView iv_image;
        TextView name;

    }


    public void setListClicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
