package com.raffle.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raffle.dao.ContactDataAccess;
import com.raffle.dao.GeneralCatalogosDataAccess;
import com.raffle.dao.TicketDataAccess;
import com.raffle.pojo.Contact;
import com.raffle.pojo.ContactCallDetail;
import com.raffle.pojo.FollowUpCall;
import com.raffle.pojo.User;
import com.raffle.util.GeneralResponse;
import com.raffle.view.model.contact.ContactInformationDetailModel;

@Controller
class ContactController extends BaseController {

	@Autowired
	public ContactDataAccess contactDataAccess;

	@Autowired
	public TicketDataAccess ticketDataAccess;

	@Autowired
	public GeneralCatalogosDataAccess catalogsDataAccess;

	@RequestMapping("/contact/home-view-list")
	String home(HttpServletRequest request, Model model, Integer defaultIdContact) {

		List<Contact> contactList = contactDataAccess.searchContactsByFilters(100, null, "", "");

		model.addAttribute("contactList", contactList);

		return "contact-list";
	}

	@RequestMapping("/contact/table-view-list")
	String home(HttpServletRequest request, Model model, String firstNameFilter, String lastNameFilter) {

		List<Contact> contactList = contactDataAccess.searchContactsByFilters(100, null, firstNameFilter,
				lastNameFilter);

		model.addAttribute("contactList", contactList);

		return "contact-list :: [@id='rows-container']";
	}

	@RequestMapping("/contact/view-contact-detail")
	String viewContactDetail(HttpServletRequest request, Model model, int idContact) {

		ContactInformationDetailModel viewModel = new ContactInformationDetailModel();

		Contact contact = contactDataAccess.getContactById(idContact);

		viewModel.setContactInformation(contact);
		viewModel.setContactCallList(contactDataAccess.getContactCallList(idContact));
		viewModel.setTicketPurchaseList(ticketDataAccess.getTicketPurchaseList(idContact));

		model.addAttribute("model", viewModel);

		return "contact-detail-partial";

	}

	@RequestMapping("/contact/view-call-form")
	String viewAddCallForm(HttpServletRequest request, Model model, int idContact) {

		List<FollowUpCall> followUpCall = this.catalogsDataAccess.getFowllowUpCallCatalog();
		model.addAttribute("followUpCallList", followUpCall);

		User user = this.getLoggedUser();
		model.addAttribute("loggedUser", user);

		model.addAttribute("idContact", idContact);

		return "view-call-form :: [@id='add-call-form']";
	}

	@Transactional
	@RequestMapping(value = "/contact/save-call", method = RequestMethod.POST)
	public @ResponseBody GeneralResponse saveContactCall(HttpServletRequest request, Model model, Integer idContact,
			Date callDate, String memo, Boolean spokenWithContact, Integer idFollowUpCall) {
		GeneralResponse response = new GeneralResponse();

		if (idContact != null)
			System.out.println("Test");

		try {
			this.contactDataAccess.saveContactCall(idContact, callDate, memo, spokenWithContact, idFollowUpCall,
					this.getLoggedUser().getIdUser());
			response.setSuccess(true);

			return response;
		} catch (Exception ex) {
			ex.printStackTrace();
			response.setSuccess(false);
			response.addMessage("An error has occurred saving the Call!");

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return response;
		}
	}

	@RequestMapping(value = "/contact/download-contact-calls")
	public void downloadContactCalls(HttpServletResponse response, Model model, Integer idPeriod) {
		try {

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", String.format("inline; filename=\"Contact Call List.xlsx\""));

			this.generateCallsExcel(idPeriod, response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void generateCallsExcel(Integer idPeriod, OutputStream stream) {

		Workbook workbook = new XSSFWorkbook();
		Sheet datatypeSheet = workbook.createSheet("Ticket List for Period " + idPeriod);

		List<ContactCallDetail> allCalls = contactDataAccess.getContactCalls();
		writeCallsHeaders(datatypeSheet);

		SimpleDateFormat format = new SimpleDateFormat("MMM dd/yyyy");
		
		SimpleDateFormat formatTime = new SimpleDateFormat("MMM dd/yyyy HH:mm");
		
		for (int i = 0; i < allCalls.size(); i++) {
			ContactCallDetail item = allCalls.get(i);
			Row row = datatypeSheet.createRow(i + 1);

			
			row.createCell(0).setCellValue(item.getContact());
			row.createCell(1).setCellValue(item.getEmail());			
			row.createCell(2).setCellValue(item.getPhone());
			row.createCell(3).setCellValue(item.getAddress());
			row.createCell(4).setCellValue(item.getCity());
			row.createCell(5).setCellValue(item.getState());
			row.createCell(6).setCellValue(item.getZipcode());
			row.createCell(7).setCellValue(format.format( item.getCallDate()));
			row.createCell(8).setCellValue(formatTime.format( item.getCreatedOn()));
			row.createCell(9).setCellValue(item.getCreatedByUser());
			
			
			row.createCell(10).setCellValue(item.getMemo());
			row.createCell(11).setCellValue(item.isSpokenWithContact());
			row.createCell(12).setCellValue(item.getFollowUpCall());			
		}

		try {

			workbook.write(stream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeCallsHeaders(Sheet sheet) {
		Row row = sheet.createRow(0);

		row.createCell(0).setCellValue("Contact");
		row.createCell(1).setCellValue("Email");
		row.createCell(2).setCellValue("Phone");
		row.createCell(3).setCellValue("Address");
		row.createCell(4).setCellValue("City");
		row.createCell(5).setCellValue("State");
		row.createCell(6).setCellValue("ZIP Code");
		row.createCell(7).setCellValue("Call Date");
		row.createCell(8).setCellValue("Created On");

		row.createCell(9).setCellValue("Call Registered By");
		row.createCell(10).setCellValue("Memo");
		row.createCell(11).setCellValue("Spoken with Concat?");
		row.createCell(12).setCellValue("Follow up Call");

	}
	
	
	@RequestMapping("/contact/edit-contact")
	String editContactDetail(HttpServletRequest request, Model model, int idContact) 
	{

		Contact contact = contactDataAccess.getContactById(idContact);

		model.addAttribute("model", contact);

		return "contact-edit-form";

	}
	
	@Transactional
	@RequestMapping(value = "/contact/update-contact", method = RequestMethod.POST)
	public @ResponseBody GeneralResponse updateContact(HttpServletRequest request, Model model, 
			Integer idContact, String firstName, String lastName, String email, String phone, String address, 
			String zipCode, String state, String city) 
	{
		GeneralResponse response = new 	GeneralResponse();
		
		
		try
		{
			Contact contact = new 	Contact();
			contact.setIdContact(idContact);
			contact.setAddress(address);
			contact.setCity(city);
			contact.setCreationDate(null);
			contact.setEmail(email);
			contact.setFirstName(firstName);
			contact.setLastName(lastName);
			contact.setPhone(phone);
			contact.setState(state);
			contact.setZipCode(zipCode);
			
			
			contactDataAccess.update(contact);
			response.setSuccess(true);
			
		}
		catch(Exception ex)
		{
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			response.setMessage("An error has occurred updating Contact Information!");
			response.setSuccess(false);
			ex.printStackTrace();
		}
		
		return response;		
	}
}
























