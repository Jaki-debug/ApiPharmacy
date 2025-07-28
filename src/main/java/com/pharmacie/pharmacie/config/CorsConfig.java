package com.pharmacie.pharmacie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        
        registry.addMapping("/**")  
                .allowedOrigins("http://localhost:4200", "http://1.0.1.239:54711","http://localhost:49520" )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  
                .allowedHeaders("*") 
                .allowCredentials(true)  
                .maxAge(3600);  
    }
}
