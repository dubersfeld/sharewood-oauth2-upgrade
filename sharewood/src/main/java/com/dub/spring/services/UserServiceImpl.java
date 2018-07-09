package com.dub.spring.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dub.spring.controller.UserPrincipal;
import com.dub.spring.entities.MyUser;
import com.dub.spring.repositories.UserRepository;


@Service
public class UserServiceImpl implements UserService {
	    
	@Autowired UserRepository userRepository;

	    
	@Override
	@Transactional	    
	public UserPrincipal loadUserByUsername(String username) {
	    MyUser user = userRepository.getByUsername(username);

	    UserPrincipal principal = new UserPrincipal(user);
	    	
	    // make sure the authorities and password are loaded
	    principal.getAuthorities().size();
	    principal.getPassword();
	    return principal;    
	}

}