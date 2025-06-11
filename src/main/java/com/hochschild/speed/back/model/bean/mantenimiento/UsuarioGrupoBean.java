package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;
public @Data class UsuarioGrupoBean {
    private Integer id;
    private String usuario;
    private String nombres;
    private String apellidos;
    private String correo;
}