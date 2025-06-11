package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.util.Constantes;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CommonBusinessLogicService {
    Map<String, Object> generarTraza(Integer idExpediente, Usuario remitente, Constantes.Destinatario destinatario, String observacion, String accion);

    Map<String, Object> generarTraza(Integer idExpediente, Usuario remitente, Usuario userDestinatario, String observacion, String accion);

    Expediente obtenerExpediente(Integer idExpediente);

    Cliente obtenerDatosSunat(Integer idCliente);

    HcDocumentoLegal obtenerHcDocumentoLegalPorExpediente(Integer idExpediente);

    HcDocumentoLegal obtenerHcDocumentoLegal(Integer idDocumentoLegal);

    Documento obtenerDocumento(Integer idDocumento);

    void crearAlarma(HcDocumentoLegal documentoLegal, Date fechaAlarma);

    void desactivarAlarmas(HcDocumentoLegal documentoLegal);

    HcDocumentoLegal buscarContratoPorAdenda(Integer idAdenda);

    List<Usuario> buscarUsuarios(String term);

    List<Usuario> buscarUsuariosActivos();
}