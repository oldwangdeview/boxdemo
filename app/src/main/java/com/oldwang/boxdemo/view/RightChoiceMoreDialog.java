package com.oldwang.boxdemo.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.interfice.ChoiceDataReturnLister;

import java.util.ArrayList;
import java.util.List;

public class RightChoiceMoreDialog  extends Dialog {

    public RightChoiceMoreDialog(Context context, int themeResId) {
        super(context, themeResId);
    }



    public static class Params {

        private Context context;
        private List<ChoiceView> viewlist = new ArrayList<>();
        private View.OnClickListener mlister ;

    }

    public static class Builder{
        private boolean canCancel = true;
        private boolean shadow = true;
        private final RightChoiceMoreDialog.Params p;

        public Builder(Context context) {
            p = new RightChoiceMoreDialog.Params();
            p.context = context;
        }
        public Builder setconntentview(List<ChoiceView> viewlist){
            this.p.viewlist = viewlist;
            return this;
        }
        public Builder setOkButtonClickLister(View.OnClickListener mlister){
            this.p.mlister = mlister;
            return  this;
        }
        public RightChoiceMoreDialog create() {
            final RightChoiceMoreDialog dialog = new RightChoiceMoreDialog(p.context, R.style.DialogRight);
            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.ActionSheetDialogRight);

            window.getDecorView().setPadding(15, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;

            window.setAttributes(lp);
            window.setGravity(Gravity.RIGHT);


            View view = LayoutInflater.from(p.context).inflate(R.layout.dialog_choicemore, null);

            LinearLayout layout = view.findViewById(R.id.layout_1);

            layout.removeAllViews();
            for(int i =0;i<p.viewlist.size();i++){
                layout.addView(p.viewlist.get(i));
            }

            view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  if(p.viewlist.size()>0){
                      for(int i =0;i<p.viewlist.size();i++){
                          p.viewlist.get(i).RestData();
                      }
                  }
                }
            });

            view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(p.viewlist.size()>0){
                        for(int i =0;i<p.viewlist.size();i++){
                            p.viewlist.get(i).getChoicetext();
                        }
                    }
                    if(p.mlister!=null){
                        p.mlister.onClick(v);
                    }
                    dialog.dismiss();
                }
            });

            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(canCancel);
            dialog.setCancelable(canCancel);

            return dialog;
        }

        public void hideSystemUI(View view) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                );
            }
        }

    }


    @Override
    public void dismiss() {
        super.dismiss();

    }
}

