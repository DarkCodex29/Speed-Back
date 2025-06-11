package com.hochschild.speed.back.model.bean;

import com.hochschild.speed.back.model.domain.speed.Alerta;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
public @Data class BandejaBean {

    private Integer idtraza;
    private String numero;
    private String nombreCompania;
    private String razonSocial;
    private String sumilla;
    private String estado;
    private String fechaSolicitud;
    private String fechaBorrador;
    private String nombreProceso;
    private Character usuarioTrazaEstado;
    private Boolean leido;
    private String fechaMovimiento;
    private String ubicacionLegal;
    private Alerta alerta;
}