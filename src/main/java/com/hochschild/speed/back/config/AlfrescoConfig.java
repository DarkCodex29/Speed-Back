package com.hochschild.speed.back.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:alfresco.properties")
public @Data class AlfrescoConfig {

    @Value("${alfresco.habilitado}")
    private Boolean habilitado;

    @Value("${alfresco.servidor}")
    private String servidor;

    @Value("${alfresco.puerto}")
    private Integer puerto;

//    @Value("${alfresco.root}")
//    private String root;

    @Value("${alfresco.space}")
    private String space;

    @Value("${alfresco.space.plantillas}")
    private String spacePlantillas;

    @Value("${alfresco.user}")
    private String user;

    @Value("${alfresco.password}")
    private String password;

    @Value("${espacio.download}")
    private String download;

    @Value("${espacio.cambiar.download}")
    private String cambiarDownload;

    @Value("${espacio.direct}")
    private String direct;

    @Value("${espacio.cambiar.direct}")
    private String cambiarDirect;

    @Value("${alfresco.servidor.cliente}")
    private String servidorCliente;

    @Value("${alfresco.puerto.cliente}")
    private Integer puertoCliente;
}