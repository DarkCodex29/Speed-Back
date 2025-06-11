package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.dao.HcDocumentoLegalDao;
import com.hochschild.speed.back.model.domain.speed.HcUbicacion;
import com.hochschild.speed.back.model.filter.reportefilter.AlarmaContratoFilter;
import com.hochschild.speed.back.model.filter.reportefilter.DocumentoAreaFilter;
import com.hochschild.speed.back.model.filter.reportefilter.ReportesR4Filter;
import com.hochschild.speed.back.model.filter.reportefilter.SeguimientoProcesosFilter;
import com.hochschild.speed.back.model.reporte.*;
import com.hochschild.speed.back.repository.speed.DocumentoRepository;
import com.hochschild.speed.back.repository.speed.HcDocumentoLegalRepository;
import com.hochschild.speed.back.service.ReportService;
import com.hochschild.speed.back.util.Constantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);
    private final DocumentoRepository documentoRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;

    private final HcDocumentoLegalDao hcDocumentoLegalDao;

    @Autowired
    public ReportServiceImpl(DocumentoRepository documentoRepository, HcDocumentoLegalRepository hcDocumentoLegalRepository, HcDocumentoLegalDao hcDocumentoLegalDao) {
        this.documentoRepository = documentoRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.hcDocumentoLegalDao = hcDocumentoLegalDao;
    }

    @Override
    public List<DocumentoPorAreaBean> filtrarDocumentosPorArea(DocumentoAreaFilter documentoAreaFilter) {
//        Pageable pageable = new PageRequest(documentoAreaFilter.getNumberPage(), 20);
        if (documentoAreaFilter.getIdAreaCreacion().equals(0)) {
            documentoAreaFilter.setIdAreaCreacion(null);
        }
        if (documentoAreaFilter.getIdAreaActual().equals(0)) {
            documentoAreaFilter.setIdAreaActual(null);
        }
        if (documentoAreaFilter.getIdTipoDocumento().equals(0)) {
            documentoAreaFilter.setIdTipoDocumento(null);
        }
        if (documentoAreaFilter.getEstado().equals('0')) {
            documentoAreaFilter.setEstado(null);
        }
       
        LOGGER.info(documentoAreaFilter.toString());
        List<DocumentoPorAreaBean> documentoPorAreaBeans = documentoRepository.filterDocumentoPorArea(documentoAreaFilter.getIdAreaCreacion(), documentoAreaFilter.getIdAreaActual(), documentoAreaFilter.getIdTipoDocumento(), documentoAreaFilter.getFechaInicio(), documentoAreaFilter.getFechaFin(), documentoAreaFilter.getEstado(), documentoAreaFilter.getNumeroExpediente());
        return documentoPorAreaBeans;
    }

    @Override
    public List<DocumentoLegalReporteBean> filtrarDocumentosPorAbogadoResponsable(Integer idResponsable) {
        List<DocumentoLegalReporteBean> contratosPorAbogadoResponsable = hcDocumentoLegalRepository.buscarContratosPorAbogadoResponsable(idResponsable);
        getUbications(contratosPorAbogadoResponsable);
        List<DocumentoLegalReporteBean> adendasPorAbogadoResponsable = hcDocumentoLegalRepository.buscarAdendasPorAbogadoResponsable(idResponsable);

        getUbications(adendasPorAbogadoResponsable);

        contratosPorAbogadoResponsable.addAll(adendasPorAbogadoResponsable);
        System.out.println("DIMENSION : " + contratosPorAbogadoResponsable.size());

        return contratosPorAbogadoResponsable;
    }

    @Override
    public List<DocumentoLegalAlarmaContratoBean> buscarAlarmasContrato(AlarmaContratoFilter filter) {
        if (filter.getIdContraparte().equals(0)) {
            filter.setIdContraparte(null);
        }
        if (filter.getIdCompania().equals(0)) {
            filter.setIdCompania(null);
        }
        if (filter.getIdArea().equals(0)) {
            filter.setIdArea(null);
        }
        if (filter.getNumeroContrato().isEmpty()) {
            filter.setNumeroContrato(null);
        }
        List<DocumentoLegalAlarmaContratoBean> contratos = hcDocumentoLegalRepository.buscarAlarmasContratos(filter.getAbogadoResponsableIdAC(),filter.getIdContraparte(), filter.getIdCompania(),filter.getIdArea(), filter.getNumeroContrato(), filter.getFecInicio(), filter.getFecFin());
        List<DocumentoLegalAlarmaContratoBean> adendas = hcDocumentoLegalRepository.buscarAlarmaAdendas(filter.getAbogadoResponsableIdAC(),filter.getIdContraparte(), filter.getIdCompania(),filter.getIdArea(), filter.getNumeroContrato(), filter.getFecInicio(), filter.getFecFin());
        contratos.addAll(adendas);
        return contratos;
    }

    @Override
    public List<SeguimientoProcesosReporteBean> filtrarSeguimientoProcesos(SeguimientoProcesosFilter filter) {

        return hcDocumentoLegalDao.buscarSeguimientoProcesosAll(filter.getIdSolicitanteSP(), filter.getIdCompaniaSP(), filter.getIdEstadoSP(), filter.getIdUbicacionSP());
    }

    @Override
    public List<DatosR4> filtrarReporte4(ReportesR4Filter filter) {
        List<DatosR4> reporte = hcDocumentoLegalRepository.filtrarReporteR4(filter.getAnio(), filter.getIdResponsable());
        for (DatosR4 datosR4 : reporte) {
            datosR4.setStrStatus(getNombreEstadosDL(datosR4.getStatus()));
        }
        return reporte;
    }

    private String getNombreEstadosDL(Character codigoEstado) {
        List<Map<String, Object>> estados = getEstadosDL();

        for (Map<String, Object> mapEstado : estados) {
            if (mapEstado.get("valor").equals(codigoEstado)) {
                return (String) mapEstado.get("nombre");
            }
        }
        return "";
    }

    private List<Map<String, Object>> getEstadosDL() {
        List<Map<String, Object>> estados = new ArrayList<>();

        Map<String, Object> e1 = new HashMap<String, Object>();
        e1.put("nombre", "Comunicacion a Interesados");
        e1.put("valor", Constantes.ESTADO_HC_COMUNICACION);
        estados.add(e1);

        Map<String, Object> e2 = new HashMap<String, Object>();
        e2.put("nombre", "Doc. Elaborado");
        e2.put("valor", Constantes.ESTADO_HC_ELABORADO);
        estados.add(e2);

        Map<String, Object> e3 = new HashMap<String, Object>();
        e3.put("nombre", "En Elaboracion");
        e3.put("valor", Constantes.ESTADO_HC_EN_ELABORACION);
        estados.add(e3);

        Map<String, Object> e4 = new HashMap<String, Object>();
        e4.put("nombre", "En Solicitud");
        e4.put("valor", Constantes.ESTADO_HC_EN_SOLICITUD);
        estados.add(e4);

        Map<String, Object> e5 = new HashMap<String, Object>();
        e5.put("nombre", "Enviado a Firma");
        e5.put("valor", Constantes.ESTADO_HC_ENVIADO_FIRMA);
        estados.add(e5);

        Map<String, Object> e6 = new HashMap<String, Object>();
        e6.put("nombre", "Enviado a Visado");
        e6.put("valor", Constantes.ESTADO_HC_ENVIADO_VISADO);
        estados.add(e6);

        Map<String, Object> e7 = new HashMap<String, Object>();
        e7.put("nombre", "Solicitud Enviada");
        e7.put("valor", Constantes.ESTADO_HC_SOLICITUD_ENVIADA);
        estados.add(e7);

        Map<String, Object> e8 = new HashMap<String, Object>();
        e8.put("nombre", "Vencido");
        e8.put("valor", Constantes.ESTADO_HC_VENCIDO);
        estados.add(e8);

        Map<String, Object> e9 = new HashMap<String, Object>();
        e9.put("nombre", "Vigente");
        e9.put("valor", Constantes.ESTADO_HC_VIGENTE);
        estados.add(e9);

        Map<String, Object> e10 = new HashMap<String, Object>();
        e10.put("nombre", "Visado");
        e10.put("valor", Constantes.ESTADO_HC_VISADO);
        estados.add(e10);

        return estados;
    }

    private void getUbications(List<DocumentoLegalReporteBean> contratosPorAbogadoResponsable) {
        for (DocumentoLegalReporteBean docReporte : contratosPorAbogadoResponsable) {
            List<HcUbicacion> ubicaciones = hcDocumentoLegalRepository.obtenerUbicacionesDocLegal(docReporte.getId());
            StringBuilder sb = new StringBuilder();
            for (HcUbicacion hcUbicacion : ubicaciones) {
                sb.append(hcUbicacion.getNombre()).append(" / ");
            }
            docReporte.setUbicaciones(sb.toString());
        }
    }
}
