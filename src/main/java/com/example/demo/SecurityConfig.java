package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // User configuration
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails normalUser=User
                .withUsername("Pranay")
                .password(passwordEncoder().encode("password"))
                // roles
                .roles("NORMAL")
                .build();
        UserDetails adminUser=User
                .withUsername("Admin")
                .password(passwordEncoder().encode("password"))
                .roles("ADMIN")
                .build();
        InMemoryUserDetailsManager inMemoryUserDetailsManager= new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(normalUser);
        inMemoryUserDetailsManager.createUser(adminUser);

        return inMemoryUserDetailsManager;
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity.csrf().disable()
                .authorizeHttpRequests()
                // Role based Authentication
                .requestMatchers("/home/admin")
                .hasRole("ADMIN")
                .requestMatchers("/home/normal")
                .hasRole("NORMAL")
                .requestMatchers("/home/public")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin();

        return httpSecurity.build();
    }


}
