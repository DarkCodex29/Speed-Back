package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;
public @Data class TipoAlertaBean {
    private Integer id;
    private String imagen;
    private String nombre;
    private Integer porcentajeIntervalo;
    private Boolean defecto;
}