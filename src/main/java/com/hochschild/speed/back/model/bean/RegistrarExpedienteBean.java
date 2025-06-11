package com.hochschild.speed.back.model.bean;

import lombok.Data;

public @Data class RegistrarExpedienteBean {

    private Long id;
    private String titulo;
    private Long procesoSelect;
    private Long procesoId;
    private String usuarioResponsable;
    private Long responsable;

    public void trimAttributes() {
        this.titulo = this.titulo != null ? this.titulo.trim() : null;
        this.usuarioResponsable = this.usuarioResponsable != null ? this.usuarioResponsable.trim() : null;
    }
}