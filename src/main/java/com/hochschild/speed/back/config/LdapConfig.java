package com.hochschild.speed.back.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:ldap.properties")
public @Data class LdapConfig {

    //#####################################################################################################################################
    //#																																	  #
    //#											Configuracion del LDAP      														      #
    //#																																	  #
    //#####################################################################################################################################

    @Value("${ldap.auth-enable}")
    private Boolean authEnable;

    @Value("${ldap.url}")
    private String url;

    @Value("${ldap.domain}")
    private String domain;

    //#####################################################################################################################################
    //#																																	  #
    //#											Active Directory windows      														      #
    //#																																	  #
    //#####################################################################################################################################

    @Value("${logueo.alternativo}")
    private Boolean logueoAlternativo;

    @Value("${ldap.windows.url}")
    private String windowsUrl;

    @Value("${ldap.windows.autenticacion}")
    private String windowsAutenticacion;

    @Value("${ldap.windows.domain}")
    private String windowsDomain;

}