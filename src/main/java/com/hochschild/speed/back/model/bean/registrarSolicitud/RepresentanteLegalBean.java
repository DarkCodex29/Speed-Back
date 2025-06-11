package com.hochschild.speed.back.model.bean.registrarSolicitud;

import lombok.Data;

public @Data class RepresentanteLegalBean {

    private Integer idRepresentanteLegal;
    private Integer tipoCliente;
    private String numeroIdentificacion;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correo;
    private Boolean esRepresentante;
    private Boolean esContraparte;
    private Boolean eliminado;

    public void trimAttributes() {
        this.numeroIdentificacion = this.numeroIdentificacion != null ? this.numeroIdentificacion.trim() : null;
        this.nombre = this.nombre != null ? this.nombre.trim() : null;
        this.apellidoPaterno = this.apellidoPaterno != null ? this.apellidoPaterno.trim() : null;
        this.apellidoMaterno = this.apellidoMaterno != null ? this.apellidoMaterno.trim() : null;
        this.correo = this.correo != null ? this.correo.trim() : null;
    }
}