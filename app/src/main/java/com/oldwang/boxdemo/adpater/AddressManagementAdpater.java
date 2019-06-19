package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.AddressData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;

import java.util.HashMap;
import java.util.List;

public class AddressManagementAdpater extends BaseRecycleAdapter<AddressData> {
    //选默认地址
    LinearLayout choice_morenlayout;

    //修改
    LinearLayout layout_uotdae;

    //删除
    LinearLayout layout_delete;

    ImageView choice_image;
    ListOnclickLister mlister;


    public AddressManagementAdpater(Context context, List<AddressData> datas) {
        super(context, datas, R.layout.item_addresslist);
    }

    @Override
    protected void setData(RecycleViewHolder holder, AddressData s, final int position) {

        choice_morenlayout = holder.getItemView(R.id.choice_morenlayout);
        layout_uotdae = holder.getItemView(R.id.layout_uotdae);
        layout_delete = holder.getItemView(R.id.layout_delete);
        choice_image = holder.getItemView(R.id.choice_image);

        TextView tv_name = holder.getItemView(R.id.tv_name);
        TextView tv_phone = holder.getItemView(R.id.tv_phone);
        TextView tv_address = holder.getItemView(R.id.tv_address);



        if (!TextUtils.isEmpty(s.getMemberName())){
            tv_name.setText(s.getMemberName());
        }

        if (!TextUtils.isEmpty(s.getMemberPhone())){
            tv_phone.setText(s.getMemberPhone());
        }

//        if (!TextUtils.isEmpty(s.getAddressDetail())){
//            tv_address.setText(s.getAddressDetail());
//        }

        String adddress = "";

        if (!TextUtils.isEmpty(s.getProvince())) {
            adddress = s.getProvince();
        }
        if (!TextUtils.isEmpty(s.getCity())) {
            adddress += s.getCity();
        }
        if (!TextUtils.isEmpty(s.getDistrict())) {
            adddress += s.getDistrict();
        }
        if (!TextUtils.isEmpty(s.getAddressDetail())) {
            adddress += s.getAddressDetail();
        }
        tv_address.setText(adddress);

        if(!TextUtils.isEmpty(s.getDefaultStatus()) && s.getDefaultStatus().equals("1")){
            choice_image.setImageResource(R.mipmap.addresslist_choice);
        }else{
            choice_image.setImageResource(R.mipmap.addresslist_unchoice);
        }

        choice_morenlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

        layout_uotdae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

        layout_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });



    }




    public void setlistonclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
