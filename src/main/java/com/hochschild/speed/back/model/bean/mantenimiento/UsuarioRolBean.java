package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;

public @Data class UsuarioRolBean {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String codigo;
    private String codigoSCA;
    private Character estado;
}