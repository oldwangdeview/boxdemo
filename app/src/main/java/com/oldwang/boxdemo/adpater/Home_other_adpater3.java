package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseRecycleAdapter;
import com.oldwang.boxdemo.bean.MasterData;
import com.oldwang.boxdemo.bean.MasterDataDTO;
import com.oldwang.boxdemo.event.JumpTeachers;
import com.oldwang.boxdemo.help.RecycleViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class Home_other_adpater3 extends BaseRecycleAdapter<MasterDataDTO> {
    public Home_other_adpater3(Context context, List<MasterDataDTO> datas) {
        super(context, datas, R.layout.item_other3);
    }

    //1名师2战将
    private  int type;
    public void setType(int type){
        this.type = type;
    }

    @Override
    protected void setData(RecycleViewHolder holder, final MasterDataDTO item, int position) {


        TextView text1 = holder.getItemView(R.id.text1);
        TextView text2 = holder.getItemView(R.id.text1);
        TextView text3 = holder.getItemView(R.id.text1);

        if (!TextUtils.isEmpty(item.getNewsMasterNameOne())){
            text1.setText(item.getNewsMasterNameOne());
        }
        if (!TextUtils.isEmpty(item.getNewsMasterNameTwo())){
            text2.setText(item.getNewsMasterNameTwo());
        }
        if (!TextUtils.isEmpty(item.getNewsMasterNameThree())){
            text3.setText(item.getNewsMasterNameThree());
        }


        View ll_one = holder.getItemView(R.id.ll_one);
        View ll_two = holder.getItemView(R.id.ll_two);
        View ll_three = holder.getItemView(R.id.ll_three);

        ll_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(item.getNewsMasterIdOne())){
                    EventBus.getDefault().post(new JumpTeachers(type,item.getNewsMasterIdOne()));
                }
            }
        });
        ll_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(item.getNewsMasterIdTwo())){
                    EventBus.getDefault().post(new JumpTeachers(type,item.getNewsMasterIdTwo()));
                }
            }
        });
        ll_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(item.getNewsMasterIdThree())){
                    EventBus.getDefault().post(new JumpTeachers(type,item.getNewsMasterIdThree()));
                }
            }
        });

    }
}
