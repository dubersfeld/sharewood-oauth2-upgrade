package com.dub.client.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class CustomLogoutHandler implements LogoutHandler {

	@Autowired
	@Qualifier("sharewoodRestTemplate")   
	private OAuth2RestTemplate sharewoodRestTemplate;
	 
	@Value("${sharewoodTokenBaseURL}")
	private String sharewoodTokenBaseURL;
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
	
    	String revokeUrl = sharewoodTokenBaseURL + "/revoke";
    	
		String tokenId = sharewoodRestTemplate
								.getOAuth2ClientContext()
								.getAccessToken()
								.getValue();
	
		// revoke token
		sharewoodRestTemplate.postForObject(revokeUrl, tokenId, String.class);
		
	}

}
