package com.raffle.controller;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raffle.dao.ContactDataAccess;
import com.raffle.dao.DashboardDataAccess;
import com.raffle.dao.PeriodDataAccess;
import com.raffle.dao.TicketDataAccess;
import com.raffle.pojo.Contact;
import com.raffle.pojo.Period;
import com.raffle.pojo.ProductEnum;
import com.raffle.pojo.TicketListItem;
import com.raffle.util.GeneralResponse;

@Controller
 class DasboardController extends BaseController{
	
	private static final int defaultLimitList = 50;
	
	@Autowired
	ContactDataAccess contactDataAccess;
	
	@Autowired
	TicketDataAccess ticketDataAccess;
	
	@Autowired 
	DashboardDataAccess dashboardDataAccess;
	
	@Autowired
	PeriodDataAccess periodDataAccess;
	
	
	@RequestMapping({"/dashboard/home/{idPeriod}","/dashboard/home"})
	String home(Model model , @PathVariable( value="idPeriod")  Optional<Integer> idPeriod)
	{
		int periodToUse = 0;
		List<Period> periods = periodDataAccess.getPeriodList();
				
		boolean isCurrentPeriod = false;
		
		if(idPeriod.isPresent())
		{
			periodToUse = idPeriod.get();
						
			for(Period period : periods)	
			{
				if(period.getIdPeriod() == periodToUse && period.isActive() )
				{
					isCurrentPeriod = true;
				}
			}
		}
		else
		{		
			periodToUse = 0;
			for(Period period : periods)	
			{
				if(period.isActive())
				{					
					periodToUse = period.getIdPeriod();
					isCurrentPeriod = true;
				}
			}
		}
		
		model.addAttribute("isCurrentPeriod", isCurrentPeriod);
		model.addAttribute("periodToUseId", periodToUse);
		model.addAttribute("availablePeriods",periods); 

		//load main view list.
		List<TicketListItem> tickets = ticketDataAccess.getTicketList( defaultLimitList, periodToUse, null,null,null);
		
		model.addAttribute("ticketList", tickets);		
		model.addAttribute("totalAmounts", dashboardDataAccess.getTotalAmounts(periodToUse) );
		
		//load indicators
		
		return "dashboard";
	}
	
	@RequestMapping("/dashboard/period-selection")
	String changeActivePeriod(HttpServletRequest request,Model model )
	{
		
		List<Period> periods = periodDataAccess.getPeriodList();
		model.addAttribute("periodList",periods);
		
		
		return "period-list";
	}
	
	
	@Transactional
	@RequestMapping(value = "/dashboard/set-active-period", method = RequestMethod.POST)
	@ResponseBody GeneralResponse setActivePeriod(HttpServletRequest request,Model model , int idPeriod)
	{
		GeneralResponse response = new 	GeneralResponse();
		
		try{
			
			periodDataAccess.setActivePeriod(idPeriod);
			response.setSuccess(true);
		}
		catch(Exception ex)
		{
			response.setSuccess(false);
			response.setMessage("Period not changed!, Server error!");
			
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			
		}
		
		return response; 
	}
	
	@RequestMapping("/dashboard/home/view-list")
	String home(HttpServletRequest request,Model model , int idPeriod, String searchText)
	{
		//load main view list.
		
		Enumeration<String> params = request.getParameterNames();
		
		List<Integer> idProducts = new ArrayList<Integer>(); //Sea usa para el filtro de las columnas
		
		Map<String, String> columnFilters = new HashMap<String,String>();
						
		while(params.hasMoreElements())
		{
			String parameterName = params.nextElement();
			if(parameterName.startsWith("fs_"))
			{
				String columnName = getParameterNameByFilter(parameterName);		
				
				if(parameterName.equals("fs_carNumber"))
					idProducts.add(ProductEnum.Car);
				
				if(parameterName.equals("fs_vacationNumber"))
					idProducts.add(ProductEnum.Vacation);
				
				if(parameterName.equals("fs_bundleNumber"))
					idProducts.add(ProductEnum.Bundle);
				
				
				if(columnName !=null )
					columnFilters.put(columnName, request.getParameter(parameterName));				
			}
		}
				
		List<TicketListItem> tickets = ticketDataAccess.getTicketList(defaultLimitList, idPeriod, searchText, columnFilters,idProducts);
		model.addAttribute("ticketList", tickets);
		
		model.addAttribute("totalAmounts", dashboardDataAccess.getTotalAmounts(idPeriod) );
		
		//load indicators
		
		
		return "dashboard :: [@id='filters-row-body']";
	}
	
	
	@RequestMapping("/dashboard/home/export-results")
	void exportList(HttpServletResponse response, int idPeriod)
	{
		try {
			
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", String.format("inline; filename=\"Ticket-List.xlsx\""));
			
			this.generateTicketExcel(idPeriod, response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	///This is to avoid SQLInjection
	private static String getParameterNameByFilter(String paramName)
	{
		if(paramName.equals("fs_bundleNumber") || paramName.equals("fs_carNumber")|| paramName.equals("fs_vacationNumber"))
		{
			return "ticketNumber";
		}
		
		if(paramName.equals("fs_firstName"))
			return "firstName";
		
		
		if(paramName.equals("fs_lastName"))
			return "lastName";
		
		if(paramName.equals("fs_address"))
			return "address";
		
		if(paramName.equals("fs_city"))
			return "city";
		
		
		if(paramName.equals("fs_state"))
			return "state";
		
		
		if(paramName.equals("fs_zipcode"))
			return "zipcode";
		
		if(paramName.equals("fs_phone"))
			return "phone";
		
		if(paramName.equals("fs_purchaseDate"))
			return "purchaseDate";
		
		if(paramName.equals("fs_paymentType"))
			return "paymentType";
				
		if(paramName.equals("fs_email"))
			return "email";
		
		return null;
	}
	
	
	private  void generateTicketExcel(int idPeriod, OutputStream stream ) throws IOException
	{
			
		 Workbook workbook = new XSSFWorkbook();
         Sheet datatypeSheet = workbook.createSheet("Ticket List for Period " + idPeriod);
		
		List<TicketListItem> allTickets =  ticketDataAccess.getTicketList( 100000 ,idPeriod, "", null,null);
		writeTicketsHeaders(datatypeSheet);
		
		for(int i = 0 ; i < allTickets.size();i++)
		{
			TicketListItem item = allTickets.get(i);
			Row row = datatypeSheet.createRow(i+1);
			
			row.createCell(0).setCellValue(  item.getIdProduct() == 3 ? item.getTicketNumber() : "" );
			row.createCell(1).setCellValue(item.getIdProduct() == 1 ? item.getTicketNumber():"");
			row.createCell(2).setCellValue(item.getIdProduct() == 2 ? item.getTicketNumber(): "");
			row.createCell(3).setCellValue(item.getFirstName());
			row.createCell(4).setCellValue(item.getLastName());
			row.createCell(5).setCellValue(item.getAddress());
			row.createCell(6).setCellValue(item.getCity());
			row.createCell(7).setCellValue(item.getState());
			row.createCell(8).setCellValue(item.getZipcode());
			row.createCell(9).setCellValue(item.getPhone());
			row.createCell(10).setCellValue(item.getPurchaseDate());
			
			row.createCell(11).setCellValue(item.getPaymentType());
			row.createCell(12).setCellValue(item.getEmail());
			row.createCell(13).setCellValue(item.getIdProduct() == 3 ? item.getPrice().doubleValue() : 0);
			row.createCell(14).setCellValue(item.getIdProduct() == 1 ? item.getPrice().doubleValue() : 0);
			row.createCell(15).setCellValue(item.getIdProduct() == 2 ? item.getPrice().doubleValue() : 0);
			row.createCell(16).setCellValue(item.getTotal().doubleValue());
			
			row.createCell(17).setCellValue(item.getMemo());
			
		
			
			row.createCell(18).setCellValue(item.isActive());
			row.createCell(19).setCellValue(item.getCreatedBy());
			row.createCell(20).setCellValue(item.getModifiedBy());
			row.createCell(21).setCellValue(item.getWeekNumber());
			
			
			
		}
		
		 try {
	            //FileOutputStream outputStream = new FileOutputStream(new File("D:/LAPTOP-BK/Harry Work/allList.xlsx"));
	            
			 	
			 
			 	workbook.write(stream);
	            workbook.close();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			
	}


	
	
	@RequestMapping("/dashboard/home/export-contacts")
	void exportContactList(HttpServletResponse response, int idPeriod)
	{
		try {
			
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", String.format("inline; filename=\"Contact-List.xlsx\""));
			
			this.generateContactsExcel(idPeriod, response.getOutputStream());
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping("/dashboard/home/view-ticket-details")
	String viewTicketDetails(Model model ,int idTicket)
	{
		
	  TicketListItem ticketItem = 	ticketDataAccess.getTicketDetails(idTicket);
	  
	  	model.addAttribute("ticketDetail", ticketItem);		
		
		//load indicators		
		return "ticket-details";
		
	}
		
	private void generateContactsExcel(int idPeriod, OutputStream stream) throws IOException
	{
			
		
		 Workbook workbook = new XSSFWorkbook();
         Sheet datatypeSheet = workbook.createSheet("Contact List ");
		
		List<Contact> allContacts =  contactDataAccess.getContactList( idPeriod );
		writeContactHeaders(datatypeSheet);
		
		for(int i = 0 ; i < allContacts.size();i++)
		{
			Contact item = allContacts.get(i);
			Row row = datatypeSheet.createRow(i+1);
			
			row.createCell(0).setCellValue( item.getLastName() );
			row.createCell(1).setCellValue( item.getFirstName() );
			row.createCell(2).setCellValue( item.getEmail() );
			row.createCell(3).setCellValue( item.getPhone() );
			row.createCell(4).setCellValue( item.getCreationDate() );
			row.createCell(5).setCellValue( item.getAddress());
			row.createCell(6).setCellValue( item.getCity());
			
			row.createCell(7).setCellValue( item.getState());
			
			row.createCell(8).setCellValue( item.getZipCode());			
		}
		
		 try 
		 {
			 	workbook.write(stream);
	            workbook.close();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	private void writeContactHeaders(Sheet sheet)
	{
		Row row = sheet.createRow(0);
		
		row.createCell(0).setCellValue("First Name");
		row.createCell(1).setCellValue("Last Name");		
		row.createCell(2).setCellValue("Email");
		row.createCell(3).setCellValue("Phone");
		row.createCell(4).setCellValue("Created On");
		row.createCell(5).setCellValue("Address");
		row.createCell(6).setCellValue("City");
		row.createCell(7).setCellValue("State");
		row.createCell(8).setCellValue("Zipcode");
		
		
		
	}
	private void writeTicketsHeaders(Sheet sheet)
	{
		Row row = sheet.createRow(0);
		
		row.createCell(0).setCellValue("BUN");
		row.createCell(1).setCellValue("CAR");
		row.createCell(2).setCellValue("VAC");
		row.createCell(3).setCellValue("FIRST");
		row.createCell(4).setCellValue("LAST");
		row.createCell(5).setCellValue("ADDRESS");
		row.createCell(6).setCellValue("CITY");
		row.createCell(7).setCellValue("STATE");
		row.createCell(8).setCellValue("ZIPCODE");
		row.createCell(9).setCellValue("Phone");
		row.createCell(10).setCellValue("DATE");
		
		row.createCell(11).setCellValue("PAYTYPE");
		row.createCell(12).setCellValue("E-MAIL");
		row.createCell(13).setCellValue("BUN$$");
		row.createCell(14).setCellValue("CAR$$");
		row.createCell(15).setCellValue("TRIP$$");
		row.createCell(16).setCellValue("Total");
		row.createCell(17).setCellValue("MEMO");
		
		
		row.createCell(18).setCellValue("Active");
		row.createCell(19).setCellValue("Created By");
		row.createCell(20).setCellValue("Modified By");
		row.createCell(21).setCellValue("Week Number");
		
	}

	
	/*TICKET VALIDATIONS*/
	
	@RequestMapping(value = "/dashboard/validate-ticket-number", method = RequestMethod.POST)
	@ResponseBody GeneralResponse validateTicketNumber(HttpServletRequest request,Model model , int idPeriod, int idProduct, String ticketNumber)
	{
		GeneralResponse response = new GeneralResponse();
		//
		
		
		
		return response;
	}
	
	
}

