package com.raffle.util;

import java.util.ArrayList;
import java.util.List;

public class GeneralResponse {

	private boolean isSuccess;
	private String message;
	
	private List<String> multipleMessage;
	
	public List<String> getMultipleMessage() {
		return multipleMessage;
	}
	public void setMultipleMessage(List<String> multipleMessage) {
		this.multipleMessage = multipleMessage;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	public void addMessage(String message)
	{
		if(this.multipleMessage == null)
		{
			this.multipleMessage = new ArrayList<String>();
		}
		this.multipleMessage.add(message);
	}
	
}
