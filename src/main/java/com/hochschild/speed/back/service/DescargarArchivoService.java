package com.hochschild.speed.back.service;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface DescargarArchivoService {
    void descargarArchivo(Integer idArchivo, String version, HttpServletResponse httpServletResponse);

    void descargarPlantillaByRuta(Map<String, String> archivo, HttpServletResponse response);
}
