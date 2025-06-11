package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.model.response.ResponseModelFile;
import com.hochschild.speed.back.util.exception.ExcepcionAlfresco;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RegistrarExpedienteService {

    Integer registrarExpedienteSolicitudHC(Usuario usuario, Integer idExpediente, Integer idResponsable, Boolean notificar) throws RuntimeException;

    ResponseModelFile subirArchivo(MultipartFile archivoSubir, Integer idUsuario);

    Expediente obtenerExpedienteNuevo(Integer idExpedienteRegistrado);

    Expediente obtenerExpediente(Integer idExpediente);

    void subirArchivosAlfresco(Expediente expedienteObj, Usuario usuario) throws ExcepcionAlfresco;

    List<Proceso> getProcesos();

    ResponseModel eliminarArchivo(String file);
}