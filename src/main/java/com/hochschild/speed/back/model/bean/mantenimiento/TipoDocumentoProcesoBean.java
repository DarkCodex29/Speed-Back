package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;
public @Data class TipoDocumentoProcesoBean {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer cantidad;
}