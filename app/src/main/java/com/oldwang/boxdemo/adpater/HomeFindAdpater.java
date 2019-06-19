package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;

import java.util.List;

public class HomeFindAdpater extends BaseRecycleAdapter<String> {
    private ListOnclickLister mlister;

    public HomeFindAdpater(Context context, List<String> datas ){
        super(context, datas, R.layout.item_homefind);
    }

    @Override
    protected void setData(RecycleViewHolder holder, String s, final int position) {

        TextView tv_name = holder.getItemView(R.id.tv_name);
        LinearLayout itemView = holder.getItemView(R.id.ll_item);
        tv_name.setText(s);
        if (position > 9){
            itemView.setVisibility(View.GONE);
        }else {
            itemView.setVisibility(View.VISIBLE);
        }

        holder.getItemView(R.id.deletet_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return  mDatas.size() <= 10 ?mDatas.size():10;
    }

    public void setonclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
