package com.hochschild.speed.back.model.bean.registrarSolicitud;

import lombok.Data;

public @Data class DocumentoLegalBean {

    //Expediente
    private Integer idExpediente;
    private Boolean esAvance;
    private Boolean esContrato;
    private Boolean esAdenda;
    private Boolean esAdendaAutomatica;

    //DocumentoLegal
    private Integer idDocumentoLegal;

    //Datos Generales
    private Integer abogadoResponsable;
    private Integer solicitante;
    private Integer tipoContrato;
    private String fechaBorrador;
    private String sumilla;
    private Boolean aplicaPenalidad;

    //Tipo Proceso
    private ContratoBean contrato;
    private AdendaBean adenda;
}