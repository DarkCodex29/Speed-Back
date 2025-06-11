package com.hochschild.speed.back.dao;

public interface HcUbicacionPorDocumentoDao {
    boolean documentoContieneUbicacion(Integer idDocumentoLegal, String codigoUbicacion);

    public String obtenerUbicacionesReporte(Integer idDocumentoLegal);

}
