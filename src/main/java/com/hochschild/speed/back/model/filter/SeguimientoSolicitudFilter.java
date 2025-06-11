package com.hochschild.speed.back.model.filter;

import lombok.Data;

public @Data class SeguimientoSolicitudFilter {
    private String numero;
    private String sumilla;
    private Integer idContraparte;
    private Integer idResponsable;
    private Integer idSolicitante;
    private String estado;
}
