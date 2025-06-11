package com.hochschild.speed.back.model.bean.dashboard.adc;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
public @Data class ProcesoFirmaBean {

    private String solicitud;
    private String abogado;
    private String contraparte;
    private String fechaEnvio;
    private Integer diasEnvio;
}