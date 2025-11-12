package com.app.learningspringai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                        .requestMatchers("/chat.html").permitAll()
                        .requestMatchers("/api/ai/**").permitAll() // needed for chat functionality
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(request ->
                                request.getMethod().equals("POST") &&
                                        request.getRequestURI().startsWith("/api/ai/"))
                )
                .httpBasic(withDefaults());

        http.formLogin(withDefaults());

        return http.build();
    }

/*
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user1= User.withDefaultPasswordEncoder().username("user1").password("user1").roles("ADMIN").build();
        UserDetails user2= User.withDefaultPasswordEncoder().username("user2").password("user2").roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user1,user2);
    }
    */

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

}
