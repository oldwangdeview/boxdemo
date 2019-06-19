package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.bean.ServiceData;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class BaseDetail_Bottom_gridviewAdpater1 extends BaseAdapter {
    Context mContext;
    List<ServiceData> listdat = new ArrayList<>();

    public BaseDetail_Bottom_gridviewAdpater1(Context mCOontet,List<ServiceData> listdata){
        this.mContext = mCOontet;
        this.listdat = listdata;

    }
    @Override
    public int getCount() {
        return listdat.size();
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
            convertView = UIUtils.inflate(mContext, R.layout.item_other3);
        }

        ServiceData serviceData = listdat.get(position);

        TextView text1 = convertView.findViewById(R.id.text1);
        TextView text2 = convertView.findViewById(R.id.text2);
        TextView text3 = convertView.findViewById(R.id.text3);
        View mview = convertView.findViewById(R.id.view);

        if (serviceData != null){

            if (!TextUtils.isEmpty(serviceData.getVenueProjectName())){
                text1.setText(serviceData.getVenueProjectName());
            }
            if (!TextUtils.isEmpty(serviceData.getVenueProjectPrice())){
                text2.setText(serviceData.getVenueProjectPrice());
            }
            if (!TextUtils.isEmpty(serviceData.getDiscount())){
                text3.setText(serviceData.getDiscount());
            }
        }

        if(listdat.size()-1==position){
            mview.setVisibility(View.GONE);
        }else{
            mview.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
