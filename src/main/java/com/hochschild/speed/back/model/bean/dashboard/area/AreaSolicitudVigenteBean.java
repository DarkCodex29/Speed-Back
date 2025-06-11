package com.hochschild.speed.back.model.bean.dashboard.area;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class AreaSolicitudVigenteBean {
    private String solicitud;
    private String abogado;
    private String solicitante;
    private String contraparte;
    private String sumilla;
    private String fechaVencimiento;
    private Integer diasPorVencer;

}
