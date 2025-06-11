package com.hochschild.speed.back.dao;

import com.hochschild.speed.back.model.bean.buscarDocumento.FilaBusquedaDocDashboardBean;
import com.hochschild.speed.back.model.domain.speed.HcDocumentoLegal;
import com.hochschild.speed.back.model.domain.speed.utils.FilaBusquedaDTS;
import com.hochschild.speed.back.model.domain.speed.utils.FilaSeguimientoSolicitudDTS;
import com.hochschild.speed.back.model.filter.BuscarDocumentoLegalFilter;
import com.hochschild.speed.back.model.reporte.ArrendamientoReporteBean;
import com.hochschild.speed.back.model.reporte.SeguimientoProcesosReporteBean;
import java.util.Date;
import java.util.List;

public interface HcDocumentoLegalDao {

    String normalizarTerminoBusqueda(String cadena);

    Integer obtenerNumeroAutomatico(Integer idCompania, Integer anio);
    Integer obtenerNumeroManual(Integer idCompania, Integer anio);
    Boolean solicitudActualizarVigencia(Integer idDocumento);

    List<FilaBusquedaDTS> buscarDocumentosLegales(
            BuscarDocumentoLegalFilter buscarDocumentoLegalFilter, Boolean datosAdicionales, List<String> numeroExpedientes);

    List<ArrendamientoReporteBean> buscarDocumentosLegalesArrendamiento(String numero, Date fechaFirmaInicio, Date fechaFirmaFin, Date fechaVencimientoInicio, Date fechaVencimientoFin,
                                                                        Integer idPais, Integer idCompania, Integer idArea, String tipoUbicacion, Integer idUbicacion,
                                                                        Integer idContraparte, Double montoDesde, Double montoHasta, Integer idTipoContrato,
                                                                        Character estado, List<String> numeroExpedientes, boolean datosAdicionales, boolean aplicaA);

    String obtenerFechaInicio(Integer idDocumentoLegal);

    String obtenerFechaVencimiento(Integer idDocumentoLegal);

    Integer normalizarIdBusqueda(Integer id);

    Double normalizarMonto(Double monto);

    List<FilaBusquedaDTS> filtrarPorVO(List<FilaBusquedaDTS> lista, List<String> valoresORG);

    Boolean validarPorVO(FilaBusquedaDTS datos, List<String> valoresORG);

    List<SeguimientoProcesosReporteBean> buscarSeguimientoProcesosAll(Integer[] idSolicitante, Integer idCompania,
                                                                      String[] estadoMultiple, String idUbicacion);

    List<FilaSeguimientoSolicitudDTS> buscarSeguimientoSolicitud(String numero,
                                                                 String sumilla,
                                                                 Integer idContraparte,
                                                                 Integer idSolicitante,
                                                                 Integer idResponsable,
                                                                 String estado);

    List<FilaBusquedaDTS> completarOrdenar(List<FilaBusquedaDTS> lista, Boolean integracionSCA);

    List<HcDocumentoLegal> obtenerVencimientoHoy();

    List<FilaBusquedaDocDashboardBean> buscarDocumentosLegalesDashboard(BuscarDocumentoLegalFilter buscarDocumentoLegalFilter);
    
    String obtenerAlerta(Integer idDocumentoLegal);
}