package com.hochschild.speed.back.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:mail.properties")
public @Data class MailConfig {

    @Value("${mail.host}")
    private String host;

    @Value("${mail.starttls.enable}")
    private Boolean starttls;

    @Value("${mail.port}")
    private Integer port;

    @Value("${mail.user}")
    private String user;

    @Value("${mail.auth}")
    private Boolean auth;

    @Value("${mail.pass}")
    private String pass;

    @Value("${mail.transport}")
    private String transport;

    @Value("${mail.debug}")
    private Boolean debug;
}