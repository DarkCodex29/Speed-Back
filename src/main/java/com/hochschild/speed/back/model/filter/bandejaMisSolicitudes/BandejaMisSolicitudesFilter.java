package com.hochschild.speed.back.model.filter.bandejaMisSolicitudes;

import lombok.Data;

public @Data class BandejaMisSolicitudesFilter {

    private String estado;
    private String usuario;
    private String numero;
    private String compania;
    private String contraparte;
    private String sumilla;
    private String proceso;
}