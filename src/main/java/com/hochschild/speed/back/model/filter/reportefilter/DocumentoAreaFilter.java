package com.hochschild.speed.back.model.filter.reportefilter;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
@ToString
public @Data class DocumentoAreaFilter {
    private Integer idAreaCreacion;
    private Integer idAreaActual;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date fechaInicio;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date fechaFin;
    private Integer idTipoDocumento;
    private Character estado;
    private String numeroExpediente;
    private Integer numberPage;
}
