package com.hochschild.speed.back.model.domain.speed.utils;

import lombok.Data;

@Data
public class ElementoListaVisadoDTO {
    private Integer idDocumentoLegal;
    private String numeroDocumentoLegal;
    private String compania;
    private String tipoCliente;
    private String clienteNombre;
    private String clienteRazonSocial;
    private String sumilla;
    private String tipoContrato;
}
