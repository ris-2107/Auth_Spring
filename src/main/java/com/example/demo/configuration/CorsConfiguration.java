 
package com.example.demo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration
{

    private static final String Get = "GET";
    private static final String Post = "POST";
    private static final String Put = "PUT";
    private static final String Delete = "DELETE";

    public WebMvcConfigurer corsConfigurer ()
    {
        return new WebMvcConfigurer()
        {
            @Override
            public void addCorsMappings (CorsRegistry registry)
            {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods(Get, Post, Put, Delete)
                        .allowedHeaders("*")
                        .allowedOriginPatterns("*")
                        .allowCredentials(true);
            }
        };
    }
}
