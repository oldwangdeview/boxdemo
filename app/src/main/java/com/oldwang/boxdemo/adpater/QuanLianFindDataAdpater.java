package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.CommodityTypeData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.interfice.OrderAdpaterClickGoodsLister;
import com.oldwang.boxdemo.view.MyGridView;

import java.util.List;

public class QuanLianFindDataAdpater extends BaseRecycleAdapter<CommodityTypeData> {
    MyGridView mygridview;
    OrderAdpaterClickGoodsLister mlister;
    public QuanLianFindDataAdpater(Context context, List<CommodityTypeData> datas) {
        super(context, datas, R.layout.item_quanlianfinddata);
    }

    @Override
    protected void setData(RecycleViewHolder holder, CommodityTypeData commodityTypeData, final int position) {


        TextView tv_name = holder.getItemView(R.id.tv_name);

        if (!TextUtils.isEmpty(commodityTypeData.getCommodityTypeName())){
            tv_name.setText(commodityTypeData.getCommodityTypeName());
        }

        mygridview = holder.getItemView(R.id.mygridview);

        List<CommodityTypeData> childs = commodityTypeData.getChilds();

        if (childs != null && childs.size() > 0){
            QuanLianFindGrideAdpater mpadater = new QuanLianFindGrideAdpater(mContext,childs);
            mpadater.setListClicklister(new ListOnclickLister() {
                @Override
                public void onclick(View v, int zposition) {
                    if(mlister!=null){
                        mlister.click(position,v,zposition);
                    }
                }
            });
            mygridview.setAdapter(mpadater);
        }





    }


    public void setListonclicklister(OrderAdpaterClickGoodsLister mlister){
        this.mlister = mlister;
    }
}
