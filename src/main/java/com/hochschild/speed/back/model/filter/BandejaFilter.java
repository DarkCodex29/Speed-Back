package com.hochschild.speed.back.model.filter;

import lombok.Data;

public @Data class BandejaFilter {
    private String numero;
    private Integer pais;
    private Integer compania;
    private Integer contraparte;
    private String sumilla;
    private Character estado;
    private Integer proceso;
    private Integer pagina;
    private Integer numRegistrosPagina;
    private Integer usuario;
    private String fecha;
}
