package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.CommodityTypeData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.HashMap;
import java.util.List;

public class QuanLianDataAdpater extends BaseRecycleAdapter<CommodityTypeData> {

    ListOnclickLister mlister;

    TextView text;
    View view;
    ImageView image;
    LinearLayout ll_item;

    static HashMap<Integer,Boolean> clickdata = new HashMap<>();



    public QuanLianDataAdpater(Context context, List<CommodityTypeData> datas) {
        super(context, datas, R.layout.item_quanlian_data);
    }

    @Override
    protected void setData(RecycleViewHolder holder, CommodityTypeData s, final int position) {


        text = holder.getItemView(R.id.text);
        view = holder.getItemView(R.id.view);
        image = holder.getItemView(R.id.image);
        ll_item = holder.getItemView(R.id.ll_item);
        if (!TextUtils.isEmpty(s.getCommodityTypeName())){
            text.setText(s.getCommodityTypeName());
        }


        if (!TextUtils.isEmpty(s.getCommodityTypeImg())){
            UIUtils.loadImageView(mContext,s.getCommodityTypeImg(),image);
        }

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });
        ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickPosition(position);
                mlister.onclick(view,position);
            }
        });
        if(clickdata.get(position)){
            text.setTextColor(mContext.getResources().getColor(R.color.c_d52e21));
            view.setVisibility(View.VISIBLE);
        }else{
            text.setTextColor(mContext.getResources().getColor(R.color.c_525259));
            view.setVisibility(View.INVISIBLE);
        }
    }

    public void setAlldataFalse(boolean type){
        for(int i =0;i<mDatas.size();i++){
            clickdata.put(i,false);
        }
//        if(type){
//            clickdata.put(0,true);
//        }
    }

    public void ClickPosition(int index){
        setAlldataFalse(false);
        clickdata.put(index,true);
        notifyDataSetChanged();
    }

    public void SetListClickLister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
