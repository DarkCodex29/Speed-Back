package com.hochschild.speed.back.model.filter.reportefilter;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
@ToString
public @Data class ExpedienteAreaFilter {
    private Integer idUsuario;
    private Integer idProceso;
    private String numeroExpediente;
    private String fechaInicio;
    private String fechaFin;
    private Character estado;
    private Integer areaCreadora;
    private Integer areaDestino;
    private Integer sedeOrigen;
    private Integer sedeDestino;
    private Integer idUsuarioFinal;
    private String fechaUltDerivacion;
}
