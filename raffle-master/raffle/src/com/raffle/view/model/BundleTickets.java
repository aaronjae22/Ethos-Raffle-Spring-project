package com.raffle.view.model;

import java.util.List;

import com.raffle.util.GeneralResponse;

public class BundleTickets extends GeneralResponse {

	private String bundleNumber;
	private List<String> carTickets;
	private List<String> vacTickets;
	
	public String getBundleNumber() {
		return bundleNumber;
	}
	public void setBundleNumber(String bundleNumber) {
		this.bundleNumber = bundleNumber;
	}
	public List<String> getCarTickets() {
		return carTickets;
	}
	public void setCarTickets(List<String> carTickets) {
		this.carTickets = carTickets;
	}
	public List<String> getVacTickets() {
		return vacTickets;
	}
	public void setVacTickets(List<String> vacationTickets) {
		this.vacTickets = vacationTickets;
	}
	
	
	/**
	 * Demuestra si este bundle ha sido roto(separado en tickets sueltas)
	 */
	private boolean isBroken;
	
	/**
	 * Period del Bundle
	 */
	private int idPeriod;

	public boolean isBroken() {
		return isBroken;
	}
	public void setBroken(boolean isBroken) {
		this.isBroken = isBroken;
	}
	public int getIdPeriod() {
		return idPeriod;
	}
	public void setIdPeriod(int idPeriod) {
		this.idPeriod = idPeriod;
	}
	
}
