package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.registrarContratoManual.DocumentoLegalManualBean;
import com.hochschild.speed.back.model.bean.registrarSolicitud.PenalidadesBean;
import com.hochschild.speed.back.model.domain.speed.Cliente;
import com.hochschild.speed.back.model.domain.speed.HcTipoContratoConfiguracion;
import com.hochschild.speed.back.model.domain.speed.utils.DocumentoLegalXAdendaDTS;
import com.hochschild.speed.back.model.response.ResponseModel;

import java.util.List;

public interface RegistrarSolicitudService {
    List<Cliente> obtenerRepresentantesDocumentoLegal(Integer idDocumentoLegal);

    DocumentoLegalXAdendaDTS datosContratoParaAdenda(Integer idDocumentoLegal);

    List<HcTipoContratoConfiguracion> buscarTipoAdenda(Character codigo, String estado);

    ResponseModel registrarHcDocumentoLegalManual(DocumentoLegalManualBean documentoLegalManualBean, Integer idUsuario);
}
