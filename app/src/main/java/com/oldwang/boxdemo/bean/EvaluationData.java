package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class EvaluationData implements Serializable{


    //评测装备图片
    private String commodityPicUrl;
    //评测装备ID
    private String evaluationDetailsId;
    //评测名称
    private String evaluationDetailsName;
    //评测分数
    private String evaluationDetailsScore;
    //评测时间
    private String evaluationTime;
    //评测详细
    private String evaluationDetailsDetail;

    private String commodityId;

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getEvaluationDetailsDetail() {
        return evaluationDetailsDetail;
    }

    public void setEvaluationDetailsDetail(String evaluationDetailsDetail) {
        this.evaluationDetailsDetail = evaluationDetailsDetail;
    }

    public String getCommodityPicUrl() {
        return commodityPicUrl;
    }

    public void setCommodityPicUrl(String commodityPicUrl) {
        this.commodityPicUrl = commodityPicUrl;
    }

    public String getEvaluationDetailsId() {
        return evaluationDetailsId;
    }

    public void setEvaluationDetailsId(String evaluationDetailsId) {
        this.evaluationDetailsId = evaluationDetailsId;
    }

    public String getEvaluationDetailsName() {
        return evaluationDetailsName;
    }

    public void setEvaluationDetailsName(String evaluationDetailsName) {
        this.evaluationDetailsName = evaluationDetailsName;
    }

    public String getEvaluationDetailsScore() {
        return evaluationDetailsScore;
    }

    public void setEvaluationDetailsScore(String evaluationDetailsScore) {
        this.evaluationDetailsScore = evaluationDetailsScore;
    }

    public String getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(String evaluationTime) {
        this.evaluationTime = evaluationTime;
    }
}
