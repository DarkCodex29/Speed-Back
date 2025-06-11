package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;
public @Data class TipoNotificacionBean {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String codigo;
    private Boolean estado;
}