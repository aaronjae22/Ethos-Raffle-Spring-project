package com.raffle.controller;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.raffle.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raffle.dao.ContactDataAccess;
import com.raffle.dao.ProductDataAccess;
import com.raffle.dao.TicketDataAccess;
import com.raffle.util.GeneralResponse;
import com.raffle.util.ProductsList;
import com.raffle.utilities.zipUtilities.IZipCodeManager;
import com.raffle.utilities.zipUtilities.entities.ZipCodeResponse;
import com.raffle.view.model.BundleTickets;
import com.raffle.view.model.BundleValidationInfo;
import com.raffle.view.model.EditTicketValidationInfo;
import com.raffle.view.model.MultipleTicketFormValues;
import com.raffle.view.model.PurchasedTicketInformation;

@Controller
class TicketController extends BaseController {

	
	@Autowired
	ContactDataAccess contactDataAccess;
	
	@Autowired
	TicketDataAccess ticketDataAccess;
	
	@Autowired
	ProductDataAccess productDataAccess;
	
	
	@Autowired
	IZipCodeManager zipCodeManager;
	
	@RequestMapping("/contact/get-zip-info")
	public @ResponseBody ZipCodeResponse getZipInfo(String zipCode)
	{
	
		ZipCodeResponse response =zipCodeManager.requestZipInformation(zipCode);
		
		if(response!=null)
			return response;		
		else	
		{
			ZipCodeResponse errorResponse  = new 	 ZipCodeResponse();
			errorResponse.setError("An internal error ocurred reading the ZIP code Information!");
			return errorResponse;
		}
	}
	
	
	@RequestMapping("/contact/list")
	public @ResponseBody List<Contact> listContacts(String searchTerm, int limit)
	{
		List<Contact> contacts = contactDataAccess.searchContactsByFirstName(searchTerm, limit);
		
		return contacts;
	}
	
	/**
	 * Makes a transaction where saves multiple tickets from the UI
	 * @param tickets A collection of tickts bunldes and tickets to be saved
	 * @return A respose with success or fail, and a reason message
	 */
	@Transactional
	@RequestMapping(value = "/ticket/save-multiple-tickets", method = RequestMethod.POST)
	public @ResponseBody GeneralResponse saveMultipleTickets(@RequestBody MultipleTicketFormValues tickets)
	{
		
		GeneralResponse validationResponse =  this.validateTickets(tickets);
		if(validationResponse != null && validationResponse.isSuccess() == false )
		{
			return validationResponse;
		}
		
		
		GeneralResponse response = new GeneralResponse();
		
		ProductsList productList = new ProductsList( productDataAccess.getProductList() );
		
		String loggedUser = this.getLoggedUserName();
		
		//validate data
		if(tickets == null)
		{
			response.setSuccess(false);
			response.setMessage("No data was sent to server!");
			return response;
		}

		if(tickets.getBundleTickets() == null || tickets.getBundleTickets().size() == 0)
		{
			response.setSuccess(false);
			response.setMessage("Please provide the tickets numbers!");
			return response;
		}
		
		
		try
		{					
			if(tickets == null || tickets.getContactInformation() == null )
			{
				response.setSuccess(false);
				response.setMessage("No information to save!");
			}
						
		
			TicketListItem ticket = tickets.getContactInformation();
			Contact contact = new Contact();
			contact.setFirstName(ticket.getFirstName());
			contact.setLastName(ticket.getLastName());
			contact.setAddress(ticket.getAddress());
			contact.setCity(ticket.getCity());
			contact.setCreationDate(new java.util.Date());
			contact.setEmail(ticket.getEmail());
			contact.setPhone(ticket.getPhone());
			contact.setZipCode(ticket.getZipcode());
			contact.setState(ticket.getState());
			
			//save or update contact
			if(ticket.getIdContact() == 0)
			{
				contact.setIdContact(0);
				
				ticket.setIdContact(contact.getIdContact());
				
				Integer idContact = contactDataAccess.createContact(contact).intValue();
				contact.setIdContact(idContact);				
				ticket.setIdContact(idContact);
			}
			else
			{
				contact.setIdContact(ticket.getIdContact());
				contactDataAccess.update(contact);				 
			}
						
			//ingresar la informacion del bundle
			
			List<TicketListItem> allTickets = new ArrayList<TicketListItem>();
			
			//ingresar los tickets numbers para los bundles
			for( BundleTickets bundle : tickets.getBundleTickets() )
			{	
				boolean isBundle = false;
				if(bundle.getBundleNumber() != null &&  !(bundle.getBundleNumber()).trim() .equals("")    )
				{
					isBundle = true;
									
					TicketListItem newBundleTicket =  (TicketListItem) ticket.clone();
					
					newBundleTicket.setIdProduct(ProductEnum.Bundle);
					newBundleTicket.setTicketNumber(bundle.getBundleNumber());
					newBundleTicket.setPrice( productList.getBundleProduct().getCurrentPrice() );
					
					
					allTickets.add(newBundleTicket);
					
				}
				else					
				{
					isBundle = false;
				}
				
				
				for(String carTicket : bundle.getCarTickets())
				{
					TicketListItem newCarTicket =  (TicketListItem) ticket.clone();					
					newCarTicket.setIdProduct( ProductEnum.Car );	
					newCarTicket.setTicketNumber(carTicket);
					
					if(isBundle)
					{
						newCarTicket.setBundleNumber(bundle.getBundleNumber());
						newCarTicket.setPrice(productList.getCarProduct().getCurrentPrice());
					}
					else
					{
						newCarTicket.setPrice(productList.getCarProduct().getCurrentPrice());
					}
					
					allTickets.add(newCarTicket);
				}
				
				for(String vacationTicket : bundle.getVacTickets())
				{
					TicketListItem newVacationTicket =  (TicketListItem) ticket.clone();					
					newVacationTicket.setIdProduct( ProductEnum.Vacation );	
					newVacationTicket.setTicketNumber(vacationTicket);
					
					if(isBundle)
					{
						newVacationTicket.setBundleNumber(bundle.getBundleNumber());
						newVacationTicket.setPrice(productList.getVacProduct().getCurrentPrice());
					}
					else
					{
						newVacationTicket.setPrice(productList.getVacProduct().getCurrentPrice());
					}
					
					allTickets.add(newVacationTicket);
				}							
			}
			
			ticketDataAccess.saveAllTickets(allTickets,loggedUser);
			response.setSuccess(true);
			return response;
		}
		catch(Exception ex)
		{
			response.setSuccess(false);
			ex.printStackTrace();
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	   			
		}
		return response;
	}

	@Transactional
	@RequestMapping(value ="/ticket/save-ticket",method = RequestMethod.POST)
	public @ResponseBody GeneralResponse saveTicket( @RequestBody TicketListItem ticket) throws Exception
	{
		GeneralResponse response  = new GeneralResponse();
		
		try
		{	
			
			
			Contact contact = new Contact();
			
			
			contact.setFirstName(ticket.getFirstName());
			contact.setLastName(ticket.getLastName());
			contact.setAddress(ticket.getAddress());
			contact.setCity(ticket.getCity());
			contact.setCreationDate(new java.util.Date());
			contact.setEmail(ticket.getEmail());
			contact.setPhone(ticket.getPhone());
			contact.setZipCode(ticket.getZipcode());
			contact.setState(ticket.getState());
			
			if(ticket.getIdContact() == 0)
			{
				contact.setIdContact(0);
				
				ticket.setIdContact(contact.getIdContact());
				
				int idContact = contactDataAccess.createContact(contact).intValue();
				contact.setIdContact(idContact);				
				ticket.setIdContact(idContact);
			}
			else
			{
				contact.setIdContact(ticket.getIdContact());
				contactDataAccess.update(contact);
				 
			}
			
			User user =  ticketDataAccess.getUserByUserName(this.getLoggedUserName());
			
			ticketDataAccess.saveTicket(ticket, user.getIdUser() , UUID.randomUUID().toString());
			response.setMessage("Ticket saved successfully!");
			response.setSuccess(true);
		}
		catch(Exception ex)
		{
			response.setMessage("An error has occurred saving the Ticket information!");
			response.setSuccess(false);
			ex.printStackTrace();
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	    
		}
		
		return response;
		
	}
	
	@Transactional
	@RequestMapping(value ="/ticket/delete-ticket",method = RequestMethod.POST)
	public @ResponseBody GeneralResponse deleteTicket( int idTicket, boolean remove) throws Exception
	{
		GeneralResponse response = new GeneralResponse();
		
	
		try
		{
			User user =  ticketDataAccess.getUserByUserName(this.getLoggedUserName());			
			ticketDataAccess.deleteTicket(idTicket, user.getIdUser() , remove  );
			
			
			response.setMessage("Ticket voided successfully!");
			response.setSuccess(true);
		}
		catch(Exception ex)
		{
			response.setMessage("An error has occurred saving the Ticket information!");
			response.setSuccess(false);
			ex.printStackTrace();
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	    
		}
		
		return response;
				
	}
	  
	
	@RequestMapping (value = "/ticket/validate-ticket")
	public @ResponseBody GeneralResponse validateTickets(@RequestBody MultipleTicketFormValues allTickets )
	{		
		GeneralResponse response = new GeneralResponse();		
		try
		{
			if(allTickets == null || allTickets.getBundleTickets() == null || allTickets.getBundleTickets().size() == 0)
			{ 
				throw new InvalidParameterException("No valid ticket was provided to the validation process!");
			}
			
			List<String> invalidTickets = ticketDataAccess.validateTickets(allTickets);
			
			if(invalidTickets != null && allTickets.getBundleTickets().size() > 0)
			{			
				for(String ticket : invalidTickets)
				{					
					response.addMessage("The ticket number <b>" + ticket + "</b> is already used, please use another ticket number!");
				}
			}
			
			response.setSuccess(true);			
		}
		catch(Exception ex)
		{
			response.setSuccess(false);
			response.setMessage("Tickets can't be validated!, Server error!");
			
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			
		}
		return response;
	}
	
	@RequestMapping( value = "/ticket/view-ticket-counters")
	public String viewTicketCounterByWeek ( Model model, int idPeriod)
	{
		List<TicketCounterWeek> counterList = ticketDataAccess.getTicketCountByWeek(idPeriod);
		
		model.addAttribute("ticketCounters", counterList);
		
		return "view-ticket-counters";
	}
	
	
	
	@Transactional
	@RequestMapping(value ="/ticket/winner-ticket",method = RequestMethod.POST)
	public @ResponseBody GeneralResponse winnerTicket( int idTicket, boolean makeWinner) throws Exception
	{
		GeneralResponse response = new GeneralResponse();
		
	
		try
		{
			User user =  ticketDataAccess.getUserByUserName(this.getLoggedUserName());
			
			ticketDataAccess.winnerTicket(idTicket, user.getIdUser() , makeWinner );
					
			response.setMessage("Ticket set as Winner successfully!");
			response.setSuccess(true);
		}
		catch(Exception ex)
		{
			response.setMessage("An error has occurred saving the Ticket information!");
			response.setSuccess(false);
			ex.printStackTrace();
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	    
		}
		
		return response;
				
	}
	
	
	@RequestMapping(value="/ticket/view-bundle-info")
	public @ResponseBody BundleTickets retrieveBundleInformation(int idPeriod, String ticketNumber, boolean isBundleNumber )
	{
		BundleTickets bundleTickets = new BundleTickets();
		
		bundleTickets.setSuccess(true);
		try
		{
			List<BundleDetails> bundleDetails = this.ticketDataAccess.retrieveBundleDetails(idPeriod, ticketNumber, isBundleNumber);
			bundleTickets.setIdPeriod(idPeriod);
			if(bundleDetails.size() > 0) //hay details
			{
				BundleDetails det = bundleDetails.get(0);
				
				bundleTickets.setBroken(det.isBroken());
				bundleTickets.setBundleNumber(det.getBundleNumber());
				bundleTickets.setCarTickets( new ArrayList<String>());
				bundleTickets.setVacTickets( new ArrayList<String>());
				
				for(BundleDetails d : bundleDetails)
				{
					if(d.getIdProduct() == Ticket.PRODUCT_ID_CAR) //CAR TICKETS
						bundleTickets.getCarTickets().add(d.getTicketNumber());
					else
						bundleTickets.getVacTickets().add(d.getTicketNumber()); //VACATION TICKETS
				}	
			}
			else				
			{
				bundleTickets.setSuccess(false);
				bundleTickets.setMessage("Bundle not found in database!");
				
			}
		}
		catch(Exception ex)
		{
			bundleTickets.setSuccess(false);
			bundleTickets.addMessage("An error has ocurred retrieving the Bundle Information!");
			ex.printStackTrace();
		}
		
		return bundleTickets;
	}
	
	/**
	 * Muestra la informacion de un bundle, generalmente se utiliza esta informacion para poder hacer un break del bundle
	 * @param model
	 * @param idPeriod
	 * @param bundleNumber
	 * @param ticketNumberFocus
	 * @return
	 */
	@RequestMapping( value = "/ticket/view-bundle-details")
	public String viewTicketCounterByWeek ( Model model, int idPeriod, String bundleNumber, String ticketNumberFocus)
	{
		
		BundleTickets tickets =  retrieveBundleInformation(idPeriod, bundleNumber, true);
		
		model.addAttribute("tickets", tickets);
		model.addAttribute("ticketNumberFocus", ticketNumberFocus);
		
		return "view-bundle-details";
	}
	
	
	@Transactional	
	@RequestMapping( value = "/ticket/break-bundle-ticket")
	public @ResponseBody GeneralResponse breakBundleTicket(int idPeriod, String bundleNumber )
	{		
		GeneralResponse response = new GeneralResponse();		
		try
		{
			this.ticketDataAccess.breakBundleTicket(idPeriod, bundleNumber, this.getLoggedUser().getIdUser());
			
			response.setSuccess(true);			   
		}
		catch(Exception ex)
		{
			response.setSuccess(false);
			response.setMessage("Ticket can't be split!, Server error!");
			
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			
		}
		return response;
	}
	
	
	/**
	 * 
	 * Validada la informacion del ticket, para conocer su existencia
	 * @param idPeriod
	 * @param tNumber
	 * @param idProduct
	 * @return
	 */
	@RequestMapping( value = "/ticket/validate-ticket-number")
	public @ResponseBody GeneralResponse validateTicketNumber(int idPeriod, String ticketNumber , int idProduct)
	{
		GeneralResponse response = new GeneralResponse();
		
		 List<TicketNumbers> tickets = this.ticketDataAccess.getTicketData(idPeriod, ticketNumber);
		
		 if(tickets == null || tickets.size() == 0)
		 {
			 response.setSuccess( false);
			 response.addMessage("Ticket number not found in database!, please provide a valid ticket!");
			 return response;
		 }		
		 else
		 {
			 TicketNumbers ticket =tickets.get(0);
			
			 
			 if(ticket.getIdProduct() != idProduct )
			 {
				 
				 String productName = "";
				 
				 switch(ticket.getIdProduct())
				 {
				 	case ProductEnum.Car:
				 		productName = "Car";
				 	break;
				 	case ProductEnum.Vacation:
				 		productName = "Vacation";
				 	break;
				 	case ProductEnum.Bundle:
				 		productName = "Bundle";
				 	break;
				 }
				 
				 
				 response.setSuccess(false);
				 response.addMessage("The Ticket number is a " + productName + " ticket, can't be used here!" );
				 return response;
			 }
			 
			 
			 response.setSuccess(true);
		 }
		 
		 
		 return response;
	}	
	
	
	
	/*Ticket edition section*/
	
	@RequestMapping( value = "/ticket/ticket-edit-form")
	public String viewTicketCounterByWeek ( Model model, int idPeriod, int idTicket)
	{
		
		PurchasedTicketInformation info = this.ticketDataAccess.getPurchasedTicketInformation(idPeriod, idTicket);	
		
		
		model.addAttribute("ticketInformation",info);
		
		return "ticket-edit-form";
	}
	
	@RequestMapping( value = "/ticket/validate-new-edit-ticket")
	public @ResponseBody GeneralResponse validateNewEditTicketInformation(Model model, int idPeriod, String ticketNumber, int idProduct)
	{
		
		
		GeneralResponse response = new GeneralResponse();
		response.setSuccess(true);
		
		//si es un bundle, 
		if(idProduct == ProductEnum.Bundle)
		{		
			//validar que el bundle nuevo exista
			//validar si esta vendido
			//validar que no esté broken		
			BundleValidationInfo validationInfo = this.ticketDataAccess.getBundleValidationInfo(idPeriod, ticketNumber);
			
			
			if(validationInfo == null)
			{
				response.addMessage("The Bundle #" + ticketNumber+" do not exists!");				
			}
			else
			{
				if(validationInfo.isSold())
				{
					response.addMessage("This Bundle is already sold!");
				}
				
				if(validationInfo.isBroken())
				{
					response.addMessage("You can't use this Bundle because is Split into single Tickets!");					
				}
				
			}			
			return response;
		}
		else			
		{
			//si no es bundle
			//validar que el ticket nuevo exista
			 EditTicketValidationInfo validationInfo = this.ticketDataAccess.getEditTicketValidationInfo(idPeriod, ticketNumber);
		
			 if(validationInfo == null)
			 {
				 response.addMessage("This ticket number do not exists!");
			 }
			 else
			 {
				 //validar que no este vendido
				 if(validationInfo.isSold())
				 {
					 response.addMessage("You can't use this ticket, because was sold previously!");
				 }
				 
				 //validar que no se sea parte de un bundle no broken			
				 if( validationInfo.getBundleNumber() != null &&  !validationInfo.isBroken())
				 {
					 response.addMessage("This ticket is part of an active Bundle, can't be used!");
				 }
				 
			 }
			 if( response.getMultipleMessage() != null && response.getMultipleMessage().size() > 0)
				 response.setSuccess(false);
			 
			 return response;
		}
	}
	
	@Transactional
	@RequestMapping( value = "/ticket/save-new-edit-ticket")
	public @ResponseBody GeneralResponse saveNewEditTicketInformation(Model model, int idPeriod, String newTicketNumber, String oldTicketNumber, int idProduct, Date purchaseDate, String paymentType)
	{
	
		GeneralResponse response = new GeneralResponse();
		
		try
		{
			 this.ticketDataAccess.saveNewEditTicketInformation(idPeriod, newTicketNumber, oldTicketNumber, idProduct,purchaseDate,paymentType,getLoggedUser().getIdUser());
			
			 response.setSuccess(true);
			 
		}
		catch(Exception ex)
		{
			response.setMessage("An error has occurred saving the Ticket information!");
			response.setSuccess(false);
			ex.printStackTrace();
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return response;
	}
}



















