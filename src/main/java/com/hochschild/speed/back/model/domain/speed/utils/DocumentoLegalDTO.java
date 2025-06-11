package com.hochschild.speed.back.model.domain.speed.utils;

import java.util.Date;
public class DocumentoLegalDTO {
    private String documentoLegalNumero;
    private String documentoLegalNombreCompania;
    private String cliente;
    private String documentoLegalSumilla;
    private String documentoLegalEstado;
    private String nombreProceso;
    private String solicitante;
    private String areaSolicitante;
    private String abogadoResponsable;
    private Integer idExpediente;

    public String getDocumentoLegalNumero() {
        return documentoLegalNumero;
    }

    public void setDocumentoLegalNumero(String documentoLegalNumero) {
        this.documentoLegalNumero = documentoLegalNumero;
    }

    public String getDocumentoLegalNombreCompania() {
        return documentoLegalNombreCompania;
    }

    public void setDocumentoLegalNombreCompania(String documentoLegalNombreCompania) {
        this.documentoLegalNombreCompania = documentoLegalNombreCompania;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDocumentoLegalSumilla() {
        return documentoLegalSumilla;
    }

    public void setDocumentoLegalSumilla(String documentoLegalSumilla) {
        this.documentoLegalSumilla = documentoLegalSumilla;
    }

    public String getDocumentoLegalEstado() {
        return documentoLegalEstado;
    }

    public void setDocumentoLegalEstado(String documentoLegalEstado) {
        this.documentoLegalEstado = documentoLegalEstado;
    }

    public String getNombreProceso() {
        return nombreProceso;
    }

    public void setNombreProceso(String nombreProceso) {
        this.nombreProceso = nombreProceso;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getAreaSolicitante() {
        return areaSolicitante;
    }

    public void setAreaSolicitante(String areaSolicitante) {
        this.areaSolicitante = areaSolicitante;
    }

    public String getAbogadoResponsable() {
        return abogadoResponsable;
    }

    public void setAbogadoResponsable(String abogadoResponsable) {
        this.abogadoResponsable = abogadoResponsable;
    }

    public Integer getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(Integer idExpediente) {
        this.idExpediente = idExpediente;
    }
}
