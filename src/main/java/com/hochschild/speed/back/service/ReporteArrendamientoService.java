package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.reporte.ArrendamientoReporteBean;

import java.util.Date;
import java.util.List;

public interface ReporteArrendamientoService {

    List<ArrendamientoReporteBean> buscarDocumentosLegales(String numero, Date fechaFirmaInicio, Date fechaFirmaFin, Date fechaVencimientoInicio, Date fechaVencimientoFin, Integer idPais, Integer idCompania, Integer idArea, String tipoUbicacion, Integer idUbicacion, Integer idContraparte, Double montoDesde, Double montoHasta, Integer idTipoContrato, Character estado, List<String> valoresORG, boolean datosAdicionales, Integer idUser);
}
