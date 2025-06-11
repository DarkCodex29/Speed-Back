package com.hochschild.speed.back.model.reporte;

import lombok.Data;

import java.util.Date;

public @Data class ExpedientePorAreaBean {
    private Integer idExpediente;
    private String numero;
    private String titulo;
    private String usuario;
    private Date fechaCreacion;
    private String cliente;
    private String razonSocial;
    private String proceso;
    private Character estado;
    private String areaCreadora;
    private String sedeOrigen;
    private String areaDestino;
    private String sedeDestino;
    private Date fechaUltimaTraza;

    public ExpedientePorAreaBean(Integer idExpediente, String numero, String titulo, String usuario, Date fechaCreacion, String cliente, String razonSocial, String proceso, Character estado, String areaCreadora, String sedeOrigen, String areaDestino, String sedeDestino, Date fechaUltimaTraza) {
        this.idExpediente = idExpediente;
        this.numero = numero;
        this.titulo = titulo;
        this.usuario = usuario;
        this.fechaCreacion = fechaCreacion;
        this.cliente = cliente;
        this.razonSocial = razonSocial;
        this.proceso = proceso;
        this.estado = estado;
        this.areaCreadora = areaCreadora;
        this.sedeOrigen = sedeOrigen;
        this.areaDestino = areaDestino;
        this.sedeDestino = sedeDestino;
        this.fechaUltimaTraza = fechaUltimaTraza;
    }
}
