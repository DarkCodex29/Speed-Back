package com.hochschild.speed.back.model.reporte;

import lombok.Data;

import java.util.Date;

public @Data class DocumentoLegalReporteBean {
    private Integer id;
    private String numero;
    private String nombreCompania;
    private String razonSocial;
    private Date fechaInicio;
    private Date fechaSolicitud;
    private Date fechaBorrador;
    private String nombreSolicitante;
    private Character estado;
    private String nombreResponsable;

    private String ubicaciones;

    public DocumentoLegalReporteBean(Integer id, String numero, String nombreCompania, String razonSocial, Date fechaInicio, Date fechaSolicitud, Date fechaBorrador, String nombreSolicitante, Character estado, String nombreResponsable) {
        this.id = id;
        this.numero = numero;
        this.nombreCompania = nombreCompania;
        this.razonSocial = razonSocial;
        this.fechaInicio = fechaInicio;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaBorrador = fechaBorrador;
        this.nombreSolicitante = nombreSolicitante;
        this.estado = estado;
        this.nombreResponsable = nombreResponsable;
    }
}
