package com.oldwang.boxdemo.bean;

/***
 * 提现信息
 */
public class WithdrawalInfo {

    /**能提现金额**/
    private String CanWithdrawalMoney;

    /**冻结金额**/
    private String frozenMoney;

    /**收入金额**/
    private String incomeTotal;

    private String myscore;
    private String nowScore;

    private String myRank;


    public String getNowScore() {
        return nowScore;
    }

    public void setNowScore(String nowScore) {
        this.nowScore = nowScore;
    }

    public String getMyRank() {
        return myRank;
    }

    public void setMyRank(String myRank) {
        this.myRank = myRank;
    }

    public String getCanWithdrawalMoney() {
        return CanWithdrawalMoney;
    }

    public void setCanWithdrawalMoney(String canWithdrawalMoney) {
        CanWithdrawalMoney = canWithdrawalMoney;
    }

    public String getFrozenMoney() {
        return frozenMoney;
    }

    public void setFrozenMoney(String frozenMoney) {
        this.frozenMoney = frozenMoney;
    }

    public String getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(String incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public String getMyScore() {
        return myscore;
    }

    public void setMyScore(String myScore) {
        this.myscore = myScore;
    }
}
