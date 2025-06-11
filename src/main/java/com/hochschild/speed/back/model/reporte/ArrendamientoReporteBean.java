package com.hochschild.speed.back.model.reporte;

import lombok.Data;

public @Data class ArrendamientoReporteBean {
    private Integer idExpediente;
    private Integer idDocumentoLegal;
    private String numero;
    private String tipo;
    private String tipoContrato;
    private String contraparte;
    private String cnt_tipo;
    private String cnt_nombre;
    private String cnt_razon;
    private String ubicacion;
    private Character est_codigo;
    private String estado;
    private String voPais;
    private String voCompania;
    private String voArea;

    private String sumilla;
    private String compania;
    private String solicitante;
    private String responsable;
    private String moneda;
    private Float flMonto;
    private String monto;
    private String fechaInicio;
    private String fechaVencimiento;
    private String pais;
    private String area;
    private boolean puedeVisualizar;

    private String aplicaArrendamiento;


    public ArrendamientoReporteBean(Integer idExpediente,
                                        Integer idDocumentoLegal,
                                        String numero,
                                        String tipo,
                                        String tipoContrato,
                                        String cnt_tipo,
                                        String cnt_nombre,
                                        String cnt_razon,
                                        Character est_codigo,
                                        String voPais,
                                        String voCompania,
                                        String voArea,
                                        String sumilla,
                                        String aplicaArrendamiento) {
        this.idExpediente = idExpediente;
        this.idDocumentoLegal = idDocumentoLegal;
        this.numero = numero;
        this.tipo = tipo;
        this.tipoContrato = tipoContrato;
        this.cnt_tipo = cnt_tipo;
        this.cnt_nombre = cnt_nombre;
        this.cnt_razon = cnt_razon;
        this.est_codigo = est_codigo;
        this.voPais = voPais;
        this.voCompania = voCompania;
        this.voArea = voArea;
        this.sumilla = sumilla;
        this.aplicaArrendamiento = aplicaArrendamiento;
    }


    public ArrendamientoReporteBean(Integer idExpediente,
                                        Integer idDocumentoLegal,
                                        String numero,
                                        String tipo,
                                        String tipoContrato,
                                        String cnt_tipo,
                                        String cnt_nombre,
                                        String cnt_razon,
                                        Character est_codigo,
                                        String voPais,
                                        String voCompania,
                                        String voArea,
                                        String sumilla,
                                        String compania,
                                        String solicitante,
                                        String responsable,
                                        String moneda,
                                        Float flMonto,
                                        String pais,
                                        String area,
                                        String aplicaArrendamiento) {
        this.idExpediente = idExpediente;
        this.idDocumentoLegal = idDocumentoLegal;
        this.numero = numero;
        this.tipo = tipo;
        this.tipoContrato = tipoContrato;
        this.cnt_tipo = cnt_tipo;
        this.cnt_nombre = cnt_nombre;
        this.cnt_razon = cnt_razon;
        this.est_codigo = est_codigo;
        this.voPais = voPais;
        this.voCompania = voCompania;
        this.voArea = voArea;
        this.sumilla = sumilla;
        this.compania = compania;
        this.solicitante = solicitante;
        this.responsable = responsable;
        this.moneda = moneda;
        this.flMonto = flMonto;
        this.pais = pais;
        this.area = area;
        this.aplicaArrendamiento = aplicaArrendamiento;
    }

}
