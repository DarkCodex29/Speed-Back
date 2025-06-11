package com.hochschild.speed.back.model.filter.bandejaEntrada;

import lombok.Data;
import lombok.ToString;

@ToString

public @Data class BandejaEntradaFilter {

    private String numero;
    private String compania;
    private String contraparte;
    private String sumilla;
    private String proceso;
    private String estado;
    private String usuario;
    private String fecha;
    private String pais;
}