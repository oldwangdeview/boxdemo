package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.MainPath_ListBean;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.bean.UserInfo;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ImageUtil;
import com.oldwang.boxdemo.util.PreferencesUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 二维码扫描
 * <p/>
 */
public class MyCaptureActivity extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;

    @BindView(R.id.tv_small_title_layout_head)
    TextView tv_small_title_layout_head;



    private static final String TAG = MyCaptureActivity.class.getSimpleName();

    private static final int REQUEST_IMAGE = 101;

    public static void start(Context context) {
        Intent intent = new Intent(context, MyCaptureActivity.class);
        context.startActivity(intent);
    }



    @Override
    protected void initView() {
        setContentView(R.layout.capture_activity);
    }

    @Override
    protected void initData() {
        super.initData();
        initComment();
    }

    private void initComment() {
        tv_title_activity_baseperson.setText("扫一扫");
        tv_small_title_layout_head.setVisibility(View.VISIBLE);
        tv_small_title_layout_head.setText("相册");

        /**
         * 执行扫面Fragment的初始化操作
         */
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);

        captureFragment.setAnalyzeCallback(analyzeCallback);
        /**
         * 替换我们的扫描控件
         */ getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {

            doResult(result);

        }

        @Override
        public void onAnalyzeFailed() {
            ToastUtils.makeText("解析二维码失败");


        }
    };
    @OnClick({R.id.iv_back_activity_basepersoninfo,R.id.tv_small_title_layout_head})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.iv_back_activity_basepersoninfo:
                finish();
                break;
            case R.id.tv_small_title_layout_head:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
        }
    }

    /*****
     * 处理扫描结果
     * @param result
     */
    public void doResult(String result){

        if (!TextUtils.isEmpty(result)){
            bindCode(result);
        }
    }


    /**
     * 绑定二维码
     */
    private void bindCode(String result) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code",result);
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());

        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().bindCode(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDialog();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo1>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo1> stringStatusCode) {

                if (stringStatusCode != null) {
//                    String mainId = stringStatusCode.getData().getMainId();
                    ToastUtils.makeText("关系保存成功");
                    finish();
                    dismissLoadingDialog();
                }
            }

            @Override
            protected void _onError(String message) {
                finish();
                dismissLoadingDialog();
                ToastUtils.makeText(message);
            }
        }, "", lifecycleSubject, false, true);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            doResult(result);
//                            Toast.makeText(MyCaptureActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(MyCaptureActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
