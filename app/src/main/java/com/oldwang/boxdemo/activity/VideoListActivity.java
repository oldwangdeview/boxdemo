package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.event.UpdateUserEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.Api;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class VideoListActivity extends BaseActivity {

    @BindView(R.id.jz_video)
    JzvdStd jzvdStd;

    @BindView(R.id.et_title)
    EditText et_title;



    private String videopath;
    private int boxingVideoDuration;
    private long boxingVideoSize;

    private File file;
    private File imageFile;

    private int tag;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_videolist);
    }


    @Override
    protected void initData() {
        super.initData();
        tag = 0;
        videopath = getIntent().getStringExtra(Contans.INTENT_DATA);
        boxingVideoDuration = getIntent().getIntExtra(Contans.INTENT_TYPE,0);

        file = new File(videopath);
        boxingVideoSize = file.length();




        if (!TextUtils.isEmpty(videopath)) {
            Bitmap bitmap = getVideoThumbnail(videopath);

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date(System.currentTimeMillis());
            String filename = format.format(date)+".jpg";

            try {
                imageFile = saveFile(bitmap,filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
//                UIUtils.loadImageView(mContext, newsInfo.getBoxingVideoImg(), iv_image);

//                jzvdStd.setUp("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
//                        , "饺子快长大", JzvdStd.SCREEN_NORMAL);
//                Glide.with(this).load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png").into(jzvdStd.thumbImageView);

            jzvdStd.setUp(videopath
                    , "", JzvdStd.SCREEN_NORMAL);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes=baos.toByteArray();
            Glide.with(VideoListActivity.this).load(bytes).into(jzvdStd.thumbImageView);

        }


    }

    public File saveFile(Bitmap bm, String fileName) throws IOException {//将Bitmap类型的图片转化成file类型，便于上传到服务器
        String path = Environment.getExternalStorageDirectory() + "/Ask";
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;

    }


    @OnClick(R.id.iv_back)
    public void colosebtn(){
        finish();
    }

    @OnClick(R.id.iv_delete)
    public void iv_delete(){
        et_title.setText("");
    }


    @OnClick(R.id.tv_send)
    public void send(){
        String title = et_title.getText().toString();
        if (TextUtils.isEmpty(title)){
            ToastUtils.makeText("请输入视频标题");
            return;
        }
        videoSave(title);

    }



    /**
     * 视频保存
     */
    private void videoSave(String boxingVideoTitle) {
        showLoadingDialog();
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("boxingVideoDuration", boxingVideoDuration);
        jsonObject.addProperty("boxingVideoSize", boxingVideoSize);
        jsonObject.addProperty("boxingVideoTitle", boxingVideoTitle);

        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().videoSave(requestBean)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<DataInfo>(mContext) {
            @Override
            protected void _onNext(StatusCode<DataInfo> stringStatusCode) {

                if (stringStatusCode != null){
                    String boxingVideoId = stringStatusCode.getData().getBoxingVideoId();
                    if (!TextUtils.isEmpty(boxingVideoId)){
                        uploadVideo(imageFile,"boxing_video.boxing_video_img",boxingVideoId);
                        uploadVideo(file,"boxing_video.boxing_video_url",boxingVideoId);
                    }else {
                        ToastUtils.makeText("保存失败，未获取到视频ID");
                    }
                }

            }

            @Override
            protected void _onError(String message) {
                dismissLoadingDialog();
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }


    public void uploadVideo(File video,String businessType,String boxingVideoId) {
        MultipartBody.Part body = null;

        if (video.exists()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), video);
            body = MultipartBody.Part.createFormData("file", video.getName(), requestBody);
        }

        Observable observable = ApiUtils.getApi().uploadfile(boxingVideoId, businessType, body)
                .compose(RxHelper.getObservaleTransformer())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(VideoListActivity.this) {
            @Override
            protected void _onNext(StatusCode<Object> stringStatusCode) {
                tag++;
                if (tag == 2){
                    tag = 0;
                    ToastUtils.makeText("上传成功");
                    finish();
                    dismissLoadingDialog();
                }


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                dismissLoadingDialog();
            }
        }, "", lifecycleSubject, false, true);
    }


    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(0);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        if (bitmap == null)
            return null;
        // Scale down the bitmap if it's too large.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight()-100;
        // Log.i(TAG, "bitmap:" + width + " " + height);
        WindowManager wm = getWindowManager();

        int pWidth = wm.getDefaultDisplay().getWidth();
        ;// 容器宽度
        int pHeight = wm.getDefaultDisplay().getHeight();
        ;// 容器高度
        // Log.i(TAG, "parent:" + pWidth + " " + pHeight);
        // 获取宽高跟容器宽高相比较小的倍数，以此为标准进行缩放
        float scale = Math
                .min((float) width / pWidth, (float) height / pHeight);
        // Log.i(TAG, scale + "");
        int w = Math.round(scale * pWidth);
        int h = Math.round(scale * pHeight);
        // Log.i(TAG, "parent:" + w + " " + h);
        bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
        return bitmap;
    }

    public static void startactivity(Context mContext, String videopath, int boxingVideoDuration){
        Intent mIntent = new Intent(mContext,VideoListActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,videopath);
        mIntent.putExtra(Contans.INTENT_TYPE,boxingVideoDuration);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //home back
        JzvdStd.goOnPlayOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //     Jzvd.clearSavedProgress(this, null);
        //home back
        JzvdStd.goOnPlayOnPause();
    }


    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
