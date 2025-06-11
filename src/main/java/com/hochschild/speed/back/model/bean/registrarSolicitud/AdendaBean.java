package com.hochschild.speed.back.model.bean.registrarSolicitud;

import lombok.Data;
import java.util.List;

public @Data class AdendaBean {

    //Elaboración de Documento
    private Integer idArea;
    private Integer tipoDocumento;
    private Integer idContrato;

    //Ubicacion
    private Integer idOperacion;
    private Integer idOficina;
    private Integer idProyecto;
    private String exploracion;
    private List<UbicacionesBean> ubicaciones;

    private String fechaInicio;
    private String fechaFin;
    private Boolean aplicaFechaFin;
    private Boolean esIndefinido;
    private String propositoObservaciones;

    //Contraparte
    private Integer idContraparte;
    private String domicilioContraparte;
    private String nombreContraparte;
    private String telefonoContraparte;
    private String emailContraparte;
    private List<RepresentanteLegalBean> representantesLegales;
}