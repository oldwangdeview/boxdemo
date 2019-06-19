package com.oldwang.boxdemo.activity;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.ConfigInfo;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.videoview.FUckTest;
import com.oldwang.boxdemo.videoview.FocusImageView;
import com.oldwang.boxdemo.videoview.VideoNewBean;
import com.oldwang.boxdemo.videoview.VideoNewParentBean;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class RecordvideoActivity extends BaseActivity implements
        SurfaceHolder.Callback {

    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.video_new_img_start)
    ImageButton img_start;
    @BindView(R.id.video_new_img_enter)
    Button img_enter;
    @BindView(R.id.video_new_img_right)
    ImageView img_camera;

    @BindView(R.id.video_new_surfaceview)
    SurfaceView surfaceView;

    @BindView(R.id.focusImageView)
    FocusImageView mFocusImageView;

    /** 计时器 */
    private TimeCount timeCount;
    /** 视频最大支持30秒 */
    public static int VIDEO_TIME_END = 30;
    /** 视频最少必须5秒 */
    public static final int VIDEO_TIME = 3;
    /** 录制了多少秒 */
    private int now = 0;
    /** 每次录制结束时是多少秒 */
    private int old;
    /** 屏幕宽度 */
    private int width;
    /** 偶数才执行 */
    private int even;
    /** 是否点击删除了一次 */
    private boolean isOnclick = false;
    /** 录制视频集合 */
    private ArrayList<VideoNewBean> list;
    /** 录制bean */
    private VideoNewBean bean;
    /** 为了能保存到bundler 录制bean */
    private VideoNewParentBean parent_bean;
    /** 录制视频保存文件 */
    private String vedioPath;
    /** 合并之后的视频文件 */
    private String videoPath_merge;
    /** 是否满足视频的最少播放时长 */
    private boolean isMeet = false;
    /** 录制视频的类 */
    private MediaRecorder mMediaRecorder;
    /** 摄像头对象 */
    private Camera mCamera;

    /** 摄像头参数 */
    private Parameters mParameters;
    // /** 视频输出质量 */
    private CamcorderProfile mProfile;
    /** 文本属性获取器 */
    private SharedPreferences mPreferences;
    /** 刷新界面的回调 */
    private SurfaceHolder mHolder;
    /** 1表示后置，0表示前置 */
    private int cameraPosition = 1;
    /** 路径 */
    private String Ppath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/videoTest/";


    // /** 压缩jni */
    // private LoadJNI vk;
    @Override
    protected void initView() {

        setContentView(R.layout.activity_recordvideo);


    }


    @Override
    protected void initData() {
        super.initData();
        surfaceView.setOnTouchListener(new TouchListener());
        widgetListener();
        init();
        baseConfigInfo();
    }

    /**
     * 配置信息
     */
    private void baseConfigInfo() {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("configType", "video");
        jsonObject.addProperty("configCode","duration");
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());


        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().baseConfigInfo(requestBean)
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
                if (stringStatusCode != null) {
                    ConfigInfo configInfo = stringStatusCode.getData().getConfigInfo();
                    if (configInfo != null){
                        ConfigInfo configData = configInfo.getConfigData();

                        if (configData != null){
                            String duration = configData.getDuration();
                            if (!TextUtils.isEmpty(duration)){
                                VIDEO_TIME_END = Integer.valueOf(duration);
                            }

                        }
                    }
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

//    @SuppressWarnings("deprecation")
//    protected void findViews() {
//
//    }

    @OnClick(R.id.video_new_img_back)
    public void finishactivity(){
        if (list.size() > 0) {
            exitVideoNewDialog();
        } else {
            releaseCamera();
            finish();
        }
    }

    @SuppressWarnings("deprecation")
    protected void init() {
        handler.postDelayed(runnable, 0);
        even = 0;
        old = 0;
        // 创建文件夹
        File file = new File(Ppath);
        if (!file.exists()) {
            file.mkdir();
        }
        list = new ArrayList<VideoNewBean>();
        parent_bean = new VideoNewParentBean();
        // 安装一个SurfaceHolder.Callback
        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);
        // 针对低于3.0的Android
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
//		readVideoPreferences();

    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mCamera = getCamera();
        if (mCamera != null) {
            // 因为android不支持竖屏录制，所以需要顺时针转90度，让其游览器显示正常
            mCamera.setDisplayOrientation(90);
            mCamera.lock();
//			initCameraParameters();
            setCameraParameters();
        }
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        releaseCamera();
    }



    private final class TouchListener implements OnTouchListener {

        /** 记录是拖拉照片模式还是放大缩小照片模式 */

        private static final int MODE_INIT = 0;
        /** 放大缩小照片模式 */
        private static final int MODE_ZOOM = 1;
        private int mode = MODE_INIT;// 初始状态

        /** 用于记录拖拉图片移动的坐标位置 */

        private float startDis;
        private float mPosX;
        private float mCurrentPosX;
        final int FLING_MIN_DISTANCE = 200;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            /** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                // 手指压下屏幕
                case MotionEvent.ACTION_DOWN:
                    mode = MODE_INIT;
                    mPosX = event.getX();
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = MODE_ZOOM;
                    /** 计算两个手指间的距离 */
                    startDis = distance(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                // 手指离开屏幕
                case MotionEvent.ACTION_UP:
                    mCurrentPosX = event.getX();

                    if ((mCurrentPosX - mPosX) > FLING_MIN_DISTANCE) {// 向右滑动
//					mContext.sendBroadcast(new Intent(CameraActivity.ACTIVITY_ONTOUCH).putExtra("ACTION_MOVE", 1));
                    } else if (mCurrentPosX - mPosX < 0 && Math.abs(mCurrentPosX - mPosX) > FLING_MIN_DISTANCE) {
//					mContext.sendBroadcast(new Intent(CameraActivity.ACTIVITY_ONTOUCH).putExtra("ACTION_MOVE", 0));
                    }

                    if (mode != MODE_ZOOM) {
                        // 设置聚焦
                        Point point = new Point((int) event.getX(), (int) event.getY());
                        onFocus(point, autoFocusCallback);
                        mFocusImageView.startFocus(point);
                    }
                    break;
            }
            return true;
        }

        /** 计算两个手指间的距离 */
        private float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            /** 使用勾股定理返回两点之间的距离 */
            return (float) Math.sqrt(dx * dx + dy * dy);
        }

    }

    private final AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            // 聚焦之后根据结果修改图片
            if (success) {
                camera.cancelAutoFocus();
                mFocusImageView.onFocusSuccess();
            } else {
                // 聚焦失败显示的图片，由于未找到合适的资源，这里仍显示同一张图片
                mFocusImageView.onFocusFailed();

            }
        }
    };

    /**
     * 手动聚焦
     *
     * @param point
     *            触屏坐标
     */
    protected void onFocus(Point point, AutoFocusCallback callback) {
        Parameters parameters = mCamera.getParameters();
        // 不支持设置自定义聚焦，则使用自动聚焦，返回
        if (parameters.getMaxNumFocusAreas() <= 0) {
            mCamera.autoFocus(callback);
            return;
        }
        List<Area> areas = new ArrayList<Area>();
        int left = point.x - 300;
        int top = point.y - 300;
        int right = point.x + 300;
        int bottom = point.y + 300;
        left = left < -1000 ? -1000 : left;
        top = top < -1000 ? -1000 : top;
        right = right > 1000 ? 1000 : right;
        bottom = bottom > 1000 ? 1000 : bottom;
        areas.add(new Area(new Rect(left, top, right, bottom), 100));
        parameters.setFocusAreas(areas);
        try {
            // 本人使用的小米手机在设置聚焦区域的时候经常会出异常，看日志发现是框架层的字符串转int的时候出错了，
            // 目测是小米修改了框架层代码导致，在此try掉，对实际聚焦效果没影响
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        mCamera.autoFocus(callback);
    }

    /**
     * 获取摄像头实例
     *
     * @version 1.0
     * @createTime 2015年6月16日,上午10:44:11
     * @updateTime 2015年6月16日,上午10:44:11
     * @createAuthor WangYuWen
     * @updateAuthor WangYuWen
     * @updateInfo (此处输入修改内容,若无修改可不写.)
     *
     * @return
     */
    private Camera getCamera() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            camera = null;
        }
        return camera;
    }

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {

            if(handler!=null){
                handler.postDelayed(runnable, 500);
            }
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    protected void widgetListener() {
        img_start.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        even = 1;
                        timeCount = new TimeCount( VIDEO_TIME_END * 1000 - old, 10);
                        timeCount.start();// 开始计时
                        startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("now", now + "");
                        old = now + old;

                        if (old >= VIDEO_TIME * 1000) {
                            isMeet = true;
                        }

                        timeCount.cancel();
                        stopRecord();

                        break;
                }
                return false;
            }
        });


        /** 摄像头切换 */
        img_camera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                switchCamera();
            }
        });

        /** 确认按钮 */
        img_enter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isMeet) {

                    int size = list.size();
                    String[] strs = new String[size];
                    videoPath_merge = Ppath + System.currentTimeMillis()
                            + ".mp4";
                    for (int i = 0; i < size; i++) {
                        strs[i] = list.get(i).getPath();
                    }
                    try {
                        FUckTest.appendVideo(strs, videoPath_merge);

                        for (int i = size - 1; i >= 0; i--) {
                            File file = new File(list.get(i).getPath());
                            if (file.exists()) {
                                file.delete();
                            }
                            list.remove(i);
                        }
                        Log.e("path", videoPath_merge);


                        VideoListActivity.startactivity(mContext,videoPath_merge,now);
                        finish();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, "视频最少必须录制3秒以上才能用！",
                            Toast.LENGTH_LONG).show();
                }
            }
        });



        surfaceView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mParameters != null && mCamera != null) {
                    mParameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);
                    try {
                        mCamera.setParameters(mParameters);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 弹出对话框
     *
     * @version 1.0
     * @createTime 2015年6月16日,下午3:45:35
     * @updateTime 2015年6月16日,下午3:45:35
     * @createAuthor WangYuWen
     * @updateAuthor WangYuWen
     * @updateInfo (此处输入修改内容,若无修改可不写.)
     *
     */
    private void exitVideoNewDialog() {

        Builder builder = new Builder(mContext);
        builder.setMessage("确定放弃这段视频吗？");
        builder.setTitle("温馨提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                for (int i = 0; i < list.size(); i++) {
                    File file = new File(list.get(i).getPath());
                    if (file.exists()) {
                        file.delete();
                    }
                }
              finish();
            }

        });
        builder.create().show();
    }

    /**
     * 切换摄像头
     *
     * @version 1.0
     * @createTime 2015年6月16日,上午10:40:17
     * @updateTime 2015年6月16日,上午10:40:17
     * @createAuthor WangYuWen
     * @updateAuthor WangYuWen
     * @updateInfo (此处输入修改内容,若无修改可不写.)
     *
     */
    @SuppressLint("NewApi")
    private void switchCamera() {
        // 切换前后摄像头
        int cameraCount = 0;
        CameraInfo cameraInfo = new CameraInfo();
        cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
            if (cameraPosition == 1) {
                // 现在是后置，变更为前置
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
                    // CAMERA_FACING_BACK后置
                    // 前置摄像头时必须关闭闪光灯，不然会报错
                    if (mParameters != null) {
                        if (mParameters.getFlashMode() != null
                                && mParameters.getFlashMode().equals(
                                Parameters.FLASH_MODE_TORCH)) {
                            mParameters.setFlashMode(Parameters.FLASH_MODE_OFF);
//                            img_flashlight
//                                    .setImageResource(R.drawable.img_video_new_flashlight_close);
                        }
                        if (mCamera != null) {
                            mCamera.setParameters(mParameters);
                        }
                    }

                    // 释放Camera
                    releaseCamera();

                    // 打开当前选中的摄像头
                    mCamera = Camera.open(i);
                    mCamera.setDisplayOrientation(90);
                    mCamera.lock();

                    // 通过surfaceview显示取景画面
                    setStartPreview(mHolder);

                    cameraPosition = 0;

                    break;
                }
            } else {
                // 现在是前置， 变更为后置
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
                    // CAMERA_FACING_BACK后置
                    // 释放Camera
                    releaseCamera();
                    // 打开当前选中的摄像头
                    mCamera = Camera.open(i);
                    mCamera.setDisplayOrientation(90);
                    mCamera.lock();

                    // 通过surfaceview显示取景画面
                    setStartPreview(mHolder);

                    cameraPosition = 1;

                    break;
                }
            }

        }
    }

    /**
     * 定义一个倒计时的内部类
     *
     * @Description
     * @author
     * @version 1.0
     * @date 2015-5-25
     * @Copyright: Copyright (c) 2015 Shenzhen Utoow Technology Co., Ltd. All
     *             rights reserved.
     */
    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发

        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            now = (int) (30000 - millisUntilFinished - old);
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss:SSS");
            time.setText(sdf.format(new Date(Long.valueOf((now+old)))));
            if ((old > 0 && old > VIDEO_TIME * 1000)
                    || (old == 0 && now > VIDEO_TIME * 1000)) {
                img_enter.setEnabled(true);
            }

        }
    }

    /**
     * 初始化摄像头参数
     *
     * @version 1.0
     * @createTime 2015年6月15日,下午4:53:41
     * @updateTime 2015年6月15日,下午4:53:41
     * @createAuthor WangYuWen
     * @updateAuthor WangYuWen
     * @updateInfo (此处输入修改内容,若无修改可不写.)
     *
     */
    @SuppressWarnings("deprecation")
    private void initCameraParameters() {
        // 初始化摄像头参数
        mParameters = mCamera.getParameters();

        mParameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);

        // 设置白平衡参数。
        String whiteBalance = mPreferences.getString(
                "pref_camera_whitebalance_key", "auto");
        if (isSupported(whiteBalance, mParameters.getSupportedWhiteBalance())) {
            mParameters.setWhiteBalance(whiteBalance);
        }

        // 参数设置颜色效果。
        String colorEffect = mPreferences.getString(
                "pref_camera_coloreffect_key", "none");
        if (isSupported(colorEffect, mParameters.getSupportedColorEffects())) {
            mParameters.setColorEffect(colorEffect);
        }

        try {
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setCameraParameters() {
        Parameters parameters = mCamera.getParameters();
        // 选择合适的预览尺寸
        List<Size> sizeList = parameters.getSupportedPreviewSizes();
        if (sizeList.size() > 0) {
            // Size cameraSize=sizeList.get(0);
            // 预览图片大小
//			Log.e("whith:hight", "" + this.getWidth() + ":" + this.getHeight());
            for (Size cameraSize : sizeList) {
                if (cameraSize.width * 9 == cameraSize.height * 16) {
                    Log.e("da",
                            cameraSize.width + ":" + cameraSize.height);
                    parameters.setPreviewSize(cameraSize.width,
                            cameraSize.height);
                    break;
                }
            }

        }
        // 设置生成的图片大小
        sizeList = parameters.getSupportedPictureSizes();
        if (sizeList.size() > 0) {
            Size cameraSize = sizeList.get(0);
            for (Size size : sizeList) {

                if (size.width * 9 == size.height * 16) {
                    Log.e("da ", size.width
                            + ":" + size.height);
                    cameraSize = size;
                    break;
                }

                // }

            }
            for (Size size : sizeList) {
                // 小于100W像素

                if (size.width * size.height < 100 * 10000
                        && size.width * size.height > 20 * 10000) {
                    cameraSize = size;
                    break;
                }

            }
            parameters.setPictureSize(cameraSize.width, cameraSize.height);
        }
        // 设置图片格式
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setJpegQuality(100);
        parameters.setJpegThumbnailQuality(100);
        // 自动聚焦模式
        parameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(parameters);
        // 设置闪光灯模式。此处主要是用于在相机摧毁后又重建，保持之前的状态
//		setFlashMode(mFlashMode);
//		// 设置缩放级别
//		setZoom(mZoom);
//		// 开启屏幕朝向监听
//		startOrientationChangeListener();
    }
    /**
     * 开始录制
     *
     * @version 1.0
     * @createTime 2015年6月15日,下午4:48:49
     * @updateTime 2015年6月15日,下午4:48:49
     * @createAuthor WangYuWen
     * @updateAuthor WangYuWen
     * @updateInfo (此处输入修改内容,若无修改可不写.)
     *
     */
    @SuppressLint("NewApi")
    private void startRecord() {
        try {
            bean = new VideoNewBean();
            vedioPath = Ppath + System.currentTimeMillis() + ".mp4";
            bean.setPath(vedioPath);

            mCamera.unlock();
            mMediaRecorder = new MediaRecorder();// 创建mediaRecorder对象
            mMediaRecorder.setCamera(mCamera);
            // 设置录制视频源为Camera(相机)
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setProfile( CamcorderProfile
                    .get(CamcorderProfile.QUALITY_720P));

            // mMediaRecorder.setVideoSize(560,560);//设置视频大小（分辨率）

            mMediaRecorder.setVideoEncodingBitRate(1024 * 1024);// 设置视频一次写多少字节(可调节视频空间大小)

            // 最大期限
            mMediaRecorder.setMaxDuration(35 * 1000);

            // 第4步:指定输出文件 ， 设置视频文件输出的路径

            mMediaRecorder.setOutputFile(vedioPath);

            mMediaRecorder.setPreviewDisplay(mHolder.getSurface());

            // // 设置保存录像方向
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                if (cameraPosition == 1) {
                    // 由于不支持竖屏录制，后置摄像头需要把视频顺时针旋转90度、、但是视频本身在电脑上看还是逆时针旋转了90度
                    mMediaRecorder.setOrientationHint(90);
                } else if (cameraPosition == 0) {
                    // 由于不支持竖屏录制，前置摄像头需要把视频顺时针旋转270度、、而前置摄像头在电脑上则是顺时针旋转了90度
                    mMediaRecorder.setOrientationHint(270);
                }
            }

            mMediaRecorder.setOnInfoListener(new OnInfoListener() {

                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {

                }
            });

            mMediaRecorder.setOnErrorListener(new OnErrorListener() {

                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    recodError();
                }
            });

            // 第6步:根据以上配置准备MediaRecorder

            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            recodError();
        } catch (IOException e) {
            e.printStackTrace();
            recodError();
        } catch (RuntimeException e) {
            e.printStackTrace();
            recodError();
        }

    }

    /**
     * 异常处理
     *
     * @version 1.0
     * @createTime 2015年6月16日,上午10:49:18
     * @updateTime 2015年6月16日,上午10:49:18
     * @createAuthor WangYuWen
     * @updateAuthor WangYuWen
     * @updateInfo (此处输入修改内容,若无修改可不写.)
     *
     */
    private void recodError() {
        Builder builder = new Builder(mContext);
        builder.setMessage("该设备暂不支持视频录制");
        builder.setTitle("出错啦");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
               finish();
            }

        });
        builder.create().show();

    }

    /**
     * 结束录制
     *
     * @version 1.0
     * @createTime 2015年6月15日,下午4:48:53
     * @updateTime 2015年6月15日,下午4:48:53
     * @createAuthor WangYuWen
     * @updateAuthor WangYuWen
     * @updateInfo (此处输入修改内容,若无修改可不写.)
     *
     */
    private void stopRecord() {

        if (bean != null) {
            if (list.size() > 0) {
                bean.setTime(now - list.get(list.size() - 1).getTime());
            } else {
                bean.setTime(now);
            }
            bean.setCameraPosition(cameraPosition);
            list.add(bean);
        }

        if (mMediaRecorder != null) {
            try {
                // 停止录像，释放camera
                mMediaRecorder.setOnErrorListener(null);
                mMediaRecorder.setOnInfoListener(null);
                mMediaRecorder.stop();
                // 清除recorder配置
                mMediaRecorder.reset();
                // 释放recorder对象
                mMediaRecorder.release();
                mMediaRecorder = null;
                // 没超过3秒就删除录制所有数据
                if (old < 1000) {
                    clearList();
                }
            } catch (Exception e) {
                Log.e("yihcang", "yihcang");
                clearList();
            }
        }
    }

    /**
     * 清楚数据
     *
     * @version 1.0
     * @createTime 2015年6月25日,下午6:04:28
     * @updateTime 2015年6月25日,下午6:04:28
     * @createAuthor WangYuWen
     * @updateAuthor WangYuWen
     * @updateInfo (此处输入修改内容,若无修改可不写.)
     *
     */
    private void clearList() {
        Toast.makeText(mContext, "单次录制视频最少3秒", Toast.LENGTH_LONG).show();

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                File file = new File(list.get(list.size() - 1).getPath());
                if (file.exists()) {
                    file.delete();
                }
            }
            list.remove(list.size() - 1);
            if (list.size() <= 0) {
                img_enter.setVisibility(View.VISIBLE);
                img_camera.setVisibility(View.VISIBLE);
            }
        }
    }

    private static boolean isSupported(String value, List<String> supported) {
        return supported == null ? false : supported.indexOf(value) >= 0;
    }

    public static boolean getVideoQuality(String quality) {
        return "youtube".equals(quality) || "high".equals(quality);
    }

    /**
     * 设置摄像头参数
     *
     * @version 1.0
     * @createTime 2015年6月15日,下午5:12:31
     * @updateTime 2015年6月15日,下午5:12:31
     * @createAuthor WangYuWen
     * @updateAuthor WangYuWen
     * @updateInfo (此处输入修改内容,若无修改可不写.)
     *
     */
    private void readVideoPreferences() {
        String quality = mPreferences.getString("pref_video_quality_key",
                "high");

        boolean videoQualityHigh = getVideoQuality(quality);

        // 设置视频质量。
        Intent intent = getIntent();
        if (intent.hasExtra(MediaStore.EXTRA_VIDEO_QUALITY)) {
            int extraVideoQuality = intent.getIntExtra(
                    MediaStore.EXTRA_VIDEO_QUALITY, 0);
            videoQualityHigh = (extraVideoQuality > 0);
        }

        videoQualityHigh = false;
        mProfile = CamcorderProfile
                .get(videoQualityHigh ? CamcorderProfile.QUALITY_HIGH
                        : CamcorderProfile.QUALITY_LOW);
        mProfile.videoFrameWidth = (int) (mProfile.videoFrameWidth * 2.0f);
        mProfile.videoFrameHeight = (int) (mProfile.videoFrameHeight * 2.0f);
        mProfile.videoBitRate = 256000 * 3;

        CamcorderProfile highProfile = CamcorderProfile
                .get(CamcorderProfile.QUALITY_HIGH);
        mProfile.videoCodec = highProfile.videoCodec;
        mProfile.audioCodec = highProfile.audioCodec;
        mProfile.fileFormat = MediaRecorder.OutputFormat.MPEG_4;
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(holder);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

        // 先开启在关闭 先开启录制在关闭可以 解决游览的时候比较卡顿的现象，但是会有视频开启时声音。打开这个功能时较慢
        // startRecord();
        // stopRecord();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    /**
     * 设置camera显示取景画面,并预览
     *
     * @version 1.0
     * @createTime 2015年6月16日,上午10:48:15
     * @updateTime 2015年6月16日,上午10:48:15
     * @createAuthor WangYuWen
     * @updateAuthor WangYuWen
     * @updateInfo (此处输入修改内容,若无修改可不写.)
     *
     * @param holder
     */
    private void setStartPreview(SurfaceHolder holder) {
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException e) {

        }
    }

    /**
     * 释放Camera
     *
     * @version 1.0
     * @createTime 2015年6月16日,上午10:38:08
     * @updateTime 2015年6月16日,上午10:38:08
     * @createAuthor WangYuWen
     * @updateAuthor WangYuWen
     * @updateInfo (此处输入修改内容,若无修改可不写.)
     *
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();// 停掉原来摄像头的预览
            mCamera.release();
            mCamera = null;
        }
    }



    public String saveMyBitmap(Bitmap mBitmap, String bitName) {
        String path = "/sdcard/DCIM/Camera" + bitName + ".jpg";
        File f = new File(path);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
        this.handler = null;
    }

    public static Bitmap createVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(1);
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }

        if (bitmap == null) return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int max = Math.max(width, height);
        if (max > 512) {
            float scale = 512f / max;
            int w = Math.round(scale * width);
            int h = Math.round(scale * height);
            bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
        }
        return bitmap;
    }


    public static void setartactivity(Context mContext){
        Intent mIntent  = new Intent(mContext,RecordvideoActivity.class);
        mContext.startActivity(mIntent);
    }
}
