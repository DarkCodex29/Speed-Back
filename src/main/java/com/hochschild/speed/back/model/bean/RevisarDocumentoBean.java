package com.hochschild.speed.back.model.bean;

import com.hochschild.speed.back.model.domain.speed.Archivo;
import com.hochschild.speed.back.model.domain.speed.Boton;
import com.hochschild.speed.back.model.domain.speed.CampoPorDocumento;
import lombok.Data;

import java.util.List;

public @Data class RevisarDocumentoBean {
    private List<Archivo> lstArchivos;
    private List<Boton> ltsBotonesDoc;
    private List<CampoPorDocumento> campos;
    private String tamanioArchivo;
    private double tamanioArchivoAdjunto;
}
