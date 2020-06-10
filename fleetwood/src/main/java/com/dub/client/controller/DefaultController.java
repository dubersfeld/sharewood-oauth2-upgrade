package com.dub.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.dub.client.photos.Photo;

@Controller
public class DefaultController {
	
	 @Autowired
	 @Qualifier("sharewoodRestTemplate")   
	 private OAuth2RestTemplate sharewoodRestTemplate;

	 @GetMapping({"/", "/backHome", "/index"})
	    public String home1() {
	    
	        return "index";
	    }
	    
	    @GetMapping("/login")
	    public String login() {
	    
	        return "login";
	    }
	    
	  
}
