package com.oldwang.boxdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.rxjava.Api;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的邀请吗
 */
public class InvitationcodeActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    @BindView(R.id.shareimage)
    ImageView share_image;
    @BindView(R.id.iv_qr_code)
    ImageView iv_qr_code;


    @BindView(R.id.tv_code)
    TextView tv_code;

    private Bitmap img;
    private ImageHandler imgHandler = new ImageHandler();

    private String invitateCode;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_invitationcode);
    }

    @Override
    protected void initData() {
        super.initData();
        titlename.setText("我的邀请码");
        share_image.setVisibility(View.VISIBLE);
        invitateCode = getIntent().getStringExtra(Contans.INTENT_DATA);
        tv_code.setText("邀请码："+invitateCode);
        downloadImg();
    }
    /**
     * 异步从服务器加载图片数据
     */
    private void downloadImg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap img =  getImg();
                Message msg = imgHandler.obtainMessage();
                msg.what = 0;
                msg.obj = img;
                imgHandler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 异步线程请求到的图片数据，利用Handler，在主线程中显示
     */
    class ImageHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    img = (Bitmap)msg.obj;
                    if(img != null){
                        iv_qr_code.setImageBitmap(img);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 从服务器读取图片流数据，并转换为Bitmap格式
     * @return Bitmap
     */
    private Bitmap getImg(){
        Bitmap img = null;

        try {
            String url = Api.baseUrl + "/api/member/generateMemberCode.do ";
            URL imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();

            conn.setRequestMethod("POST");
            conn.setConnectTimeout(1000 * 6);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            conn.setRequestProperty("Content-Type", "JSON");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.connect();

            //输出流写参数
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
            RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
            String param = new Gson().toJson(requestBean);
            dos.writeBytes(param);
            dos.flush();
            dos.close();

            int resultCode = conn.getResponseCode();

            if(HttpURLConnection.HTTP_OK == resultCode){
                InputStream is = conn.getInputStream();
                img = BitmapFactory.decodeStream(is);
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }



    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void overnowactivity() {
        finish();
    }

    @OnClick(R.id.shareimage)
    public void share() {
        share(1, "", "", "");
    }




    public static void startactivity(Context mContex,String invitateCode) {
        Intent mIntent = new Intent(mContex, InvitationcodeActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,invitateCode);
        mContex.startActivity(mIntent);
    }



}
