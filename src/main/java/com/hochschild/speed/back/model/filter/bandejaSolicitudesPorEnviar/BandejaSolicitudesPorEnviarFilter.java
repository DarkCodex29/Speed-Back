package com.hochschild.speed.back.model.filter.bandejaSolicitudesPorEnviar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class BandejaSolicitudesPorEnviarFilter {

    private String usuario;
    private String numero;
    private String compania;
    private String contraparte;
    private String proceso;
}