package com.raffle.pojo;

import java.math.BigDecimal;


public class CurrentTotalAmounts {

	private String period;
	private BigDecimal totalAmount;
	private BigDecimal totalBundle;
	private BigDecimal totalCar;
	private BigDecimal totalVac;
	
	private int totalCount;
	
	private int totalCountBundle; 
	private int totalCountCar; 
	private int totalCountVac;
	
	
	private int totalTickets; 

	public String getPeriod() {
		return period;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public BigDecimal getTotalBundle() {
		return totalBundle;
	}

	public BigDecimal getTotalCar() {
		return totalCar;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getTotalCountBundle() {
		return totalCountBundle;
	}

	public int getTotalCountCar() {
		return totalCountCar;
	}

	
	public int getTotalCountVac() {
		return totalCountVac;
	}

	public int getTotalTickets() {
		return totalTickets;
	}

	public BigDecimal getTotalVac() {
		return totalVac;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setTotalBundle(BigDecimal totalBundle) {
		this.totalBundle = totalBundle;
	}

	public void setTotalCar(BigDecimal totalCar) {
		this.totalCar = totalCar;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public void setTotalCountBundle(int totalCountBundle) {
		this.totalCountBundle = totalCountBundle;
	}

	public void setTotalCountCar(int totalCountCar) {
		this.totalCountCar = totalCountCar;
	}

	public void setTotalCountVac(int totalCountVac) {
		this.totalCountVac = totalCountVac;
	}

	public void setTotalTickets(int totalTickets) {
		this.totalTickets = totalTickets;
	}

	public void setTotalVac(BigDecimal totalVac) {
		this.totalVac = totalVac;
	}
}
