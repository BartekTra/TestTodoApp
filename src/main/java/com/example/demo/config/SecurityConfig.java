package com.example.demo.config;

import com.example.demo.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import com.example.demo.dao.User;


import jakarta.servlet.Filter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * SecurityConfig class is responsible for configuring the security settings of the application.
 * It enables web security, defines authentication providers, and configures HTTP security.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAthFilter jwtAuthFilter;
    private final UserDao userDao;

    /**
     * Configures the security filter chain for the application.
     *
     * @param http HttpSecurity object used to customize security settings.
     * @return Configured SecurityFilterChain object.
     * @throws Exception in case of any configuration errors.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors(cors -> corsConfigurationSource())
                .authorizeHttpRequests()
                .requestMatchers("/", "/**", "/api/v1/auth/**", "/main.css").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.loginPage("http://localhost:3000/Pages/login").permitAll());

        return http.build();
    }

    /**
     * Provides the authentication provider for the application.
     *
     * @return Configured DaoAuthenticationProvider object.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProviderObject = new DaoAuthenticationProvider();
        authenticationProviderObject.setUserDetailsService(userDetailsService());
        authenticationProviderObject.setPasswordEncoder(passwordEncoder());

        return authenticationProviderObject;
    }

    /**
     * Provides the password encoder to be used for encoding and decoding user passwords.
     *
     * @return PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * Configures the CORS settings for the application.
     *
     * @return CorsConfigurationSource object with the required settings.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Provides the user details service for the application.
     *
     * @return UserDetailsService implementation.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userDao.findUserByEmail(email);
    }

    /**
     * Provides the authentication manager for the application.
     *
     * @param config AuthenticationConfiguration object.
     * @return AuthenticationManager instance.
     * @throws Exception in case of any errors.
     */
    @Bean(name = "authenticationManager")
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

