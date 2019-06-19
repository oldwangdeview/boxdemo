package com.oldwang.boxdemo.rxjava;


import com.oldwang.boxdemo.bean.BankData;
import com.oldwang.boxdemo.bean.CouponData;
import com.oldwang.boxdemo.bean.DataInfo;
import com.oldwang.boxdemo.bean.DataInfo1;
import com.oldwang.boxdemo.bean.DataInfo2;
import com.oldwang.boxdemo.bean.GoodsOrderData;
import com.oldwang.boxdemo.bean.GoodsOrderDataTwo;
import com.oldwang.boxdemo.bean.ListData;
import com.oldwang.boxdemo.bean.RequestBean;
import com.oldwang.boxdemo.bean.StatusCode;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Author wangshifu
 * Dest  访问网络的方法
 */
public interface Api {
    boolean isRelease = false;
    /**
     * 发布地址
     */
    String baseUrl = "http://my.cdsxlc.cn:11006";
//    String baseUrl = "http://125.71.134.238:11006";

    /**
     * 测试地址
     */
    String testBaseUrl = "http://my.cdsxlc.cn:11006";
    String imageUrl = "http://my.cdsxlc.cn:8087";
//    String testBaseUrl = "http://125.71.134.238:11006";



    /**
     *  单个文件上传
     **/
    @Multipart
    @POST("/api/common/appUpload.do")
    Observable<StatusCode<Object>> uploadfile(
            @Query("businessId") String businessId,
            @Query("businessType") String businessType,
            @Part MultipartBody.Part file);

    /**
     *  多个文件上传
     **/
    @Multipart
    @POST("/api/common/appUpload.do")
    Observable<StatusCode<Object>> uploadfileList1(
            @Query("businessId") String businessId,
            @Query("businessType") String businessType,
            @Part List<MultipartBody.Part> files);


    /**
     * 注册
     **/
    @POST("/api/member/register.do")
    Observable<StatusCode<DataInfo>> register(
            @Body RequestBean requestBean);


    /**
     * 获取验证码
     **/
    @POST("/api/member/identifyingCode.do")
    Observable<StatusCode<Object>> getCode(
            @Body RequestBean requestBean);

    /**
     * 找回密码
     **/
    @POST("/api/member/forgetPwd.do")
    Observable<StatusCode<Object>> forgetPwd(
            @Body RequestBean requestBean);

    /**
     * 重置密码
     **/
    @POST("/api/member/resetpwd.do")
    Observable<StatusCode<Object>> resetpwd(
            @Body RequestBean requestBean);


    /**
     * 登录
     **/
    @POST("/api/member/login.do")
    Observable<StatusCode<DataInfo>> login(
            @Body RequestBean requestBean);

    /**
     * 获取首页轮播图和商品推荐
     **/
    @POST("/api/home/page01.do")
    Observable<StatusCode<DataInfo>> getBannerAndGoods(
            @Body RequestBean requestBean);


    /**
     * 获取武动拳名
     **/
    @POST("/api/home/page03.do")
    Observable<StatusCode<DataInfo>> getPage03(
            @Body RequestBean requestBean);

    /**
     * 获取功夫快讯
     **/
    @POST("/api/home/page02.do")
    Observable<StatusCode<DataInfo>> getPage02(
            @Body RequestBean requestBean);

    /**
     * 获取十佳馆校-场馆
     **/
    @POST("/api/home/page08.do")
    Observable<StatusCode<DataInfo>> getPage08(
            @Body RequestBean requestBean);


    /**
     * 获取十佳馆校-学校
     **/
    @POST("/api/home/page07.do")
    Observable<StatusCode<DataInfo>> getPage07(
            @Body RequestBean requestBean);


    /**
     *  获取名师战将数据
     **/
    @POST("/api/home/page05.do")
    Observable<StatusCode<DataInfo>> getPage05(
            @Body RequestBean requestBean);

    /**
     *  获取名师战将数据
     **/
    @POST("/api/home/page06.do")
    Observable<StatusCode<DataInfo>> getPage06(
            @Body RequestBean requestBean);


    /**
     *  训练基地
     **/
    @POST("/api/home/page04.do")
    Observable<StatusCode<DataInfo>> getPage04(
            @Body RequestBean requestBean);



    /**
     * 获取个人信息
     **/
    @POST("/api/member/memberInfo.do")
    Observable<StatusCode<DataInfo>> getUerInfo(
            @Body RequestBean requestBean);



    /**
     * 修改昵称
     **/
    @POST("/api/member/updateNick.do")
    Observable<StatusCode<DataInfo>> updateNick(
            @Body RequestBean requestBean);
    /**
     * 身份认证
     **/
    @POST("/api/member/memberUpdate.do")
    Observable<StatusCode<Object>> memberUpdate(
            @Body RequestBean requestBean);



    /**
     * 获取地址列表
     **/
    @POST("/api/member/addressList.do")
    Observable<StatusCode<DataInfo>> addressList(
            @Body RequestBean requestBean);


    /**
     * 新增修改地址
     **/
    @POST("/api/member/addressSave.do")
    Observable<StatusCode<DataInfo>> saveMyAddress(
            @Body RequestBean requestBean);

    /**
     * 获取省市区
     **/
    @POST("/api/comm/regionList.do")
    Observable<StatusCode<DataInfo>> regionList(
            @Body RequestBean requestBean);


    /**
     * 删除收藏
     **/
    @POST("/api/member/delFavorite.do")
    Observable<StatusCode<DataInfo>> delFavorite(
            @Body RequestBean requestBean);

    /**
     * 收藏列表
     **/
    @POST("/api/member/favoriteListByType.do")
    Observable<StatusCode<DataInfo>> favoriteListByType(
            @Body RequestBean requestBean);



    /**
     * 我的发布-我的视频
     **/
    @POST("/api/member/myVideoList.do")
    Observable<StatusCode<DataInfo>> myVideoList(
            @Body RequestBean requestBean);

    /**
     * 我的钱包-可提现信息
     **/
    @POST("/api/member/withdrawalMoneyInfo.do")
    Observable<StatusCode<DataInfo>> withdrawalMoneyInfo(
            @Body RequestBean requestBean);


    /**
     * 我的预约
     **/
    @POST("/api/myBespeakEvaluateList.do")
    Observable<StatusCode<DataInfo>> myBespeakEvaluateList(
            @Body RequestBean requestBean);


    /**
     * 我的钱包-我的积分
     **/
    @POST("/api/score/myScore.do")
    Observable<StatusCode<DataInfo>> myScore(
            @Body RequestBean requestBean);

    /**
     * 我的钱包-收益记录
     **/
    @POST("/api/member/incomeResult.do")
    Observable<StatusCode<DataInfo>> incomeResult(
            @Body RequestBean requestBean);

    /**
     * 我的钱包-提现申请
     **/
    @POST("/api/member/memberWithdrawal.do")
    Observable<StatusCode<DataInfo>> memberWithdrawal(
            @Body RequestBean requestBean);


    /**
     * 我的钱包-积分规则
     **/
    @POST("/api/score/scoreRuleInfo.do")
    Observable<StatusCode<DataInfo>> scoreRuleInfo(
            @Body RequestBean requestBean);


    /**
     * 我的钱包-积分提现
     **/
    @POST("/api/score/scoreWithdrawSave.do")
    Observable<StatusCode<DataInfo>> scoreWithdrawSave(
            @Body RequestBean requestBean);

    /**
     * 银行卡列表
     **/
    @POST("/api/member/bankCardList.do")
    Observable<StatusCode<List<BankData>>> bankCardList(
            @Body RequestBean requestBean);

    /**
     * 新增/修改银行卡信息
     **/
    @POST("/api/member/bankCardSave.do")
    Observable<StatusCode<DataInfo>> bankCardSave(
            @Body RequestBean requestBean);

    /**
     * 删除银行卡信息
     **/
    @POST("/api/member/bankCardDel.do")
    Observable<StatusCode<DataInfo>> bankCardDel(
            @Body RequestBean requestBean);


    /**
     * 提现申请列表
     **/
    @POST("/api/member/memberWithdrawalList.do")
    Observable<StatusCode<DataInfo>> memberWithdrawalList(
            @Body RequestBean requestBean);

    /**
     * 积分提现申请列表
     **/
    @POST("/api/score/withdrawalList.do")
    Observable<StatusCode<DataInfo>> scoreWithdrawalList(
            @Body RequestBean requestBean);

    /**
     * 总积分排行榜
     **/
    @POST("/api/score/rankingList.do")
    Observable<StatusCode<DataInfo>> rankingList(
            @Body RequestBean requestBean);


    /**
     * 日积分排行榜
     **/
    @POST("/api/score/dayScoreList.do")
    Observable<StatusCode<DataInfo>> dayScoreList(
            @Body RequestBean requestBean);

    /**
     * 代理申请
     **/
    @POST("/api/member/agentApply.do")
    Observable<StatusCode<DataInfo>> agentApply(
            @Body RequestBean requestBean);

    /**
     * 基地入驻申请
     **/
    @POST("/api/base/baseEnter.do")
    Observable<StatusCode<DataInfo>> baseEnter(
            @Body RequestBean requestBean);


    /**
     * 团队成员列表
     **/
    @POST("/api/member//teamMemberList.do")
    Observable<StatusCode<DataInfo1>> teamMemberList(
            @Body RequestBean requestBean);

    /**
     * 我的消息
     **/
    @POST("/api/member/msgList.do")
    Observable<StatusCode<DataInfo>> msgList(
            @Body RequestBean requestBean);

    /**
     * 入驻基地信息
     **/
    @POST("/api/base/baseEnterInfo.do")
    Observable<StatusCode<DataInfo>> baseEnterInfo(
            @Body RequestBean requestBean);



    /**
     * 商品分类
     **/
    @POST("/api/commodity/commodityTypeList.do")
    Observable<StatusCode<DataInfo>> commodityTypeList(
            @Body RequestBean requestBean);

    /**
     * 拳联纪实--选择馆校/战将名师类型
     **/
    @POST("/api/news/types.do")
    Observable<StatusCode<DataInfo>> types(
            @Body RequestBean requestBean);

    /**
     * 拳联纪实--功夫快讯列表
     **/
    @POST("/api/news/newsList.do")
    Observable<StatusCode<DataInfo>> newsList(
            @Body RequestBean requestBean);

    /**
     * 拳联纪实--功夫快讯详情
     **/
    @POST("/api/news/newInfo.do")
    Observable<StatusCode<DataInfo1>> newInfo(
            @Body RequestBean requestBean);

    /**
     * 拳联纪实--功夫快讯评论、回复
     **/
    @POST("/api/news/newsCommentSave.do")
    Observable<StatusCode<DataInfo1>> newsCommentSave(
            @Body RequestBean requestBean);
    /**
     * 拳联纪实--功夫快讯赞、踩
     **/
    @POST("/api/news/newsCommentPraise.do")
    Observable<StatusCode<DataInfo1>> newsCommentPraise(
            @Body RequestBean requestBean);


    /**
     * 拳联纪实--十佳管校-场馆
     **/
    @POST("/api/news/venueList.do")
    Observable<StatusCode<DataInfo>> venueList(
            @Body RequestBean requestBean);

    /**
     * 举报
     **/
    @POST("/api/venue/delateBespeak.do")
    Observable<StatusCode<DataInfo>> delateBespeak(
            @Body RequestBean requestBean);

    /**
     * 拳联纪实--十佳管校-场馆详情
     **/
    @POST("/api/base/venueDetail.do")
    Observable<StatusCode<DataInfo>> venueDetail(
            @Body RequestBean requestBean);
    /**
     * 拳联纪实--十佳管校-场馆公告列表
     **/
    @POST("/api/base/noticeList.do")
    Observable<StatusCode<DataInfo>> noticeList(
            @Body RequestBean requestBean);

    /**
     * 拳联纪实--十佳管校-场馆公众评价
     **/
    @POST("/api/base/venueCommentList.do")
    Observable<StatusCode<DataInfo>> venueCommentList(
            @Body RequestBean requestBean);

    /**
     * 拳联纪实--十佳管校-学校
     **/
    @POST("/api/news/schoolList.do")
    Observable<StatusCode<DataInfo>> schoolList(
            @Body RequestBean requestBean);

    /**
     * 拳联纪实--十佳管校-学校详情
     **/
    @POST("/api/news/schoolDetail.do")
    Observable<StatusCode<DataInfo>> schoolDetail(
            @Body RequestBean requestBean);
    /**
     * 拳联纪实--十佳管校-学校点赞
     **/
    @POST("/api/news/schoolPraise.do")
    Observable<StatusCode<DataInfo>> schoolPraise(
            @Body RequestBean requestBean);


    /**
     * 拳联纪实--名师战将
     **/
    @POST("/api/news/masterList.do")
    Observable<StatusCode<DataInfo>> masterList(
            @Body RequestBean requestBean);

    /**
     * 拳联纪实--名师战将详情
     **/
    @POST("/api/news/masterDetail.do")
    Observable<StatusCode<DataInfo>> masterDetail(
            @Body RequestBean requestBean);

    /**
     * 拳联纪实--名师战将 点赞
     **/
    @POST("/api/news/masterPraise.do")
    Observable<StatusCode<DataInfo>> masterPraise(
            @Body RequestBean requestBean);
    /**
     * 拳联纪实--装备评测
     **/
    @POST("/api/news/evaluationList.do")
    Observable<StatusCode<DataInfo>> evaluationList(
            @Body RequestBean requestBean);

    /**
     * 拳联纪实--装备评测详情
     **/
    @POST("/api/news/evaluationDetail.do")
    Observable<StatusCode<DataInfo>> evaluationDetail(
            @Body RequestBean requestBean);

    /**
     * 拳联装备-拼团商品列表
     **/
    @POST("/api/teamBuy/teamBuyList.do")
    Observable<StatusCode<DataInfo>> teamBuyList(
            @Body RequestBean requestBean);

    /**
     * 拳联装备-分类商品
     **/
    @POST("/api/commodity/searchCommodity.do")
    Observable<StatusCode<DataInfo2>> searchCommodity(
            @Body RequestBean requestBean);

    /**
     * 拼团商品详情
     **/
    @POST("/api/commodity/teamBuyCommodityDetail.do")
    Observable<StatusCode<DataInfo>> teamBuyCommodityDetail(
            @Body RequestBean requestBean);

    /**
     * 普通商品详情
     **/
    @POST("/api/commodity/commodityDetail.do")
    Observable<StatusCode<DataInfo>> commodityDetail(
            @Body RequestBean requestBean);

    /**
     * 普通商品规格
     **/
    @POST("/api/commodity/commodityAttribute.do")
    Observable<StatusCode<DataInfo>> commodityAttribute(
            @Body RequestBean requestBean);

    /**
     * 拼团商品规格
     **/
    @POST("/api/commodity/commodityTeamAttribute.do")
    Observable<StatusCode<DataInfo>> commodityTeamAttribute(
            @Body RequestBean requestBean);

    /**
     * 商品评论信息
     **/
    @POST("/api/commodity/commodityCommentInfo.do")
    Observable<StatusCode<DataInfo>> commodityCommentInfo(
            @Body RequestBean requestBean);
    /**
     * 拼团列表
     **/
    @POST("/api/teamBuy/teamList.do")
    Observable<StatusCode<DataInfo>> teamList(
            @Body RequestBean requestBean);

    /**
     * 普通商品创建订单
     **/
    @POST("/api/order/confirmOrderForm.do")
    Observable<StatusCode<GoodsOrderData>> confirmOrderForm(
            @Body RequestBean requestBean);

    /**
     * 普通商品添加购物车
     **/
    @POST("/api/cart/addCart.do")
    Observable<StatusCode<GoodsOrderData>> addCart(
            @Body RequestBean requestBean);

    /**
     * 购物车列表
     **/
    @POST("/api/cart/cartInfo.do")
    Observable<StatusCode<DataInfo1>> cartInfo(
            @Body RequestBean requestBean);


    /**
     * 移除修改购物车商品
     **/
    @POST("/api/cart/updateCartInfo.do")
    Observable<StatusCode<DataInfo1>> updateCartInfo(
            @Body RequestBean requestBean);

    /**
     * 批量删除购物车
     **/
    @POST("/api/cart/batchDelCartInfo.do")
    Observable<StatusCode<DataInfo1>> batchDelCartInfo(
            @Body RequestBean requestBean);

    /**
     * 创建团创建订单
     **/
    @POST("/api/teamBuy/createTeam.do")
    Observable<StatusCode<GoodsOrderDataTwo>> createTeam(
            @Body RequestBean requestBean);


    /**
     * 将购物车商品生成订单
     **/
    @POST("/api/cart/balance.do")
    Observable<StatusCode<GoodsOrderData>> balance(
            @Body RequestBean requestBean);


    /**
     * 加入团创建订单
     **/
    @POST("/api/teamBuy/joinTeamAndCreateOrder.do")
    Observable<StatusCode<GoodsOrderDataTwo>> joinTeamAndCreateOrder(
            @Body RequestBean requestBean);
    /**
     * 计算运费
     **/
    @POST("/api/order/computeFreightPrice.do")
    Observable<StatusCode<DataInfo>> getLogisticsList(
            @Body RequestBean requestBean);

    /**
     * 计算运费
     **/
    @POST("/api/order/computeFreightPrice.do")
    Observable<StatusCode<DataInfo1>> computeFreightPrice(
            @Body RequestBean requestBean);

    /**
     * 我的优惠券列表
     **/
    @POST("/api/coupon/couponList.do")
    Observable<StatusCode<DataInfo>> couponList(
            @Body RequestBean requestBean);

    /**
     * 获取订单可用积分
     **/
    @POST("/api/order/scoreInfo.do")
    Observable<StatusCode<DataInfo1>> scoreInfo(
            @Body RequestBean requestBean);


    /**
     * 订单选择可使用优惠券列表
     **/
    @POST("/api/commodity/getCouponList.do")
    Observable<StatusCode<ListData<CouponData>>> getCouponList(
            @Body RequestBean requestBean);

    /**
     * 普通商品确认订单提交
     **/
    @POST("/api/order/submitOrderForm.do")
    Observable<StatusCode<DataInfo1>> submitOrderForm(
            @Body RequestBean requestBean);

    /**
     * 拼团商品确认订单提交
     **/
    @POST("/api/teamBuy/submitOrder.do")
    Observable<StatusCode<DataInfo1>> submitOrder(
            @Body RequestBean requestBean);

    /**
     * 支付宝支付
     **/
    @POST("/api/alipay/paySign.do")
    Observable<StatusCode<DataInfo1>> alipay(
            @Body RequestBean requestBean);

    /**
     * 微信支付
     **/
    @POST("/api/weixin/getSign.do")
    Observable<StatusCode<DataInfo1>> weixin(
            @Body RequestBean requestBean);


    /**
     * 训练基地
     **/
    @POST("/api/venue/venueList.do")
    Observable<StatusCode<DataInfo>> getVenueList(
            @Body RequestBean requestBean);

    /**
     * 品牌
     **/
    @POST("/api/commodity/getBrandList.do")
    Observable<StatusCode<DataInfo>> getBrandList(
            @Body RequestBean requestBean);

    /**
     * 场馆服务列表
     **/
    @POST("/api/venue/serviceProjectList.do")
    Observable<StatusCode<DataInfo>> serviceProjectList(
            @Body RequestBean requestBean);

    /**
     * 预约保存
     **/
    @POST("/api/base/venueLookSave.do")
    Observable<StatusCode<DataInfo>> venueLookSave(
            @Body RequestBean requestBean);

    /**
     * 我的订单列表
     **/
    @POST("/api/order/myOrderList.do")
    Observable<StatusCode<DataInfo1>> myOrderList(
            @Body RequestBean requestBean);

    /**
     * 取消订单
     **/
    @POST("/api/order/orderCancel.do")
    Observable<StatusCode<DataInfo1>> orderCancel(
            @Body RequestBean requestBean);

    /**
     * 确认收货
     **/
    @POST("/api/order/confirmTake.do")
    Observable<StatusCode<DataInfo1>> confirmTake(
            @Body RequestBean requestBean);

    /**
     * 删除订单
     **/
    @POST("/api/order/orderDel.do")
    Observable<StatusCode<DataInfo1>> orderDel(
            @Body RequestBean requestBean);

    /**
     * 订单详情
     **/
    @POST("/api/order/orderDetail.do")
    Observable<StatusCode<DataInfo2>> orderDetail(
            @Body RequestBean requestBean);


    /**
     *  首页热门搜索
     **/
    @POST("/api/member/search.do")
    Observable<StatusCode<DataInfo>> hotSearch(
            @Body RequestBean requestBean);


    /**
     * 视频列表
     **/
    @POST("/api/video/videoList.do")
    Observable<StatusCode<DataInfo>> videoList(
            @Body RequestBean requestBean);


    /**
     * 视频评论列表
     **/
    @POST("/api/video/videoCommentList.do")
    Observable<StatusCode<DataInfo1>> videoCommentList(
            @Body RequestBean requestBean);
    /**
     * 用户关注
     **/
    @POST("/api/video/memberRelation.do")
    Observable<StatusCode<DataInfo>> memberRelation(
            @Body RequestBean requestBean);

    /**
     * 视频回复评论
     **/
    @POST("/api/video/videoCommentRepaySave.do")
    Observable<StatusCode<DataInfo>> videoCommentRepaySave(
            @Body RequestBean requestBean);

    /**
     * 点赞，收藏，分享，播放次数记录
     **/
    @POST("/api/video/updateVideoInfo.do")
    Observable<StatusCode<DataInfo>> updateVideoInfo(
            @Body RequestBean requestBean);

    /**
     * 视频评论
     **/
    @POST("/api/video/videoCommentSave.do")
    Observable<StatusCode<DataInfo>> videoCommentSave(
            @Body RequestBean requestBean);
    /**
     * 视频l评论点赞踩
     **/
    @POST("/api/video/videoCommentUp.do")
    Observable<StatusCode<DataInfo>> videoCommentUp(
            @Body RequestBean requestBean);

    /**
     * 视频举报
     **/
    @POST("/api/video/videorReportSave.do")
    Observable<StatusCode<DataInfo>> videorReportSave(
            @Body RequestBean requestBean);
    /**
     * 视频回复详情
     **/
    @POST("/api/video/videoRepayDetail.do")
    Observable<StatusCode<DataInfo>> videoRepayDetail(
            @Body RequestBean requestBean);

    /**
     * 快讯回复详情
     **/
    @POST("/api/news/repayInfo.do")
    Observable<StatusCode<DataInfo>> repayInfo(
            @Body RequestBean requestBean);

    /**
     * 首页装备搜索
     **/
    @POST("/api/commodity/appSearchCommodity")
    Observable<StatusCode<DataInfo1>> appSearchCommodity(
            @Body RequestBean requestBean);

    /**
     * 查询物流信息
     **/
    @POST("/api/order/logisticsInfo.do")
    Observable<StatusCode<DataInfo1>> logisticsInfo(
            @Body RequestBean requestBean);

    /**
     * 拼团分类
     */
    @POST("/api/commodity/selectAllTeamBuy.do")
    Observable<StatusCode<DataInfo>> selectAllTeamBuy(
            @Body RequestBean requestBean);



    /**
     * 拳联装备-分类商品列表
     **/
    @POST("/api/commodity/commodityList.do")
    Observable<StatusCode<DataInfo>> commodityList(
            @Body RequestBean requestBean);

    /**
     * 申请售后
     **/
    @POST(" /api/order/customerServiceSave.do")
    Observable<StatusCode<DataInfo1>> customerServiceSave(
            @Body RequestBean requestBean);


    @POST("/api/commodity/getAttributeList.do")
    Observable<StatusCode<DataInfo>> getAttributeList(
            @Body RequestBean requestBean);


    /**
     * 评价商品
     **/
    @POST("/api/order/commodityRecommendSave.do")
    Observable<StatusCode<DataInfo1>> commodityRecommendSave(
            @Body RequestBean requestBean);


    /**
     * 填写单号
     **/
    @POST("/api/order/submitCourierNumber.do")
    Observable<StatusCode<DataInfo1>> submitCourierNumber(
            @Body RequestBean requestBean);

    /**
     * 我的评论-商品评论
     * @param requestBean
     * @return
     */
    @POST("/api/member/myCommodityCommentList.do")
    Observable<StatusCode<DataInfo1>> myCommodityCommentList(
            @Body RequestBean requestBean);



    /**
     * 我的评论-场馆评论
     * @param requestBean
     * @return
     */
    @POST("/api/member/venueCommentList.do")
    Observable<StatusCode<DataInfo1>> venuemyCommentList(
            @Body RequestBean requestBean);


    /**
     * 我的评论-快讯评论
     * @param requestBean
     * @return
     */
    @POST(" /api/member/newsCommentList.do")
    Observable<StatusCode<DataInfo1>> newsCommentList(
            @Body RequestBean requestBean);


    /**
     * 我的评论-视频评论
     * @param requestBean
     * @return
     */
    @POST("/api/member/videoCommentList.do")
    Observable<StatusCode<DataInfo1>> videomyCommentList(
            @Body RequestBean requestBean);


    /**
     * 保存服务区信息
     **/
    @POST("/api/member/saveRegion.do")
    Observable<StatusCode<DataInfo1>> saveRegion(
            @Body RequestBean requestBean);

    /**
     * 绑定会员二维码
     **/
    @POST("/api/member/bindCode.do")
    Observable<StatusCode<DataInfo1>> bindCode(
            @Body RequestBean requestBean);

    /**
     * 用户读取消息
     **/
    @POST("/api/member/msgRead.do")
    Observable<StatusCode<DataInfo1>> msgRead(
            @Body RequestBean requestBean);


    /**
     * 获取未读消息条数
     **/
    @POST("/api/member/isMsgRead.do")
    Observable<StatusCode<DataInfo>> isMsgRead(
            @Body RequestBean requestBean);

    /**
     * 手机号绑定验证
     **/
    @POST("/api/member/bindCheck.do")
    Observable<StatusCode<DataInfo>> bindCheck(
            @Body RequestBean requestBean);

    /**
     * 手机号码绑定
     **/
    @POST("/api/member/bindingPhone.do")
    Observable<StatusCode<DataInfo>> bindingPhone(
            @Body RequestBean requestBean);

    /**
     * qq登录
     **/
    @POST("/api/member/loginQQ.do")
    Observable<StatusCode<DataInfo>> loginQQ(
            @Body RequestBean requestBean);

    /**

     * 删除视频
     * @param requestBean
     * @return
     */
    @POST("/api/member/delMyVideo.do")
    Observable<StatusCode<Object>> delMyVideo(
            @Body RequestBean requestBean);
     /**
     * 微信登录
     **/
    @POST("/api/member/loginWeiXin.do")
    Observable<StatusCode<DataInfo>> loginWeiXin(
            @Body RequestBean requestBean);

    /**
     * 资料完善
     **/
    @POST("/api/member/updateMemberInfo.do")
    Observable<StatusCode<DataInfo>> updateMemberInfo(
            @Body RequestBean requestBean);

    /**
     * 商品收藏
     **/
    @POST("/api/commodity/commodityCollection.do")
    Observable<StatusCode<DataInfo>> commodityCollection(
            @Body RequestBean requestBean);

    /**
     * 关注列表
     **/
    @POST("/api/video/lookList.do")
    Observable<StatusCode<DataInfo>> lookList(
            @Body RequestBean requestBean);


    /**
     * 我的发布——删除商品评论
     * @param requestBean
     * @return
     */
    @POST("/api/member/delCommodityComment.do")
    Observable<StatusCode<Object>> delCommodityComment(
            @Body RequestBean requestBean);




    /**
     * 我的发布——删除场馆评论
     * @param requestBean
     * @return
     */
    @POST("/api/member/delVenueComment.do")
    Observable<StatusCode<Object>> delVenueComment(
            @Body RequestBean requestBean);

    /**
     * 我的发布——删除视频评论
     * @param requestBean
     * @return
     */
    @POST("/api/member/delVideoComment.do")
    Observable<StatusCode<Object>> delVideoComment(
            @Body RequestBean requestBean);

    /**
     * 我的发布——删除快讯评论
     * @param requestBean
     * @return
     */
    @POST("/api/member/delNewsComment.do")
    Observable<StatusCode<Object>> delNewsComment(
            @Body RequestBean requestBean);
    /**
     * 关于我们、联系客服、帮助中心
     * @param requestBean
     * @return
     */
    @POST("/api/common/getInfo.do")
    Observable<StatusCode<DataInfo>> getInfo(
            @Body RequestBean requestBean);


    /**
     * 配置信息
     * @param requestBean
     * @return
     */
    @POST("/api/config/baseConfigInfo.do")
    Observable<StatusCode<DataInfo>> baseConfigInfo(
            @Body RequestBean requestBean);

    /**
     * 视频保存
     **/
    @POST("/api/video/videoSave.do")
    Observable<StatusCode<DataInfo>> videoSave(
            @Body RequestBean requestBean);

    /**
     * 预约评价
     **/
    @POST("/api/venue/bespeakEvaluate.do")
    Observable<StatusCode<DataInfo>> bespeakEvaluate(
            @Body RequestBean requestBean);

    /**
     * 获取首页优惠券
     * @param requestBean
     * @return
     */
    @POST("/api/commodity/updateComupons.do")
    Observable<StatusCode<DataInfo>> updateComupons(
            @Body RequestBean requestBean);

}


