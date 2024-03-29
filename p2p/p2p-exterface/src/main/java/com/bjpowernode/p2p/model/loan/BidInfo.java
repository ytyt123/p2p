package com.bjpowernode.p2p.model.loan;

import com.bjpowernode.p2p.model.user.User;

import java.io.Serializable;
import java.util.Date;

public class BidInfo implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_bid_info.id
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_bid_info.loan_id
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    private Integer loanId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_bid_info.uid
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    private Integer uid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_bid_info.bid_money
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    private Double bidMoney;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_bid_info.bid_time
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    private Date bidTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_bid_info.bid_status
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    private Integer bidStatus;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_bid_info.id
     *
     * @return the value of b_bid_info.id
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_bid_info.id
     *
     * @param id the value for b_bid_info.id
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_bid_info.loan_id
     *
     * @return the value of b_bid_info.loan_id
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    public Integer getLoanId() {
        return loanId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_bid_info.loan_id
     *
     * @param loanId the value for b_bid_info.loan_id
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_bid_info.uid
     *
     * @return the value of b_bid_info.uid
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_bid_info.uid
     *
     * @param uid the value for b_bid_info.uid
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_bid_info.bid_money
     *
     * @return the value of b_bid_info.bid_money
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    public Double getBidMoney() {
        return bidMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_bid_info.bid_money
     *
     * @param bidMoney the value for b_bid_info.bid_money
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    public void setBidMoney(Double bidMoney) {
        this.bidMoney = bidMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_bid_info.bid_time
     *
     * @return the value of b_bid_info.bid_time
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    public Date getBidTime() {
        return bidTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_bid_info.bid_time
     *
     * @param bidTime the value for b_bid_info.bid_time
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    public void setBidTime(Date bidTime) {
        this.bidTime = bidTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_bid_info.bid_status
     *
     * @return the value of b_bid_info.bid_status
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    public Integer getBidStatus() {
        return bidStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_bid_info.bid_status
     *
     * @param bidStatus the value for b_bid_info.bid_status
     *
     * @mbggenerated Wed Nov 27 19:38:00 CST 2019
     */
    public void setBidStatus(Integer bidStatus) {
        this.bidStatus = bidStatus;
    }
}