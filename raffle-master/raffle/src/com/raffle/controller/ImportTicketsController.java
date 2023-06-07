package com.raffle.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.raffle.dao.UserDataAccess;
import com.raffle.pojo.User;

@Controller
class ImportTicketsController extends BaseController
{

    @Autowired
    UserDataAccess userDataAccess;

    @Secured("Administrator")
    @RequestMapping(value ="import-tickets")
    String viewList(Model model )
    {

        // tatuList<User> userList;

        if(this.hasRole("Administrator"))
        {
            // userList = userDataAccess.loadUsers() ;
            System.out.println("It has permission");
        }
        else
        {
            // userList = new ArrayList<User>();
            System.out.println("It doesn't have permission");
        }

        // model.addAttribute("userList", userList);

        return "import-tickets";
    }

}
