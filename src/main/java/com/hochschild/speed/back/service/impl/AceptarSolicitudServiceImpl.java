package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.NotificacionesConfig;
import com.hochschild.speed.back.dao.HcDocumentoLegalDao;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.AceptarSolicitudService;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.EnviarNotificacionService;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("AceptarSolicitudService")
public class AceptarSolicitudServiceImpl implements AceptarSolicitudService {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(AceptarSolicitudServiceImpl.class.getName());
    private final NotificacionesConfig notificacionesConfig;
    private final HcDocumentoLegalDao hcDocumentoLegalDao;
    private final UsuarioRepository usuarioRepository;
    private final ExpedienteRepository expedienteRepository;
    private final HcCompaniaRepository hcCompaniaRepository;
    private final HcContratoRepository hcContratoRepository;
    private final TipoNotificacionRepository tipoNotificacionRepository;
    private final CommonBusinessLogicService commonBusinessLogicService;
    private final EnviarNotificacionService enviarNotificacionService;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final DocumentoRepository documentoRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final HcNumeracionRepository hcNumeracionRepository;

    public AceptarSolicitudServiceImpl(NotificacionesConfig notificacionesConfig,
                                       HcDocumentoLegalDao hcDocumentoLegalDao,
                                       ExpedienteRepository expedienteRepository,
                                       HcCompaniaRepository hcCompaniaRepository,
                                       HcContratoRepository hcContratoRepository,
                                       TipoNotificacionRepository tipoNotificacionRepository,
                                       CommonBusinessLogicService commonBusinessLogicService,
                                       EnviarNotificacionService enviarNotificacionService,
                                       TipoDocumentoRepository tipoDocumentoRepository,
                                       DocumentoRepository documentoRepository,
                                       HcDocumentoLegalRepository hcDocumentoLegalRepository,
                                       HcNumeracionRepository hcNumeracionRepository,
                                       UsuarioRepository usuarioRepository) {
        this.notificacionesConfig = notificacionesConfig;
        this.hcDocumentoLegalDao = hcDocumentoLegalDao;
        this.expedienteRepository = expedienteRepository;
        this.hcCompaniaRepository = hcCompaniaRepository;
        this.hcContratoRepository = hcContratoRepository;
        this.tipoNotificacionRepository = tipoNotificacionRepository;
        this.commonBusinessLogicService = commonBusinessLogicService;
        this.enviarNotificacionService = enviarNotificacionService;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.documentoRepository = documentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.hcNumeracionRepository = hcNumeracionRepository;
    }

    @Override
    public ResponseModel aceptarSolicitud(Integer idExpediente, Integer idUsuario) {

        LOGGER.info("----------------AceptarSolicitudService - AceptarSolicitud-------------------------");
        LOGGER.info("-----------------------------------------------------------------------------------");

        ResponseModel responseModel = new ResponseModel();

        Expediente expediente;
        HcDocumentoLegal hcDocumentoLegal = null;
        Map<String, Object> mapTrazaGenerada;
        TipoNotificacion tipoNotificacion;

        Usuario usuario = usuarioRepository.findById(idUsuario);
        expediente = expedienteRepository.findById(idExpediente);
        hcDocumentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(expediente.getId());

        try {
            if (!validarAceptarSolicitud(hcDocumentoLegal)) {
                LOGGER.info("Validacion de documento legal no exitosa. idExpediente [{}] " + idExpediente);
                responseModel.setHttpSatus(HttpStatus.CONFLICT);
                responseModel.setMessage("Error al validar la solicitud");
                if (hcDocumentoLegal.getContrato() != null) {
                    responseModel.setHttpSatus(HttpStatus.CONFLICT);
                    responseModel.setMessage("Debe completar la sumilla, la fecha del primer borrador y el tipo de contrato (de ser el caso) antes de aceptar.");
                } else if (hcDocumentoLegal.getAdenda() != null) {
                    responseModel.setHttpSatus(HttpStatus.CONFLICT);
                    responseModel.setMessage("Debe completar la sumilla y la fecha del primer borrador antes de aceptar.");
                }
            } else {
                if (hcDocumentoLegal.getContrato() != null) {

                    //Logica para numerar
                    Integer anioVigencia = Integer.parseInt(new SimpleDateFormat("yyyy").format(hcDocumentoLegal.getContrato().getFechaInicio()));
                    Integer secuencia = hcDocumentoLegalDao.obtenerNumeroAutomatico(hcDocumentoLegal.getArea().getCompania().getId(), anioVigencia);
                    String numero = hcDocumentoLegal.getArea().getCompania().getCodigo() + "-" + org.apache.commons.lang.StringUtils.leftPad(secuencia.toString(), 4, '0') + "-" + anioVigencia;

                    hcDocumentoLegal.setNumero(numero);
                    hcDocumentoLegal.setEstado(Constantes.ESTADO_HC_EN_ELABORACION);
                    hcDocumentoLegalRepository.save(hcDocumentoLegal);

                    //Guardar la numeracion
                    HcNumeracion numeracion = new HcNumeracion();
                    numeracion.setCompania(hcCompaniaRepository.findById(hcDocumentoLegal.getArea().getCompania().getId()));
                    numeracion.setContrato(hcContratoRepository.findById(hcDocumentoLegal.getContrato().getId()));
                    numeracion.setAnio(anioVigencia);
                    numeracion.setSecuencia(secuencia);
                    hcNumeracionRepository.save(numeracion);
                    generarDocumento(expediente, usuario, Constantes.TIPO_DOCUMENTO_CONTRATO, numero);
                    generarDocumento(expediente, usuario, Constantes.TIPO_DOCUMENTO_BORRADORES, numero);

                } else if (hcDocumentoLegal.getAdenda() != null) {

                    hcDocumentoLegal.setEstado(Constantes.ESTADO_HC_EN_ELABORACION);
                    hcDocumentoLegalRepository.save(hcDocumentoLegal);
                    generarDocumento(expediente, usuario, Constantes.TIPO_DOCUMENTO_ADENDA, hcDocumentoLegal.getNumero());
                    generarDocumento(expediente, usuario, Constantes.TIPO_DOCUMENTO_BORRADORES, hcDocumentoLegal.getNumero());

                    //Logica para modificar la vigencia de un contrato
                    HcAdenda adenda = hcDocumentoLegal.getAdenda();
                    HcDocumentoLegal contrato = hcDocumentoLegalRepository.findById(adenda.getContrato().getId());

                    if (contrato.getEstado().equals(Constantes.ESTADO_HC_VENCIDO) && adenda.getModifica_fin()) {
                        if (adenda.getIndefinido() || adenda.getNuevaFechaFin().after(new Date())) {
                            contrato.setEstado(Constantes.ESTADO_HC_VIGENTE);
                            hcDocumentoLegalRepository.save(contrato);
                        }
                    } else {
                        if (adenda.getModifica_fin()) {
                            commonBusinessLogicService.desactivarAlarmas(contrato);

                            if (!adenda.getIndefinido() && adenda.getNuevaFechaFin().before(new Date())) {
                                contrato.setEstado(Constantes.ESTADO_HC_VENCIDO);
                                hcDocumentoLegalRepository.save(contrato);
                            }
                        }
                    }
                }

                mapTrazaGenerada = commonBusinessLogicService.generarTraza(idExpediente, usuario, Constantes.Destinatario.NO_ENVIAR, Constantes.OBS_HC_APROBACION_SOLICITUD, Constantes.ACCION_ACEPTAR);
                tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getAceptarSolicitud());

                List<Usuario> destinatarios = new ArrayList<Usuario>();

                destinatarios.add(expediente.getDocumentoLegal().getResponsable());

                if (expediente.getDocumentoLegal().getResponsable().getId() != expediente.getDocumentoLegal().getSolicitante().getId()) {
                    destinatarios.add(expediente.getDocumentoLegal().getSolicitante());
                    LOGGER.info("/aceptarSolicitud/ Solicitante = " + expediente.getDocumentoLegal().getSolicitante());
                    LOGGER.info("/aceptarSolicitud/ ID = " + expediente.getDocumentoLegal().getSolicitante().getId());
                }

                if (hcDocumentoLegal.getAdenda() != null && hcDocumentoLegal.getAdenda().getHcTipoContrato() != null) {
                    LOGGER.info("ES ADENDA AUTOMATICA");
                    LOGGER.info("TipoNotificacion = " + tipoNotificacion.getNombre());
                } else {
                    LOGGER.info("NO ES ADENDA AUTOMATICA");
                    LOGGER.info("TipoNotificacion = " + tipoNotificacion.getNombre());
                    enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, (Traza) mapTrazaGenerada.get(Constantes.MAP_KEY_TRAZA));
                }
                responseModel.setId(idExpediente);
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setMessage("La solicitud fue aceptada");
            }
        } catch (Exception ex) {
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseModel.setMessage("Ocurrió un error en la solicitud. Inténtelo nuevamente.");
            LOGGER.error(ex.getMessage(),ex);
        }

        return responseModel;
    }

    private void generarDocumento(Expediente expediente, Usuario userLogged, String nombreTipoDocumento, String numero) {

        Documento documento = null;
        TipoDocumento tipoDocumento = tipoDocumentoRepository.obtenerTipoDocumentoPorNombre(nombreTipoDocumento);

        //Verificamos si el documento ya existe
        List<Documento> documentosExistentes = documentoRepository.obtenerPorExpedienteYTipoDocumento(expediente.getId(), tipoDocumento.getId());
        if (documentosExistentes != null && !documentosExistentes.isEmpty()) {
            documento = documentosExistentes.get(0);
        }

        if (documento == null) {
            documento = new Documento();
            documento.setExpediente(expediente);
            documento.setTipoDocumento(tipoDocumento);
            documento.setAutor(userLogged);
            documento.setTitulo(nombreTipoDocumento + " " + numero);
            documento.setFechaCreacion(new Date());
            documento.setFechaDocumento(new Date());
            documento.setNumero(numero);
            LOGGER.info("✅ NUEVO DOCUMENTO CREADO: " + documento.getTitulo() + " (ID: " + expediente.getId() + ")");
        } else {
            String tituloAnterior = documento.getTitulo();
            documento.setAutor(userLogged);
            documento.setTitulo(nombreTipoDocumento + " " + numero);
            documento.setFechaDocumento(new Date());
            documento.setNumero(numero);
            LOGGER.info("✅ DOCUMENTO ACTUALIZADO: '" + tituloAnterior + "' → '" + documento.getTitulo() + "' (ID: " + documento.getId() + ")");
        }
        documentoRepository.save(documento);
    }

    private Boolean validarAceptarSolicitud(HcDocumentoLegal hcDocumentoLegal) {
        if (hcDocumentoLegal.getContrato() != null) {
            HcContrato hcContrato = hcDocumentoLegal.getContrato();
            return hcContrato.getTipo_contrato() != null && StringUtils.isNotEmpty(hcDocumentoLegal.getSumilla()) && hcDocumentoLegal.getFechaBorrador() != null;

        } else if (hcDocumentoLegal.getAdenda() != null) {
            return StringUtils.isNotEmpty(hcDocumentoLegal.getSumilla()) && hcDocumentoLegal.getFechaBorrador() != null;

        }
        return false;
    }
}