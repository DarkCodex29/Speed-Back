package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.RevisarDocumentoBean;
import com.hochschild.speed.back.model.domain.speed.Archivo;
import com.hochschild.speed.back.model.domain.speed.Boton;
import com.hochschild.speed.back.model.domain.speed.Documento;
import com.hochschild.speed.back.model.domain.speed.Perfil;
import com.hochschild.speed.back.model.domain.speed.utils.EstadoDocumentoDTO;
import com.hochschild.speed.back.util.Constantes;

import java.util.List;

public interface RevisarDocumentoService {
    Archivo obtenerArchivo(Integer idArchivo);

    RevisarDocumentoBean obtenerDocumento(Integer idUsuario, Integer idDocumento, Boolean soloLectura, Integer idPerfil);

    List<EstadoDocumentoDTO> listarEstadosDocumento(String codigoDocumento);
    public List<Boton> obtenerBotones(Perfil perfil, Character paraEstado, Boolean responsable);
    List<Boton> obtenerBotonAdendaAutomaticaPosicionContractual(Perfil perfilSesion, Character paraEstado, Boolean responsable, String parametro, Integer idRecurso);

    List<Boton> obtenerBotonesDocumentoLegalExceptoParametro(Perfil perfilSesion, Character paraEstado, Boolean responsable, String parametro);
    List<Boton> obtenerBotonesDocumentoLegalExceptoParametroDocumento(Perfil perfilSesion, Character paraEstado, Boolean responsable, String parametro);

    List<Archivo> obtenerArchivosxDocumento(Documento documento);
}
