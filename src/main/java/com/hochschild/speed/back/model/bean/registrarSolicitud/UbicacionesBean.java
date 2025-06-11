package com.hochschild.speed.back.model.bean.registrarSolicitud;

import lombok.Data;

public @Data class UbicacionesBean {

    private Integer idUbicacion;
    private String nombre;
    private Boolean esNuevo;
    private Boolean eliminado;

    public void trimAttributes() {
        this.nombre = this.nombre != null ? this.nombre.trim() : null;
    }
}