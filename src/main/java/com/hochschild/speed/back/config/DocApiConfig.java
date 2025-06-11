package com.hochschild.speed.back.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:docapi.properties")
public @Data class DocApiConfig {

    @Value("${docapi.root}")
    private String root;

    @Value("${docapi.connect_timeout}")
    private Integer connectTimeout;

    @Value("${docapi.read_timeout}")
    private Integer readTimeout;
}
