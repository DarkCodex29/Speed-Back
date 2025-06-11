package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;
import java.util.Date;

public @Data class ClienteBean {

    //Elaboración de Documento
    private Integer idCliente;
    private String apellidoMaterno;
    private String apellidoPaterno;
    private Character estado;
    private Date fechaCreacion;
    private String nombre;
    private String numeroIdentificacion;
    private String razonSocial;
    private String telefono;
    private Integer tipoCliente;
    private String correo;
    private Integer distancia;
    private Boolean esContraparte;
    private Boolean esRepresentante;
    private String situacionSunat;

    //Campo solicitado por HOC
    private String direccion;
    private String contacto;

}