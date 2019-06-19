package com.oldwang.boxdemo.view;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.oldwang.boxdemo.R;

public class PayForBottomDialog extends Dialog {





    public PayForBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }



    public static class Params {

        private Context context;
        private View.OnClickListener mlister;

        private ImageView iv_one;
        private ImageView iv_two;

    }

    public static class Builder {
        private boolean canCancel = true;
        private boolean shadow = true;
        private final PayForBottomDialog.Params p;

        public Builder(Context context) {
            p = new PayForBottomDialog.Params();
            p.context = context;
        }

        public Builder setClicklister(View.OnClickListener mlister ){

            this.p.mlister = mlister;
            return this;
        }



        public PayForBottomDialog create() {
            final PayForBottomDialog dialog = new PayForBottomDialog(p.context, shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.Animation_Bottom_Rising);

            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);
            View view = LayoutInflater.from(p.context).inflate(R.layout.dialog_payfordialog, null);
             p.iv_one = view.findViewById(R.id.iv_one);
             p.iv_two = view.findViewById(R.id.iv_two);


            if(p.mlister!=null) {
                view.findViewById(R.id.layout_ali).setOnClickListener(p.mlister);
                view.findViewById(R.id.layout_wechart).setOnClickListener(p.mlister);
                view.findViewById(R.id.ojbk_btn).setOnClickListener(p.mlister);
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

        public ImageView getOneImageView() {
            return p.iv_one;
        }
        public ImageView getTwoImageView() {
            return p.iv_two;
        }

    }


}

