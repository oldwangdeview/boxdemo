package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class CommentImageAdpater extends BaseAdapter {
    Context mContext;
    List<String> listdata = new ArrayList<>();
    public CommentImageAdpater(Context mContext, List<String> listdata){
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
            convertView = UIUtils.inflate(mContext, R.layout.item_commentimage);
        }
        ImageView image = convertView.findViewById(R.id.image);
        if(!TextUtils.isEmpty(listdata.get(position))){
            UIUtils.loadImageView(mContext,listdata.get(position),image);
        }
        return convertView;
    }
}
