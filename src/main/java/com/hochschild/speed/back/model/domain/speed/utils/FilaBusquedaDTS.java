package com.hochschild.speed.back.model.domain.speed.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilaBusquedaDTS {

    private Integer idExpediente;
    private Integer idDocumentoLegal;
    private Integer idTipoProceso;
    private Integer idContratoAdenda;
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
    private List<FilaBusquedaDTS> docHijos;

    public FilaBusquedaDTS() {
        this.docHijos = new ArrayList<>();

    }
    
    public FilaBusquedaDTS(Integer idExpediente, Integer idDocumentoLegal, Integer idTipoProceso,  Integer idContratoAdenda, String numero, String tipo, String tipoContrato, String cnt_tipo, String cnt_nombre, String cnt_razon,
            Character est_codigo, String voPais, String voCompania, String voArea, String sumilla) {
        this.idExpediente = idExpediente;
        this.idDocumentoLegal = idDocumentoLegal;
        this.idTipoProceso = idTipoProceso;
        this.idContratoAdenda = idContratoAdenda;
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
        this.docHijos = new ArrayList<>();
    }

    public FilaBusquedaDTS(Integer idExpediente, Integer idDocumentoLegal, Integer idTipoProceso, Integer idContratoAdenda, String numero, String tipo, String tipoContrato, String cnt_tipo, String cnt_nombre, String cnt_razon,
            Character est_codigo, String voPais, String voCompania, String voArea, String sumilla, String compania, String solicitante, String responsable, String moneda,
            Float flMonto, String pais, String area) {
        this.idExpediente = idExpediente;
        this.idDocumentoLegal = idDocumentoLegal;
        this.idContratoAdenda = idContratoAdenda;
        this.idTipoProceso = idTipoProceso;
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
        this.docHijos = new ArrayList<>();
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

    public List<FilaBusquedaDTS> getDocHijos() {
        return docHijos;
    }

    public void setDocHijos(List<FilaBusquedaDTS> docHijos) {
        this.docHijos = docHijos;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.idExpediente);
        hash = 71 * hash + Objects.hashCode(this.idDocumentoLegal);
        hash = 71 * hash + Objects.hashCode(this.numero);
        hash = 71 * hash + Objects.hashCode(this.tipo);
        hash = 71 * hash + Objects.hashCode(this.tipoContrato);
        hash = 71 * hash + Objects.hashCode(this.contraparte);
        hash = 71 * hash + Objects.hashCode(this.cnt_tipo);
        hash = 71 * hash + Objects.hashCode(this.cnt_nombre);
        hash = 71 * hash + Objects.hashCode(this.cnt_razon);
        hash = 71 * hash + Objects.hashCode(this.ubicacion);
        hash = 71 * hash + Objects.hashCode(this.est_codigo);
        hash = 71 * hash + Objects.hashCode(this.estado);
        hash = 71 * hash + Objects.hashCode(this.voPais);
        hash = 71 * hash + Objects.hashCode(this.voCompania);
        hash = 71 * hash + Objects.hashCode(this.voArea);
        hash = 71 * hash + Objects.hashCode(this.sumilla);
        hash = 71 * hash + Objects.hashCode(this.compania);
        hash = 71 * hash + Objects.hashCode(this.solicitante);
        hash = 71 * hash + Objects.hashCode(this.responsable);
        hash = 71 * hash + Objects.hashCode(this.moneda);
        hash = 71 * hash + Objects.hashCode(this.flMonto);
        hash = 71 * hash + Objects.hashCode(this.monto);
        hash = 71 * hash + Objects.hashCode(this.fechaInicio);
        hash = 71 * hash + Objects.hashCode(this.fechaVencimiento);
        hash = 71 * hash + Objects.hashCode(this.pais);
        hash = 71 * hash + Objects.hashCode(this.area);
        hash = 71 * hash + (this.puedeVisualizar ? 1 : 0);
        hash = 71 * hash + Objects.hashCode(this.docHijos);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FilaBusquedaDTS other = (FilaBusquedaDTS) obj;
        if (this.puedeVisualizar != other.puedeVisualizar) {
            return false;
        }
        if (!Objects.equals(this.numero, other.numero)) {
            return false;
        }
        if (!Objects.equals(this.tipo, other.tipo)) {
            return false;
        }
        if (!Objects.equals(this.tipoContrato, other.tipoContrato)) {
            return false;
        }
        if (!Objects.equals(this.contraparte, other.contraparte)) {
            return false;
        }
        if (!Objects.equals(this.cnt_tipo, other.cnt_tipo)) {
            return false;
        }
        if (!Objects.equals(this.cnt_nombre, other.cnt_nombre)) {
            return false;
        }
        if (!Objects.equals(this.cnt_razon, other.cnt_razon)) {
            return false;
        }
        if (!Objects.equals(this.ubicacion, other.ubicacion)) {
            return false;
        }
        if (!Objects.equals(this.estado, other.estado)) {
            return false;
        }
        if (!Objects.equals(this.voPais, other.voPais)) {
            return false;
        }
        if (!Objects.equals(this.voCompania, other.voCompania)) {
            return false;
        }
        if (!Objects.equals(this.voArea, other.voArea)) {
            return false;
        }
        if (!Objects.equals(this.sumilla, other.sumilla)) {
            return false;
        }
        if (!Objects.equals(this.compania, other.compania)) {
            return false;
        }
        if (!Objects.equals(this.solicitante, other.solicitante)) {
            return false;
        }
        if (!Objects.equals(this.responsable, other.responsable)) {
            return false;
        }
        if (!Objects.equals(this.moneda, other.moneda)) {
            return false;
        }
        if (!Objects.equals(this.monto, other.monto)) {
            return false;
        }
        if (!Objects.equals(this.fechaInicio, other.fechaInicio)) {
            return false;
        }
        if (!Objects.equals(this.fechaVencimiento, other.fechaVencimiento)) {
            return false;
        }
        if (!Objects.equals(this.pais, other.pais)) {
            return false;
        }
        if (!Objects.equals(this.area, other.area)) {
            return false;
        }
        if (!Objects.equals(this.idExpediente, other.idExpediente)) {
            return false;
        }
        if (!Objects.equals(this.idDocumentoLegal, other.idDocumentoLegal)) {
            return false;
        }
        if (!Objects.equals(this.est_codigo, other.est_codigo)) {
            return false;
        }
        if (!Objects.equals(this.flMonto, other.flMonto)) {
            return false;
        }
        if (!Objects.equals(this.docHijos, other.docHijos)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FilaBusquedaDTS{" + "idExpediente=" + idExpediente + ", idDocumentoLegal=" + idDocumentoLegal + ", numero=" + numero + ", tipo=" + tipo + ", tipoContrato=" + tipoContrato + ", contraparte=" + contraparte + ", cnt_tipo=" + cnt_tipo + ", cnt_nombre=" + cnt_nombre + ", cnt_razon=" + cnt_razon + ", ubicacion=" + ubicacion + ", est_codigo=" + est_codigo + ", estado=" + estado + ", voPais=" + voPais + ", voCompania=" + voCompania + ", voArea=" + voArea + ", sumilla=" + sumilla + ", compania=" + compania + ", solicitante=" + solicitante + ", responsable=" + responsable + ", moneda=" + moneda + ", flMonto=" + flMonto + ", monto=" + monto + ", fechaInicio=" + fechaInicio + ", fechaVencimiento=" + fechaVencimiento + ", pais=" + pais + ", area=" + area + ", puedeVisualizar=" + puedeVisualizar + ", docHijos=" + docHijos + '}';
    }

    public Integer getIdContratoAdenda() {
        return idContratoAdenda;
    }

    public void setIdContratoAdenda(Integer idContratoAdenda) {
        this.idContratoAdenda = idContratoAdenda;
    }

    public Integer getIdTipoProceso() {
        return idTipoProceso;
    }

    public void setIdTipoProceso(Integer idTipoProceso) {
        this.idTipoProceso = idTipoProceso;
    }
}
