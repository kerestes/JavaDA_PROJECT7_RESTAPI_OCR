package com.nnk.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     *
     * @param HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception
     * This method integrates the Spring Boot filter chain and is responsible for authorizing freely accessible pages, or verifying credentials for protected pages
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests( authorize -> {
                   authorize.requestMatchers("/", "/error").permitAll();
                   authorize.requestMatchers("/bidList/**", "/rating/**", "/ruleName/**", "/trade/**", "/curvePoint/**", "/app/logout").hasAnyAuthority("ADMIN", "USER");
                   authorize.requestMatchers("/user/**", "/admin/**", "/secure/**").hasAuthority("ADMIN");
                   authorize.anyRequest().authenticated();
                })
                .formLogin(formLogin -> formLogin.permitAll()
                        .defaultSuccessUrl("/bidList/list", true))
                .logout(logout -> logout.logoutUrl("/app-logout").logoutSuccessUrl("/").permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        return http.build();
    }

    /**
     *
     * @return PasswordEncoder(Bcrypt Algorithme)
     * This Methode returns a Bcrypt Object
     */
    @Bean
    public PasswordEncoder getPasswordEncoder (){
        return new BCryptPasswordEncoder();
    }

    /**
     *
     * @param HttpSecurity
     * @param BCryptPasswordEncoder
     * @return AuthenticationManager
     * @throws Exception
     * This method is responsible for checking the validity of the credentials entered by the user in comparison with the data in the database
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(this.userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }
}
