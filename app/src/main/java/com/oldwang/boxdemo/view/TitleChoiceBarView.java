package com.oldwang.boxdemo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.interfice.TitleChoiceBarLister;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class TitleChoiceBarView extends LinearLayout {

    private String[] titledata;
    private int choicecolor;
    private int textcolor;
    private Context mContext;
    private LinearLayout mlayout;
    private TitleChoiceBarLister mlister;
    private List<View> viewlist = new ArrayList<>();
    public TitleChoiceBarView(Context context) {
        super(context);
    }


    public TitleChoiceBarView(Context context, String[] titledata, int choiceColor, int textcolor, TitleChoiceBarLister mlister) {
        super(context);
        this.mContext = context;
        this.titledata = titledata;
        this.choicecolor = choiceColor;
        this.textcolor = textcolor;
        this.mlister = mlister;
        LayoutInflater.from(context).inflate(R.layout.view_titlechoicelayout, this);
        mlayout = findViewById(R.id.mlayout);
        intdata();
    }

    private void intdata(){
        if(titledata.length>0){

            for (int i = 0; i < titledata.length; i++) {

                View view = UIUtils.inflate(mContext,R.layout.view_titchoiceview_item);
                TextView text = view.findViewById(R.id.text);
                if(i==0){
                    text.setTextColor(mContext.getResources().getColor(choicecolor));
                    view.findViewById(R.id.view).setVisibility(VISIBLE);
                }else{
                    text.setTextColor(mContext.getResources().getColor(textcolor));
                    view.findViewById(R.id.view).setVisibility(INVISIBLE);
                }
                text.setText(titledata[i]);
                viewlist.add(view);
                final int position = i;
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        seltetposition(position);
                        if(mlister!=null){
                            mlister.chicepositiob(position,titledata[position]);
                        }
                    }
                });
                LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,1);
                mlayout.addView(view,params);
            }
        }
    }

    public void seltetposition(int position){
        for (int i = 0; i < viewlist.size(); i++) {
            TextView text = viewlist.get(i).findViewById(R.id.text);
            if(position==i){
                text.setTextColor(mContext.getResources().getColor(choicecolor));
                viewlist.get(i).findViewById(R.id.view).setVisibility(VISIBLE);
            }else{
                text.setTextColor(mContext.getResources().getColor(textcolor));
                viewlist.get(i).findViewById(R.id.view).setVisibility(INVISIBLE);
            }
        }
    }




}
