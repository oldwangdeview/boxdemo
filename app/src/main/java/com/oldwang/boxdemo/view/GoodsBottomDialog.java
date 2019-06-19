package com.oldwang.boxdemo.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.interfice.TextChangeOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;


public class GoodsBottomDialog extends Dialog {





    public GoodsBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }



    public static class Params {

        private View.OnClickListener cancelListener;
        private BaseAdapter coloradpater;
        private BaseAdapter typeadpater;
        private BaseAdapter threeAdapter;
        private Context context;
        private TextView tv_now_pirce;
        private TextView tv_price;
        private TextView tv_content;
        private ImageView iv_image;
        private TextView tv_reduce;
        private TextView et_count;
        private TextView tv_add;


    }

    public static class Builder {
        private boolean canCancel = true;
        private boolean shadow = true;
        private final GoodsBottomDialog.Params p;

        public Builder(Context context) {
            p = new GoodsBottomDialog.Params();
            p.context = context;
        }


        public Builder setcolorbaseadpater(BaseAdapter baseadpater ){

            this.p.coloradpater = baseadpater;
            return this;
        }


        public Builder settypebaseadpater(BaseAdapter baseadpater ){

            this.p.typeadpater = baseadpater;
            return this;
        }

        public Builder setThreeAdapter(BaseAdapter baseadpater ){
            this.p.threeAdapter = baseadpater;
            return this;
        }

        public Builder setbuttonclicklister(View.OnClickListener lister ){

            this.p.cancelListener = lister;
            return this;
        }




        public GoodsBottomDialog create() {
            final GoodsBottomDialog dialog = new GoodsBottomDialog(p.context, shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.Animation_Bottom_Rising);

            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = UIUtils.getScreenHeigh(p.context) / 2 + 300 ;

            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);
            View view = LayoutInflater.from(p.context).inflate(R.layout.dialog_goodsdetailchoice, null);

             p.tv_now_pirce =  view.findViewById(R.id.tv_now_pirce);
            p.tv_price =  view.findViewById(R.id.tv_price);
            p.tv_content =  view.findViewById(R.id.tv_content);
            p.iv_image =  view.findViewById(R.id.iv_image);

            p.tv_reduce =  view.findViewById(R.id.tv_reduce);
            p.et_count =  view.findViewById(R.id.et_count);
            p.tv_add =  view.findViewById(R.id.tv_add);

            MyGridView colorgrideview = view.findViewById(R.id.colorgrideview);
            MyGridView sizegrideview = view.findViewById(R.id.sizegrideview);
            MyGridView three_gridview = view.findViewById(R.id.three_gridview);




            if(p.coloradpater!=null){
                colorgrideview.setAdapter(p.coloradpater);
            }

            if(p.typeadpater!=null){
                sizegrideview.setAdapter(p.typeadpater);
            }
            if(p.typeadpater!=null){
                three_gridview.setAdapter(p.threeAdapter);
            }

            if(p.cancelListener!=null){
                view.findViewById(R.id.ojbk_btn).setOnClickListener(p.cancelListener);
            }


            view.findViewById(R.id.colose).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });



            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(canCancel);
            dialog.setCancelable(canCancel);
            return dialog;
        }


        public TextView getTv_now_pirce() {
            return p.tv_now_pirce;
        }
        public TextView getTvPrice() {
            return p.tv_price;
        }
        public TextView getTvContent() {
            return p.tv_content;
        }
        public ImageView getIv_image() {
            return p.iv_image;
        }
        public TextView getTv_reduce() {
            return p.tv_reduce;
        }

        public TextView getEt_count() {
            return p.et_count;
        }

        public TextView getTv_add() {
            return p.tv_add;
        }
    }

    private static class BottomMenu {
        public String funName;
        public View.OnClickListener listener;

        public BottomMenu(String funName, View.OnClickListener listener) {
            this.funName = funName;
            this.listener = listener;
        }
    }
}
