package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface FirmaElectronicaService {

    Map<String, Object> createContrato(Integer idExpediente, Usuario usuario, String language, Integer idRepresentante, String tipoFirma, boolean registrarMovimientoTransaccion);

    Map<String, Object> createContrato(Integer idExpediente, Integer idUsuario, String language, Integer idRepresentante, String tipoFirma, boolean registrarMovimientoTransaccion);

    Map<String, Object> resendContrato(Integer idExpediente, Integer idUsuario, String language, Integer idRepresentante, String tipoFirma);

    Map<String, Object> getContratoDetail(Integer idExpediente);

    Map<String, Object> reenviarNotificacion(Integer idExpediente, HttpServletRequest request);

    Map<String, Object> deleteFirmaElectronica(
            Expediente expediente,
            HcDocumentoLegal documentoLegal,
            HcFirmaElectronicaDocLegal hcFirmaElectronicaDocLegal,
            Usuario usuario,
            boolean actualizarEstadoHcDocumentoLegal,
            boolean registrarMovimientoTransaccion);

    Map<String, Object> deleteFirmaElectronica(Integer idExpediente, Integer idUsuario, boolean actualizarEstadoHcDocumentoLegal, boolean registrarMovimientoTransaccion);

    List<Usuario> getListaRepresentantes(String estado);

    List<Parametro> getParametrosPorTipo(String parametro);
}
