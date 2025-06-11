package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.registrarSolicitud.DocumentoLegalBean;
import com.hochschild.speed.back.model.domain.speed.HcDocumentoLegal;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.filter.bandejaEntrada.ContratoBean;
import com.hochschild.speed.back.model.bean.bandejaEntrada.DatosContratoBean;
import com.hochschild.speed.back.model.response.ResponseModel;

public interface DatosContratoService {
    DatosContratoBean botonDatosContrato(ContratoBean contratoBean, Integer idUsuario);
    ResponseModel guardarHcDocumentoLegal(DocumentoLegalBean documentoLegalBean, Integer idUsuario);
    HcDocumentoLegal guardarHcDocumentoLegal(DocumentoLegalBean documentoLegalBean, Usuario usuario);
}
