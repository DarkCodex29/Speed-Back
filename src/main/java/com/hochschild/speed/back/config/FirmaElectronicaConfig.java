package com.hochschild.speed.back.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:firmaelectronica.properties")
public @Data class FirmaElectronicaConfig {

    @Value("${efirma.root}")
    private String root;

    @Value("${efirma.user_email_notifications}")
    private Boolean userEmailNotifications;

    @Value("${efirma.template_id_dni_ce}")
    private String templateDniCe;

    @Value("${efirma.template_id_dni}")
    private String templateDni;

    @Value("${efirma.template_id_ce}")
    private String templateCe;

    @Value("${efirma.connect_timeout}")
    private Integer connectTimeout;

    @Value("${efirma.read_timeout}")
    private Integer readTimeout;
}