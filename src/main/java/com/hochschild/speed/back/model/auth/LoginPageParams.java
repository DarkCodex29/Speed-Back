package com.hochschild.speed.back.model.auth;

import lombok.Data;

public @Data class LoginPageParams {

    private String applicationName;
    private String applicationAcronym;
    private Boolean scaActivo;
}
