package com.hochschild.speed.back.model.bean;

import lombok.Data;

public @Data class EnviarVisadoBean {
    Integer idExpediente;
    String observacion;
    Integer[] idVisadores;
}
