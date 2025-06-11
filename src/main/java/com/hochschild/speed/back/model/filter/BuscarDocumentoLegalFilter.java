package com.hochschild.speed.back.model.filter;

import lombok.Data;
import java.util.Date;
import java.util.List;

public @Data class BuscarDocumentoLegalFilter {

    private String numero;
    private String sumilla;
    private Date fechaFirmaInicio;
    private Date fechaFirmaFin;
    private Date fechaVencimientoInicio;
    private Date fechaVencimientoFin;
    private Integer idPais;
    private Integer idCompania;
    private Integer idArea;
    private String tipoUbicacion;
    private Integer idUbicacion;
    private String textoArchivo;
    private Integer idSolicitante;
    private Integer idResponsable;
    private Integer idContraparte;
    private Integer idRepresentante;
    private Double montoDesde;
    private Double montoHasta;
    private Integer idTipoContrato;
    private Character estado;
    private Integer tipoSolicitud;

    private String usuario;
    private Integer pagina;
    private Integer numRegistrosPagina;
    private List<String> valoresORG;
    private Integer idUsuario;
    private Boolean flagPracticante;
}