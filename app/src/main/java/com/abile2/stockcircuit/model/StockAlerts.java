package com.abile2.stockcircuit.model;

public class StockAlerts {
	
	public String id;
	public String mobile;
	public String deviceID;
	public String regID;
	private String nseid;
	private String fullid;
	private String name;
	private String alert_price;
	private String low_high;
	private String has_hit;
	public String is_active;
	public String live_quote;
	private String toBeNotified;
	
	public StockAlerts(){
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getNseid() {
		return nseid;
	}

	public void setNseid(String nseid) {
		this.nseid = nseid;
	}

	public String getAlert_price() {
		return alert_price;
	}

	public void setAlert_price(String alert_price) {
		this.alert_price = alert_price;
	}

	public String getLow_high() {
		return low_high;
	}

	public void setLow_high(String low_high) {
		this.low_high = low_high;
	}

	public String getHas_hit() {
		return has_hit;
	}

	public void setHas_hit(String has_hit) {
		this.has_hit = has_hit;
	}

	public String getIs_active() {
		return is_active;
	}

	public void setIs_active(String is_active) {
		this.is_active = is_active;
	}

	public String getToBeNotified() {
		return toBeNotified;
	}

	public void setToBeNotified(String toBeNotified) {
		this.toBeNotified = toBeNotified;
	}

	public String getLive_quote() {
		return live_quote;
	}

	public void setLive_quote(String live_quote) {
		this.live_quote = live_quote;
	}

	public String getRegID() {
		return regID;
	}

	public String getFullid() {
		return fullid;
	}

	public void setFullid(String fullid) {
		this.fullid = fullid;
	}

	public void setRegID(String regID) {
		this.regID = regID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
