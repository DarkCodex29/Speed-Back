package com.hochschild.speed.back.model.bean;

import lombok.Data;

public @Data class TipoClienteBean {

    private Long id;
    private String descripcion;
    private String direccion;
    private Long idTipoConfiguracion;
    private String configuracion;

    public void trimAttributes() {
        this.descripcion = this.descripcion != null ? this.descripcion.trim() : null;
        this.direccion = this.direccion != null ? this.direccion.trim() : null;
    }
}