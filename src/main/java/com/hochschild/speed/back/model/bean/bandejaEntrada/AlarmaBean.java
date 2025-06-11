package com.hochschild.speed.back.model.bean.bandejaEntrada;

import lombok.Data;
import java.util.Date;

public @Data class AlarmaBean {
    private Integer id;
    private Integer idDocumentoLegal;
    private String nombreGrupo;
    private Integer[] idVisadores;
    private Integer[] esGrupo;
    private String fechaAlarma;
    private Boolean anual;
    private Integer activacion;
    private Integer intervalo;
    private String titulo;
    private String mensaje;
    private Character estado;
    private NombresIdBean[] namesIdVisadores;
    private NombresIdBean[] namesIdGrupo;
}