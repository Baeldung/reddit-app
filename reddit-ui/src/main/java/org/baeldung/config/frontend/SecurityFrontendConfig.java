package org.baeldung.config.frontend;

import org.baeldung.security.MyRememberMeTokenService;
import org.baeldung.security.MyUserDetailsService;
import org.baeldung.security.RememberMePersistentTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SecurityFrontendConfig extends WebSecurityConfigurerAdapter {

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
        http.anonymous().disable().csrf().disable().authorizeRequests()
    	    .antMatchers("/feedForm","/profile","/scheduledPosts","/feeds","/changePassword","/updatePassword").authenticated()
            .antMatchers("/adminHome","/users").hasAuthority("USER_READ_PRIVILEGE")
        .and()
	    .formLogin().loginPage("/").loginProcessingUrl("/j_spring_security_check").defaultSuccessUrl("/home")
            .failureUrl("/?error").usernameParameter("username").passwordParameter("password")
            .successHandler(successHandler)
            .and()
            .logout().invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutUrl("/logout").logoutSuccessUrl("/")
            .and()
            .rememberMe().userDetailsService(userDetailsService).authenticationSuccessHandler(successHandler).tokenValiditySeconds(60*60).rememberMeServices(rememberMeService())
//            .and()
//            .sessionManagement().sessionFixation().none().maximumSessions(1).maxSessionsPreventsLogin(true);
;        // @formatter:on
    }

    private RememberMeServices rememberMeService() {
        final MyRememberMeTokenService service = new MyRememberMeTokenService("mykey", userDetailsService, tokenRepository);
        service.setAlwaysRemember(true);
        return service;
    }
}
