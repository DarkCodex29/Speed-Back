package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;
public @Data class AlertaCleanBean {

    private Integer id;
    private GridBean grid;
    private TipoAlertaBean tipoAlerta;
}