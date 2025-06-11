package com.hochschild.speed.back.model.bean.bandejaEntrada;

import com.hochschild.speed.back.model.domain.speed.*;
import lombok.Data;
import java.util.List;

public @Data class BandejaAlarmaBean {

    private Integer idDocumentoLegal;
    private Usuario usuario;
    private List<AlarmaBean> alarmas;
}