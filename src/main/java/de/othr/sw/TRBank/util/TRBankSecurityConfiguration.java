package de.othr.sw.TRBank.util;

import de.othr.sw.TRBank.service.KundeServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TRBankSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String[] ALLOW_ACCESS_WITHOUT_AUTHENTICATION = {
            "/static/css/**", "/static/images/**", "/images/**", "/fonts/**", "/js/**", "/login", "/register", "/static/**", "/resources/**", "/css/**", "/error",
            "/api/rest/**"
    };
    @Autowired
    private KundeServiceIF kundeService;
    @Autowired
    private TRBankSecurityUtilities securityUtilities;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private BCryptPasswordEncoder passwordEncoder() {
        return securityUtilities.passwordEncoder();
    }

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
