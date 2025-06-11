package com.hochschild.speed.back.model.bean.mantenimiento;

import com.hochschild.speed.back.model.domain.speed.Parametro;
import java.util.List;
import lombok.Data;

public @Data class GrupoUsuariosBean {

    private Integer id;
    private String nombre;
    private Boolean estado;
    private Parametro tipoGrupo;
    private List<UsuarioGrupoBean> usuarios;
}