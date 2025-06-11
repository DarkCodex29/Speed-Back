package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.registrarSolicitud.*;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.response.ResponseModel;

import java.util.List;

public interface RegistroSolicitudService {

    List<Usuario> buscarUsuarioSolicitante(String codigoRol);

    List<Usuario> buscarUsuarioResponsable(String codigoRol);

    List<Cliente> buscarClientesContraparte(Character estado);

    List<Cliente> buscarClientesRepresentanteLegal(Character estado);

    List<HcArea> listarAreasPorCompania(Integer idCompania, Character estado);

    List<HcUbicacion> obtenerUbicacionOperacionPorPorCompania(String operacion, Integer idCompania, Character estado);

    List<HcUbicacion> obtenerUbicacionOficinaPorPorCompania(String operacion, Integer idCompania, Character estado);

    List<HcUbicacion> obtenerUbicacionProyectoPorPorCompania(String operacion, Integer idCompania, Character estado);

    List<HcUbicacion> obtenerUbicacionExploracionPorPorCompania(String operacion, Integer idCompania, Character estado);

    List<Cliente> obtenerClientesRepresentantesContraparte(Integer idContraparte);

    List<ContratoSumillaBean> autocompletarContrato(AutoContratoBean autoContratoBean);

    ResponseModel guardarHcDocumentoLegal(DocumentoLegalBean documentoLegalBean, Integer idUsuario);

    ResponseModel guardarPenalidadesPorDocumentoLegal(PenalidadesBean penalidadesBean);

    ResponseModel registrarExpediente(ExpedienteBean expedienteBean, Integer idUsuario);

    HcDocumentoLegal guardarHcDocumentoLegal(DocumentoLegalBean documentoLegalBean, Usuario usuario);

    void borrarRepresentantesDocumentoLegal(Integer idDocumentoLegal);

    void borrarUbicacionesDocumentoLegal(Integer idDocumentoLegal);

    void borrarRepresentantesContraparte(Integer idContraparte);

    void borrarDocumentosDocumentoLegal(Integer idExpediente);

    void borrarAdenda(Integer idAdenda);

    void borrarDocumentoLegal(Integer idExpediente);

    List<PenalidadBean> obtenerPenalidadesPorExpediente(Integer idExpediente);

    ContratoSumillaBean getContratoSumillaById(Integer idHcContrato);
}