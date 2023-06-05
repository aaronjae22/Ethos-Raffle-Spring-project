package com.raffle.view.model.contact;

import java.math.BigDecimal;
import java.util.List;

import com.raffle.pojo.Contact;
import com.raffle.pojo.ContactCall;

public class ContactInformationDetailModel {

	
	
	/**
	 * Informacion del Contacto a mostrar
	 */
	private Contact contactInformation; 
	
	
	/**
	 * Lista de items comprados por el contacto
	 */
	List<TicketPurchaseItem> ticketPurchaseList;
	
	/**
	 * Lista de llamadas realizadas al contacto
	 */
	List<ContactCall> contactCallList;

	public Contact getContactInformation() {
		return contactInformation;
	}

	public void setContactInformation(Contact contactInformation) {
		this.contactInformation = contactInformation;
	}

	public List<TicketPurchaseItem> getTicketPurchaseList() {
		return ticketPurchaseList;
	}

	public void setTicketPurchaseList(List<TicketPurchaseItem> ticketPurchaseList) {
		this.ticketPurchaseList = ticketPurchaseList;
	}

	public List<ContactCall> getContactCallList() {
		return contactCallList;
	}

	public void setContactCallList(List<ContactCall> contactCallList) {
		this.contactCallList = contactCallList;
	}
	
	
	public BigDecimal getTotalAmount()
	{
		if(this.ticketPurchaseList == null || this.ticketPurchaseList.size() == 0)
			return new BigDecimal(0);
		
		
		
		BigDecimal total = new BigDecimal(0);
		
		for( TicketPurchaseItem item : this.ticketPurchaseList)
		{
			total = total.add(item.price);
		}
		
		return total;
	}
}
