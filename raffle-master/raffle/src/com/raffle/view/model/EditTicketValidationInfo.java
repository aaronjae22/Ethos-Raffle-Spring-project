package com.raffle.view.model;

import java.util.Date;

public class EditTicketValidationInfo {

	private String ticketNumber;
	private int idProduct;
	private boolean isSold;
	
	private String bundleNumber;
	private boolean isBroken;
	private int brokenByUserId;
	private Date brokenOn;
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public int getIdProduct() {
		return idProduct;
	}
	public void setIdProduct(int idProduct) {
		this.idProduct = idProduct;
	}
	public boolean isSold() {
		return isSold;
	}
	public void setSold(boolean isSold) {
		this.isSold = isSold;
	}
	public String getBundleNumber() {
		return bundleNumber;
	}
	public void setBundleNumber(String bundleNumber) {
		this.bundleNumber = bundleNumber;
	}
	public boolean isBroken() {
		return isBroken;
	}
	public void setBroken(boolean isBroken) {
		this.isBroken = isBroken;
	}
	public int getBrokenByUserId() {
		return brokenByUserId;
	}
	public void setBrokenByUserId(int brokenByUserId) {
		this.brokenByUserId = brokenByUserId;
	}
	public Date getBrokenOn() {
		return brokenOn;
	}
	public void setBrokenOn(Date brokenOn) {
		this.brokenOn = brokenOn;
	}
	
}
