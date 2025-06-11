package com.hochschild.speed.back.model.domain.speed.utils;

import java.util.List;

/**
 *
 * @author Luciano Carhuaricra - lcarhuaricra@consultorahdc.com
 * @date 11/02/2022
 */
public class SeguridadConfigDTO {

    private Boolean esConfidencial;
    private Integer idExpediente;
    private List<UsuarioNotificacionDTO> usuarios;

    public Boolean getEsConfidencial() {
        return esConfidencial;
    }

    public void setEsConfidencial(Boolean esConfidencial) {
        this.esConfidencial = esConfidencial;
    }

    public Integer getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(Integer idExpediente) {
        this.idExpediente = idExpediente;
    }

    public List<UsuarioNotificacionDTO> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UsuarioNotificacionDTO> usuarios) {
        this.usuarios = usuarios;
    }
}