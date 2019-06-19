package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class UserInfo implements Serializable{

    /**区域ID*/
    private String agentAreaId;

    /**代理类型*/
    private String agentType;

    /**代理类型名称*/
    private String agentTypeName;

    /**区域名称*/
    private String areaName;

    /**用户账号*/
    private String memberAccount;

    /**会员ID*/
    private String memberId;

    /**昵称*/
    private String memberNickname;

    /**真实姓名*/
    private String realName;

    /**服务器区域信息逗号分隔*/
    private String regionIds;

    /**登录成功系统返回TOKEN凭证*/
    private String token;

    private String baseStatus;

    private String memberHeadLogo;
    private String identStates;
    private String identStatesDes;
    private String checkStatus;

    private String principalName;
    private String principalPhone;
    private String baseName;
    private String baseDetailedAddress;
    private String provinceId;
    private String cityId;
    private String districtId;
    private String townshipId;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getTownshipId() {
        return townshipId;
    }

    public void setTownshipId(String townshipId) {
        this.townshipId = townshipId;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getPrincipalPhone() {
        return principalPhone;
    }

    public void setPrincipalPhone(String principalPhone) {
        this.principalPhone = principalPhone;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getBaseDetailedAddress() {
        return baseDetailedAddress;
    }

    public void setBaseDetailedAddress(String baseDetailedAddress) {
        this.baseDetailedAddress = baseDetailedAddress;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getBaseStatus() {
        return baseStatus;
    }

    public void setBaseStatus(String baseStatus) {
        this.baseStatus = baseStatus;
    }

    public String getAgentAreaId() {
        return agentAreaId;
    }

    public void setAgentAreaId(String agentAreaId) {
        this.agentAreaId = agentAreaId;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public String getAgentTypeName() {
        return agentTypeName;
    }

    public void setAgentTypeName(String agentTypeName) {
        this.agentTypeName = agentTypeName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getMemberAccount() {
        return memberAccount;
    }

    public void setMemberAccount(String memberAccount) {
        this.memberAccount = memberAccount;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRegionIds() {
        return regionIds;
    }

    public void setRegionIds(String regionIds) {
        this.regionIds = regionIds;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMemberHeadLogo() {
        return memberHeadLogo;
    }

    public void setMemberHeadLogo(String memberHeadLogo) {
        this.memberHeadLogo = memberHeadLogo;
    }

    public String getIdentStates() {
        return identStates;
    }

    public void setIdentStates(String identStates) {
        this.identStates = identStates;
    }

    public String getIdentStatesDes() {
        return identStatesDes;
    }

    public void setIdentStatesDes(String identStatesDes) {
        this.identStatesDes = identStatesDes;
    }
}
