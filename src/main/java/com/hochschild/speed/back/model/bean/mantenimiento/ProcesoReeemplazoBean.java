package com.hochschild.speed.back.model.bean.mantenimiento;

import com.hochschild.speed.back.model.domain.speed.TipoProceso;
import lombok.Data;
public @Data class ProcesoReeemplazoBean {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String estado;
    private TipoProceso tipoProceso;
}