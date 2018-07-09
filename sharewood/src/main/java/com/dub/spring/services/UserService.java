package com.dub.spring.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;


@Validated
public interface UserService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username);// custom implementation

    

}
