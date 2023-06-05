package main.java.hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;

//@RestController
@Controller
public class GreetingController {

	@RequestMapping("/hello/dashboard")
	String dashboard()
	{
		return "dashboard";
	}
	
	
    @RequestMapping("/hello/{name}")
    String hello(@PathVariable String name,Model model) {
    	 
    	   
    	
    	model.addAttribute("recipient", name+"55-55-joel-test");
        return "index";///"Hello, " + name + "!";
    } 
} 