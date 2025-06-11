package com.hochschild.speed.back.model.bean.dashboard.area;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class SolicitudBean {
    private String solicitud;
    private String abogado;
    private String contraparte;
    private String estado;
    private String fechaEnvio;
    private Integer diasEnvio;
}
