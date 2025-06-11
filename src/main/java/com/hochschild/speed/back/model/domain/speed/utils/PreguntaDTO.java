package com.hochschild.speed.back.model.domain.speed.utils;

import lombok.Data;

@Data
public class PreguntaDTO {
    private Integer idAplicacion;
    private Integer codigo;
    private Integer codigoArea;
    private Integer codigoTema;
    private String codigoPregunta;
    private String descripcionPregunta;
    private String codigoRepuesta;
    private String descripcionRespuesta;
    private String vigente;
}
