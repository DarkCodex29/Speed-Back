package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;

public @Data class RepresentanteCompBean {

    private Integer id;
    private Integer idUsuario;
    private String correo;
    private String numeroDocumento;
    private Boolean estado;
}