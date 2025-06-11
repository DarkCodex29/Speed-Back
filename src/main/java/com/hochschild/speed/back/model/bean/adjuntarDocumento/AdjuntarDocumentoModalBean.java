package com.hochschild.speed.back.model.bean.adjuntarDocumento;

import com.hochschild.speed.back.model.domain.speed.Boton;
import com.hochschild.speed.back.model.domain.speed.TipoDocumento;
import lombok.Data;

import java.util.List;

public @Data class AdjuntarDocumentoModalBean {
    private double tamanioArchivoAdjunto;
    private String tamanioArchivo;
    private List<TipoDocumento> tipos;
    private List<Boton> botones;
}
