package com.abile2.stockcircuit.model;


public class Stock {

	
	public String id;
	public String stockname;
	private String nseid;
	private String fullid;
	public String bseid;
	private String isin;
	private String industry_vertical;
	private String currentPrice;
	private String change;
	private String change_percent;
	private String changeStr;


	public Stock()
	{
		
	}
	public Stock(String name)
	{
		this.stockname = name;

	}

	public Stock(String name,String nseid,  String fullid)
	{
		this.stockname = name;
		this.nseid = nseid;
		this.fullid = fullid;
	}
	public String toString(){
		 return stockname;
	}
//	public Stock(String name, String rating, String notes)
//	{
//		this.stockname = name;
//		this.rating = rating;
//		this.notes = notes;
//	}

	public String getStockname() {
		return stockname;
	}

	public void setStockname(String stockname) {
		this.stockname = stockname;
	}

	public String getNseid() {
		return nseid;
	}
	public void setNseid(String nseid) {
		this.nseid = nseid;
	}
	public String getBseid() {
		return bseid;
	}
	public void setBseid(String bseid) {
		this.bseid = bseid;
	}
	public String getIsin() {
		return isin;
	}
	public void setIsin(String isin) {
		this.isin = isin;
	}
	public String getIndustry_vertical() {
		return industry_vertical;
	}
	public void setIndustry_vertical(String industry_vertical) {
		this.industry_vertical = industry_vertical;
	}
	public String getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}
	public String getChange() {
		return change;
	}
	public void setChange(String change) {
		this.change = change;
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
	public String getChange_percent() {
		return change_percent;
	}
	public void setChange_percent(String change_percent) {
		this.change_percent = change_percent;
	}
	public String getChangeStr() {
		return changeStr;
	}
	public void setChangeStr(String changeStr) {
		this.changeStr = changeStr;
	}

}
