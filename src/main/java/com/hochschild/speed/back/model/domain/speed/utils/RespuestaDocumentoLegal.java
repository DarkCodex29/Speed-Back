package com.hochschild.speed.back.model.domain.speed.utils;

import java.util.List;

public class RespuestaDocumentoLegal {
    private String mensaje;
    private DocumentoLegalDTO documentoLegal;
    private List<ArchivoDTO> archivos;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public DocumentoLegalDTO getDocumentoLegal() {
        return documentoLegal;
    }

    public void setDocumentoLegal(DocumentoLegalDTO documentoLegal) {
        this.documentoLegal = documentoLegal;
    }

    public List<ArchivoDTO> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<ArchivoDTO> archivos) {
        this.archivos = archivos;
    }
}