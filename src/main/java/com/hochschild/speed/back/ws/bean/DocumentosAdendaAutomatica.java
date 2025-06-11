package com.hochschild.speed.back.ws.bean;

import com.hochschild.speed.back.model.domain.speed.Documento;

public class DocumentosAdendaAutomatica {
    
    private Documento documentoOtro;
    private Documento documentoFirmado;

    /**
     * @return the documentoOtro
     */
    public Documento getDocumentoOtro() {
        return documentoOtro;
    }

    /**
     * @param documentoOtro the documentoOtro to set
     */
    public void setDocumentoOtro(Documento documentoOtro) {
        this.documentoOtro = documentoOtro;
    }

    /**
     * @return the documentoFirmado
     */
    public Documento getDocumentoFirmado() {
        return documentoFirmado;
    }

    /**
     * @param documentoFirmado the documentoFirmado to set
     */
    public void setDocumentoFirmado(Documento documentoFirmado) {
        this.documentoFirmado = documentoFirmado;
    }
}
