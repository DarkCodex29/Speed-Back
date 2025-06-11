package com.hochschild.speed.back.model.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class SolicitudesPorEnviarBean {

    private Integer idExpediente;
    private String numero;
    private String nombreCompania;
    private String nombreContraparte;
    private String fechaCreacion;
    private Integer idProceso;
    private String proceso;
}
