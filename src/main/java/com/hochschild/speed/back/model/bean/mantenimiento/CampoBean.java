package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;

public @Data class CampoBean {
    private Integer id;
    private Boolean buscable;
    private String contenido;
    private String descripcion;
    private String etiqueta;
    private String fechaCreacion;
    private String nombre;
    private Boolean obligatorio;
    private Integer idTipoCampo;
}