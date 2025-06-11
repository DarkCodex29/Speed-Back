package com.hochschild.speed.back.model.filter.mantenimiento;

import lombok.Data;

public @Data class ProcesoFilter {

    private String nombre;
    private Integer idTipoProceso;
    private boolean onlyParents;

}