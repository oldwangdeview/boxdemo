package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.CommodityData;
import com.oldwang.boxdemo.bean.ScoreData;
import com.oldwang.boxdemo.event.CartCheckEvent;
import com.oldwang.boxdemo.event.CollectCheckEvent;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class GoodCarsAdpater extends BaseRecycleAdapter<CommodityData> {


    ListOnclickLister listOnclickLister;
    ImageView choice_image;
    View view1;

    public GoodCarsAdpater(Context context, List<CommodityData> datas) {
        super(context, datas, R.layout.item_goodscars);
    }

    @Override
    protected void setData(RecycleViewHolder holder, final CommodityData s, final int position) {


        ImageView iv_goods_image = holder.getItemView(R.id.iv_goods_image);
        TextView tv_goods_name = holder.getItemView(R.id.tv_goods_name);
        TextView tv_attribute = holder.getItemView(R.id.tv_attribute);
        TextView tv_price = holder.getItemView(R.id.tv_price);
        TextView tv_reduce = holder.getItemView(R.id.tv_reduce);
        TextView tv_count = holder.getItemView(R.id.tv_count);
        TextView tv_add = holder.getItemView(R.id.tv_add);

        LinearLayout ll_item = holder.getItemView(R.id.ll_item);


        if (!TextUtils.isEmpty(s.getCommodityUrl())) {
            UIUtils.loadImageView(mContext, s.getCommodityUrl(), iv_goods_image);
        }

        if (!TextUtils.isEmpty(s.getCommodityName())) {
            tv_goods_name.setText(s.getCommodityName());
        }


        String attribute = "";

        if (!TextUtils.isEmpty(s.getAttributeColor())) {
            attribute = "颜色：" + s.getAttributeColor() + " ";
        }
        if (!TextUtils.isEmpty(s.getAttributeQuality())) {
            attribute += "材质：" + s.getAttributeQuality() + " ";
        }
        if (!TextUtils.isEmpty(s.getAttributeSize())) {
            attribute += "型号：" + s.getAttributeSize();
        }
        tv_attribute.setText(attribute);

        if (!TextUtils.isEmpty(s.getSamePrice())) {
            tv_price.setText("¥ " + s.getSamePrice());
        } else {
            tv_price.setText("¥ " + 0.00);
        }

        if (!TextUtils.isEmpty(s.getCommodityNum())) {
            tv_count.setText(s.getCommodityNum());
        }


        choice_image = holder.getItemView(R.id.choice_image);
        view1 = holder.getItemView(R.id.view);
        if (s.isCheck()) {
            choice_image.setImageResource(R.mipmap.addresslist_choice);
        } else {
            choice_image.setImageResource(R.mipmap.addresslist_unchoice);
        }
        if (position == 0) {
            view1.setVisibility(View.VISIBLE);
        } else {
            view1.setVisibility(View.GONE);
        }

        tv_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOnclickLister.onclick(view, position);
            }
        });

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOnclickLister.onclick(view, position);
            }
        });

        ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOnclickLister.onclick(view, position);
            }
        });
        choice_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(s.getShelfStatus()) && s.getShelfStatus().equals("0")) {
                    ToastUtils.makeText("该商品为下架状态 ");
                    return;
                }
                s.setCheck(!s.isCheck());
                boolean temp = true;
                for (CommodityData mData : mDatas) {
                    if (!mData.isCheck()) {
                        temp = false;
                        break;
                    }
                }
                //子条目是否全部选中
                EventBus.getDefault().post(new CartCheckEvent(temp));
                notifyDataSetChanged();
            }
        });
    }


    public void settadlldataType(boolean isCheck) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (!TextUtils.isEmpty(mDatas.get(i).getShelfStatus()) && mDatas.get(i).getShelfStatus().equals("0")) {
                mDatas.get(i).setCheck(false);
            } else {
                mDatas.get(i).setCheck(isCheck);

            }
        }
        notifyDataSetChanged();
    }


    public List<CommodityData> getListClickdata() {
        List<CommodityData> listdat = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).isCheck()) {
                listdat.add(mDatas.get(i));
            }
        }
        return listdat;
    }

    public void setListOnclickLister(ListOnclickLister listOnclickLister) {
        this.listOnclickLister = listOnclickLister;
    }
}
