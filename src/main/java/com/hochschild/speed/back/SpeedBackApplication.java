package com.hochschild.speed.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@PropertySource(value = {"classpath:application.properties", "classpath:mail.properties", "classpath:jdbc.properties"})
public class SpeedBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpeedBackApplication.class, args);
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("POST", "PUT", "GET", "OPTIONS", "DELETE").exposedHeaders(HttpHeaders.CONTENT_DISPOSITION);
            }
        };
    }
}