package com.oldwang.boxdemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.adpater.AppoinmentAdpater;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.ScoreData;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.LoadingDialogUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.oldwang.boxdemo.view.YRecycleview;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ImageFragment extends BaseFragment {


    @BindView(R.id.content)
    TextView content;

    @BindView(R.id.iv_image)
    ImageView iv_image;

    private String image;
    private String text;

    public static ImageFragment newInstance(String image,String text) {
        ImageFragment f = new ImageFragment();
        Bundle b = new Bundle();
        b.putString("image", image);
        b.putString("text", text);

        f.setArguments(b);
        return f;
    }

    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_image);
    }

    @Override
    protected void initData() {
        Bundle args = getArguments();
        if (args != null) {
            image = args.getString("image");
            text = args.getString("text");
        }
        if (!TextUtils.isEmpty(image)){
            UIUtils.loadImageView(mContext,image,iv_image);
        }
        if (!TextUtils.isEmpty(text)){
            content.setText(text);
        }
        super.initData();
    }

}

