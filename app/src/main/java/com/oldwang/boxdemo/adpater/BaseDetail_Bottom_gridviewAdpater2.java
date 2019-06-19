package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class BaseDetail_Bottom_gridviewAdpater2 extends BaseAdapter {

    Context mContext;
    List<String> listdat = new ArrayList<>();
    public BaseDetail_Bottom_gridviewAdpater2(Context mContext, List<String> listdata){

        this.mContext = mContext;
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
            convertView = UIUtils.inflate(mContext, R.layout.item_basedetailmessage);
        }

        ImageView image = convertView.findViewById(R.id.image);
        TextView text = convertView.findViewById(R.id.text);


        if(!TextUtils.isEmpty(listdat.get(position))){
            text.setText(listdat.get(position));
            int baseDetailImage = UIUtils.getBaseDetailImage(listdat.get(position));
            if (baseDetailImage > 0){
                image.setVisibility(View.VISIBLE);
                image.setImageResource(baseDetailImage);
            }else {
                image.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}
