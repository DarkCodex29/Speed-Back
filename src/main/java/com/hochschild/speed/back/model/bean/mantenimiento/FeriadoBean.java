package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;
public @Data class FeriadoBean {
    private Integer id;
    private String fecha;
    private Integer idSede;
    private Boolean estado;
}