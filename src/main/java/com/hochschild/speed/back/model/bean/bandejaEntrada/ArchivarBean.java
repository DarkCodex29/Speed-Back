package com.hochschild.speed.back.model.bean.bandejaEntrada;

import lombok.Data;
public @Data class ArchivarBean {

    private Integer idExpediente;
    private String observacion;
    private String accion;
    private Boolean eliminarSolicitud;
}