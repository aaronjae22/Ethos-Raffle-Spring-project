package com.raffle.utilities.zipUtilities.entities;



/**
 * @author lcruz
 * Response from http://ziptasticapi.com/ZIPCODE
 */

public class ZipCodeResponse {

	private String error;
	private String country;
	private String state;
	private String city;
	
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	
	
	
	
}
