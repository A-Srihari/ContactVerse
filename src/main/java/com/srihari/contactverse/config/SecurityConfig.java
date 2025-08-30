package com.srihari.contactverse.config;

import com.srihari.contactverse.services.SecurityCustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailService securityCustomUserDetailService;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(securityCustomUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth ->
                    auth.requestMatchers("/login", "/signup", "/register", "/home").permitAll() // Allow public access
                        .requestMatchers("/user/**").authenticated() // Restrict user-related routes
                        .anyRequest().permitAll() // Permit all other requests
                );

        // Configure login form
        httpSecurity.formLogin(formLogin ->
            formLogin.loginPage("/login").permitAll() // Custom login page
                .loginProcessingUrl("/authenticate") // Process login request
                .defaultSuccessUrl("/user/profile", true) // Explicitly redirect to user profile after login success
                .failureUrl("/login?error=true") // Redirect to login page after login failure
                .usernameParameter("email") // Username field
                .passwordParameter("password") // Password field
        );

        // Logout configuration
        httpSecurity.logout(logout ->
            logout.logoutUrl("/logout")
                  .logoutSuccessUrl("/login?logout=true")
                  .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
        );

        // OAuth2 Login configuration
        httpSecurity.oauth2Login(oauth ->
            oauth.loginPage("/login")
                 .successHandler(authenticationSuccessHandler)  // Custom handler for OAuth2 success
        );

        // // Enforce HTTPS for all requests
        // httpSecurity.requiresChannel(channel ->
        //     channel.anyRequest().requiresSecure()
        // );

        // CSRF disable (optional, for APIs)
        // httpSecurity.csrf(csrf -> csrf.disable());

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
