package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.dao.ExpedienteDao;
import com.hochschild.speed.back.model.bean.GuardarExpedienteBean;
import com.hochschild.speed.back.model.bean.registrarSolicitud.ExploracionBean;
import com.hochschild.speed.back.model.domain.speed.Expediente;
import com.hochschild.speed.back.model.domain.speed.HcUbicacion;
import com.hochschild.speed.back.model.domain.speed.Proceso;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.filter.ExpedienteFilter;
import com.hochschild.speed.back.model.filter.reportefilter.ExpedienteAreaFilter;
import com.hochschild.speed.back.model.reporte.ExpedientePorAreaBean;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.ExpedienteService;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExpedienteServiceImpl implements ExpedienteService {

    private static final Logger LOGGER = Logger.getLogger(ExpedienteServiceImpl.class.getName());
    private final ExpedienteRepository expedienteRepository;
    private final HcUbicacionRepository hcUbicacionRepository;
    private final HcCompaniaRepository hcCompaniaRepository;
    private final HcTipoUbicacionRepository hcTipoUbicacionRepository;
    private final ProcesoRepository procesoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ExpedienteDao expedienteDao;

    @Autowired
    public ExpedienteServiceImpl(ExpedienteRepository expedienteRepository,
                                 HcUbicacionRepository hcUbicacionRepository,
                                 HcCompaniaRepository hcCompaniaRepository,
                                 HcTipoUbicacionRepository hcTipoUbicacionRepository,
                                 ProcesoRepository procesoRepository,
                                 UsuarioRepository usuarioRepository,
                                 ExpedienteDao expedienteDao) {
        this.expedienteRepository = expedienteRepository;
        this.hcUbicacionRepository = hcUbicacionRepository;
        this.hcCompaniaRepository = hcCompaniaRepository;
        this.hcTipoUbicacionRepository = hcTipoUbicacionRepository;
        this.procesoRepository = procesoRepository;
        this.usuarioRepository = usuarioRepository;
        this.expedienteDao = expedienteDao;
    }

    @Override
    public List<Expediente> findAll(ExpedienteFilter expedienteFilter) {

        List<Expediente> result;

        try {
            result = expedienteRepository.findAll();

        } catch (Exception ex) {
            result = new ArrayList<>();
            LOGGER.info(ex.getMessage(), ex);
        }
        return result;
    }

    @Override
    public Expediente findById(Integer idExpediente) {

        Expediente result;

        try {
            result = expedienteRepository.findOne(idExpediente);

        } catch (Exception ex) {
            result = new Expediente();
            LOGGER.info(ex.getMessage(), ex);
        }

        return result;
    }

    @Override
    public ResponseModel guardarExpediente(GuardarExpedienteBean guardarExpedienteBean, Integer idPuesto) {

        ResponseModel responseModel = new ResponseModel();
        try {
            Integer idExpediente = null;
            idExpediente = guardarExpedienteBean.getIdExpediente();

            LOGGER.info("Registrar Solicitud /guardarExpediente");
            LOGGER.info(idExpediente);
            LOGGER.info("--------------------------------------");

            if (idExpediente != null) {

                LOGGER.info("YA EXISTE EL EXPEDIENTE");
                // Ya existe el expediente
                Expediente expediente = expedienteRepository.findOne(idExpediente);
                Proceso proceso = procesoRepository.findById(guardarExpedienteBean.getIdProceso());
                expediente.setProceso(proceso);
                expediente.setAplicaPenalidad(guardarExpedienteBean.getAplicaPenalidad());
                idExpediente = actualizarExpediente(expediente, idPuesto);
                if (idExpediente != null && idExpediente > 0) {
                    responseModel.setMessage("Registro exitoso");
                    responseModel.setId(idExpediente);
                }
            } else {

                Expediente expediente = new Expediente();
                expediente.setTitulo(guardarExpedienteBean.getTitulo());
                if (guardarExpedienteBean.getTitulo() != null) {
                    String[] cadena = guardarExpedienteBean.getTitulo().split(",");
                    if (cadena.length > 1) {
                        String primeraCadena = cadena[0];
                        expediente.setTitulo(primeraCadena);
                    }
                }

                LOGGER.info("SE CREA NUEVO EXPEDIENTE");
                LOGGER.info("APLICA PENALIDAD " + guardarExpedienteBean.getAplicaPenalidad());
                Proceso proceso = procesoRepository.findById(guardarExpedienteBean.getIdProceso());
                expediente.setProceso(proceso);
                expediente.setAplicaPenalidad(guardarExpedienteBean.getAplicaPenalidad());
                idExpediente = guardarExpediente(expediente, idPuesto);
                if (idExpediente != null && idExpediente > 0) {
                    responseModel.setMessage("Registro exitoso");
                    responseModel.setId(idExpediente);
                }
            }
            responseModel.setHttpSatus(HttpStatus.OK);

        } catch (Exception e) {
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseModel.setMessage("Error");
            LOGGER.error(e.getMessage(), e);
        }

        return responseModel;
    }

    @Override
    public ResponseModel guardarExploracion(ExploracionBean exploracionBean, Integer idUsuario) {

        ResponseModel responseModel = new ResponseModel();
        HcUbicacion ubicacion = null;

        if (exploracionBean.getNombre() != null) {

            ubicacion = hcUbicacionRepository.getUbicacionActivaPorTipoCodigo(Constantes.TIPO_UBICACION_EXPLORACION, exploracionBean.getIdCompania(), exploracionBean.getNombre());

            if (ubicacion == null) {
                //Es nueva
                ubicacion = new HcUbicacion();
                ubicacion.setCompania(hcCompaniaRepository.findById(exploracionBean.getIdCompania()));
                ubicacion.setEstado(Constantes.ESTADO_ACTIVO);
                ubicacion.setTipo_ubicacion(hcTipoUbicacionRepository.buscarPorCodigo(Constantes.TIPO_UBICACION_EXPLORACION));
                ubicacion.setNombre(exploracionBean.getNombre());

                hcUbicacionRepository.save(ubicacion);
                responseModel.setId(ubicacion.getId());
                responseModel.setMessage("Exploración guardada");
                responseModel.setHttpSatus(HttpStatus.OK);

            } else {
                responseModel.setMessage("Exploración ya registrada");
                responseModel.setHttpSatus(HttpStatus.OK);
            }
        } else {
            responseModel.setMessage("Parametros incompletos");
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseModel;
    }

    @Override
    public Integer actualizarExpediente(Expediente expediente, Integer idUsuario) {

        if (expediente.getCliente() == null || expediente.getCliente().getId() == null) {
            expediente.setCliente(null);
        }

        try {
            expedienteRepository.save(expediente);
            LOGGER.info("Expediente actualizado satisfactoriamente");
        } catch (Exception e) {
            LOGGER.error("No se pudo actualizar el expediente, error [{" + e.getMessage() + "]}");
        }
        // se cambio el return de expendiente.getExistente a expediente.getId
        return expediente.getId();
    }

    @Override
    public Integer guardarExpediente(Expediente expediente, Integer idPuesto) {

        LOGGER.info("TITULO DEL EXPEDIENTE EN EL SERVICE: " + expediente.getTitulo());
        Usuario usuario = usuarioRepository.findById(Integer.valueOf(idPuesto));

        if (expediente.getCliente() == null || expediente.getCliente().getId() == null) {
            expediente.setCliente(null);
        }
        expediente.setNumero(expedienteDao.obtenerNumeracionExpediente());
        expediente.setCreador(usuario);
        expediente.setFechaCreacion(new Date());
        expediente.setEstado(Constantes.ESTADO_GUARDADO);
        expediente.setAplicaArrendamiento("N");
        expediente.setFirmaElectronica("N");

        expedienteRepository.save(expediente);

        LOGGER.info("Expediente guardado satisfactoriamente");
        return expediente.getId();
    }

    @Override
    public List<ExpedientePorAreaBean> filterExpedientesByArea(ExpedienteAreaFilter filter) {
        if (filter.getIdUsuario().equals(0)) {
            filter.setIdUsuario(null);
        }
        if (filter.getIdProceso().equals(0)) {
            filter.setIdProceso(null);
        }
        if (filter.getAreaCreadora().equals(0)) {
            filter.setAreaCreadora(null);
        }
        if (filter.getAreaDestino().equals(0)) {
            filter.setAreaDestino(null);
        }
        if (filter.getEstado().equals('0')) {
            filter.setEstado(null);
        }
        if (filter.getSedeOrigen().equals(0)) {
            filter.setSedeOrigen(null);
        }
        if (filter.getSedeDestino().equals(0)) {
            filter.setSedeDestino(null);
        }
        if (filter.getIdUsuarioFinal().equals(0)) {
            filter.setIdUsuarioFinal(null);
        }
        Date date;
        if (filter.getFechaUltDerivacion().equals("")) {
            date = null;
        } else {
            date = DateUtil.convertStringToDate(filter.getFechaUltDerivacion(), DateUtil.FORMAT_DATE_XML);
        }
        return this.expedienteRepository.filtrarExpedientesPorArea(
                filter.getIdUsuario(),
                filter.getIdProceso(),
                filter.getNumeroExpediente(),
                DateUtil.convertStringToDate(filter.getFechaInicio(), DateUtil.FORMAT_DATE_XML),
                DateUtil.convertStringToDate(filter.getFechaFin(), DateUtil.FORMAT_DATE_XML),
                filter.getEstado(),
                filter.getAreaCreadora(),
                filter.getAreaDestino(),
                filter.getSedeOrigen(),
                filter.getSedeDestino(),
                filter.getIdUsuarioFinal(),
                date
        );


    }

}
