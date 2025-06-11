package com.hochschild.speed.back.model.domain.speed.utils;

import lombok.Data;

import java.util.Date;

public @Data class EstadoDocumentoDTO {
    private Integer id;
    private String codigoEstado;
    private String estado;
    private Date fecha;
    private Integer situacion;
    private String flujo;
}
