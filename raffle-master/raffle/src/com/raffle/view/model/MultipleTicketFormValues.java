package com.raffle.view.model;

import java.util.List;

import com.raffle.pojo.TicketListItem;

public class MultipleTicketFormValues {	
	private List<BundleTickets> bundleTickets;
	
	public List<BundleTickets> getBundleTickets() {
		return bundleTickets;
	}

	public void setBundleTickets(List<BundleTickets> bundleTickets) {
		this.bundleTickets = bundleTickets;
	}

	public TicketListItem getContactInformation() {
		return contactInformation;
	}

	public void setContactInformation(TicketListItem contactInformation) {
		this.contactInformation = contactInformation;
	}

	private TicketListItem contactInformation;
	
	
}
