package com.hochschild.speed.back.model.reporte;

import lombok.Data;

import java.util.Date;

public @Data class DocumentoPorAreaBean {
    private String area;
    private String nombreDocumento;
    private String numeroDocumento;
    private Date fechaCreacion;
    private String asunto;
    private String numeroExpediente;
    private Character estado;
    private String areaActual;

    public DocumentoPorAreaBean(String area, String nombreDocumento, String numeroDocumento, Date fechaCreacion, String asunto, String numeroExpediente, Character estado, String areaActual) {
        this.area = area;
        this.nombreDocumento = nombreDocumento;
        this.numeroDocumento = numeroDocumento;
        this.fechaCreacion = fechaCreacion;
        this.asunto = asunto;
        this.numeroExpediente = numeroExpediente;
        this.estado = estado;
        this.areaActual = areaActual;
    }


}
