package com.hochschild.speed.back.model.domain.speed.utils;

import java.util.List;
import com.hochschild.speed.back.model.domain.speed.Cliente;
import com.hochschild.speed.back.model.domain.speed.HcUbicacion;

public class DocumentoLegalXAdendaDTS {
    private Integer numeroAdenda;
    private Integer idArea;
    private String area;
    private Integer idCompania;
    private String compania;
    private String pais;
    private Integer idContraparte;
    private String cntNombre;
    private String cntSituacion;
    private String cntDomicilio;
    private String cntNombreContacto;
    private String cntTelefono;
    private String cntEmail;
    private String cntNumeroIdentificacion;
    private List<Cliente> representantes;

    public String getCntNumeroIdentificacion() {
        return cntNumeroIdentificacion;
    }

    public void setCntNumeroIdentificacion(String cntNumeroIdentificacion) {
        this.cntNumeroIdentificacion = cntNumeroIdentificacion;
    }

    private List<HcUbicacion> ubicaciones;

    public DocumentoLegalXAdendaDTS() {

    }

    public DocumentoLegalXAdendaDTS(Integer idArea, String area, Integer idCompania, String compania, String pais, Integer idContraparte, String cntNombre, String cntDomicilio, String cntNombreContacto, String cntTelefono, String cntEmail) {
        this.idArea = idArea;
        this.area = area;
        this.idCompania = idCompania;
        this.compania = compania;
        this.pais = pais;
        this.idContraparte = idContraparte;
        this.cntNombre = cntNombre;
        this.cntDomicilio = cntDomicilio;
        this.cntNombreContacto = cntNombreContacto;
        this.cntTelefono = cntTelefono;
        this.cntEmail = cntEmail;
    }

    public Integer getNumeroAdenda() {
        return numeroAdenda;
    }

    public void setNumeroAdenda(Integer numeroAdenda) {
        this.numeroAdenda = numeroAdenda;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCompania() {
        return compania;
    }

    public void setCompania(String compania) {
        this.compania = compania;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCntNombre() {
        return cntNombre;
    }

    public void setCntNombre(String cntNombre) {
        this.cntNombre = cntNombre;
    }

    public String getCntSituacion() {
        return cntSituacion;
    }

    public void setCntSituacion(String cntSituacion) {
        this.cntSituacion = cntSituacion;
    }

    public String getCntDomicilio() {
        return cntDomicilio;
    }

    public void setCntDomicilio(String cntDomicilio) {
        this.cntDomicilio = cntDomicilio;
    }

    public String getCntNombreContacto() {
        return cntNombreContacto;
    }

    public void setCntNombreContacto(String cntNombreContacto) {
        this.cntNombreContacto = cntNombreContacto;
    }

    public String getCntTelefono() {
        return cntTelefono;
    }

    public void setCntTelefono(String cntTelefono) {
        this.cntTelefono = cntTelefono;
    }

    public String getCntEmail() {
        return cntEmail;
    }

    public void setCntEmail(String cntEmail) {
        this.cntEmail = cntEmail;
    }

    public List<Cliente> getRepresentantes() {
        return representantes;
    }

    public void setRepresentantes(List<Cliente> representantes) {
        this.representantes = representantes;
    }

    public List<HcUbicacion> getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(List<HcUbicacion> ubicaciones) {
        this.ubicaciones = ubicaciones;
    }

    public Integer getIdCompania() {
        return idCompania;
    }

    public void setIdCompania(Integer idCompania) {
        this.idCompania = idCompania;
    }

    public Integer getIdArea() {
        return idArea;
    }

    public void setIdArea(Integer idArea) {
        this.idArea = idArea;
    }

    public Integer getIdContraparte() {
        return idContraparte;
    }

    public void setIdContraparte(Integer idContraparte) {
        this.idContraparte = idContraparte;
    }
}