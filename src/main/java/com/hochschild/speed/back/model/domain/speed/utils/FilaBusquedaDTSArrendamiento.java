package com.hochschild.speed.back.model.domain.speed.utils;

public class FilaBusquedaDTSArrendamiento {

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

    public FilaBusquedaDTSArrendamiento() {

    }

    public FilaBusquedaDTSArrendamiento(Integer idExpediente,
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

    public FilaBusquedaDTSArrendamiento(Integer idExpediente,
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getContraparte() {
        return contraparte;
    }

    public void setContraparte(String contraparte) {
        this.contraparte = contraparte;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getCnt_nombre() {
        return cnt_nombre;
    }

    public void setCnt_nombre(String cnt_nombre) {
        this.cnt_nombre = cnt_nombre;
    }

    public String getCnt_razon() {
        return cnt_razon;
    }

    public void setCnt_razon(String cnt_razon) {
        this.cnt_razon = cnt_razon;
    }

    public String getCnt_tipo() {
        return cnt_tipo;
    }

    public void setCnt_tipo(String cnt_tipo) {
        this.cnt_tipo = cnt_tipo;
    }

    public Character getEst_codigo() {
        return est_codigo;
    }

    public void setEst_codigo(Character est_codigo) {
        this.est_codigo = est_codigo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getVoPais() {
        return voPais;
    }

    public void setVoPais(String voPais) {
        this.voPais = voPais;
    }

    public String getVoCompania() {
        return voCompania;
    }

    public void setVoCompania(String voCompania) {
        this.voCompania = voCompania;
    }

    public String getVoArea() {
        return voArea;
    }

    public void setVoArea(String voArea) {
        this.voArea = voArea;
    }

    public Integer getIdDocumentoLegal() {
        return idDocumentoLegal;
    }

    public void setIdDocumentoLegal(Integer idDocumentoLegal) {
        this.idDocumentoLegal = idDocumentoLegal;
    }

    public Integer getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(Integer idExpediente) {
        this.idExpediente = idExpediente;
    }

    public String getSumilla() {
        return sumilla;
    }

    public void setSumilla(String sumilla) {
        this.sumilla = sumilla;
    }

    public String getCompania() {
        return compania;
    }

    public void setCompania(String compania) {
        this.compania = compania;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Float getFlMonto() {
        return flMonto;
    }

    public void setFlMonto(Float flMonto) {
        this.flMonto = flMonto;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean isPuedeVisualizar() {
        return puedeVisualizar;
    }

    public void setPuedeVisualizar(boolean puedeVisualizar) {
        this.puedeVisualizar = puedeVisualizar;
    }

    public String getAplicaArrendamiento() {
        return aplicaArrendamiento;
    }

    public void setAplicaArrendamiento(String aplicaArrendamiento) {
        this.aplicaArrendamiento = aplicaArrendamiento;
    }
}