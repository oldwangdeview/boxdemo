package com.oldwang.boxdemo.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.ChoicemoreAdpater;
import com.oldwang.boxdemo.interfice.ChoiceDataReturnLister;
import com.oldwang.boxdemo.interfice.ChoicePathReturnLister;
import com.oldwang.boxdemo.interfice.ChoicePriceLister;
import com.oldwang.boxdemo.interfice.ChoiceViewTitleClickLister;
import com.oldwang.boxdemo.interfice.ListOnclickLister;

import java.util.ArrayList;
import java.util.List;

public class ChoiceView<T> extends LinearLayout {
    TextView text_type;
    ImageView image_more;
    MyGridView mygridview;
    ChoicemoreAdpater<T> choicemoreAdpater;
    ChoiceDataReturnLister<T> returnlister;

    Context mContext;
    boolean isshowmore = false;
    boolean showtype = true;
    String title_name;
    boolean choicemore = false;
    LinearLayout choicepath_layout;
    List<T> listdata = new ArrayList<>();

    //选择地址

    List<TextView> textlist = new ArrayList<>();
    List<View>viewlist = new ArrayList<>();
    List<LinearLayout> layoutlist = new ArrayList<>();
;

    ChoicemoreAdpater<T> choicemoreAdpater1;
    ChoicePathReturnLister<T> returnlister1;



    //选择最大值最小值
    ChoicePriceLister pricelister;
    EditText min_editext;
    EditText max_editext;

    public ChoiceView(Context context) {
        super(context);
        this.mContext = context;

    }

    /**
     *
     * @param context
     * @param listdata   列表数据
     * @param title_name  列表类型名称
     * @param isshowmore  是否有隐藏按钮
     */
    public ChoiceView(Context context , List<T> listdata , String title_name, boolean isshowmore, ChoiceDataReturnLister<T> returnlister) {
        super(context);
        this.mContext = context;
        this.listdata = listdata;
        LayoutInflater.from(context).inflate(R.layout.view_choicemore, this);
        text_type = findViewById(R.id.text_type);
        image_more = findViewById(R.id.image_more);
        mygridview = findViewById(R.id.mygridview);
        this.title_name = title_name;
        this.returnlister = returnlister;
        if(!TextUtils.isEmpty(title_name)){
            text_type.setText(title_name);
        }
        indata();

    }

    /**
     *
     * @param context
     * @param listdata   列表数据
     * @param title_name 列表类型名称
     * @param isshowmore 是否有隐藏按钮
     * @param choicemore 是否支持多选
     */
    public ChoiceView(Context context , List<T> listdata , String title_name, boolean isshowmore, ChoiceDataReturnLister<T> returnlister,boolean choicemore) {
        super(context);
        this.mContext = context;
        this.listdata = listdata;
        this.isshowmore = isshowmore;
        this.choicemore = choicemore;
        LayoutInflater.from(context).inflate(R.layout.view_choicemore, this);
        text_type = findViewById(R.id.text_type);
        image_more = findViewById(R.id.image_more);
        mygridview = findViewById(R.id.mygridview);
        this.title_name = title_name;
        this.returnlister = returnlister;
        if(!TextUtils.isEmpty(title_name)){
            text_type.setText(title_name);
        }
        indata();

    }

    /**8
     * 选择地址
     * @param context
     * @param listdata 省的数据
     * @param returnlister 点击省返回数据
     */

    public ChoiceView(Context context , List<T> listdata ,String title_name, ChoicePathReturnLister<T> returnlister) {
        super(context);
        this.mContext = context;
        this.listdata = listdata;
        this.returnlister1 = returnlister;
        this.title_name = title_name;

        LayoutInflater.from(context).inflate(R.layout.view_choicemore, this);
        text_type =  findViewById(R.id.text_type);
        image_more = findViewById(R.id.image_more);
        mygridview = findViewById(R.id.mygridview);



        textlist.add((TextView) findViewById(R.id.text1));
        textlist.add((TextView) findViewById(R.id.text2));
        textlist.add((TextView) findViewById(R.id.text3));
        textlist.add((TextView) findViewById(R.id.text4));

        viewlist.add((View)findViewById(R.id.view1));
        viewlist.add((View)findViewById(R.id.view2));
        viewlist.add((View)findViewById(R.id.view3));
        viewlist.add((View)findViewById(R.id.view4));

        layoutlist.add((LinearLayout)findViewById(R.id.layout_1));
        layoutlist.add((LinearLayout)findViewById(R.id.layout_2));
        layoutlist.add((LinearLayout)findViewById(R.id.layout_3));
        layoutlist.add((LinearLayout)findViewById(R.id.layout_4));
        if(!TextUtils.isEmpty(title_name)){
            text_type.setText(title_name);
        }
        indata();

    }


    /**
     *
     * @param context
     */
    public ChoiceView(Context context  , String title_name, ChoicePriceLister pricelister) {
        super(context);
        this.mContext = context;
        this.pricelister = pricelister;
        LayoutInflater.from(context).inflate(R.layout.view_choiceprice, this);
        min_editext = findViewById(R.id.min_editext);
        max_editext = findViewById(R.id.max_editext);
        if (!TextUtils.isEmpty(title_name) && title_name.equals("评分区间")){
            min_editext.setHint("最低评分");
            max_editext.setHint("最高评分");
        }

        indata();

    }



    OnClickListener mlayoutclicklister = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.layout_1:
                    setlayoutgone(0);
                    break;
                case R.id.layout_2:
                    setlayoutgone(1);
                    break;
                case R.id.layout_3:
                    setlayoutgone(2);
                    break;
                case R.id.layout_4:
                    setlayoutgone(3);
                    break;

            }
        }
    };


    private void setSeleteposition(int position){
        for(int i =0;i<textlist.size();i++){
            if(i==position){
              textlist.get(position).setTextColor(mContext.getResources().getColor(R.color.c_d52e21));
              viewlist.get(position).setVisibility(VISIBLE);


            }else{
                textlist.get(i).setTextColor(mContext.getResources().getColor(R.color.c_595959));
                viewlist.get(i).setVisibility(INVISIBLE);


            }
        }
    }

    private void setlayoutgone(int clickpoition){
        if(layoutlist.size()>0){
            setSeleteposition(clickpoition);
            for(int i=0;i<layoutlist.size();i++){
                if(i>clickpoition){
                    layoutlist.get(i).setVisibility(GONE);
                }else{
                    layoutlist.get(i).setVisibility(VISIBLE);
                }
            }
        }
    }

    private void indata(){

        if(mygridview!=null) {
            choicemoreAdpater = new ChoicemoreAdpater<T>(mContext,listdata);
            choicemoreAdpater.setclickmoreddata(choicemore);
            mygridview.setAdapter(choicemoreAdpater);
            if(isshowmore){
                mygridview.setVisibility(VISIBLE);
                image_more.setVisibility(VISIBLE);
            }else{
                mygridview.setVisibility(VISIBLE);
                image_more.setVisibility(GONE);
            }

        }

        if(!TextUtils.isEmpty(title_name)){
            text_type.setText(title_name);
        }
        if(image_more!=null) {
            image_more.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showtype = !showtype;
                    if (showtype) {
                        image_more.setImageResource(R.mipmap.choice_image_more);
                        mygridview.setVisibility(VISIBLE);
                    } else {
                        image_more.setImageResource(R.mipmap.choice_image_gone);
                        mygridview.setVisibility(GONE);
                    }

                }
            });

        }





    }


    public void setadaterdata(int posiion,List<T> listdata){
        choicemoreAdpater.setdata(listdata);
        choicemoreAdpater.setTitleposition(posiion);
    }







    public void getChoicetext(){
        if(choicemoreAdpater!=null){
            returnlister.getChoicedata( choicemoreAdpater.getChoiceData());
        }

        if(pricelister!=null&&min_editext!=null&&max_editext!=null){
            String mindata = min_editext.getText().toString().trim();
            String maxdata = max_editext.getText().toString().trim();
            pricelister.returndata(mindata,maxdata);
        }

    }





    /**
     * 重置数据
     */
    public void RestData(){
        if(choicemoreAdpater!=null){
            choicemoreAdpater.sealldataType(false);
        }
        if (min_editext != null && max_editext != null){
            min_editext.setText("");
            max_editext.setText("");
        }

    }


}
