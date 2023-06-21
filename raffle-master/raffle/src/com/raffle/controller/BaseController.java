package com.raffle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.raffle.dao.UserDataAccess;
import com.raffle.pojo.User;

public class BaseController 
{
	
	@Autowired 
	UserDataAccess userDataAccess;
	
	public String getLoggedUserName()
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName(); // get logged in username
		return name;
	}
	
	protected boolean hasRole(String role) {
        // get security context from thread local
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null)
            return false;

        Authentication authentication = context.getAuthentication();
        if (authentication == null)
            return false;

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (role.equals(auth.getAuthority()))
                return true;
        }

        return false;
    }

	@ModelAttribute("loggedUser")
	public User getLoggedUser() 
	{
		try
		{
	   User user = this.userDataAccess.getUserByUserName(this.getLoggedUserName());
	   return user;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return new User();
		}
	}
	
}
