package com.dub.spring.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class DefaultController {
	
    @GetMapping("/login")
    public ModelAndView login(ModelAndView modelAndView) {
    	
    	modelAndView.setViewName("login");
    	return modelAndView;
    }
    
/*
    @GetMapping("/logout")
    public String logout(ModelAndView modelAndView) {
    	
    	
    	return "login";
    }
    	
    @GetMapping("/basiclogout")
	public String blogout() {
    
	    	return "login";
	    }
*/
}
