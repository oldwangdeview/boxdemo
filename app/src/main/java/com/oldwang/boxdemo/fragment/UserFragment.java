package com.oldwang.boxdemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.activity.AddressManagmentActivity;
import com.oldwang.boxdemo.activity.BaseCheckInActivity;
import com.oldwang.boxdemo.activity.CustomerActivity;
import com.oldwang.boxdemo.activity.MyAddMessageActivity;
import com.oldwang.boxdemo.activity.MyAppoinmentActivity;
import com.oldwang.boxdemo.activity.MyCollectionActivity;
import com.oldwang.boxdemo.activity.MyCouponActivity;
import com.oldwang.boxdemo.activity.MyMessageActivity;
import com.oldwang.boxdemo.activity.MyOrderListActivity;
import com.oldwang.boxdemo.activity.MyWalletActivity;
import com.oldwang.boxdemo.activity.SetingActivity;
import com.oldwang.boxdemo.activity.UserInfoActivity;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.BaseActivity;
import com.oldwang.boxdemo.base.BaseFragment;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.MemberInfo;
import com.oldwang.boxdemo.bean.MsgInfo;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.event.UpdateNoticeEvent;
import com.oldwang.boxdemo.event.UpdateUserEvent;
import com.oldwang.boxdemo.http.HttpUtil;
import com.oldwang.boxdemo.http.ProgressSubscriber;
import com.oldwang.boxdemo.http.RxHelper;
import com.oldwang.boxdemo.rxjava.ApiUtils;
import com.oldwang.boxdemo.util.ToastUtils;
import com.oldwang.boxdemo.util.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class UserFragment extends BaseFragment {

    @BindView(R.id.heand_image)
    ImageView heand_image;

    @BindView(R.id.nick_name)
    TextView nick_name;

    @BindView(R.id.tv_msg_count)
    TextView tv_msg_count;


    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragmnet_muser);
    }

    @Override
    protected void initData() {
        super.initData();
        getUserInfo();
        isMsgRead();
    }


    /**
     * 获取用户信息
     */
    private void getUserInfo() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().getUerInfo(requestBean)
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
                    DataInfo data = stringStatusCode.getData();
                   MemberInfo userInfo = data.getMemberInfo();
                   if (userInfo != null){
                       if (!TextUtils.isEmpty(userInfo.getMemberNickname())) {
                           nick_name.setText(userInfo.getMemberNickname());
                       }
                       if (!TextUtils.isEmpty(userInfo.getMemberHeadLogo())) {
                           UIUtils.loadImageView(mContext, userInfo.getMemberHeadLogo(), heand_image);
                           BaseActivity.setmUserHeandImage(userInfo.getMemberHeadLogo());
                       }else{
                           BaseActivity.setmUserHeandImage("");
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


    private String readCount;

    /**
     * 获取未读消息条数
     */
    private void isMsgRead() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", AbsSuperApplication.getMemberId());
        RequestBean requestBean = HttpUtil.getRequsetBean(mContext, jsonObject);
        Observable observable =
                ApiUtils.getApi().isMsgRead(requestBean)
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
                    DataInfo data = stringStatusCode.getData();
                    MsgInfo msgInfo = data.getMsgInfo();
                    
                    if (msgInfo != null){
                        if (!TextUtils.isEmpty(msgInfo.getReadCount())){
                            readCount = msgInfo.getReadCount();
                        }
                    }
                    int count = 0;
                    if (!TextUtils.isEmpty(readCount)){
                         count = new BigDecimal(readCount).intValue();
                    }

                    if (count > 0){
                        tv_msg_count.setVisibility(View.VISIBLE);
                        tv_msg_count.setText(readCount);
                    }else {
                        tv_msg_count.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);

            }
        }, "", lifecycleSubject, false, true);

    }

    /**
     * 场馆入住
     */
    @OnClick(R.id.layout_basecheckin)
    public void gotoBaseCheckInActivity(){
        BaseCheckInActivity.startactivity(mContext);
    }
    /**
     * 地址管理
     */
    @OnClick(R.id.layout_addressmanagment)
    public void gotoAddressMeanagementActivity(){
        AddressManagmentActivity.startactivity(mContext);
    }
    /**
     * 个人资料
     */
    @OnClick(R.id.layout_userinfo)
    public void gotoUserinfoActivity(){
        UserInfoActivity.startactivity(mContext);
    }
    /**
     * 设置
     */
    @OnClick(R.id.layout_seting)
    public void gotoSeting(){
        SetingActivity.startactivity(mContext);
    }

    /***
     * 联系客服
     */
    @OnClick(R.id.layout_callCustomeractivity)
    public void gotocallcustomeractivity(){
        CustomerActivity.startactivity(mContext);
    }
    /**
     * 我的订单
     */
    @OnClick(R.id.layout_myorderlist)
    public void gotoMyorderListactivity(){
        MyOrderListActivity.startactivity(mContext, 0);
    }

    /**
     * 我的发布
     */
    @OnClick(R.id.layout_addmessage)
    public void gotoMyAddMessaageActivity(){
        MyAddMessageActivity.startactivity(mContext);
    }
    /**
     * 我的钱包
     */
    @OnClick(R.id.layout_mywellat)
    public void  gotoMyWellatActivity(){
        MyWalletActivity.startactivity(mContext);
    }
    /**
     * 我的收藏
     */
    @OnClick(R.id.layout_collection)
    public void gotoMycollection(){
        MyCollectionActivity.startactivity(mContext);
    }
    /**
     * 我的消息
     */
    @OnClick(R.id.notice_image)
    public void gotoMymessageActivity(){
        MyMessageActivity.startactivity(mContext);
    }
    /**
     * 我的优惠券
     */
    @OnClick(R.id.layout_coupon)
    public void gotoCouponActivity(){
        MyCouponActivity.startactivity(mContext);
    }

    /**
     * 我的预约
     */
    @OnClick(R.id.layout_yuyue)
    public  void gotoMyAppomentActivity(){
        MyAppoinmentActivity.startactivity(mContext);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 更新消息未读数量
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateList(UpdateNoticeEvent event) {
        isMsgRead();
    }


    /**
     * 更新用户信息
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateUserEvent event) {
        getUserInfo();
    }
}
