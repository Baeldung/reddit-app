package org.baeldung.config.api;

import org.baeldung.security.MyRememberMeTokenService;
import org.baeldung.security.MyUserDetailsService;
import org.baeldung.security.RememberMePersistentTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;

@Configuration
@EnableWebSecurity
@ComponentScan({ "org.baeldung.security" })
public class SecurityApiConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private RememberMePersistentTokenRepository tokenRepository;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
        http.csrf().disable().authorizeRequests()
    	    .antMatchers("/users*").authenticated()
    	    .antMatchers("/loginn*").anonymous()
        .and()
	    .formLogin().loginPage("/loginn")
	    .and()
            .rememberMe().userDetailsService(userDetailsService).rememberMeServices(rememberMeService())

;//            .and()
//            .sessionManagement().sessionFixation().none().maximumSessions(1).maxSessionsPreventsLogin(true);
        // @formatter:on
    }

    @Bean
    public RememberMeServices rememberMeService() {
        final MyRememberMeTokenService service = new MyRememberMeTokenService("mykey", userDetailsService, tokenRepository);
        service.setAlwaysRemember(true);
        return service;
    }
}
