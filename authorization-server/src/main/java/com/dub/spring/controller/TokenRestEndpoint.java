package com.dub.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tokens")
public class TokenRestEndpoint {

	@Autowired
	DefaultTokenServices tokenServices; 
	
	@RequestMapping(
			value = "revoke",
			method = RequestMethod.POST)
	@ResponseBody
	public String revokeToken(@RequestBody String tokenId) {
	    
		tokenServices.revokeToken(tokenId);
	    return tokenId;
	}
}
