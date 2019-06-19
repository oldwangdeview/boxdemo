package com.oldwang.boxdemo.bean;

import com.oldwang.boxdemo.activity.OrderData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataInfo implements Serializable{

    private MemberInfo memberInfo;
    private UserInfo userInfo;
    private UserInfo baseInfo;
    private ScoresInfo scoresInfo;

    private WithdrawalInfo withdrawalInfo;
    private WithdrawalInfo scoreInfo;
    private List<PictureSetData> pictureSetData;
    private List<CommodityData> commodityData;
    private ListData<VideoData> videoData;
    private ListData<NewsData> newsData;
    private ListData<VenueData> venueData;
    private ListData<SchoolData> schoolData;
    private ListData<MasterData> masterData;
    private ListData<EvaluationData> evaluationData;
    private ListData<VenueData> baseData;
    private ListData<AddressData> addressData;
    private ListData<OrderData> orderData;
    private ListData<BankData> bankData;
    private List<RegionData> regionData;
    private ListData<ScoreData> scoreData;
    private ListData<WithdrawalData> withdrawalData;
    private List<CommodityTypeData> commodityTypeData;
    private ListData<TypeData> typeData;
    private CommodityTeamBuyData commodityInfo;
    private ListData<CommodityTeamBuyData> data;
    private ListData<CommodityChildCommentData> commodityCommentData;
    private CommodityCommentInfo commodityCommentInfo;
    private ListData<CommodityTeamBuyData> commodityTeamBuyData;
    private List<CourierData> courierInfos;
    private ListData<BrandData> brandData;
    private List<ServiceData> serviceData;
    private SchoolData schoolInfo;
    private MasterData masterInfo;
    private VenueInfo venueInfo;
    private EvaluationData evaluationInfo;
    private ListData<NoticeData> noticeData;
    private List<String> hotData;
    private MsgInfo msgInfo;


    private BoxingVideoCommentInfo boxingVideoCommentInfo;
    private ListData<NewsData> boxingVideoCommentRepayData;
    private ListData<MemberRelationData> memberRelationData;


    private NewsData newsCommentInfo;
    private ListData<NewsData> repayData;



    private List<AttributeColorBean> attributeColor = new ArrayList<>();
    private List<AttributeQualityBean> attributeQuality = new ArrayList<>();
    private List<AttributeSizeBean> attributeSize = new ArrayList<>();



    private String info;
    private ConfigInfo configInfo;
    private String boxingVideoId;

    public String getBoxingVideoId() {
        return boxingVideoId;
    }

    public void setBoxingVideoId(String boxingVideoId) {
        this.boxingVideoId = boxingVideoId;
    }

    public ConfigInfo getConfigInfo() {
        return configInfo;
    }

    public void setConfigInfo(ConfigInfo configInfo) {
        this.configInfo = configInfo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<AttributeQualityBean> getAttributeQuality() {
        return attributeQuality;
    }


    public List<CourierData> getCourierInfos() {
        return courierInfos;
    }


    public ListData<MemberRelationData> getMemberRelationData() {
        return memberRelationData;
    }

    public void setMemberRelationData(ListData<MemberRelationData> memberRelationData) {
        this.memberRelationData = memberRelationData;
    }

    public void setCourierInfos(List<CourierData> courierInfos) {
        this.courierInfos = courierInfos;
    }

    public MsgInfo getMsgInfo() {
        return msgInfo;
    }

    public void setMsgInfo(MsgInfo msgInfo) {
        this.msgInfo = msgInfo;
    }

    public void setAttributeQuality(List<AttributeQualityBean> attributeQuality) {
        this.attributeQuality = attributeQuality;
    }

    public List<AttributeColorBean> getAttributeColor() {
        return attributeColor;
    }

    public void setAttributeColor(List<AttributeColorBean> attributeColor) {
        this.attributeColor = attributeColor;
    }



    public List<AttributeSizeBean> getAttributeSize() {
        return attributeSize;
    }

    public void setAttributeSize(List<AttributeSizeBean> attributeSize) {
        this.attributeSize = attributeSize;
    }

    public NewsData getNewsCommentInfo() {
        return newsCommentInfo;
    }

    public void setNewsCommentInfo(NewsData newsCommentInfo) {
        this.newsCommentInfo = newsCommentInfo;
    }

    public ListData<NewsData> getRepayData() {
        return repayData;
    }

    public void setRepayData(ListData<NewsData> repayData) {
        this.repayData = repayData;
    }

    public BoxingVideoCommentInfo getBoxingVideoCommentInfo() {
        return boxingVideoCommentInfo;
    }

    public void setBoxingVideoCommentInfo(BoxingVideoCommentInfo boxingVideoCommentInfo) {
        this.boxingVideoCommentInfo = boxingVideoCommentInfo;
    }

    public ListData<NewsData> getBoxingVideoCommentRepayData() {
        return boxingVideoCommentRepayData;
    }

    public void setBoxingVideoCommentRepayData(ListData<NewsData> boxingVideoCommentRepayData) {
        this.boxingVideoCommentRepayData = boxingVideoCommentRepayData;
    }

    public List<String> getHotData() {
        return hotData;
    }

    public void setHotData(List<String> hotData) {
        this.hotData = hotData;
    }

    private ListData<VenueCommentData> venueCommentData;


    public ListData<VenueCommentData> getVenueCommentData() {
        return venueCommentData;
    }

    public void setVenueCommentData(ListData<VenueCommentData> venueCommentData) {
        this.venueCommentData = venueCommentData;
    }

    public ListData<NoticeData> getNoticeData() {
        return noticeData;
    }

    public void setNoticeData(ListData<NoticeData> noticeData) {
        this.noticeData = noticeData;
    }

    public EvaluationData getEvaluationInfo() {
        return evaluationInfo;
    }

    public void setEvaluationInfo(EvaluationData evaluationInfo) {
        this.evaluationInfo = evaluationInfo;
    }

    public MasterData getMasterInfo() {
        return masterInfo;
    }

    public void setMasterInfo(MasterData masterInfo) {
        this.masterInfo = masterInfo;
    }

    public SchoolData getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolData schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    public VenueInfo getVenueInfo() {
        return venueInfo;
    }

    public void setVenueInfo(VenueInfo venueInfo) {
        this.venueInfo = venueInfo;
    }

    private ListData<CouponData> couponData;

    public ListData<CouponData> getCouponData() {
        return couponData;
    }

    public ListData<BrandData> getBrandData() {
        return brandData;
    }

    public void setBrandData(ListData<BrandData> brandData) {
        this.brandData = brandData;
    }

    public void setCouponData(ListData<CouponData> couponData) {
        this.couponData = couponData;
    }

    public List<CourierData> getCourierData() {
        return courierInfos;
    }

    public void setCourierData(List<CourierData> courierData) {
        this.courierInfos = courierData;
    }


    private List<Attribute> commodityAttributeInfo;

    private String mainId;

    public List<ServiceData> getServiceData() {
        return serviceData;
    }

    public void setServiceData(List<ServiceData> serviceData) {
        this.serviceData = serviceData;
    }

    public ListData<CommodityTeamBuyData> getCommodityTeamBuyData1() {
        return commodityTeamBuyData;
    }

    public void setCommodityTeamBuyData1(ListData<CommodityTeamBuyData> commodityTeamBuyData1) {
        this.commodityTeamBuyData = commodityTeamBuyData1;
    }

    public List<Attribute> getCommodityAttributeInfo() {
        return commodityAttributeInfo;
    }

    public void setCommodityAttributeInfo(List<Attribute> commodityAttributeInfo) {
        this.commodityAttributeInfo = commodityAttributeInfo;
    }


    public ListData<CommodityChildCommentData> getCommodityCommentData() {
        return commodityCommentData;
    }

    public void setCommodityCommentData(ListData<CommodityChildCommentData> commodityCommentData) {
        this.commodityCommentData = commodityCommentData;
    }

    public CommodityCommentInfo getCommodityCommentInfo() {
        return commodityCommentInfo;
    }

    public void setCommodityCommentInfo(CommodityCommentInfo commodityCommentInfo) {
        this.commodityCommentInfo = commodityCommentInfo;
    }

    public CommodityTeamBuyData getCommodityInfo() {
        return commodityInfo;
    }

    public void setCommodityInfo(CommodityTeamBuyData commodityInfo) {
        this.commodityInfo = commodityInfo;
    }

    public ListData<CommodityTeamBuyData> getData() {
        return data;
    }

    public void setData(ListData<CommodityTeamBuyData> data) {
        this.data = data;
    }

    public ListData<CommodityTeamBuyData> getCommodityTeamBuyData() {
        return data;
    }

    public void setCommodityTeamBuyData(ListData<CommodityTeamBuyData> commodityTeamBuyData) {
        this.data = commodityTeamBuyData;
    }

    public ScoresInfo getScoresInfo() {
        return scoresInfo;
    }

    public void setScoresInfo(ScoresInfo scoresInfo) {
        this.scoresInfo = scoresInfo;
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public WithdrawalInfo getScoreInfo() {
        return scoreInfo;
    }

    public void setScoreInfo(WithdrawalInfo scoreInfo) {
        this.scoreInfo = scoreInfo;
    }

    public ListData<WithdrawalData> getWithdrawalData() {
        return withdrawalData;
    }

    public void setWithdrawalData(ListData<WithdrawalData> withdrawalData) {
        this.withdrawalData = withdrawalData;
    }

    public MemberInfo getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<PictureSetData> getPictureSetData() {
        return pictureSetData;
    }

    public void setPictureSetData(List<PictureSetData> pictureSetData) {
        this.pictureSetData = pictureSetData;
    }

    public List<CommodityData> getCommodityData() {
        return commodityData;
    }

    public void setCommodityData(List<CommodityData> commodityData) {
        this.commodityData = commodityData;
    }

    public void setVideoData(ListData<VideoData> videoData) {
        this.videoData = videoData;
    }

    public ListData<VideoData> getVideoData() {
        return videoData;
    }

    public ListData<NewsData> getNewsData() {
        return newsData;
    }

    public void setNewsData(ListData<NewsData> newsData) {
        this.newsData = newsData;
    }

    public ListData<VenueData> getVenueData() {
        return venueData;
    }

    public void setVenueData(ListData<VenueData> venueData) {
        this.venueData = venueData;
    }

    public ListData<SchoolData> getSchoolData() {
        return schoolData;
    }

    public void setSchoolData(ListData<SchoolData> schoolData) {
        this.schoolData = schoolData;
    }

    public ListData<MasterData> getMasterData() {
        return masterData;
    }

    public void setMasterData(ListData<MasterData> masterData) {
        this.masterData = masterData;
    }

    public ListData<EvaluationData> getEvaluationData() {
        return evaluationData;
    }

    public void setEvaluationData(ListData<EvaluationData> evaluationData) {
        this.evaluationData = evaluationData;
    }

    public ListData<VenueData> getBaseData() {
        return baseData;
    }

    public void setBaseData(ListData<VenueData> baseData) {
        this.baseData = baseData;
    }

    public ListData<AddressData> getAddressData() {
        return addressData;
    }

    public void setAddressData(ListData<AddressData> addressData) {
        this.addressData = addressData;
    }

    public List<RegionData> getRegionData() {
        return regionData;
    }

    public void setRegionData(List<RegionData> regionData) {
        this.regionData = regionData;
    }

    public ListData<com.oldwang.boxdemo.bean.ScoreData> getScoreData() {
        return scoreData;
    }

    public void setScoreData(ListData<com.oldwang.boxdemo.bean.ScoreData> scoreData) {
        this.scoreData = scoreData;
    }

    public WithdrawalInfo getWithdrawalInfo() {
        return withdrawalInfo;
    }

    public void setWithdrawalInfo(WithdrawalInfo withdrawalInfo) {
        this.withdrawalInfo = withdrawalInfo;
    }

    public ListData<OrderData> getOrderData() {
        return orderData;
    }

    public void setOrderData(ListData<OrderData> orderData) {
        this.orderData = orderData;
    }

    public ListData<BankData> getBankData() {
        return bankData;
    }

    public void setBankData(ListData<BankData> bankData) {
        this.bankData = bankData;
    }

    public UserInfo getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(UserInfo baseInfo) {
        this.baseInfo = baseInfo;
    }

    public List<CommodityTypeData> getCommodityTypeData() {
        return commodityTypeData;
    }

    public void setCommodityTypeData(List<CommodityTypeData> commodityTypeData) {
        this.commodityTypeData = commodityTypeData;
    }

    public ListData<TypeData> getTypeData() {
        return typeData;
    }

    public void setTypeData(ListData<TypeData> typeData) {
        this.typeData = typeData;
    }
}
