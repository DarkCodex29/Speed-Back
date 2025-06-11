package com.hochschild.speed.back.model.domain.speed.utils;
import java.io.Serializable;
import java.util.List;
import com.hochschild.speed.back.model.domain.speed.utils.UsuarioNotificacionDTO;

public class DocumentoLegalManualDTO implements Serializable {

    private List<UsuarioNotificacionDTO> usuarios; 
    private Integer idExpediente; 
    private Integer idResponsable;

    public List<UsuarioNotificacionDTO> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UsuarioNotificacionDTO> usuarios) {
        this.usuarios = usuarios;
    }

    public Integer getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(Integer idExpediente) {
        this.idExpediente = idExpediente;
    }

    public Integer getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(Integer idResponsable) {
        this.idResponsable = idResponsable;
    }
}