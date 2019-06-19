package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.base.BaseActivity;

import butterknife.OnClick;

public class AssembleSucessActivity extends BaseActivity {
    @Override
    protected void initView() {
        setContentView(R.layout.activity_assemblesuccess);
    }




    @OnClick(R.id.finish_image)
    public void overnowactivity(){
        finish();
    }

    public static void startactivity(Context mCOntext){
        Intent mIntent = new Intent(mCOntext,AssembleSucessActivity.class);
        mCOntext.startActivity(mIntent);
    }
}
