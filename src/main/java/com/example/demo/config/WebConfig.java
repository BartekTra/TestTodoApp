package com.example.demo.config; // Adjust this package according to your project structure

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig is a configuration class that customizes Cross-Origin Resource Sharing (CORS)
 * settings for the application.
 * <p>
 * This configuration allows the backend to handle requests from the specified frontend
 * origin, enabling interaction between the client and server.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures CORS settings for the application.
     * <p>
     * This method specifies the allowed origins, HTTP methods, and headers for incoming
     * requests matching the specified URL patterns.
     *
     * @param registry the CorsRegistry used to define the CORS mappings.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**") // Allows CORS for endpoints matching this pattern
                .allowedOrigins("http://localhost:3000") // Frontend origin URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // HTTP methods allowed
                .allowedHeaders("Authorization", "*") // Headers allowed in requests
                .maxAge(3600); // Pre-flight cache duration in seconds
    }
}
