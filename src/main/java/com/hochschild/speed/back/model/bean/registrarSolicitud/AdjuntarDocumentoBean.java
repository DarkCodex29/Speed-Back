package com.hochschild.speed.back.model.bean.registrarSolicitud;

import lombok.Data;

public @Data class AdjuntarDocumentoBean {

    private Integer id;
    private Integer idExpediente;
    private Integer idTipoDocumento;
    private String titulo;
    private String numero;
    private String[] archivo;
}