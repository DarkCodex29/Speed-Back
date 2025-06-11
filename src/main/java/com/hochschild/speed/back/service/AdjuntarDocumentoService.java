package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.adjuntarDocumento.AdjuntarDocumentoModalBean;
import com.hochschild.speed.back.model.bean.adjuntarDocumento.NumeracionPorTipoBean;
import com.hochschild.speed.back.model.bean.registrarSolicitud.AdjuntarDocumentoBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.response.ResponseModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface AdjuntarDocumentoService {

    Integer guardarDocumento(Documento documento, Map<String, String> map);

    Integer guardarNuevoDocumento(Documento documento, Map<String, String> map);

    Boolean borrarArchivosDocumento(Documento documento);

    ResponseModel guardarDocumentoGeneral(AdjuntarDocumentoBean adjuntarDocumentoBean, Integer idUsuario, HttpServletRequest request);

    AdjuntarDocumentoModalBean adjuntarDocumentoModal();

    List<TipoDocumento> getTipos();

    List<Boton> obtenerBotonesPorGrid(Perfil perfil, String codigoGrid);

    NumeracionPorTipoBean informacionNumeracion(Integer idUsuario, Integer idTipoDocumento);

    ResponseModel guardarNuevoDocumento(AdjuntarDocumentoBean adjuntarDocumentoBean, Integer idUsuario, HttpSession sesion, HttpServletRequest request);

    ResponseModel eliminarArchivoPorId(Integer[] idArchivos, Integer idUsuario);
}