package com.raffle.pojo;

public class BundleDetails {

	
	int idBundle_details; 
	int idPeriod; 
	int idProduct; 
	String bundleNumber; 
	String ticketNumber; 
	boolean isBroken;
	public int getIdBundle_details() {
		return idBundle_details;
	}
	public void setIdBundle_details(int idBundle_details) {
		this.idBundle_details = idBundle_details;
	}
	public int getIdPeriod() {
		return idPeriod;
	}
	public void setIdPeriod(int idPeriod) {
		this.idPeriod = idPeriod;
	}
	public int getIdProduct() {
		return idProduct;
	}
	public void setIdProduct(int idProduct) {
		this.idProduct = idProduct;
	}
	public String getBundleNumber() {
		return bundleNumber;
	}
	public void setBundleNumber(String bundleNumber) {
		this.bundleNumber = bundleNumber;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public boolean isBroken() {
		return isBroken;
	}
	public void setBroken(boolean isBroken) {
		this.isBroken = isBroken;
	}
		
}
