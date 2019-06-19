package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.bean.MasterData;
import com.oldwang.boxdemo.bean.MasterDataDTO;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeMasterAdpater extends BaseAdapter {

    Context mContext;
    List<MasterData> listdata = new ArrayList<>();
    ListOnclickLister mlister;
    public HomeMasterAdpater(Context mContext, List<MasterData> listdata){
        this.mContext = mContext;
        this.listdata = listdata;
    }
    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = UIUtils.inflate(mContext, R.layout.item_homegrideadpater);
        }
        TextView textd  =  view.findViewById(R.id.text);
        if(!TextUtils.isEmpty(listdata.get(i).getNewsMasterName())){
            textd.setText(listdata.get(i).getNewsMasterName());
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mlister!=null){
                    mlister.onclick(view,i);
                }
            }
        });
        return view;
    }

    public void setListonclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
