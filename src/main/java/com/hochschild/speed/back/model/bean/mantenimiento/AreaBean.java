package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;
public @Data class AreaBean {
    private Integer id;
    private String nombre;
    private Integer idSede;
    private Integer idDependencia;
}