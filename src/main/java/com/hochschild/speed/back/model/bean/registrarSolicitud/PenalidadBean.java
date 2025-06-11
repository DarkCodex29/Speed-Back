package com.hochschild.speed.back.model.bean.registrarSolicitud;

import lombok.Data;
import java.util.List;

public @Data class PenalidadBean {

    private Integer idPenalidad;
    private String descripcion;
    private Boolean aplica;
    private List<ReiteranciasBean> reiterancias;
    private Boolean aplicaValorDefecto;
    private Integer numeroReiterancia;
    private String tipoValor;
    private String valor;
}