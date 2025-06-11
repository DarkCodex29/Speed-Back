package com.hochschild.speed.back.model.bean.registrarSolicitud;

import lombok.Data;
import java.util.List;

public @Data class PenalidadesBean {

    private Integer idExpediente;
    private List<PenalidadBean> penalidades;
}