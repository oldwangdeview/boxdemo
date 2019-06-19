package com.oldwang.boxdemo.http;


import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.oldwang.boxdemo.application.AbsSuperApplication;
import com.oldwang.boxdemo.base.ActivityLifeCycleEvent;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.Security;
import com.oldwang.boxdemo.bean.StatusCode;
import com.oldwang.boxdemo.contans.Contans;
import com.oldwang.boxdemo.util.AesUtil;
import com.oldwang.boxdemo.util.LogUntil;
import com.oldwang.boxdemo.util.LogUtils;
import com.oldwang.boxdemo.util.UIUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.PublishSubject;

/**
 *
 *
 */

public class HttpUtil {

    /**
     * 构造方法私有
     */
    private HttpUtil() {

    }

    /**
     * 在访问HttpMethods时创建单例
     */
    private static class SingletonHolder {
        private static final HttpUtil INSTANCE = new HttpUtil();
    }

    /**
     * 获取单例
     */
    public static HttpUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 添加线程管理并订阅
     *
     * @param ob               处罚
     * @param subscriber       处理
     * @param cacheKey         缓存kay
     * @param lifecycleSubject 生命周期
     * @param isSave           是否缓存
     * @param forceRefresh     是否强制刷新
     */
    public void toSubscribe(Observable ob, final ProgressSubscriber subscriber, String cacheKey, final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject, boolean isSave, boolean forceRefresh) {
        //数据预处理
        ObservableTransformer<StatusCode<Object>, StatusCode<Object>> result = RxHelper.handleResult(ActivityLifeCycleEvent.DESTROY, lifecycleSubject);
        Observable observable = ob.compose(result);
              /*  .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        //显示Dialog和一些其他操作
                        subscriber.showProgressDialog();
                    }
                });*/
        RetrofitCache.load(cacheKey, observable, isSave, forceRefresh).subscribe(subscriber);
    }






    public static RequestBean getRequsetBean(Context context, JsonObject jsonObject) {

        RequestBean requestBean = new RequestBean();

        String json = jsonObject.toString();
        LogUtils.e("测试",json);
        Security security = new Security();
//        String xx = "1";
        String temp = Contans.appid + Contans.salt;

        String token = AbsSuperApplication.getToken();
        String token_fianl = UIUtils.getMd5Value(temp);


        String param = AesUtil.encode(json);

        requestBean.setParam(param);
        String lol = UIUtils.getMd5Value(param);
        String temp1 = lol + token_fianl + Contans.appid;
        String sign = UIUtils.getMd5Value(temp1);
        if (!TextUtils.isEmpty(token)) {
            security.setToken(token);
        }
//        security.setXx(xx);
        security.setSign(sign);
        requestBean.setSecurity(security);
        LogUntil.show(context, "测试", new Gson().toJson(requestBean));
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        return requestBean;
    }


//    代理申请：
//    上传身份证正反面
//            business_type = member_agent_apply.card_positive
//    business_type =member_agent_apply.card_negative
//            business_id = 代理审核ID
//
//    基地入驻：
//    上传身份证正反面和营业执照照片
//            business_type = b_base_info.file_data
//    business_id =基地ID
//
//              申请售后：
//              上传图片
//            business_type = shop_after_sale_service
//               business_id =服务订单ID
//
//    发表视频：上传视频.
//            business_type =
//    boxing_video.boxing_video_img
//            business_type = boxing_video.boxing_video_url
//    business_id =视频ID
//
//
//    个人资料上传头像：
//    business_type =
//    member_info.member_headurl
//            business_id = 会员ID
//
//    个人身份认证：
//    business_type =
//    member_info.positive_img 正面
//    business_type =
//    member_info.negative_img 反面
//    business_id =会员ID

//    评价的图片类型  shop_order_recommend_pics


}
