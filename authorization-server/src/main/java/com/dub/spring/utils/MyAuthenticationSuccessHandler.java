package com.dub.spring.utils;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/** 
 * This custom bean is used to force the correct redirect after a successful login.
 * The attribute requestURI was previously attached to the session by the custom bean MyExceptionTranslationFilter.  
 * */

public class MyAuthenticationSuccessHandler 
				extends SimpleUrlAuthenticationSuccessHandler {
	
	@Value("${authorization-url}")
	private String authorizationURL;
	
	@Override
	public void onAuthenticationSuccess(
						HttpServletRequest request, 
						HttpServletResponse response, 
						Authentication authentication) throws ServletException, IOException {
							
		HttpSession session = request.getSession();
			
		String requestURI = (String) session.getAttribute("requestURI");
					
		String redirectURL = (requestURI == null) ? authorizationURL  + "/" : authorizationURL  + requestURI;
			
		super.setDefaultTargetUrl(redirectURL);
		
		handle(request, response, authentication);
		clearAuthenticationAttributes(request);	
	}
	
}