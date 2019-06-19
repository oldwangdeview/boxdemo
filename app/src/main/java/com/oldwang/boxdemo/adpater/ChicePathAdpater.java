package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.AddressData;
import com.oldwang.boxdemo.help.RecycleViewHolder;
import com.oldwang.boxdemo.interfice.ListOnclickLister;

import java.util.HashMap;
import java.util.List;

public class ChicePathAdpater extends BaseRecycleAdapter<AddressData> {
    ImageView choice_image;
    TextView text_usernameandphone;
    TextView morendata;
    TextView pathdetail;
    ListOnclickLister mlister;


    static HashMap<Integer,Boolean> clickdata = new HashMap<>();
    public ChicePathAdpater(Context context, List<AddressData> datas) {
        super(context, datas, R.layout.item_choicelocation);
    }

    @Override
    protected void setData(RecycleViewHolder holder, AddressData s, final int position) {

        choice_image = holder.getItemView(R.id.choice_image);
        text_usernameandphone = holder.getItemView(R.id.text_usernameandphone);
        morendata = holder.getItemView(R.id.morendata);
        pathdetail = holder.getItemView(R.id.pathdetail);





        String title = "";
        if (!TextUtils.isEmpty(s.getMemberName())){
            title = s.getMemberName();
        }

        if (!TextUtils.isEmpty(s.getMemberPhone())){
            title = title+" "+s.getMemberPhone();
        }

        if(!TextUtils.isEmpty(title)){
            text_usernameandphone.setText(title);
        }



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
        pathdetail.setText(adddress);


        if(!TextUtils.isEmpty(s.getDefaultStatus()) && s.getDefaultStatus().equals("1")){
            morendata.setVisibility(View.VISIBLE);
        }else{
            morendata.setVisibility(View.GONE);
        }


        if(clickdata.get(position)){
            choice_image.setImageResource(R.mipmap.addresslist_choice);
        }else{
            choice_image.setImageResource(R.mipmap.addresslist_unchoice);
        }

        text_usernameandphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlldatatype(false);
                clickdata.put(position,true);
                notifyDataSetChanged();
            }
        });

        choice_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlldatatype(false);
                clickdata.put(position,true);
                notifyDataSetChanged();
            }
        });
        pathdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlldatatype(false);
                clickdata.put(position,true);
                notifyDataSetChanged();
            }
        });




        holder.getItemView(R.id.bianji).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

    }


    public void setAlldatatype(boolean type){
        for(int i = 0;i<mDatas.size();i++){
            clickdata.put(i,type);
        }
        notifyDataSetChanged();
    }


    public AddressData getChoicedata(){
        for(int i =0;i<mDatas.size();i++){
            if(clickdata.get(i)){
                return mDatas.get(i);
            }
        }
        return null;
    }


    public void setListOnclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }


    public void setdata(List<AddressData> datas){
        mDatas.clear();
        mDatas.addAll(datas);
        setAlldatatype(false);
        notifyDataSetChanged();
    }
}
