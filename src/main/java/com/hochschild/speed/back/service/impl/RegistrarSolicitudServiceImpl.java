package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.NotificacionesConfig;
import com.hochschild.speed.back.dao.HcDocumentoLegalDao;
import com.hochschild.speed.back.model.bean.registrarContratoManual.DocumentoLegalManualBean;
import com.hochschild.speed.back.model.bean.registrarContratoManual.UsuarioNotificacionBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.domain.speed.utils.DocumentoLegalXAdendaDTS;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.EnviarNotificacionService;
import com.hochschild.speed.back.service.RegistrarExpedienteService;
import com.hochschild.speed.back.service.RegistrarSolicitudService;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.exception.ExcepcionAlfresco;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("registrarSolicitudService")
public class RegistrarSolicitudServiceImpl implements RegistrarSolicitudService {
    private static final Logger LOGGER = Logger.getLogger(RegistrarSolicitudServiceImpl.class.getName());
    private final NotificacionesConfig notificacionesConfig;
    private final HcDocumentoLegalDao hcDocumentoLegalDao;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final ClienteRepository clienteRepository;
    private final HcAlarmaRepository hcAlarmaRepository;
    private final HcAdendaRepository hcAdendaRepository;
    private final UsuarioRepository usuarioRepository;
    private final HcRepresentantePorDocumentoRepository hcRepresentantePorDocumentoRepository;
    private final HcUbicacionPorDocumentoRepository hcUbicacionPorDocumentoRepository;
    private final HcTipoContratoConfiguracionRepository hcTipoContratoConfiguracionRepository;
    private final UsuarioPorTrazaRepository usuarioPorTrazaRepository;
    private final TrazaRepository trazaRepository;
    private final TipoNotificacionRepository tipoNotificacionRepository;
    private final HcNumeracionRepository hcNumeracionRepository;
    private final ParametroRepository parametroRepository;
    private final HcContratoRepository hcContratoRepository;
    private final ExpedienteRepository expedienteRepository;
    private final HcCompaniaRepository hcCompaniaRepository;
    private final EnviarNotificacionService enviarNotificacionService;
    private final CommonBusinessLogicService commonBusinessLogicService;
    private final RegistrarExpedienteService registrarExpedienteService;

    @Autowired
    public RegistrarSolicitudServiceImpl(NotificacionesConfig notificacionesConfig,
                                         HcDocumentoLegalDao hcDocumentoLegalDao,
                                         HcDocumentoLegalRepository hcDocumentoLegalRepository,
                                         ClienteRepository clienteRepository,
                                         HcAlarmaRepository hcAlarmaRepository,
                                         HcAdendaRepository hcAdendaRepository,
                                         UsuarioRepository usuarioRepository,
                                         HcRepresentantePorDocumentoRepository hcRepresentantePorDocumentoRepository,
                                         HcUbicacionPorDocumentoRepository hcUbicacionPorDocumentoRepository,
                                         HcTipoContratoConfiguracionRepository hcTipoContratoConfiguracionRepository,
                                         UsuarioPorTrazaRepository usuarioPorTrazaRepository,
                                         TrazaRepository trazaRepository,
                                         TipoNotificacionRepository tipoNotificacionRepository,
                                         HcNumeracionRepository hcNumeracionRepository,
                                         ParametroRepository parametroRepository,
                                         HcContratoRepository hcContratoRepository,
                                         ExpedienteRepository expedienteRepository,
                                         HcCompaniaRepository hcCompaniaRepository,
                                         EnviarNotificacionService enviarNotificacionService,
                                         CommonBusinessLogicService commonBusinessLogicService,
                                         RegistrarExpedienteService registrarExpedienteService) {
        this.notificacionesConfig = notificacionesConfig;
        this.hcDocumentoLegalDao = hcDocumentoLegalDao;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.clienteRepository = clienteRepository;
        this.hcAlarmaRepository = hcAlarmaRepository;
        this.hcAdendaRepository = hcAdendaRepository;
        this.usuarioRepository = usuarioRepository;
        this.hcRepresentantePorDocumentoRepository = hcRepresentantePorDocumentoRepository;
        this.hcUbicacionPorDocumentoRepository = hcUbicacionPorDocumentoRepository;
        this.hcTipoContratoConfiguracionRepository = hcTipoContratoConfiguracionRepository;
        this.usuarioPorTrazaRepository = usuarioPorTrazaRepository;
        this.trazaRepository = trazaRepository;
        this.tipoNotificacionRepository = tipoNotificacionRepository;
        this.hcNumeracionRepository = hcNumeracionRepository;
        this.parametroRepository = parametroRepository;
        this.hcContratoRepository = hcContratoRepository;
        this.expedienteRepository = expedienteRepository;
        this.hcCompaniaRepository = hcCompaniaRepository;
        this.enviarNotificacionService = enviarNotificacionService;
        this.commonBusinessLogicService = commonBusinessLogicService;
        this.registrarExpedienteService = registrarExpedienteService;
    }

    @Override
    public List<Cliente> obtenerRepresentantesDocumentoLegal(Integer idContrato) {
        return hcRepresentantePorDocumentoRepository.obtenerRepresentantesDocumentoLegal(idContrato);
    }

    @Override
    public DocumentoLegalXAdendaDTS datosContratoParaAdenda(Integer idDocumentoLegal) {

        HcAdenda hcAdenda;
        Cliente lastContraparte = new Cliente();

        LOGGER.info("--------------------------------------------");
        LOGGER.info("ID DOCUMENTO LEGAL / DocumentoLegalXAdendaDTS: " + idDocumentoLegal);
        LOGGER.info("--------------------------------------------");

        DocumentoLegalXAdendaDTS documentoDTS = hcDocumentoLegalRepository.obtenerDatosParaAdenda(idDocumentoLegal);
        Cliente contraparte = clienteRepository.findById(documentoDTS.getIdContraparte());
        Integer secuenciaActual = hcAdendaRepository.obtenerSecuenciaAdendaPorContrato(idDocumentoLegal);

        LOGGER.info("--------------------------------------------");
        LOGGER.info("SECUENCIA ACTUAL: " + secuenciaActual);
        LOGGER.info("--------------------------------------------");

        hcAdenda = hcAdendaRepository.obtenerByIdContratoAndSecuencia(idDocumentoLegal, secuenciaActual);

        secuenciaActual = secuenciaActual != null ? secuenciaActual + 1 : 1;
        documentoDTS.setNumeroAdenda(secuenciaActual);
        //Se modifica para que envie el nombre de la contraparte
        documentoDTS.setCntNombre(contraparte.getNombres());
        documentoDTS.setCntNumeroIdentificacion(contraparte.getNumeroIdentificacion());

        if (hcAdenda != null) {

            LOGGER.info("--------------------------------------------");
            LOGGER.info("ADENDA ULTIMA REGISTRADA: " + hcAdenda.getId());
            LOGGER.info("--------------------------------------------");

            HcDocumentoLegal documentoLegal=hcAdenda.getDocumentoLegal();

            documentoDTS.setRepresentantes(hcRepresentantePorDocumentoRepository.obtenerRepresentantesDocumentoLegal(hcAdenda.getId()));
            documentoDTS.setUbicaciones(hcUbicacionPorDocumentoRepository.obtenerUbicacionesDocumentoLegal(hcAdenda.getId()));


            if(documentoLegal.getContraparte()!=null){
                documentoDTS.setIdContraparte(documentoLegal.getContraparte().getId());
                documentoDTS.setCntNombre(documentoLegal.getContraparte().getNombres());
                documentoDTS.setCntNumeroIdentificacion(documentoLegal.getContraparte().getNumeroIdentificacion());
            }

            documentoDTS.setCntDomicilio(documentoLegal.getCnt_domicilio());
            documentoDTS.setCntNombreContacto(documentoLegal.getCnt_nombre_contacto());
            documentoDTS.setCntTelefono(documentoLegal.getCnt_telefono_contacto());
            documentoDTS.setCntEmail(documentoLegal.getCnt_correo_contacto());

            LOGGER.info("Trabajando con ADENDAS YA CREADAS");
        } else {
            documentoDTS.setRepresentantes(hcRepresentantePorDocumentoRepository.obtenerRepresentantesDocumentoLegal(idDocumentoLegal));
            documentoDTS.setUbicaciones(hcUbicacionPorDocumentoRepository.obtenerUbicacionesDocumentoLegal(idDocumentoLegal));
            LOGGER.info("Trabajando con PRIMERA ADENDA");
        }

        try {

            Cliente cliente;
            if (hcAdenda != null) {
                cliente = commonBusinessLogicService.obtenerDatosSunat(lastContraparte.getId());
            } else {
                cliente = commonBusinessLogicService.obtenerDatosSunat(contraparte.getId());
            }
            
			Optional.ofNullable(cliente).map(Cliente::getSituacionSunat).filter(situacion -> !situacion.isEmpty())
					.ifPresent(situacion -> documentoDTS.setCntSituacion(situacion));

        } catch (Exception e) {
            LOGGER.error("No se pudo conectar con SUNAT", e);
        }

        return documentoDTS;
    }

    @Override
    public List<HcTipoContratoConfiguracion> buscarTipoAdenda(Character codigo, String estado) {
        return hcTipoContratoConfiguracionRepository.getListaTipoAdenda(codigo, estado);
    }

    @Override
    public ResponseModel registrarHcDocumentoLegalManual(DocumentoLegalManualBean documentoLegalManualBean, Integer idUsuario) {

        ResponseModel responseModel = new ResponseModel();
        Expediente expediente = registrarExpedienteService.obtenerExpedienteNuevo(documentoLegalManualBean.getIdExpediente());
        Usuario usuario = usuarioRepository.findById(idUsuario);
        LOGGER.info("registrarHcDocumentoLegalManual 555555");
        //Subimos archivos a Alfresco
        try {
            registrarExpedienteService.subirArchivosAlfresco(expediente, usuario);
        } catch (ExcepcionAlfresco ex) {
            LOGGER.info("Error en el alfresco", ex);
            ex.printStackTrace();
            throw new RuntimeException("ALFRESCO");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            responseModel.setMessage(e.getMessage());
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return responseModel;
        }

        //Si la carga es correcta, se procede con el resto
        expediente.setEstado(Constantes.ESTADO_ARCHIVADO);
        expedienteRepository.save(expediente);

        HcDocumentoLegal documentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(expediente.getId());
        HcAdenda adenda = null;

        documentoLegal.setSumilla(expediente.getTitulo());
        documentoLegal.setFechaSolicitud(new Date());

        if (documentoLegal.getContrato() != null) {
            //Logica para numerar
            Integer anioVigencia = Integer.parseInt(new SimpleDateFormat("yyyy").format(documentoLegal.getContrato().getFechaInicio()));
            Integer secuencia = hcDocumentoLegalDao.obtenerNumeroManual(documentoLegal.getArea().getCompania().getId(), anioVigencia);
            documentoLegal.setNumero(documentoLegal.getArea().getCompania().getCodigo() + "-" + StringUtils.leftPad(secuencia.toString(), 4, '0') + "-" + anioVigencia);

            //Guardar la numeracion
            HcNumeracion numeracion = new HcNumeracion();
            //HcNumeracionPk pkNumeracion = new HcNumeracionPk();
            numeracion.setCompania(hcCompaniaRepository.findById(documentoLegal.getArea().getCompania().getId()));
            numeracion.setContrato(hcContratoRepository.findById(documentoLegal.getContrato().getId()));
            //numeracion.setId(pkNumeracion);
            numeracion.setAnio(anioVigencia);
            numeracion.setSecuencia(secuencia);

            hcNumeracionRepository.save(numeracion);

            //Logica para estado de contrato
            if (documentoLegal.getContrato() != null && !documentoLegal.getContrato().getIndefinido()) {
                Calendar hoy = Calendar.getInstance();
                hoy.set(Calendar.HOUR_OF_DAY, 0);
                hoy.set(Calendar.MINUTE, 0);
                hoy.set(Calendar.SECOND, 0);
                hoy.set(Calendar.MILLISECOND, 0);

                Calendar fechaVencimiento = Calendar.getInstance();
                fechaVencimiento.setTime(documentoLegal.getContrato().getFechaFin());
                fechaVencimiento.set(Calendar.HOUR_OF_DAY, 0);
                fechaVencimiento.set(Calendar.MINUTE, 0);
                fechaVencimiento.set(Calendar.SECOND, 0);
                fechaVencimiento.set(Calendar.MILLISECOND, 0);

                if (hoy.after(fechaVencimiento)) {
                    documentoLegal.setEstado(Constantes.ESTADO_HC_VENCIDO);
                } else {
                    documentoLegal.setEstado(Constantes.ESTADO_HC_VIGENTE);

                    //Creamos alarma para el vencimiento
                    commonBusinessLogicService.crearAlarma(documentoLegal, fechaVencimiento.getTime());
                }

            } else {
                documentoLegal.setEstado(Constantes.ESTADO_HC_VIGENTE);
            }

        } else if (documentoLegal.getAdenda() != null) {
            adenda = hcAdendaRepository.findById(documentoLegal.getId());

            documentoLegal.setEstado(Constantes.ESTADO_HC_VIGENTE);

            //Logica para modificar la vigencia de un contrato
            HcDocumentoLegal contrato = hcDocumentoLegalRepository.findById(adenda.getContrato().getId());

            if (contrato.getEstado().equals(Constantes.ESTADO_HC_VENCIDO) && adenda.getModifica_fin()) {
                if (adenda.getIndefinido() || adenda.getNuevaFechaFin().after(new Date())) {
                    contrato.setEstado(Constantes.ESTADO_HC_VIGENTE);
                    hcDocumentoLegalRepository.save(contrato);

                    //Desactivar las alarmas del contrato
                    List<HcAlarma> alarmas = hcAlarmaRepository.obtenerAlarmas(contrato.getId());
                    if (alarmas != null && !alarmas.isEmpty()) {
                        for (HcAlarma alarma : alarmas) {
                            if (alarma.getEstado().equals(Constantes.ESTADO_ACTIVO)) {
                                alarma.setEstado(Constantes.ESTADO_INACTIVO);
                                hcAlarmaRepository.save(alarma);
                            }
                        }
                    }

                    //Creamos la nueva alarma
                    if (!adenda.getIndefinido()) {
                        commonBusinessLogicService.crearAlarma(contrato, adenda.getNuevaFechaFin());
                    }
                }
            } else {
                if (adenda.getModifica_fin()) {
                    //Desactivamos alarmas
                    commonBusinessLogicService.desactivarAlarmas(contrato);

                    if (!adenda.getIndefinido() && adenda.getNuevaFechaFin().before(new Date())) {
                        contrato.setEstado(Constantes.ESTADO_HC_VENCIDO);
                        hcDocumentoLegalRepository.save(contrato);
                    } else {
                        if (!adenda.getIndefinido()) {
                            //Creamos la nueva alarma
                            commonBusinessLogicService.crearAlarma(contrato, adenda.getNuevaFechaFin());
                        }
                    }
                }
            }
        }

        hcDocumentoLegalRepository.save(documentoLegal);

        if (adenda != null) {
            adenda.setDocumentoLegal(documentoLegal);
            hcAdendaRepository.save(adenda);
        }

        //Logica para archivar
        Traza traza = new Traza();
        traza.setExpediente(expediente);
        traza.setRemitente(documentoLegal.getResponsable());
        traza.setActual(true);
        traza.setFechaCreacion(new Date());
        traza.setAccion(Constantes.ACCION_REGISTRO_MANUAL);
        traza.setOrden(1);
        traza.setPrioridad(parametroRepository.obtenerPorTipoValor(Constantes.PARAMETRO_PRIORIDAD_EXPEDIENTE, Constantes.VALOR_PRIORIDAD_NORMAL));
        traza.setObservacion(Constantes.OBS_HC_ARCHIVO_SOLICITUD_MANUAL);
        traza.setProceso(expediente.getProceso());

        trazaRepository.save(traza);

        UsuarioPorTraza ut = new UsuarioPorTraza(documentoLegal.getResponsable(), traza);
        ut.setResponsable(true);
        ut.setAprobado(false);
        ut.setBloqueado(false);
        ut.setLeido(false);
        usuarioPorTrazaRepository.save(ut);

        TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getRegistroManual());
        List<Usuario> destinatarios = new ArrayList<Usuario>();

        for (UsuarioNotificacionBean item : documentoLegalManualBean.getUsuarios()) {
            if (item.getEsGrupo() == 1) {
                for (Usuario usuarioGrupo : usuarioRepository.obtenerUsuariosxGrupo(Constantes.ESTADO_ACTIVO, item.getId())) {
                    destinatarios.add(usuarioGrupo);
                }
            } else if (item.getEsGrupo() == 0) {
                LOGGER.info("El usuario es: " + usuarioRepository.findById(item.getId()));
                destinatarios.add(usuarioRepository.findById(item.getId()));
            }
        }

        for (Usuario item : destinatarios) {
            LOGGER.info("DESDE EL SERVICE REGISTRAR SOLICITUD");
            LOGGER.info("Usuario:" + item.getId());
            LOGGER.info("Nombre:" + item.getNombres());
        }

        enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, traza);

        responseModel.setId(traza.getId());
        responseModel.setMessage("Proceso exitoso");
        responseModel.setHttpSatus(HttpStatus.OK);
        return responseModel;
    }

}