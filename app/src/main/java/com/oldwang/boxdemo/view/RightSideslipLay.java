package com.oldwang.boxdemo.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.RightSideslipLayAdapter;
import com.oldwang.boxdemo.adpater.ScreenAdapter;
import com.oldwang.boxdemo.bean.AttrList;
import com.oldwang.boxdemo.bean.ScreenChildData;
import com.oldwang.boxdemo.bean.ScreenData;
import com.oldwang.boxdemo.interfice.ScreenOnclickLister;
import com.oldwang.boxdemo.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 属性选择的布局及逻辑
 */
public class RightSideslipLay extends RelativeLayout {
    private Context mCtx;
    private ListView selectList;
    private TextView resetBrand;
    private TextView okBrand;

    private ScreenAdapter screenAdapter;

    private List<ScreenData> screenDataList = new ArrayList<>();
    private ScreenOnclickLister lister;

    public void setScreenDataList(List<ScreenData> list) {
        screenDataList.clear();
        screenDataList.addAll(list);
        if (screenAdapter != null){
            screenAdapter.notifyDataSetChanged();
        }else {
            screenAdapter =  new ScreenAdapter(mCtx,screenDataList);
            selectList.setAdapter(screenAdapter);
        }
    }

    /***
     * 重置信息
     * @param list
     */
    public void setNewScreenDataList(List<ScreenData> list) {
        screenDataList.clear();
        screenDataList.addAll(list);
        for (ScreenChildData screenChildData : screenDataList.get(0).getScreenChildDataList()) {
            screenChildData.setCheck(false);
        }

        for (ScreenChildData screenChildData : screenDataList.get(1).getScreenChildDataList()) {
            screenChildData.setCheck(false);
        }


        screenAdapter =  new ScreenAdapter(mCtx,screenDataList);
        selectList.setAdapter(screenAdapter);
    }


    public RightSideslipLay(Context context) {
        super(context);
        mCtx = context;
        inflateView();
    }

    private void inflateView() {
        View.inflate(getContext(), R.layout.include_right_sideslip_layout, this);
        selectList = (ListView) findViewById(R.id.selsectFrameLV);
        resetBrand = (TextView) findViewById(R.id.fram_reset_but);
        okBrand = (TextView) findViewById(R.id.fram_ok_but);
        resetBrand.setOnClickListener(mOnClickListener);
        okBrand.setOnClickListener(mOnClickListener);

    }




    private OnClickListenerWrapper mOnClickListener = new OnClickListenerWrapper() {
        @Override
        protected void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.fram_reset_but:
                    menuCallBack.setupCloseMean(true);
                    break;
                case R.id.fram_ok_but:
                    menuCallBack.setupCloseMean(false);
                    break;
            }
        }
    };





    private CloseMenuCallBack menuCallBack;

    public interface CloseMenuCallBack {
        void setupCloseMean(boolean isReset);
    }

    public void setCloseMenuCallBack(CloseMenuCallBack menuCallBack) {
        this.menuCallBack = menuCallBack;
    }
}
