package com.pharmacie.pharmacie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configuration pour servir les fichiers dans le dossier "uploads"
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");  // ./uploads/ fait référence au dossier uploads à la racine de votre projet
    }
}
