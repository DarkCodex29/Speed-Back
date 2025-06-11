package com.hochschild.speed.back.model.bean.buscarDocumento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public @Data class FilaBusquedaDocumentoBean {

    private Integer idDocumentoLegal;
    private Integer idExpediente;
    private Integer idContratoAdenda;
    private Integer idTipoProceso;
    private String numeroDocumento;
    private String tipoSolicitud;
    private String tipoContrato;
    private String sumilla;
    private String contraparte;
    private String ubicacion;

    private String compania;
    private String area;
    private String pais;
    private String solicitante;
    private String responsable;
    private String moneda;
    private Float monto;
    private String fechaInicio;
    private String fechaVencimiento;

    private String estado;
    private List<FilaBusquedaDocumentoBean> adendas;

    public FilaBusquedaDocumentoBean() {
        this.adendas = new ArrayList<>();

    }
}
