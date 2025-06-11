package com.hochschild.speed.back.model.reporte;

import lombok.Data;

import java.util.Date;

public @Data class DocumentoLegalAlarmaContratoBean {
    private String numero;
    private String compania;
    private String nombreContraparte;
    private String nombreResponsable;
    private String nombreSolicitante;
    private Date fechaFinContrato;
    private Character estado;
    private Date fechaAlarma;
    private Integer diasActivacion;
    private Integer intervaloPorMes;
    private String tituloAlarma;
    private Character estadoAlarma;

    public DocumentoLegalAlarmaContratoBean(String numero, String compania, String nombreContraparte, String nombreResponsable, String nombreSolicitante, Date fechaFinContrato, Character estado, Date fechaAlarma, Integer diasActivacion, Integer intervaloPorMes, String tituloAlarma, Character estadoAlarma) {
        this.numero = numero;
        this.compania = compania;
        this.nombreContraparte = nombreContraparte;
        this.nombreResponsable = nombreResponsable;
        this.nombreSolicitante = nombreSolicitante;
        this.fechaFinContrato = fechaFinContrato;
        this.estado = estado;
        this.fechaAlarma = fechaAlarma;
        this.diasActivacion = diasActivacion;
        this.intervaloPorMes = intervaloPorMes;
        this.tituloAlarma = tituloAlarma;
        this.estadoAlarma = estadoAlarma;
    }
}
