package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.Archivo;
import com.hochschild.speed.back.model.domain.speed.Documento;
import com.hochschild.speed.back.util.exception.ExcepcionAlfresco;
import java.util.List;
import java.util.Map;

public interface AlfrescoService {
    String subirArchivo(String space, String fileLocalPathName) throws ExcepcionAlfresco;
    String subirArchivoPlantilla(String space, String fileLocalPathName, String name) throws ExcepcionAlfresco;

    /**
     * Obtiene el archivo fisico solicitado
     *
     * @param archivo
     * @return mapa con los siguientes elementos:<br><code>mime</code> - tipo de contenido<br><code>stream</code> - InputStream con los datos
     */
    Map<String, Object> obtenerArchivo(Archivo archivo, String version);

    /**
     * Sube todos los archivos que se encuentren en <code>documento.getArchivos()</code> a Alfresco. Se verifica <code>archivo.isNuevo()</code> para determinar si es un archivo nuevo o se sube una nueva version.
     *
     * @param documentos lista de los documentos cuyos archivos se desea subir
     */
    void subirArchivos(List<Documento> documentos) throws ExcepcionAlfresco;


    Map<String, Object> descargarArchivo(String space, String fileName) throws Exception;

    String obtenerIdAlfresco(Archivo archivo);
    
    String obtenerUrlAlfresco(Archivo archivo);

    boolean existeArchivo(Archivo archivo);

    void obtenerVersiones(Archivo archivo);

    String getTicket();

    String getDownloadUrl(Archivo archivo);

    List<String> buscarExpedientesPorContenido(String busquedaAlfresco);

}
