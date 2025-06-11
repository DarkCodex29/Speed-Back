package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;
import java.util.List;
public @Data class GrupoBean {

    private Integer id;
    private String nombre;
    private Boolean estado;
    private Integer idTipoGrupo;
    private List<UsuariosBean> usuarios;
}