package com.hochschild.speed.back.ws.model.firma_electronica.request;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

public @Data
class UserReqBean implements Serializable {

    private String name;
    private String email;
    private List<String> groups;
    private List<PrefilledItemReqBean> prefilledItems;
    private Integer idCliente;
    private Integer idTipoRepresentante;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombre;
    private String numeroIdentificacion;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<PrefilledItemReqBean> getPrefilledItems() {
        return prefilledItems;
    }

    public void setPrefilledItems(List<PrefilledItemReqBean> prefilledItems) {
        this.prefilledItems = prefilledItems;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdTipoRepresentante() {
        return idTipoRepresentante;
    }

    public void setIdTipoRepresentante(Integer idTipoRepresentante) {
        this.idTipoRepresentante = idTipoRepresentante;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

}
