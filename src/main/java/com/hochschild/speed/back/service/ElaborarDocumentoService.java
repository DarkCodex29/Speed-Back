package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.elaborarDocumento.DocumentoInfo;
import com.hochschild.speed.back.model.bean.elaborarDocumento.UsuarioDestinatarioBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.response.ResponseModelFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ElaborarDocumentoService {

    ResponseModelFile subirArchivo(MultipartFile archivoSubir, Integer idUsuario, String rename);

    Map<String, String> subirArchivo(File archivoSubir, Usuario usuario);

    Map<String, Object> guardarBorrador(Documento documento, String archivo, Integer[] idDestinatarios, Usuario usuario, Boolean enviadoC);

    List<HcDocumentoLegal> buscarDocumentosLegales(Map<String, Object> filtros);

    List<HcDocumentoLegal> buscarDocumentosLegalesPorAbogadoResponsable(Integer idAbogadoResponsable);

    List<HcDocumentoLegal> buscarAlarmasDeContratos(Integer idAbogadoResponsable, Integer idCompania, String numeroContrato, Date fechaInicio, Date fechaFin);

    List<HcDocumentoLegal> buscarSeguimientoProcesos(Integer[] idSolicitante, Integer idCompania, Character estado, String idUbicacion);

    List<Usuario> buscarSolicitanteDocumentoLegal();

    List<HcTipoUbicacion> listaTiposUbicacion();

    List<HcUbicacion> listarUbicacionPorTipo(Integer idtipo);

    List<Integer> listaAnios();

    List<HcDocumentoLegal> buscarSeguimientoProcesosAll(Integer[] idSolicitanteSP, Integer idCompaniaSP,
                                                        String[] idEstadoSP, String idUbicacionSP);

    DocumentoInfo buscarDestinatariosDefecto(Integer id);

    List<UsuarioDestinatarioBean> buscarUsuariosUbicacion(List<Usuario> usuarios);

}
