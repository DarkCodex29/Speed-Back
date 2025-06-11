package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.Archivo;
import com.hochschild.speed.back.model.domain.speed.Documento;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.util.exception.ExcepcionAlfresco;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface AdjuntarArchivoService {

    Archivo subirArchivo(Documento documento, String nombreArchivoDisco, Integer fila, Usuario usuario, Map<String, String> map) throws ExcepcionAlfresco;

    Archivo subirArchivo(Documento documento, String nombreArchivoDisco, Integer fila, Integer idUsuario, Map<String, String> map) throws ExcepcionAlfresco;

    void versionarArchivo(List<Documento> documentos, Usuario usuario);

    @Transactional
    ResponseModel verificarExistenciaArchivo(Integer idDocumento, String nombreArchivo) throws Exception;

    @Transactional
    Archivo subirArchivoAlfrescoSinGuardar(int idDocumento, int idArchivo, Usuario usuario) throws ExcepcionAlfresco;

    @Transactional
    Archivo subirArchivoAlfrescoSinGuardar(int idDocumento, int idArchivo, Integer idUsuario) throws ExcepcionAlfresco;

    Documento obtenerDocumentoPorId(Integer idDocumento);

    @Transactional
    Map<String,Object> adjuntarArchivo(Integer idDocumento, String nombre, String usuario, InputStream inputStream);

    @Transactional
    int adjuntarArchivoFirmado(Integer idArchivo, String version, Integer idUsuario, String firmante, InputStream inputStream);

    String obtenerIdAlfresco(Integer idArchivo);
    
    @Transactional
    String obtenerUrlAlfresco(Integer idArchivo);
    
    String getDownloadUrl(Integer idArchivo);
    
    ResponseModel verificarExistenciaArchivoEnExpediente(Integer idExpediente, String nombreArchivo, Integer idUsuario);
}
