package com.hochschild.speed.back.model.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class MisPendientesBean {

    private Integer idExpediente;
    private String numero;
    private String nombreCompania;
    private String contraparte;
    private String sumilla;
    private String estado;
    private String fechaSolicitud;
    private String fechaBorrador;
    private String nombreProceso;
}
