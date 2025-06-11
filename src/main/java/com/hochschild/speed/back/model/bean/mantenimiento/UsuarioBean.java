package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;
import java.util.List;
public @Data class UsuarioBean {

    private Integer id;
    private String nombres;
    private String apellidos;
    private String clave;
    private String correo;
    private Boolean estado;
    private Boolean requiereAprobacion;
    private String usuario;
    private Integer idArea;
    private Integer idJefe;
    private List<UsuarioRolBean> rolesAsignados;
    private List<UsuarioPerfilBean> perfilesAsignados;
}