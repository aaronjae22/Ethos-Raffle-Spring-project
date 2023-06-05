package com.raffle.pojo;

import java.util.Date;

public class TicketListItem implements Cloneable{
	
	private int idTicket;
	
	public int getIdTicket() {
		return idTicket;
	}

	public void setIdTicket(int idTicket) {
		this.idTicket = idTicket;
	}

	
	private String address;

	private String city;

	private Date creationDate;

	private String email;

	private String firstName;

	private int idProduct;

	private String lastName;

	private String phone;

	private java.math.BigDecimal price;

	private String productName;

	private Date purchaseDate;

	private String state;

	private java.math.BigDecimal tax;

	private String ticketNumber;

	private java.math.BigDecimal total;

	private String zipcode;

	private String paymentType;
	
	private int idContact;
	
	private String bundleNumber;
	
	private String memo;
	
	private boolean active;
	
	
	private String createdBy;
	
	private String modifiedBy;
	
	private Integer weekNumber;
	
	
	private boolean IsWinner; 
	private int SetWinnerBy; 
	private Date SetWinnerOn; 
	private String SetWinnerDescription  ;
	
	
	private String bundleNumberFromCatalog;
	
	private boolean isInBundle;
	
	public boolean isIsWinner() {
		return IsWinner;
	}

	public void setIsWinner(boolean isWinner) {
		IsWinner = isWinner;
	}

	public int getSetWinnerBy() {
		return SetWinnerBy;
	}

	public void setSetWinnerBy(int setWinnerBy) {
		SetWinnerBy = setWinnerBy;
	}

	public Date getSetWinnerOn() {
		return SetWinnerOn;
	}

	public void setSetWinnerOn(Date setWinnerOn) {
		SetWinnerOn = setWinnerOn;
	}

	public String getSetWinnerDescription() {
		return SetWinnerDescription;
	}

	public void setSetWinnerDescription(String setWinnerDescription) {
		SetWinnerDescription = setWinnerDescription;
	}

	public Integer getWeekNumber() {
		return weekNumber;
	}

	public void setWeekNumber(Integer weekNumber) {
		this.weekNumber = weekNumber;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getBundleNumber() {
		return bundleNumber;
	}

	public void setBundleNumber(String bundleNumber) {
		this.bundleNumber = bundleNumber;
	}

	public int getIdContact() {
		return idContact;
	}

	public void setIdContact(int idContact) {
		this.idContact = idContact;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public int getIdProduct() {
		return idProduct;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhone() {
		return phone;
	}

	public java.math.BigDecimal getPrice() {
		return price;
	}

	public String getProductName() {
		return productName;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public String getState() {
		return state;
	}

	public java.math.BigDecimal getTax() {
		return tax;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public java.math.BigDecimal getTotal() {
		return total;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setIdProduct(int idProduct) {
		this.idProduct = idProduct;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setTax(java.math.BigDecimal tax) {
		this.tax = tax;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public void setTotal(java.math.BigDecimal total) {
		this.total = total;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public boolean isInBundle() {
		return isInBundle;
	}

	public void setInBundle(boolean isInBundle) {
		this.isInBundle = isInBundle;
	}

	public String getBundleNumberFromCatalog() {
		return bundleNumberFromCatalog;
	}

	public void setBundleNumberFromCatalog(String bundleNumberFromCatalog) {
		this.bundleNumberFromCatalog = bundleNumberFromCatalog;
	}


	
}
