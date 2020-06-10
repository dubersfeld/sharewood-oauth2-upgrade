package com.dub.spring.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import com.dub.spring.services.UserService;
import com.dub.spring.utils.MyAuthenticationSuccessHandler;
import com.dub.spring.utils.RedirectFilter;


@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true, order = 0, mode = AdviceMode.PROXY,
        proxyTargetClass = true
)
//@Order(1)
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

	
	@Value("${login-url}")
	String loginUrl;
	
	@Autowired
	private UserService userService;
	
	@Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	
	
	@Bean
	public SimpleUrlAuthenticationSuccessHandler myAuthenticationSuccessHandler() {
		
		SimpleUrlAuthenticationSuccessHandler handler 
				= new MyAuthenticationSuccessHandler();
			
		handler.setDefaultTargetUrl("/");
		
		return handler;
	}
	
	@Bean
	public AuthenticationEntryPoint myAuthenticationEntryPoint() {
		return new LoginUrlAuthenticationEntryPoint(loginUrl);	
	}
	     
    @Override
    protected void configure(HttpSecurity security) 
    		throws Exception {
        security
                .authorizeRequests()
                    .antMatchers("/oauth/**")
                    	.hasAuthority("USER")                                     	
                    .antMatchers("/login*")
                    	.permitAll()
                    .antMatchers("/login")
                      	.permitAll() 
                    .antMatchers("/api/**")
                    	.permitAll()
                    .antMatchers("/**")
                    	.authenticated()  
                    .and()
                    
                    .formLogin()
                    .loginProcessingUrl("/login")
                    .loginPage("/login").failureUrl("/login?loginFailed")
                    .successHandler(myAuthenticationSuccessHandler()) 
                        
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll()
                    .and().logout()
                    .logoutUrl("/logout")
                    .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                    .permitAll()
                .and().sessionManagement()
                    .sessionFixation().changeSessionId()
                    .maximumSessions(1).maxSessionsPreventsLogin(true)
                   
                .and().and().csrf()
                
        
        		.requireCsrfProtectionMatcher((r) -> {
                        String m = r.getMethod();
                        return !(r.getServletPath().startsWith("/oauth/") || r.getServletPath().startsWith("/api/")) &&
                                ("POST".equals(m) || "PUT".equals(m) ||
                                        "DELETE".equals(m) || "PATCH".equals(m));
                    });
         
        
        security.addFilterAfter(new RedirectFilter(), 
										LogoutFilter.class);
        
    }
    
  
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
	
    	 builder
         	.userDetailsService(this.userService)     
         	.passwordEncoder(passwordEncoder())
         	.and()
         	.eraseCredentials(true);        
    	
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
          return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    
}