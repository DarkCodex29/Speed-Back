package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.filter.reportefilter.AlarmaContratoFilter;
import com.hochschild.speed.back.model.filter.reportefilter.DocumentoAreaFilter;
import com.hochschild.speed.back.model.filter.reportefilter.ReportesR4Filter;
import com.hochschild.speed.back.model.filter.reportefilter.SeguimientoProcesosFilter;
import com.hochschild.speed.back.model.reporte.*;

import java.util.List;

public interface ReportService {

    List<DocumentoPorAreaBean> filtrarDocumentosPorArea(DocumentoAreaFilter documentoAreaFilter);

    List<DocumentoLegalReporteBean> filtrarDocumentosPorAbogadoResponsable(Integer idResponsable);

    List<DocumentoLegalAlarmaContratoBean> buscarAlarmasContrato(AlarmaContratoFilter filter);

    List<SeguimientoProcesosReporteBean> filtrarSeguimientoProcesos(SeguimientoProcesosFilter filter);

    List<DatosR4> filtrarReporte4(ReportesR4Filter filter);
}
