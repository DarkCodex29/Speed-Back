package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;
public @Data class HcUbicacionBean {

    private Integer idUbicacion;
    private String nombre;
    private Integer idTipoUbicacion;
    private Integer idCompania;
    private Character estado;
    private String codigo;
}