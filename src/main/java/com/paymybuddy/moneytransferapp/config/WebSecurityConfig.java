package com.paymybuddy.moneytransferapp.config;

import com.paymybuddy.moneytransferapp.service.UserAccountUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

@Autowired
    private DataSource dataSource;

    @Bean
    public UserAccountUserDetailsService userAccountUserDetailsService(){
        return new UserAccountUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userAccountUserDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/all").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                    .usernameParameter("email")
                    .defaultSuccessUrl("/all")
                    .permitAll()
                .and()
                .logout().logoutUrl("/").permitAll();
    }

}
