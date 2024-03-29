package com.raffle.pojo;

import java.util.Date;

public class Ticket {

	public final static int PRODUCT_ID_CAR = 1, PRODUCT_ID_VACATION = 2;
	private int idTicket;
	private int idPeriod;
	private int idContact;
	private String ticketNumber;
	private Date purchaseDate;
	private Date creationDate;
	public int getIdTicket() {
		return idTicket;
	}
	public void setIdTicket(int idTicket) {
		this.idTicket = idTicket;
	}
	public int getIdPeriod() {
		return idPeriod;
	}
	public void setIdPeriod(int idPeriod) {
		this.idPeriod = idPeriod;
	}
	public int getIdContact() {
		return idContact;
	}
	public void setIdContact(int idContact) {
		this.idContact = idContact;
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
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public int getIdUser() {
		return idUser;
	}
	public void setIdUser(int idUser) {
		this.idUser = idUser;
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
	private int idUser;
	private String paymentType;
	private int idProduct;

	@Override
	public String toString() {
		return "Ticket{" +
				"idTicket=" + idTicket +
				", idPeriod=" + idPeriod +
				", idContact=" + idContact +
				", ticketNumber='" + ticketNumber + '\'' +
				", purchaseDate=" + purchaseDate +
				", creationDate=" + creationDate +
				", idUser=" + idUser +
				", paymentType='" + paymentType + '\'' +
				", idProduct=" + idProduct +
				'}';
	}
}
