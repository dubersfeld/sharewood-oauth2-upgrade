package com.dub.spring.utils;


import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



public class RedirectFilter implements Filter {
	
	
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpSession session = httpRequest.getSession();
		
        String requestURI = httpRequest.getRequestURI();
		String queryStr = httpRequest.getQueryString();
		String fullRequestURI = requestURI + "?" + queryStr;
		
        Authentication auth = SecurityContextHolder
        							.getContext()
        							.getAuthentication();
            
        String patternString1 = "/authorization/login.*";
        String patternString2 = "/authorization/css.*";
        Pattern pattern1 = Pattern.compile(patternString1);
        Pattern pattern2 = Pattern.compile(patternString2);
        Matcher matcher1 = pattern1.matcher(requestURI);
        Matcher matcher2 = pattern2.matcher(requestURI);
            
        if ((auth == null) && !matcher1.matches() && !matcher2.matches()) {
			
			session.setAttribute("requestURI", fullRequestURI);	
		} 
        
        chain.doFilter(request, response);
    }
  
       
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {  	
    }

    @Override
    public void destroy() { }

	
}