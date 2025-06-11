package com.hochschild.speed.back.model.response;

import lombok.Data;

@Data
public class CancelarVisadoResponse {
    private String tipo;
    private String numero;
    private Integer idExpediente;
}
