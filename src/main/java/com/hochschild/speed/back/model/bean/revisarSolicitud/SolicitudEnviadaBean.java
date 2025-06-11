package com.hochschild.speed.back.model.bean.revisarSolicitud;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class SolicitudEnviadaBean {

    private Integer idProceso;
    private Integer idExpediente;
}