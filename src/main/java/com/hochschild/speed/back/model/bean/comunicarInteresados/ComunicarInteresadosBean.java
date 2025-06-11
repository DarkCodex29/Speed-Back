package com.hochschild.speed.back.model.bean.comunicarInteresados;

import lombok.Data;

public @Data class ComunicarInteresadosBean {

    private Integer idExpediente;
    private Integer[] idInteresados;
    private Integer[] esGrupo;
}
