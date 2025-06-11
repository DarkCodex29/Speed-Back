package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;

public @Data class PenalidadBean {

    private Integer id;
    private String penalidad;
    private String idReiterancia;
    private Boolean aplicaPenalidad;
    private Boolean estado;
    private String etiqueta;
    private Boolean aplicaValorDefecto;
    private String numeroReiterancia;
    private String idTipoValor;
    private String valor;
}