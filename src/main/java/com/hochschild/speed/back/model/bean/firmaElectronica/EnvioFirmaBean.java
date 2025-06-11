package com.hochschild.speed.back.model.bean.firmaElectronica;

import lombok.Data;

public @Data class EnvioFirmaBean {
    private Integer idExpediente;
    private String idioma;
    private Integer idRepresentante;
    private String tipoFirma;
}
