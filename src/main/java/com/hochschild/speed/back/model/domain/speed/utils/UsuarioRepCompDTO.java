package com.hochschild.speed.back.model.domain.speed.utils;

import java.io.Serializable;
public class UsuarioRepCompDTO implements Serializable {
    private Integer idRepComp;
    private String usuario;
    private Integer id;
    private String correo;    
    private String nroDocumento;
    private String estado;
    
    public Integer getIdRepComp() {
        return idRepComp;
    }

    public void setIdRepComp(Integer idRepComp) {
        this.idRepComp = idRepComp;
    }
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}