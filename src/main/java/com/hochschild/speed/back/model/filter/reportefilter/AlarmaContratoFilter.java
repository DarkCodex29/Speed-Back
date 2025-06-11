package com.hochschild.speed.back.model.filter.reportefilter;

import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
@ToString
public @Data class AlarmaContratoFilter {
    private Integer abogadoResponsableIdAC;
    private Integer idContraparte;
    private Integer idCompania;
    private Integer idArea;
    private String numeroContrato;
  //  @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date fecInicio;
  //  @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date fecFin;
}
