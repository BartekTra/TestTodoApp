package com.example.demo.config; // Możesz dostosować pakiet do swojego projektu

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Adnotacja, która mówi Springowi, że jest to klasa konfiguracyjna
public class WebConfig implements WebMvcConfigurer {

    // Nadpisujemy metodę addCorsMappings, aby dodać naszą konfigurację CORS
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")  // Zezwalaj na wszystkie endpointy z tego wzorca
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("Authorization", "*")  // Dodaj nagłówek Authorization
                .maxAge(3600);
    }
}
