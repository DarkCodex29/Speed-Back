package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.CydocConfig;
import com.hochschild.speed.back.dao.HcDocumentoLegalDao;
import com.hochschild.speed.back.dao.HcUbicacionPorDocumentoDao;
import com.hochschild.speed.back.dao.OpcionSeguridadDao;
import com.hochschild.speed.back.model.bean.buscarDocumento.FilaBusquedaDocDashboardBean;
import com.hochschild.speed.back.model.bean.buscarDocumento.FilaBusquedaDocumentoBean;
import com.hochschild.speed.back.model.bean.seguimientoSolicitudes.ListadoSolicitudesBean;
import com.hochschild.speed.back.model.bean.seguimientoSolicitudes.SolicitudBean;
import com.hochschild.speed.back.model.domain.speed.HcDocumentoLegal;
import com.hochschild.speed.back.model.domain.speed.Parametro;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.domain.speed.utils.FilaBusquedaDTS;
import com.hochschild.speed.back.model.domain.speed.utils.FilaSeguimientoSolicitudDTS;
import com.hochschild.speed.back.model.filter.BuscarDocumentoLegalFilter;
import com.hochschild.speed.back.model.filter.SeguimientoSolicitudFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.HcDocumentoLegalRepository;
import com.hochschild.speed.back.repository.speed.ParametroRepository;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.service.AlfrescoService;
import com.hochschild.speed.back.service.BusquedaDocumentoLegalService;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.DateUtil;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@Service("BusquedaDocumentoLegalService")
public class BusquedaDocumentoLegalServiceImpl implements BusquedaDocumentoLegalService {
    private static final Logger LOGGER = Logger.getLogger(BusquedaDocumentoLegalServiceImpl.class.getName());
    private final AlfrescoService alfrescoService;
    private final HcDocumentoLegalDao hcDocumentoLegalDao;

    private final HcUbicacionPorDocumentoDao hcUbicacionPorDocumentoDao;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final ParametroRepository parametroRepository;
    private final CydocConfig cydocConfig;
    private final OpcionSeguridadDao opcionSeguridadDao;
    private final UsuarioRepository usuarioRepository;

    public BusquedaDocumentoLegalServiceImpl(
            AlfrescoService alfrescoService,
            HcDocumentoLegalDao hcDocumentoLegalDao,
            HcDocumentoLegalRepository hcDocumentoLegalRepository,
            ParametroRepository parametroRepository,
            CydocConfig cydocConfig,
            OpcionSeguridadDao opcionSeguridadDao,
            UsuarioRepository usuarioRepository, HcUbicacionPorDocumentoDao hcUbicacionPorDocumentoDao) {
        this.alfrescoService = alfrescoService;
        this.hcDocumentoLegalDao = hcDocumentoLegalDao;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.parametroRepository = parametroRepository;
        this.cydocConfig = cydocConfig;
        this.opcionSeguridadDao = opcionSeguridadDao;
        this.usuarioRepository = usuarioRepository;
        this.hcUbicacionPorDocumentoDao = hcUbicacionPorDocumentoDao;
    }

    @Override
    public ResponseEntity<List<FilaBusquedaDocumentoBean>> buscarDocumentosLegales(BuscarDocumentoLegalFilter buscarDocumentoLegalFilter, Boolean datosAdicionales) {

        List<FilaBusquedaDocumentoBean> result = null;

        try {
            result = obtenerDocumentos(buscarDocumentoLegalFilter, datosAdicionales);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<FilaSeguimientoSolicitudDTS> buscarSeguimientoSolicitud(SeguimientoSolicitudFilter filter) {

        Integer defaultValue = 0;
        String numero = filter.getNumero() != null ? filter.getNumero() : "";
        String sumilla = filter.getSumilla() != null ? filter.getSumilla() : "";
        Integer idContraparte = filter.getIdContraparte() != null ? filter.getIdContraparte() : defaultValue;
        Integer idResponsable = filter.getIdResponsable() != null ? filter.getIdResponsable() : defaultValue;
        Integer idSolicitante = filter.getIdSolicitante() != null ? filter.getIdSolicitante() : defaultValue;
        String estado = filter.getEstado() != null && !filter.getEstado().equals("0") ? filter.getEstado() : "";

        LOGGER.info("=============================== buscarSeguimientoSolicitud parametros busqueda ===============================");
        LOGGER.info("Numero " + numero);
        LOGGER.info("sumilla " + sumilla);
        LOGGER.info("idContraparte " + idContraparte);
        LOGGER.info("idResponsable " + idResponsable);
        LOGGER.info("idSolicitante " + idSolicitante);
        LOGGER.info("estado " + estado);
        LOGGER.info("===============================================================================================================");

        List<FilaSeguimientoSolicitudDTS> filasBusquedaDTS = hcDocumentoLegalDao.buscarSeguimientoSolicitud(numero, sumilla, idContraparte, idResponsable, idSolicitante, estado);
        return filasBusquedaDTS;
    }

    private List<FilaBusquedaDocumentoBean> obtenerDocumentos(BuscarDocumentoLegalFilter buscarDocumentoLegalFilter, Boolean datosAdicionales) {

        List<FilaBusquedaDTS> resultado = null;

        try {
            Usuario usuario = usuarioRepository.findById(buscarDocumentoLegalFilter.getIdUsuario());
            buscarDocumentoLegalFilter.setUsuario(usuario.getUsuario());

            /**
             * Normalizamos campos  --------------------------------------------------------------------------------
             */

            Integer idAplicacion = cydocConfig.getScaIdAplicacion();
            buscarDocumentoLegalFilter.setValoresORG(opcionSeguridadDao.getValoresOrganizacionales(idAplicacion, buscarDocumentoLegalFilter.getUsuario()));

            List<String> numeroExpedientes = null;
            buscarDocumentoLegalFilter.setNumero(hcDocumentoLegalDao.normalizarTerminoBusqueda(buscarDocumentoLegalFilter.getNumero()));
            buscarDocumentoLegalFilter.setSumilla(hcDocumentoLegalDao.normalizarTerminoBusqueda(buscarDocumentoLegalFilter.getSumilla()));
            buscarDocumentoLegalFilter.setFechaFirmaInicio(DateUtil.minimizarFecha(buscarDocumentoLegalFilter.getFechaFirmaInicio()));
            buscarDocumentoLegalFilter.setFechaFirmaFin(DateUtil.maximizarFecha(buscarDocumentoLegalFilter.getFechaFirmaFin()));
            buscarDocumentoLegalFilter.setFechaVencimientoInicio(DateUtil.minimizarFecha(buscarDocumentoLegalFilter.getFechaVencimientoInicio()));
            buscarDocumentoLegalFilter.setFechaVencimientoFin(DateUtil.maximizarFecha(buscarDocumentoLegalFilter.getFechaVencimientoFin()));
            buscarDocumentoLegalFilter.setIdPais(hcDocumentoLegalDao.normalizarIdBusqueda(buscarDocumentoLegalFilter.getIdPais()));
            buscarDocumentoLegalFilter.setIdCompania(hcDocumentoLegalDao.normalizarIdBusqueda(buscarDocumentoLegalFilter.getIdCompania()));
            buscarDocumentoLegalFilter.setIdArea(hcDocumentoLegalDao.normalizarIdBusqueda(buscarDocumentoLegalFilter.getIdArea()));
            buscarDocumentoLegalFilter.setIdUbicacion(hcDocumentoLegalDao.normalizarIdBusqueda(buscarDocumentoLegalFilter.getIdUbicacion()));
            buscarDocumentoLegalFilter.setTipoUbicacion((buscarDocumentoLegalFilter.getTipoUbicacion() != null && !buscarDocumentoLegalFilter.getTipoUbicacion().equals("0")) ? buscarDocumentoLegalFilter.getTipoUbicacion() : null);
            buscarDocumentoLegalFilter.setIdSolicitante(hcDocumentoLegalDao.normalizarIdBusqueda(buscarDocumentoLegalFilter.getIdSolicitante()));
            buscarDocumentoLegalFilter.setIdResponsable(hcDocumentoLegalDao.normalizarIdBusqueda(buscarDocumentoLegalFilter.getIdResponsable()));
            buscarDocumentoLegalFilter.setIdContraparte(hcDocumentoLegalDao.normalizarIdBusqueda(buscarDocumentoLegalFilter.getIdContraparte()));
            buscarDocumentoLegalFilter.setIdRepresentante(hcDocumentoLegalDao.normalizarIdBusqueda(buscarDocumentoLegalFilter.getIdRepresentante()));
            buscarDocumentoLegalFilter.setMontoDesde(hcDocumentoLegalDao.normalizarMonto(buscarDocumentoLegalFilter.getMontoDesde()));
            buscarDocumentoLegalFilter.setMontoHasta(hcDocumentoLegalDao.normalizarMonto(buscarDocumentoLegalFilter.getMontoHasta()));
            buscarDocumentoLegalFilter.setIdTipoContrato(hcDocumentoLegalDao.normalizarIdBusqueda(buscarDocumentoLegalFilter.getIdTipoContrato()));
            buscarDocumentoLegalFilter.setEstado(buscarDocumentoLegalFilter.getEstado());
            buscarDocumentoLegalFilter.setTipoSolicitud(hcDocumentoLegalDao.normalizarIdBusqueda(buscarDocumentoLegalFilter.getTipoSolicitud()));

            // Buscamos en Alfresco (Si es el caso) --------------------------------
            if (!StringUtils.isBlank(buscarDocumentoLegalFilter.getTextoArchivo())) {
                numeroExpedientes = alfrescoService.buscarExpedientesPorContenido(buscarDocumentoLegalFilter.getTextoArchivo());
            }
            resultado = hcDocumentoLegalDao.buscarDocumentosLegales(buscarDocumentoLegalFilter, datosAdicionales, numeroExpedientes);

            Boolean integracionSCA = cydocConfig.getScaActivo();
            if (resultado != null && !resultado.isEmpty()) {
                if (buscarDocumentoLegalFilter.getValoresORG() != null && !buscarDocumentoLegalFilter.getValoresORG().isEmpty()) {
                    resultado = hcDocumentoLegalDao.filtrarPorVO(resultado, buscarDocumentoLegalFilter.getValoresORG());
                    resultado = hcDocumentoLegalDao.completarOrdenar(resultado, integracionSCA);
                } else {

                    if (!integracionSCA) {
                        resultado = hcDocumentoLegalDao.completarOrdenar(resultado, integracionSCA);
                    } else {
                        resultado = null;
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return covertToFilaBusquedaBean(resultado);
    }

    private FilaBusquedaDocumentoBean covertToFilaBusquedaBean(FilaBusquedaDTS fila) {
        FilaBusquedaDocumentoBean documentoBean = new FilaBusquedaDocumentoBean();
        documentoBean.setIdDocumentoLegal(fila.getIdDocumentoLegal());
        documentoBean.setIdExpediente(fila.getIdExpediente());
        documentoBean.setIdTipoProceso(fila.getIdTipoProceso());
        documentoBean.setIdContratoAdenda(fila.getIdContratoAdenda());
        documentoBean.setNumeroDocumento(fila.getNumero());
        documentoBean.setTipoSolicitud(fila.getTipo());
        documentoBean.setTipoContrato(fila.getTipoContrato());
        documentoBean.setSumilla(fila.getSumilla());
        documentoBean.setContraparte(fila.getContraparte());
        documentoBean.setCompania(fila.getCompania());
        documentoBean.setArea(fila.getArea());
        documentoBean.setPais(fila.getPais());
        documentoBean.setSolicitante(fila.getSolicitante());
        documentoBean.setResponsable(fila.getResponsable());
        documentoBean.setMoneda(fila.getMoneda());
        documentoBean.setMonto(fila.getFlMonto());
        documentoBean.setFechaInicio(fila.getFechaInicio());
        documentoBean.setFechaVencimiento(fila.getFechaVencimiento());
        documentoBean.setUbicacion(hcUbicacionPorDocumentoDao.obtenerUbicacionesReporte(fila.getIdDocumentoLegal()));
        documentoBean.setEstado(AppUtil.getNombreEstadoDL(fila.getEst_codigo()));
        return documentoBean;

    }

    private List<FilaBusquedaDocumentoBean> covertToFilaBusquedaBean(List<FilaBusquedaDTS> filas) {

        List<FilaBusquedaDTS> filasTemp = new ArrayList<>(filas);

        List<FilaBusquedaDocumentoBean> docPadres = new ArrayList<>();


        for (FilaBusquedaDTS fila : filas) {
            if (fila.getTipo().equals(Constantes.TIPO_DOCUMENTO_CONTRATO)) {
                docPadres.add(covertToFilaBusquedaBean(fila));
                filasTemp.remove(fila);
            }
        }

        filas.clear();
        filas.addAll(filasTemp);

        for (FilaBusquedaDocumentoBean docPadre : docPadres) {
            for (FilaBusquedaDTS fila : filas) {
                if (fila.getIdContratoAdenda() != null
                        && docPadre.getIdDocumentoLegal().equals(fila.getIdContratoAdenda())) {
                    LOGGER.info("entroooo");
                    docPadre.getAdendas().add(covertToFilaBusquedaBean(fila));
                    filasTemp.remove(fila);
                }
            }
        }

        if (!filasTemp.isEmpty()) {
            for (FilaBusquedaDTS fila : filasTemp) {
                docPadres.add(covertToFilaBusquedaBean(fila));
            }
        }

        return docPadres;
    }



    @Override
    public String obtenerFechaInicio(Integer idDocumentoLegal) {

        try {
            Date fechaInicio = hcDocumentoLegalRepository.obtenerFechaInicioDocLegal(idDocumentoLegal);
            if (fechaInicio != null) {
                return fechaInicio.toString();
            }
        } catch (NoResultException e) {
            LOGGER.error("Fecha de inicio nula para ID Documento Legal: " + idDocumentoLegal);
        }

        return "";
    }

    @Override
    public String obtenerFechaVencimiento(Integer idDocumentoLegal) {

        try {
            Date fechaInicio = hcDocumentoLegalRepository.obtenerFechaVctoDocLegal(idDocumentoLegal);
            if (fechaInicio != null) {
                return fechaInicio.toString();
            }
        } catch (NoResultException e) {
            LOGGER.error("Fecha de vencimiento nula para ID Documento Legal: " + idDocumentoLegal);
        }

        return "";
    }

    @Override
    public ResponseModel guardarSolicitudesPorDocumentoLegal(ListadoSolicitudesBean listadoSolicitudesBean) {

        ResponseModel responseModel = new ResponseModel();

        if (listadoSolicitudesBean == null || listadoSolicitudesBean.getListadoSolicitudes().isEmpty()) {
            responseModel.setMessage("Datos incompletos");
            responseModel.setHttpSatus(HttpStatus.OK);
            return responseModel;
        }

        try {
            //Guardar
            LOGGER.info("--------------------------------");
            LOGGER.info("Operacion de guardado completado");
            LOGGER.info("--------------------------------");

            for (SolicitudBean solicitud : listadoSolicitudesBean.getListadoSolicitudes()) {

                if(solicitud.getUltimoMovimiento() == null &&  solicitud.getUbicacionDocumento().equals("")){
                    continue;
                }
                //Guardar HcDocumentoLegal
                HcDocumentoLegal actualizarDocumentoLegal = hcDocumentoLegalRepository.findById(Integer.valueOf(solicitud.getIdDocumentoLegal()));
                actualizarDocumentoLegal.setFechaMovimiento(DateUtil.convertStringToDate(solicitud.getUltimoMovimiento(), DateUtil.FORMAT_DATE_XML));
                String descripcion = solicitud.getUbicacionDocumento();

                LOGGER.info("PARAMETRO DE UBICACION ENVIADO = " + solicitud.getIdDocumentoLegal());

                Parametro parametro = parametroRepository.obtenerParametroPorDescripcion(descripcion);
                if(parametro != null){
                    LOGGER.info("Valores de parametro " + parametro.getId());
                    LOGGER.info("Valores de parametro " + parametro.getDescripcion());
                    LOGGER.info("Valores de parametro " + parametro.getTipo());
                }

                actualizarDocumentoLegal.setUbicacionDocumento(parametro);

                hcDocumentoLegalRepository.save(actualizarDocumentoLegal);
            }

            responseModel.setMessage("Guardado exitoso");
            responseModel.setHttpSatus(HttpStatus.OK);

        } catch (Exception e) {
            responseModel.setMessage(e.getMessage());
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseModel;
    }

    @Override
    public Workbook exportarBusquedaDL(BuscarDocumentoLegalFilter buscarDocumentoLegalFilter) {

        try {

            List<FilaBusquedaDocumentoBean> result = obtenerDocumentos(buscarDocumentoLegalFilter, true);
            XLSTransformer transformer = new XLSTransformer();

            Map<String, Object> beans = new HashMap<>();
            beans.put("filas", result);

            File plantilla = new File(cydocConfig.getCarpetaPlantillasExcel() + File.separator + "ResultadoBusqueda.xls");
            FileInputStream plantillaStream = new FileInputStream(plantilla);

            return transformer.transformXLS(plantillaStream, beans);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public ResponseEntity<List<FilaBusquedaDocDashboardBean>> buscarDocumentosLegalesDashboard(BuscarDocumentoLegalFilter buscarDocumentoLegalFilter) {

        List<FilaBusquedaDocDashboardBean> result = null;

        try {
            Usuario usuario = usuarioRepository.findById(buscarDocumentoLegalFilter.getIdUsuario());
            buscarDocumentoLegalFilter.setUsuario(usuario.getUsuario());

            /**
             * Normalizamos campos  --------------------------------------------------------------------------------
             */

            buscarDocumentoLegalFilter.setNumero(hcDocumentoLegalDao.normalizarTerminoBusqueda(buscarDocumentoLegalFilter.getNumero()));
            buscarDocumentoLegalFilter.setIdContraparte(hcDocumentoLegalDao.normalizarIdBusqueda(buscarDocumentoLegalFilter.getIdContraparte()));
            buscarDocumentoLegalFilter.setEstado(buscarDocumentoLegalFilter.getEstado());
            buscarDocumentoLegalFilter.setIdPais(hcDocumentoLegalDao.normalizarIdBusqueda(buscarDocumentoLegalFilter.getIdPais()));
            buscarDocumentoLegalFilter.setSumilla(hcDocumentoLegalDao.normalizarTerminoBusqueda(buscarDocumentoLegalFilter.getSumilla()));

            buscarDocumentoLegalFilter.setIdCompania(hcDocumentoLegalDao.normalizarIdBusqueda(buscarDocumentoLegalFilter.getIdCompania()));

            buscarDocumentoLegalFilter.setIdTipoContrato(hcDocumentoLegalDao.normalizarIdBusqueda(buscarDocumentoLegalFilter.getIdTipoContrato()));

            result = hcDocumentoLegalDao.buscarDocumentosLegalesDashboard(buscarDocumentoLegalFilter);


            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}