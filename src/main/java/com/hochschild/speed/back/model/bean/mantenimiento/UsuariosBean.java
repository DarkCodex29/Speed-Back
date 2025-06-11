package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;

public @Data class UsuariosBean {
    private Integer id;
    private String usuario;
    private String nombres;
    private String apellidos;
    private String area;
    private String correo;
    private String nombreCompleto;
}