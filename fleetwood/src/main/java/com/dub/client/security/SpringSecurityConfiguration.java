package com.dub.client.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.dub.client.services.CustomLogoutHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true, order = 0, mode = AdviceMode.PROXY,
        proxyTargetClass = true
)
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {
		  
	/*
	@Bean
	public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
	    	return new DefaultWebSecurityExpressionHandler();
	}
	*/
	@Autowired
	CustomLogoutHandler logoutHandler;

	@Lazy
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception
	{    
		return super.authenticationManagerBean();
	}
	
	@Bean
	public SimpleUrlAuthenticationSuccessHandler myAuthenticationSuccessHandler() {
		
		SimpleUrlAuthenticationSuccessHandler handler 
				= new SimpleUrlAuthenticationSuccessHandler();
			
		handler.setDefaultTargetUrl("/index");
		
		return handler;
	}
	

    @Bean
    protected SessionRegistry sessionRegistryImpl() {
        return new SessionRegistryImpl();
    }

    
    @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
    	PasswordEncoder passwordEncoder =
    			PasswordEncoderFactories.createDelegatingPasswordEncoder();
  	
    	auth.inMemoryAuthentication().passwordEncoder(passwordEncoder)
			.withUser("Marissa")
				.password(passwordEncoder.encode("wombat"))
				.roles("USER")
			.and()
			.withUser("Steve")
				.password(passwordEncoder.encode("apple"))
				.roles("USER")
			.and()
			.withUser("Bill")
				.password(passwordEncoder.encode("orange"))
				.roles("USER");
		

	}

    
    @Override
    protected void configure(HttpSecurity security) 
    		throws Exception {
        security
                .authorizeRequests()                                                        	
                    .antMatchers("/login/**").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/logout").permitAll()    
                    .antMatchers("/**").hasAuthority("ROLE_USER")                                                
                    .and().formLogin()
                    .loginPage("/login").failureUrl("/login?loginFailed")
                    .successHandler(myAuthenticationSuccessHandler()) 
                    //.defaultSuccessUrl("/index")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll()
                .and().logout()
                    .logoutUrl("/logout").addLogoutHandler(logoutHandler)
                    .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                    .permitAll()
                .and().sessionManagement()
                    .sessionFixation().changeSessionId()
                    .maximumSessions(1).maxSessionsPreventsLogin(false)
                    .sessionRegistry(this.sessionRegistryImpl())
                .and().and().csrf().disable();
        
    }            
}