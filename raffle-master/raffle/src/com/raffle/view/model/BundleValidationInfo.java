package com.raffle.view.model;

import java.util.Date;

public class BundleValidationInfo 
{

	private String bundleNumber;
	private Date brokenOn;
	private int brokenByUserId;
	private boolean isBroken;
	private boolean isSold;
	
	
	public String getBundleNumber() {
		return bundleNumber;
	}
	public void setBundleNumber(String bundleNumber) {
		this.bundleNumber = bundleNumber;
	}
	public Date getBrokenOn() {
		return brokenOn;
	}
	public void setBrokenOn(Date brokenOn) {
		this.brokenOn = brokenOn;
	}
	public int getBrokenByUserId() {
		return brokenByUserId;
	}
	public void setBrokenByUserId(int brokenByUserId) {
		this.brokenByUserId = brokenByUserId;
	}
	public boolean isBroken() {
		return isBroken;
	}
	public void setBroken(boolean isBroken) {
		this.isBroken = isBroken;
	}
	public boolean isSold() {
		return isSold;
	}
	public void setSold(boolean isSold) {
		this.isSold = isSold;
	}
	
	
	
	
}
