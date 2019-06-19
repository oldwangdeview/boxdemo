package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.CommodityTypeData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;

import java.util.HashMap;
import java.util.List;

public class QuanLianTitleAdpater extends BaseRecycleAdapter<CommodityTypeData> {
    TextView text;
    View view;
    ListOnclickLister mlister;
    static HashMap<Integer,Boolean> clickdata = new HashMap<>();
    public QuanLianTitleAdpater(Context context, List<CommodityTypeData> datas) {
        super(context, datas, R.layout.item_quanlian_title);
    }

    @Override
    protected void setData(RecycleViewHolder holder, CommodityTypeData s, final int position) {

        text = holder.getItemView(R.id.text);
        view = holder.getItemView(R.id.view);

        if(clickdata.get(position)){
            text.setTextColor(mContext.getResources().getColor(R.color.c_d52e21));
            view.setVisibility(View.VISIBLE);
        }else{
            text.setTextColor(mContext.getResources().getColor(R.color.c_525259));
            view.setVisibility(View.INVISIBLE);
        }
        text.setText(s.getCommodityTypeName());

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickPosition(position);
                if(mlister!=null){
                    mlister.onclick(v,position);

                }
            }
        });
    }

    public void setAlldataFalse(boolean type){
        for(int i =0;i<mDatas.size();i++){
            clickdata.put(i,false);
        }
        if(type){
            clickdata.put(0,true);
        }
    }

    public void ClickPosition(int index){
        setAlldataFalse(false);
        clickdata.put(index,true);
        notifyDataSetChanged();
    }

    public void setListonClickLister(ListOnclickLister mlister){
        this.mlister = mlister;
    }

}
