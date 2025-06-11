package com.hochschild.speed.back.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public @Data class ApplicationConfig {

    @Value("${application.name}")
    private String name;
    @Value("${application.acronym}")
    private String acronym;
    @Value("${application.front.url}")
    private String frontUrl;
    @Value("${application.ver.archivo.url}")
    private String verArchivoUrl;
}
