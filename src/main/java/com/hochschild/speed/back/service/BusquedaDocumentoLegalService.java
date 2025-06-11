package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.buscarDocumento.FilaBusquedaDocDashboardBean;
import com.hochschild.speed.back.model.bean.buscarDocumento.FilaBusquedaDocumentoBean;
import com.hochschild.speed.back.model.bean.seguimientoSolicitudes.ListadoSolicitudesBean;
import com.hochschild.speed.back.model.domain.speed.utils.FilaSeguimientoSolicitudDTS;
import com.hochschild.speed.back.model.filter.BuscarDocumentoLegalFilter;
import com.hochschild.speed.back.model.filter.SeguimientoSolicitudFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import org.springframework.http.ResponseEntity;
import org.apache.poi.ss.usermodel.Workbook;
import java.util.List;

public interface BusquedaDocumentoLegalService {
    ResponseEntity<List<FilaBusquedaDocumentoBean>> buscarDocumentosLegales(BuscarDocumentoLegalFilter buscarDocumentoLegalFilter, Boolean datosAdicionales);

    List<FilaSeguimientoSolicitudDTS> buscarSeguimientoSolicitud(SeguimientoSolicitudFilter filter);

    String obtenerFechaInicio(Integer idDocumentoLegal);

    String obtenerFechaVencimiento(Integer idDocumentoLegal);

    ResponseModel guardarSolicitudesPorDocumentoLegal(ListadoSolicitudesBean listadoSolicitudesBean);

    Workbook exportarBusquedaDL(BuscarDocumentoLegalFilter buscarDocumentoLegalFilter);

    ResponseEntity<List<FilaBusquedaDocDashboardBean>> buscarDocumentosLegalesDashboard(BuscarDocumentoLegalFilter buscarDocumentoLegalFilter);
}
