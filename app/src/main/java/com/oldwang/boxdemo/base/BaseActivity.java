package com.oldwang.boxdemo.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.PublishSubject;

import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.ArtsSchoolDetailActivity;
import com.oldwang.boxdemo.activity.BaseCheckInActivity;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.bean.UserInfo;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.rxjava.Api;
import com.oldwang.boxdemo.util.LoadingDialogUtils;
import com.oldwang.boxdemo.util.PreferencesUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Author oldwang
 * Time  2017/11/27 16:36
 * Dest  Activity的基类
 */

public abstract class BaseActivity extends AppCompatActivity {
    public String TAG=this.getClass().getSimpleName();
    private Unbinder mUnbinder;
    public final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();
    private Dialog mLoadingDialog;
    public static String LOGINUSER="loginuser";
    public Context mContext = this;
    public static UserInfo mUserData = null;

    public static String mUserHeandImage = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
   //      lifecycleSubject.onNext(ActivityLifeCycleEvent.CREATE);
        Log.e("qidongactiity",TAG);
        super.onCreate(savedInstanceState);
    /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }*/
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&Build.MANUFACTURER.equals("Xiaomi")) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initView();
        mUnbinder = ButterKnife.bind(this);
        initData();
        initEvent();



    }

    private final String url = "https://www.pgyer.com/Tm67";
    private final String desc = "拳联之家，一款专注于打造拳联生态圈子的APP";
    private final String title = "拳联之家";

    private  String teamGoosUrl = "http://qlzj.siantest.com/qlzj/view/goods_detai/detail.html?id=%s";

    public void  share(int type,String goodsName,String goodsId,String goodsImage){

        //通用分享
        if (type == 1){
            UMImage image = new UMImage(mContext, R.mipmap.logo);//资源文件
            UMWeb web = new UMWeb(url);
            web.setTitle(title);//标题
            web.setThumb(image);  //缩略图
            web.setDescription(desc);//描述
            new ShareAction(this)
                    .withMedia(web)
                    .setCallback(shareListener)
                    .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                    .open();
        }else if(type == 3){
            //拼团商品分享
            if (!goodsImage.startsWith("http")){
                goodsImage = Api.imageUrl + goodsImage;
            }            UMImage image = new UMImage(mContext,goodsImage);//资源文件
            UMWeb web = new UMWeb(String.format(teamGoosUrl,goodsId));
            web.setTitle(title);//标题
            web.setThumb(image);  //缩略图
            web.setDescription(goodsName);//描述
            new ShareAction(this)
                    .withMedia(web)
                    .setCallback(shareListener)
                    .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                    .open();
        }else {
            if (!goodsImage.startsWith("http")){
                goodsImage = Api.imageUrl + goodsImage;
            }            UMImage image = new UMImage(mContext,goodsImage);//资源文件
            UMWeb web = new UMWeb(String.format(teamGoosUrl,goodsId));
            web.setTitle(title);//标题
            web.setThumb(image);  //缩略图
            web.setDescription(goodsName);//描述
            new ShareAction(this)
                    .withMedia(web)
                    .setCallback(shareListener)
                    .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                    .open();
        }

    }
    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @param platform 平台类型
         * @descrption 分享开始的回调
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

            ToastUtils.makeText("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(mContext).onActivityResult(requestCode,resultCode,data);

    }

    public static List<String> gettestdata(int size){
         List<String> testdata = new ArrayList<>();
         for(int i = 0; i<size;i++){
             testdata.add("1");
         }
         return testdata;
    }

    /**
     * 展示加载弹框
     *
     * @author wangshifu
     * @version v1.0, 2018/12/29
     */
    protected void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialogUtils.createLoadingDialog(this, "");
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    /**
     * 隐藏加载弹框
     *
     * @author wangshifu
     * @version v1.0, 2018/12/29
     */
    protected void dismissLoadingDialog(){
        LoadingDialogUtils.closeDialog(mLoadingDialog);
    }

    /**
     * 初始化事件
     */
    protected  void initEvent(){};
    /**
     * 初始化数据
     */
    protected  void initData(){};

    /**
     * 初始化界面
     */
    protected abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("qidongactiity","onDestroy"+TAG);
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()){
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
        }



    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    /**
     * 刷新小米顶部状态栏图标颜色
     */
    public void updateactionbar(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
    /**
     * 设置img标签下的width为手机屏幕宽度，height自适应
     *
     * @param data html字符串
     * @return 更新宽高属性后的html字符串
     */
    public String getNewData(String data) {
        Document document = Jsoup.parse(data);
        int screenWidth = UIUtils.getScreenWidth(mContext);
        Elements pElements = document.select("p:has(img)");
        for (Element pElement : pElements) {
            pElement.attr("style", "text-align:center");
            pElement.attr("max-width", String.valueOf(screenWidth + "px"))
                    .attr("height", "auto");
            pElement.attr("width", "100%");
        }
        Elements imgElements = document.select("img");
        for (Element imgElement : imgElements) {
            //重新设置宽高
            imgElement.attr("max-width", "100%")
                    .attr("height", "auto");
            imgElement.attr("style", "max-width:100%;height:auto");
            imgElement.attr("width", "100%");

        }
        Log.i("newData:", document.toString());
        return document.toString();
    }





    public static UserInfo getUserInfo(Context mContext){

        if(mUserData==null) {
            String preferencesdata = PreferencesUtils.getInstance().getString(Contans.userInfo, "");
            if (!TextUtils.isEmpty(preferencesdata)) {
                mUserData = new Gson().fromJson(preferencesdata, UserInfo.class);
            }
        }
        return mUserData;
    }

    public static void setmUserData(UserInfo mUserData) {
        BaseActivity.mUserData = mUserData;
    }

    public static String getmUserHeandImage(){
        if(!TextUtils.isEmpty(mUserHeandImage)){
            if(!mUserHeandImage.startsWith("http")){
                mUserHeandImage = Api.imageUrl+mUserHeandImage;
            }
        }
        return mUserHeandImage;
    }


    public static void setmUserHeandImage(String mUserHeandImage){
        BaseActivity.mUserHeandImage = mUserHeandImage;
    }
}
