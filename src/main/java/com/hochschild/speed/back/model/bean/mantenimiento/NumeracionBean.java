package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;
public @Data class NumeracionBean {
    private Integer id;
    private Integer idArea;
    private Integer idTipoDocumento;
    private String postFormato;
    private String preFormato;
    private String tipoNumeracion;
    private Integer numeroActual;
    private Integer longitud;
}