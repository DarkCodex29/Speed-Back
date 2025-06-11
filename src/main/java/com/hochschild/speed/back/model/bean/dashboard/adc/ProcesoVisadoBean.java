package com.hochschild.speed.back.model.bean.dashboard.adc;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
public @Data class ProcesoVisadoBean {

    private String solicitud;
    private String abogado;
    private String solicitante;
    private String contraparte;
    private String sumilla;
    private String visadorActual;
    private String fechaEnvio;
    private Integer diasEnvio;
}