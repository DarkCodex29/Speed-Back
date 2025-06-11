package com.hochschild.speed.back.model.bean.bandejaEntrada;

import com.hochschild.speed.back.model.domain.speed.Expediente;
import lombok.Data;

public @Data class DatosArchivarBean {

    private Expediente expediente;
    private String accion;
    private Boolean eliminarSolicitud;
}