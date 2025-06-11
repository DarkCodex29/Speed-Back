package com.hochschild.speed.back.model.domain.speed.utils;

public class SolicitudesJson {

    private String abogado;
    private String contraparte;
    private String estado;
    private String id;
    private String idDocumentoLegal;
    private String index;
    private String numeroDocumento;
    private String solicitante;
    private String sumilla;
    private String ubicacionDocumento;
    private String ultimoMovimiento;

    public SolicitudesJson(String abogado, String contraparte, String estado, String id, String idDocumentoLegal, String index, String numeroDocumento, String solicitante, String sumilla, String ubicacionDocumento, String ultimoMovimiento) {
        this.abogado = abogado;
        this.contraparte = contraparte;
        this.estado = estado;
        this.id = id;
        this.idDocumentoLegal = idDocumentoLegal;
        this.index = index;
        this.numeroDocumento = numeroDocumento;
        this.solicitante = solicitante;
        this.sumilla = sumilla;
        this.ubicacionDocumento = ubicacionDocumento;
        this.ultimoMovimiento = ultimoMovimiento;
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

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the idDocumentoLegal
     */
    public String getIdDocumentoLegal() {
        return idDocumentoLegal;
    }

    /**
     * @param idDocumentoLegal the idDocumentoLegal to set
     */
    public void setIdDocumentoLegal(String idDocumentoLegal) {
        this.idDocumentoLegal = idDocumentoLegal;
    }

    /**
     * @return the index
     */
    public String getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(String index) {
        this.index = index;
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
}