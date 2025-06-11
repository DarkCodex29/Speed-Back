package com.hochschild.speed.back.model.filter;

import lombok.Data;
import lombok.ToString;

@ToString
public @Data class BuscarClienteFilter {
    private Integer idTipo;
    private String filtroRazonSocial;
    private String filtroCorreo;
    private String filtroNumDocumento;
}
