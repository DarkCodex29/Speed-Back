package com.hochschild.speed.back.model.reporte;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@NoArgsConstructor
public @Data class SeguimientoProcesosReporteBean {
    private Integer idDocumentoLegal;
    private String ubicaciones;
    private String numero;
    private String tipoSolicitud;
    private String nombreContraparte;
    private String sumilla;
    private String compania;
    private String nombreSolicitante;
    private Date fechaSolicitud;
    private String estado;
    private String nombreResponsable;
    private String observacion;

    public SeguimientoProcesosReporteBean(Integer idDocumentoLegal, String ubicaciones, String numero, String tipoSolicitud, String nombreContraparte, String sumilla, String compania, String nombreSolicitante, Date fechaSolicitud, String estado, String nombreResponsable, String observacion) {
        this.idDocumentoLegal = idDocumentoLegal;
        this.ubicaciones = ubicaciones;
        this.numero = numero;
        this.tipoSolicitud = tipoSolicitud;
        this.nombreContraparte = nombreContraparte;
        this.sumilla = sumilla;
        this.compania = compania;
        this.nombreSolicitante = nombreSolicitante;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
        this.nombreResponsable = nombreResponsable;
        this.observacion = observacion;
    }
}
