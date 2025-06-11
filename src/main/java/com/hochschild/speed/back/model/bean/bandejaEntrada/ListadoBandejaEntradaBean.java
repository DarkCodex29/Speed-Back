package com.hochschild.speed.back.model.bean.bandejaEntrada;

import lombok.Data;
public @Data class ListadoBandejaEntradaBean {

    private String numero;
    private String compania;
    private String contraparte;
    private String sumilla;
    private String estado;
    private String fechaSolicitud;
    private String fechaPrimerBorrador;
    private String proceso;
}