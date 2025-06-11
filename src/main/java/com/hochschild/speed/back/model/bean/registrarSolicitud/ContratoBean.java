package com.hochschild.speed.back.model.bean.registrarSolicitud;

import lombok.Data;
import java.util.Date;
import java.util.List;

public @Data class ContratoBean {

    //Contraparte
    private Integer idContraparte;
    private String situacionSunatContraparte;
    private String domicilioContraparte;
    private String nombreContraparte;
    private String telefonoContraparte;
    private String emailContraparte;
    private List<RepresentanteLegalBean> representantesLegales;

    //Elaboración de Documento
    private Integer idPais;
    private Integer idCompania;
    private Integer idArea;

    //Ubicacion
    private Integer idOperacion;
    private Integer idOficina;
    private Integer idProyecto;
    private String exploracion;
    private List<UbicacionesBean> ubicaciones;

    private String propositoObservaciones;
    private String fechaInicio;
    private String fechaFin;
    private Integer idMoneda;

    //ModalidadPago
    private Boolean esPrecioFijo;
    private Boolean esPrecioUnitario;
    private Boolean esIndefinido;
    private Boolean aplicaModalidadPago;
    private Boolean aplicaAdelanto;
    private Boolean aplicaRenovacionAutomatica;
    private Boolean aplicaPeriodicidad;
    private Boolean aplicaArrendamiento;
    private Float montoFijoTotalEstimado;
    private Float montoAdelanto;
    private String periodicidadPago;
    private Integer periodoRenovar;
}