package com.hochschild.speed.back.model.bean.bandejaEntrada;

import com.hochschild.speed.back.model.domain.speed.*;
import lombok.Data;
import java.util.Date;
import java.util.List;

public @Data class DetalleExpedienteBean {

    private Integer idTraza;
    private Usuario usuario;
    private Integer idResponsable;
    private String nombreResponsable;
    private List<List<CampoPorDocumento>> campos;
    private Date fechaLimite;
    private Expediente expediente;
    private String observacion;
    private HcDocumentoLegal documentoLegal;
    private HcContrato contrato;
    private HcAdenda adenda;
    private List<Documento> documentos;
    private List<Proceso> procesos;
    private Traza traza;
    private String obsTraza;
    private String url;
    private String texto;
    private Boolean flag;
    private EnvioPendiente envioPendiente;
    private Boolean mostrarParaAprobar;
    private List<Boton> botones;
    private String estadoDl;
    private Boolean editar;
    private Boolean internas;
}
