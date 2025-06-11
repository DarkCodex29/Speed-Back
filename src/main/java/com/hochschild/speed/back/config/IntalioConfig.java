package com.hochschild.speed.back.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:intalio.properties")
public @Data class IntalioConfig {

    @Value("${intalio.servidor}")
    private String servidor;

    @Value("${intalio.puerto}")
    private Integer puerto;
}
