package com.hochschild.speed.back.model.bean.dashboard.abogado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class EstadoSolicitudBean {

    private String abogado;
    private String estado;
    private Integer cantidad;
    private String color;
}