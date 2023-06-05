package com.raffle.view.model.contact;

import java.util.Date;

public class TicketPurchaseItem 
{
	int idPeriod;
	String period;
	
	
	int idTicket;
	
	int idContact;
	String contact;
	
	String ticketNumber;
	
	Date purchaseDate;
	
	String createdByUser;
	
	String paymentType;
	
	
	
	int idProduct;
	String product;
	
	String bundleNumber;	
	java.math.BigDecimal price;

	int cretedByUserId;
	
	public int getIdPeriod() {
		return idPeriod;
	}

	public void setIdPeriod(int idPeriod) {
		this.idPeriod = idPeriod;
	}

	public int getIdTicket() {
		return idTicket;
	}

	public void setIdTicket(int idTicket) {
		this.idTicket = idTicket;
	}

	public int getIdContact() {
		return idContact;
	}

	public void setIdContact(int idContact) {
		this.idContact = idContact;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public int getCretedByUserId() {
		return cretedByUserId;
	}

	public void setCretedByUserId(int cretedByUserId) {
		this.cretedByUserId = cretedByUserId;
	}

	public String getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = createdByUser;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public int getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(int idProduct) {
		this.idProduct = idProduct;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getBundleNumber() {
		return bundleNumber;
	}

	public void setBundleNumber(String bundleNumber) {
		this.bundleNumber = bundleNumber;
	}

	public java.math.BigDecimal getPrice() {
		return price;
	}

	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
}
