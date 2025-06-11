package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.AlfrescoConfig;
import com.hochschild.speed.back.model.domain.speed.Archivo;
import com.hochschild.speed.back.service.AlfrescoService;
import com.hochschild.speed.back.service.DescargarArchivoService;
import com.hochschild.speed.back.service.RevisarDocumentoService;
import com.hochschild.speed.back.util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DescargarArchivoServiceImpl implements DescargarArchivoService {
    private static final Logger LOGGER = Logger.getLogger(DescargarArchivoServiceImpl.class);

    private final AlfrescoConfig alfrescoConfig;
    private final RevisarDocumentoService revisarDocumentoService;
    private final AlfrescoService alfrescoService;
    @Override
    public void descargarArchivo(Integer idArchivo, String version, final HttpServletResponse httpServletResponse){
        Archivo archivo = revisarDocumentoService.obtenerArchivo(idArchivo);

        if (archivo != null) {
            String contentType = null;
            InputStream in = null;
            if (alfrescoConfig.getHabilitado()) {
                Map<String, Object> file = alfrescoService.obtenerArchivo(archivo, version);
                if (file != null) {
                    contentType = (String) file.get("mime");
                    in = (InputStream) file.get("stream");
                }
            } else if (!AppUtil.checkNullOrEmpty(archivo.getRutaLocal())) {
                File file = new File(archivo.getRutaLocal());
                try {
                    contentType = Files.probeContentType(file.toPath());
                    in = new FileInputStream(file);
                } catch (IOException e) {
                    LOGGER.info("Error leyendo archivo local", e);
                }
            }
            if (contentType != null && in != null) {
                httpServletResponse.setHeader("Content-Disposition", "filename=\"" + archivo.getNombre() + "\"");
                LOGGER.info("Content-Type: [" + contentType + "]");
                httpServletResponse.setContentType(contentType);
                OutputStream out;
                try {
                    out = httpServletResponse.getOutputStream();
                    int read = 0;
                    byte[] bytes = new byte[1024];

                    while ((read = in.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }

                    in.close();
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    LOGGER.info("No se pudo descargar el archivo", e);
                    try {
                        httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                    } catch (IOException ioe) {
                        LOGGER.info("Error enviando respuesta", ioe);
                    }
                }
            } else {
                try {
                    httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                } catch (IOException e) {
                    LOGGER.info("Error enviando respuesta", e);
                }
            }
        }
    }
    @Override
    public void descargarPlantillaByRuta(Map<String, String> archivo, final HttpServletResponse response) {
        try {
            String ruta = archivo.get("ruta");
            String nombre = ruta.substring(ruta.lastIndexOf("/") + 1);
            String path = ruta.replace("/" + nombre, "");
            Map<String, Object> plantillaFromAlfresco = alfrescoService.descargarArchivo(path, nombre);
            String contentType = (String) plantillaFromAlfresco.get("mime");
            InputStream in = (InputStream) plantillaFromAlfresco.get("stream");
            if (contentType != null && in != null) {
                response.setHeader("Content-Disposition", "attachment; filename=" + nombre);
                LOGGER.info("Content-Type: [" + contentType + "]");
                response.setContentType("application/msword");
                try {
                    OutputStream out = response.getOutputStream();
                    int read = 0;
                    byte[] bytes = new byte[1024];

                    while ((read = in.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }

                    in.close();
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    LOGGER.info("No se pudo descargar el archivo", e);
                    try {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    } catch (IOException ioe) {
                        LOGGER.info("Error enviando respuesta", ioe);
                    }
                }
            } else {
                try {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                } catch (IOException e) {
                    LOGGER.info("Error enviando respuesta", e);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
