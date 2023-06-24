package com.raffle.pojo;

import java.util.List;

public class BundleDetails {

	
	int idBundle_details; 
	int idPeriod; 
	int idProduct; 
	String bundleNumber;
	String ticketNumber;
	List<Ticket> tickets;
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

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public boolean isBroken() {
		return isBroken;
	}
	public void setBroken(boolean isBroken) {
		this.isBroken = isBroken;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	@Override
	public String toString() {
		return "BundleDetails{" +
				"idBundle_details=" + idBundle_details +
				", idPeriod=" + idPeriod +
				", idProduct=" + idProduct +
				", bundleNumber='" + bundleNumber + '\'' +
				", tickets=" + tickets +
				", isBroken=" + isBroken +
				'}';
	}
}
