package com.oldwang.boxdemo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.MyOrderListActivity;
import com.oldwang.boxdemo.event.MainJumpEvent;

import org.greenrobot.eventbus.EventBus;

public class OrderNoMessageView extends LinearLayout {
    Button button;
    Context mContext;
    public OrderNoMessageView(Context context) {
        super(context);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.order_nomessage, this);
        button = findViewById(R.id.btn_go);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyOrderListActivity)mContext).finish();
                EventBus.getDefault().post(new MainJumpEvent(1));
            }
        });
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);

    }




}
