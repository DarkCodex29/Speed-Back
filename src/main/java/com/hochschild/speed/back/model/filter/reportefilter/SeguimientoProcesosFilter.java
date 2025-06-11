package com.hochschild.speed.back.model.filter.reportefilter;

import lombok.Data;
import lombok.ToString;

@ToString
public @Data class SeguimientoProcesosFilter {
    private Integer[] idSolicitanteSP;
    private Integer idCompaniaSP;
    private String[] idEstadoSP;
    private String idUbicacionSP;
}
