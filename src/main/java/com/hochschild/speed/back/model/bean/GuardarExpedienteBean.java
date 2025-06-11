package com.hochschild.speed.back.model.bean;

import lombok.Data;

public @Data class GuardarExpedienteBean {

    private Integer idExpediente;
    private String titulo;
    private Integer idProceso;
    private Boolean aplicaPenalidad;

    public void trimAttributes() {
        this.titulo = this.titulo != null ? this.titulo.trim() : null;
    }
}