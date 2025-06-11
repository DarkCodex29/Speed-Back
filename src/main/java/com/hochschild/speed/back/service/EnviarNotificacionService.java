package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.domain.speed.utils.NotificacionContraparte;
import com.hochschild.speed.back.model.domain.speed.utils.NotificacionExterna;

import java.util.List;
import java.util.Map;

public interface EnviarNotificacionService {

    Integer registrarNotificacion(List<HcFirmaElectronicaRepresentante> usersDestinatarios, TipoNotificacion tipoNotificacion, Traza traza, Object[] arguments, List<Map<String, String>> imagenesInline);

    Integer registrarNotificacion(List<Usuario> usersDestinatarios, TipoNotificacion tipoNotificacion, Traza traza);

    Integer registrarNotificacion(List<String> usersDestinatarios, String codigoTipoNotificacion, Integer idExpediente);

    Integer registrarNotificacionCopia(Usuario usersDestinatarios, TipoNotificacion tipoNotificacion, TrazaCopia trazaCopia);

    Integer registrarNotificacion(Usuario destinatario, String tipoNotificacion);

    Integer registrarNotificacion(Usuario destinatario, TipoNotificacion tipoNotificacion);

    Integer registrarNotificacion(Usuario destinatario, TipoNotificacion tipoNotificacion, Traza traza);

    Integer registrarNotificacionAlarma(List<Usuario> usersDestinatarios, TipoNotificacion tipoNotificacion, HcDocumentoLegal documentoLegal, String mensajeAlarma);

    Integer registrarNotificacionPregunta(List<Usuario> usersDestinatarios, Usuario usuario, String pregunta);

    Integer enviarNotificacionExterna(NotificacionExterna notificacionExterna);
    
    Integer enviarNotificacionContraparte(NotificacionContraparte notificacionContraparte);
}