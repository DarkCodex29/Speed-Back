package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;
public @Data class WorkflowBean {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer idProceso;
    private Integer version;
}