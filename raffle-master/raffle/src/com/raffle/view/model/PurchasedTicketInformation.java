package com.raffle.view.model;

import java.util.Date;
import java.util.List;

import com.raffle.pojo.Contact;

public class PurchasedTicketInformation {

	
	private int idTicket;

	
	
	private String ticketNumber;
	
	private boolean isWinner;
	private boolean isDeleted;
	private boolean isBundle;
	
	private List<String> vacTickets;
	private List<String> carTickets;
	
	private Date purchaseDate;
	
	private String paymentType;
	
	private Contact contact;
	
	private String productName;

	private boolean isFromBundle;
	private String bundleNumberFromCatalog;
	
	public int getIdTicket() {
		return idTicket;
	}

	public void setIdTicket(int idTicket) {
		this.idTicket = idTicket;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public boolean isWinner() {
		return isWinner;
	}

	public void setWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isBundle() {
		return isBundle;
	}

	public void setBundle(boolean isBundle) {
		this.isBundle = isBundle;
	}

	public List<String> getVacTickets() {
		return vacTickets;
	}

	public void setVacTickets(List<String> vacTickets) {
		this.vacTickets = vacTickets;
	}

	public List<String> getCarTickets() {
		return carTickets;
	}

	public void setCarTickets(List<String> carTickets) {
		this.carTickets = carTickets;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBundleNumberFromCatalog() {
		return bundleNumberFromCatalog;
	}

	public void setBundleNumberFromCatalog(String bundleNumberFromCatalog) {
		this.bundleNumberFromCatalog = bundleNumberFromCatalog;
	}

	public boolean isFromBundle() {
		return isFromBundle;
	}

	public void setFromBundle(boolean isFromBundle) {
		this.isFromBundle = isFromBundle;
	}
	
	
	
}
