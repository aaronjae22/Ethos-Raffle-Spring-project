package com.raffle.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raffle.dao.UserDataAccess;
import com.raffle.pojo.User;
import com.raffle.util.GeneralResponse;

@Controller
class UserAdministrationController  extends BaseController
{

	@Autowired
	UserDataAccess userDataAccess;
	
	@Secured("Administrator")
	//@PreAuthorize("hasRole('Administrator')")
	@RequestMapping(value ="/user/administration")
	String viewList(Model model )
	{
		
		List<User> userList;
		
		if(this.hasRole("Administrator"))
		{
			userList = userDataAccess.loadUsers() ;		
		}
		else
		{
			userList = new ArrayList<User>();
		}
		
		model.addAttribute("userList", userList);
		
		return "user-administration";
	}
	
	
	@Secured("Administrator")
	//@PreAuthorize("hasRole('Administrator')")
	@RequestMapping(value ="/user/administration/table")
	String viewTable(Model model )
	{
		List<User> userList;
		
		if(this.hasRole("Administrator"))
		{
			userList = userDataAccess.loadUsers() ;		
		}
		else
		{
			userList = new ArrayList<User>();
		}
		
		model.addAttribute("userList", userList);
		
		return "user-administration :: #users-table";	
	}
	
	@Secured("Administrator")
	@RequestMapping(value ="/user/load-user")
	@ResponseBody User loadUser(int idUser)
	{
		User user;		
		if(this.hasRole("Administrator") && idUser != 0)
		{		
			user = userDataAccess.getUserById(idUser);						
		}
		else
		{
			user = new User();
		}
		
		return user;
	}
	
	@Transactional
	@RequestMapping(value = "/user/administration/save-user")
	@ResponseBody GeneralResponse saveUser(@RequestBody User user )
	{
	
		GeneralResponse response = new GeneralResponse();
		
		try
		{		
			if(user.getIdUser() != 0)
			{
				userDataAccess.updateUser(user, this.getLoggedUser().getIdUser());
				response.setMessage("User was added successfully!");
			}
			else
			{			
				userDataAccess.saveUser(user);
				response.setMessage("User was updated successfully!");
			}
			
			response.setSuccess(true);
		}
		catch(Exception ex)
		{
			response.setMessage("An error has occurred saving the user!");
			response.setSuccess(false);
			ex.printStackTrace();
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	 
		}
		return response;
	}	
	
	
	@RequestMapping(value = "/user/view-change-password/{idUser}")
	String viewChangePassword(Model model,@PathVariable( value="idUser") int idUser)
	{
		
		User loggedUser = this.getLoggedUser();
		
		if(loggedUser.getIdUser() != idUser)
		{
			model.addAttribute("errorMessage","You only can change your own password!");
		}
		else
		{
			model.addAttribute("errorMessage","");
		}
		
		User user   = this.userDataAccess.getUserById(idUser);
		
		
		model.addAttribute("user", user);
		 
		return "change-password";
	}
	
	@RequestMapping(value = "/user/do-change-password/{idUser}",method=RequestMethod.POST)
	@ResponseBody GeneralResponse changePassword(@PathVariable( value="idUser") int idUser, @RequestParam(value="password") String password)
	{
		//TODO: Validate user Aministration
		
		GeneralResponse response = new GeneralResponse();
		
		try		
		{
			this.userDataAccess.changeUserPassword(idUser, password);
			
			response.setSuccess(true);
			response.setMessage("Password change successfully!");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			response.setSuccess(false);
			response.setMessage("An error has occurred!");
		}
		
		return response;
	}
	
	@RequestMapping(value = "/user/do-reset-password/{idUser}",method=RequestMethod.POST)
	@ResponseBody GeneralResponse resetPassword(@PathVariable( value="idUser") int idUser)
	{
		GeneralResponse response = new GeneralResponse();
		User loggedUser = this.getLoggedUser();
		if( ! loggedUser.isAdministrator())
		{
			response.setMessage("You can't reset passwords! ");
			response.setSuccess(false);
			
			return response;
		}
		
		try		
		{	
			User user = this.userDataAccess.getUserById(idUser);
			this.userDataAccess.changeUserPassword(idUser, user.getUserName());
			
			response.setSuccess(true);
			response.setMessage("Password changed successfully!");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			response.setSuccess(false);
			response.setMessage("An error has occurred!");
		}
		
		return response;
	}
}



























