package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;

public @Data class SedeBean {
    private Integer id;
    private String nombre;
    private Integer idUbigeo;
    private Boolean estado;
}