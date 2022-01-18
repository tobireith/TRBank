package de.othr.sw.TRBank.util;

import de.othr.sw.TRBank.service.KundeServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TRBankSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private KundeServiceIF kundeService;
    @Autowired
    private TRBankSecurityUtilities securityUtilities;

    private BCryptPasswordEncoder passwordEncoder() {
        return securityUtilities.passwordEncoder();
    }

    private static final String[] ALLOW_ACCESS_WITHOUT_AUTHENTICATION = {
            "/static/css/**", "/image/**", "/fonts/**", "/js/**", "/login", "/register", "/static/**", "/resources/**", "/css/**", "/error",
            "/api/rest/**"
    };

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(ALLOW_ACCESS_WITHOUT_AUTHENTICATION)
                .permitAll().anyRequest().authenticated();
        http
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/")
                    .failureUrl("/login?error")
                .and()
                    .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                    .deleteCookies("remember-me")
                    .permitAll()
                .and()
                .rememberMe();
        // Cross-Site Request Forgery ausschalten
        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(kundeService)
                .passwordEncoder(passwordEncoder());
    }

}
