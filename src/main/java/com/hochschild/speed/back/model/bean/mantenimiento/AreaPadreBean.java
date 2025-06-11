package com.hochschild.speed.back.model.bean.mantenimiento;

import com.hochschild.speed.back.model.domain.speed.Sede;
import lombok.Data;
public @Data class AreaPadreBean {
    private Integer id;
    private String nombre;
    private Sede sede;
    private PadreBean dependencia;
}