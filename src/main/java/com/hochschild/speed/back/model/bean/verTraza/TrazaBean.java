package com.hochschild.speed.back.model.bean.verTraza;

import lombok.Data;

public @Data class TrazaBean {
    private String accionSeleccionada;
    private String actividad;
    private String destinatarios;
    private String fechaRecepcion;
    private String fechaLimite;
    private String observaciones;
    private String proceso;
    private String remitente;
    private String accion;
    private Integer id;
    private boolean multiple;
}
