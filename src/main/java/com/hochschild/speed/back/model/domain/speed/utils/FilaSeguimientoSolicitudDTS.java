package com.hochschild.speed.back.model.domain.speed.utils;

public class FilaSeguimientoSolicitudDTS {

    private Integer idExpediente;
    private Integer idDocumentoLegal;
    private String numeroDocumento;
    private String abogado;
    private String sumilla;
    private String contraparte;
    private String solicitante;
    private String ultimoMovimiento;
    private Integer idUbicacion;
    private String ubicacionDocumento;
    private String estado;

    public FilaSeguimientoSolicitudDTS() {

    }

    public FilaSeguimientoSolicitudDTS(Integer idExpediente, Integer idDocumentoLegal, String numeroDocumento, String abogado, String sumilla, String contraparte, String solicitante, String ultimoMovimiento, Integer idUbicacion, String ubicacionDocumento, String estado) {
        this.idExpediente = idExpediente;
        this.idDocumentoLegal = idDocumentoLegal;
        this.numeroDocumento = numeroDocumento;
        this.abogado = abogado;
        this.sumilla = sumilla;
        this.contraparte = contraparte;
        this.solicitante = solicitante;
        this.ultimoMovimiento = ultimoMovimiento;
        this.idUbicacion = idUbicacion;
        this.ubicacionDocumento = ubicacionDocumento;
        this.estado = estado;
    }

    /**
     * @return the idExpediente
     */
    public Integer getIdExpediente() {
        return idExpediente;
    }

    /**
     * @param idExpediente the idExpediente to set
     */
    public void setIdExpediente(Integer idExpediente) {
        this.idExpediente = idExpediente;
    }

    /**
     * @return the idDocumentoLegal
     */
    public Integer getIdDocumentoLegal() {
        return idDocumentoLegal;
    }

    /**
     * @param idDocumentoLegal the idDocumentoLegal to set
     */
    public void setIdDocumentoLegal(Integer idDocumentoLegal) {
        this.idDocumentoLegal = idDocumentoLegal;
    }

    /**
     * @return the numeroDocumento
     */
    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * @param numeroDocumento the numeroDocumento to set
     */
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    /**
     * @return the abogado
     */
    public String getAbogado() {
        return abogado;
    }

    /**
     * @param abogado the abogado to set
     */
    public void setAbogado(String abogado) {
        this.abogado = abogado;
    }

    /**
     * @return the sumilla
     */
    public String getSumilla() {
        return sumilla;
    }

    /**
     * @param sumilla the sumilla to set
     */
    public void setSumilla(String sumilla) {
        this.sumilla = sumilla;
    }

    /**
     * @return the contraparte
     */
    public String getContraparte() {
        return contraparte;
    }

    /**
     * @param contraparte the contraparte to set
     */
    public void setContraparte(String contraparte) {
        this.contraparte = contraparte;
    }

    /**
     * @return the solicitante
     */
    public String getSolicitante() {
        return solicitante;
    }

    /**
     * @param solicitante the solicitante to set
     */
    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    /**
     * @return the ultimoMovimiento
     */
    public String getUltimoMovimiento() {
        return ultimoMovimiento;
    }

    /**
     * @param ultimoMovimiento the ultimoMovimiento to set
     */
    public void setUltimoMovimiento(String ultimoMovimiento) {
        this.ultimoMovimiento = ultimoMovimiento;
    }

    /**
     * @return the idUbicacion
     */
    public Integer getIdUbicacion() {
        return idUbicacion;
    }

    /**
     * @param idUbicacion the idUbicacion to set
     */
    public void setIdUbicacion(Integer idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    /**
     * @return the ubicacionDocumento
     */
    public String getUbicacionDocumento() {
        return ubicacionDocumento;
    }

    /**
     * @param ubicacionDocumento the ubicacionDocumento to set
     */
    public void setUbicacionDocumento(String ubicacionDocumento) {
        this.ubicacionDocumento = ubicacionDocumento;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
}