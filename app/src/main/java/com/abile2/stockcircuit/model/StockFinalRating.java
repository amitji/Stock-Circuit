package com.abile2.stockcircuit.model;


import java.util.Date;

public class StockFinalRating {


    public String id;
    private String fullid;
    private String nseid;
    private String name;
    private String percentage_rating;
    private String latest_quarter;
    private String revenue;
    private String profit;
    private String op_profit;
    private String ebit;
    private String profit_margin;
    private String roe;
    private String interest_cover;
    private String debt_equity_ratio;
    private String total;
    private Date last_modified;
    private Date created_on;


    public StockFinalRating() {

    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getFullid() {
        return fullid;
    }


    public void setFullid(String fullid) {
        this.fullid = fullid;
    }


    public String getNseid() {
        return nseid;
    }

    public void setNseid(String nseid) {
        this.nseid = nseid;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getPercentage_rating() {
        return percentage_rating;
    }


    public void setPercentage_rating(String percentage_rating) {
        this.percentage_rating = percentage_rating;
    }


    public String getLatest_quarter() {
        return latest_quarter;
    }


    public void setLatest_quarter(String latest_quarter) {
        this.latest_quarter = latest_quarter;
    }


    public String getRevenue() {
        return revenue;
    }


    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }


    public String getProfit() {
        return profit;
    }


    public void setProfit(String profit) {
        this.profit = profit;
    }


    public String getOp_profit() {
        return op_profit;
    }


    public void setOp_profit(String op_profit) {
        this.op_profit = op_profit;
    }


    public String getEbit() {
        return ebit;
    }


    public void setEbit(String ebit) {
        this.ebit = ebit;
    }


    public String getProfit_margin() {
        return profit_margin;
    }


    public void setProfit_margin(String profit_margin) {
        this.profit_margin = profit_margin;
    }


    public String getRoe() {
        return roe;
    }


    public void setRoe(String roe) {
        this.roe = roe;
    }


    public String getInterest_cover() {
        return interest_cover;
    }


    public void setInterest_cover(String interest_cover) {
        this.interest_cover = interest_cover;
    }


    public String getDebt_equity_ratio() {
        return debt_equity_ratio;
    }


    public void setDebt_equity_ratio(String debt_equity_ratio) {
        this.debt_equity_ratio = debt_equity_ratio;
    }


    public String getTotal() {
        return total;
    }


    public void setTotal(String total) {
        this.total = total;
    }


    public Date getLast_modified() {
        return last_modified;
    }


    public void setLast_modified(Date last_modified) {
        this.last_modified = last_modified;
    }


    public Date getCreated_on() {
        return created_on;
    }


    public void setCreated_on(Date created_on) {
        this.created_on = created_on;
    }

}
