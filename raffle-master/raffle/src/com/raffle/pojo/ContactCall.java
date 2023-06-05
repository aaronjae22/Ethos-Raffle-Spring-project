package com.raffle.pojo;

import java.util.Date;

public class ContactCall 
{
	
	int idContact_call;
	
	int idContact;
	String contact;
	
	Date callDate;
	
	Date createdOn;
	
	int createdByUserId;
	
	String createdByUser;
	
	String memo;
	
	boolean spokenWithContact;
	
	int followUpCallId;
	String followUpCall;
	
	
	
	public int getIdContact_call() {
		return idContact_call;
	}
	public void setIdContact_call(int idContact_call) {
		this.idContact_call = idContact_call;
	}
	public int getIdContact() {
		return idContact;
	}
	public void setIdContact(int idContact) {
		this.idContact = idContact;
	}
	public Date getCallDate() {
		return callDate;
	}
	public void setCallDate(Date callDate) {
		this.callDate = callDate;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public int getCreatedByUserId() {
		return createdByUserId;
	}
	public void setCreatedByUserId(int createdByUserId) {
		this.createdByUserId = createdByUserId;
	}
	public String getCreatedByUser() {
		return createdByUser;
	}
	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = createdByUser;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public boolean isSpokenWithContact() {
		return spokenWithContact;
	}
	public void setSpokenWithContact(boolean spokenWithContact) {
		this.spokenWithContact = spokenWithContact;
	}
	public int getFollowUpCallId() {
		return followUpCallId;
	}
	public void setFollowUpCallId(int followUpCallId) {
		this.followUpCallId = followUpCallId;
	}
	public String getFollowUpCall() {
		return followUpCall;
	}
	public void setFollowUpCall(String follwUpCall) {
		this.followUpCall = follwUpCall;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
		
}
