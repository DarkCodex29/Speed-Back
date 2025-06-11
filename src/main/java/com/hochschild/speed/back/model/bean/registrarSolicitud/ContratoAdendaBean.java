package com.hochschild.speed.back.model.bean.registrarSolicitud;

import com.hochschild.speed.back.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public @Data class ContratoAdendaBean {

    private Integer idDocumentoLegal;
    private String correoContacto;
    private String domicilioContacto;
    private String nombreContacto;
    private String telefonoContacto;
    private Character estado;
    private Date fechaBorrador;
    private String fechaBorradorFt;
    private String numero;
    private String sumilla;
    private Integer idContraparte;
    private Integer idExpediente;
    private Integer idArea;
    private Integer idResponsable;
    private Integer idSolicitante;
    private Date fechaSolicitud;
    private String fechaSolicitudFt;
    private Date fechaMovimiento;
    private String fechaMovimientoFt;
    private Integer idUbicacionLegal;
    private Integer confidencial;
}