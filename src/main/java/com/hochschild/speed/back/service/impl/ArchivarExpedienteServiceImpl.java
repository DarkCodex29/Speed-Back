package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.NotificacionesConfig;
import com.hochschild.speed.back.model.bean.bandejaEntrada.ArchivarBean;
import com.hochschild.speed.back.model.bean.bandejaEntrada.DatosArchivarBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.ArchivarExpedienteService;
import com.hochschild.speed.back.service.EnviarNotificacionService;
import com.hochschild.speed.back.service.FirmaElectronicaService;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("ArchivarExpedienteService")
public class ArchivarExpedienteServiceImpl implements ArchivarExpedienteService {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ArchivarExpedienteServiceImpl.class.getName());
    private final NotificacionesConfig notificacionesConfig;
    private final ExpedienteRepository expedienteRepository;
    private final TrazaRepository trazaRepository;
    private final UsuarioPorTrazaRepository usuarioPorTrazaRepository;
    private final UsuarioRepository usuarioRepository;
    private final DocumentoRepository documentoRepository;
    private final HcNumeracionRepository hcNumeracionRepository;
    private final HcContratoRepository hcContratoRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final TipoNotificacionRepository tipoNotificacionRepository;
    private final HcAdendaRepository hcAdendaRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final EnviarNotificacionService enviarNotificacionService;

    private final FirmaElectronicaService firmaElectronicaService;

    private final FirmaElectronicaRepository firmaElectronicaRepository;

    @Autowired
    public ArchivarExpedienteServiceImpl(NotificacionesConfig notificacionesConfig,
                                         ExpedienteRepository expedienteRepository,
                                         TrazaRepository trazaRepository,
                                         UsuarioPorTrazaRepository usuarioPorTrazaRepository,
                                         UsuarioRepository usuarioRepository,
                                         DocumentoRepository documentoRepository,
                                         HcNumeracionRepository hcNumeracionRepository,
                                         HcContratoRepository hcContratoRepository,
                                         HcDocumentoLegalRepository hcDocumentoLegalRepository,
                                         TipoNotificacionRepository tipoNotificacionRepository,
                                         HcAdendaRepository hcAdendaRepository,
                                         TipoDocumentoRepository tipoDocumentoRepository,
                                         EnviarNotificacionService enviarNotificacionService,
                                         FirmaElectronicaService firmaElectronicaService,
                                         FirmaElectronicaRepository firmaElectronicaRepository) {
        this.notificacionesConfig = notificacionesConfig;
        this.expedienteRepository = expedienteRepository;
        this.trazaRepository = trazaRepository;
        this.usuarioPorTrazaRepository = usuarioPorTrazaRepository;
        this.usuarioRepository = usuarioRepository;
        this.documentoRepository = documentoRepository;
        this.hcNumeracionRepository = hcNumeracionRepository;
        this.hcContratoRepository = hcContratoRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.tipoNotificacionRepository = tipoNotificacionRepository;
        this.hcAdendaRepository = hcAdendaRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.enviarNotificacionService = enviarNotificacionService;
        this.firmaElectronicaService = firmaElectronicaService;
        this.firmaElectronicaRepository = firmaElectronicaRepository;
    }

    @Override
    public DatosArchivarBean botonDatosArchivar(Integer idExpediente, Boolean eliminarSolicitud) {

        DatosArchivarBean datosArchivarBean = new DatosArchivarBean();

        try {

            Expediente expediente = expedienteRepository.findById(idExpediente);
            expediente.setDocumentoLegal(null);
            expediente.setDocumentos(null);
            datosArchivarBean.setExpediente(expediente);
            datosArchivarBean.setAccion(Constantes.ACCION_ELIMINAR);
            datosArchivarBean.setEliminarSolicitud(eliminarSolicitud);

        } catch (Exception ex) {

            LOGGER.info(ex.getMessage());
            return null;
        }

        return datosArchivarBean;
    }

    @Override
    public Integer archivarExpediente(ArchivarBean archivarBean, Integer idUsuario) {

        Usuario usuario = usuarioRepository.findById(idUsuario);

        if (usuario != null) {
            LOGGER.info("ID EXPEDIENTE");
            LOGGER.info("=============");
            LOGGER.info(archivarBean.getIdExpediente());
            LOGGER.info("=============");
            Expediente expediente = expedienteRepository.findById(archivarBean.getIdExpediente());
            if (expediente != null) {
                Integer res = archivarExpediente(expediente, usuario, archivarBean.getObservacion(), archivarBean.getAccion());
                if (res == 0 && archivarBean.getEliminarSolicitud())
                    return archivarExpedienteEnSolicitud(expediente, usuario, archivarBean.getObservacion(), archivarBean.getAccion());
                else
                    return res;
            }
            LOGGER.info("Expediente inexistente");
            return Constantes.ERROR_EXPEDIENTE_INEXISTENTE;
        }
        LOGGER.info("Remitente inexistente");
        return Constantes.ERROR_REMITENTE_INEXISTENTE;
    }

    @Override
    public Integer archivarExpediente(Expediente expediente, Usuario remitente, String observacion, String accion) {

        Traza ultimaTraza = trazaRepository.obtenerUltimaTrazaPorExpediente(expediente.getId());

        UsuarioPorTraza uxt = new UsuarioPorTraza();

        if (ultimaTraza == null) {
            return 0;
        }
        uxt = usuarioPorTrazaRepository.buscarUsuarioPorTraza(ultimaTraza.getId(), remitente.getId());
        Usuario destinatario = remitente;

        if (uxt == null)
            uxt = usuarioPorTrazaRepository.buscarUsuarioPorTrazaPorId(ultimaTraza.getId());

        if (uxt != null) {

            ultimaTraza.setActual(false);
            trazaRepository.save(ultimaTraza);

            Traza traza = new Traza();
            traza.setExpediente(expediente);
            traza.setRemitente(remitente);
            if (!AppUtil.checkNullOrEmpty(observacion)) {
                traza.setObservacion(observacion);
            } else {
                traza.setObservacion("Documento Archivado");
            }
            traza.setActual(true);
            traza.setFechaCreacion(new Date());
            traza.setAccion(Constantes.ACCION_ARCHIVAR);
            traza.setOrden(ultimaTraza.getOrden() + 1);
            trazaRepository.save(traza);

            expediente.setEstado(Constantes.ESTADO_ARCHIVADO);
            expedienteRepository.save(expediente);

            if (accion != null && !StringUtils.isEmpty(accion)) {

                if (accion.equals(Constantes.ACCION_ELIMINAR)) {

                    HcDocumentoLegal documentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(expediente.getId());

                    if (documentoLegal.getEstado().equals(Constantes.ESTADO_HC_ENVIADO_FIRMA)){
                        HcFirmaElectronicaDocLegal hcFirmaElectronicaDocLegal = firmaElectronicaRepository.obtenerPorIdDocumentoLegal(documentoLegal.getId());
                        firmaElectronicaService.deleteFirmaElectronica(expediente, documentoLegal, hcFirmaElectronicaDocLegal, remitente,false, true);
                    }

                    if (!documentoLegal.getEstado().equals(Constantes.ESTADO_HC_VENCIDO) && !documentoLegal.getEstado().equals(Constantes.ESTADO_HC_VIGENTE)) {
                        /*Actualizamos lo siguiente
                         * Borrar numero de contrato/adenda
                         * Colocar secuencia de adenda en cero
                         * Borrar el número de contrato de la tabla HC_NUMERACION
                         * Borrar el número del documento de tipo contrato/adenda
                         * Cambiar el estado del contrato a "Solicitud Enviada" y enviar al abogado responsable
                         * -> esto no aplica si el estado era "En Solicitud"
                         * */

                        documentoLegal.setNumero(null);

                        if (documentoLegal.getContrato() != null && !documentoLegal.getEstado().equals(Constantes.ESTADO_HC_EN_SOLICITUD)) {
                            destinatario = documentoLegal.getResponsable();
                            documentoLegal.setEstado(Constantes.ESTADO_HC_SOLICITUD_ENVIADA);
                        }

                        hcDocumentoLegalRepository.save(documentoLegal);

                        if (documentoLegal.getContrato() != null) {
                            HcContrato contrato = documentoLegal.getContrato();
                            HcNumeracion numeracionContrato = hcNumeracionRepository.obtenerNumeracionContrato(contrato.getId());

                            if (numeracionContrato != null) {
                                hcNumeracionRepository.delete(numeracionContrato);
                            }

                            TipoDocumento tdContrato = tipoDocumentoRepository.obtenerTipoDocumentoPorNombre(Constantes.TIPO_DOCUMENTO_CONTRATO);
                            List<Documento> documentos = documentoRepository.obtenerPorExpedienteYTipoDocumento(expediente.getId(), tdContrato.getId());

                            if (documentos != null && !documentos.isEmpty()) {
                                for (Documento doc : documentos) {
                                    doc.setTitulo(Constantes.TIPO_DOCUMENTO_CONTRATO);
                                    doc.setNumero(Constantes.DOCUMENTO_SIN_NUMERACION);
                                    documentoRepository.save(doc);
                                }
                            }

                            hcContratoRepository.save(contrato);

                        } else if (documentoLegal.getAdenda() != null) {
                            HcAdenda adenda = documentoLegal.getAdenda();

                            adenda.setSecuencia(0);

                            TipoDocumento tdAdenda = tipoDocumentoRepository.obtenerTipoDocumentoPorNombre(Constantes.TIPO_DOCUMENTO_ADENDA);
                            List<Documento> documentos = documentoRepository.obtenerPorExpedienteYTipoDocumento(expediente.getId(), tdAdenda.getId());

                            if (documentos != null && !documentos.isEmpty()) {
                                for (Documento doc : documentos) {
                                    doc.setTitulo(Constantes.TIPO_DOCUMENTO_ADENDA);
                                    doc.setNumero(Constantes.DOCUMENTO_SIN_NUMERACION);
                                    documentoRepository.save(doc);
                                }
                            }

                            hcAdendaRepository.save(adenda);

                            //Si es adenda, el expediente debe desaparecer
                            expediente.setEstado(Constantes.ESTADO_ELIMINADO);
                            expedienteRepository.save(expediente);
                        }
                    }

                    TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getExpedienteEliminado());

                    List<Usuario> destinatarios = new ArrayList<Usuario>();
                    destinatarios.add(documentoLegal.getResponsable());
                    if (documentoLegal.getResponsable().getId() != documentoLegal.getSolicitante().getId()) {
                        destinatarios.add(documentoLegal.getSolicitante());
                    }

                    enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, traza);
                }
            }

            UsuarioPorTraza usuarioPorTraza = new UsuarioPorTraza(destinatario, traza);
            usuarioPorTraza.setLeido(false);
            usuarioPorTraza.setResponsable(true);
            usuarioPorTraza.setBloqueado(false);
            usuarioPorTrazaRepository.save(usuarioPorTraza);
            LOGGER.info("El expediente " + expediente.getId() + " - [" + expediente.getNumero() + "] ha sido archivado.");

            return Constantes.OK;
        }
        LOGGER.info("Usuario no autorizado");
        return Constantes.ERROR_USUARIO_NO_AUTORIZADO;
    }

    @Override
    public Integer archivarExpedienteEnSolicitud(Expediente expediente, Usuario remitente, String observacion, String accion) {

        Usuario destinatario = remitente;
        Traza traza = new Traza();
        traza.setExpediente(expediente);
        traza.setRemitente(remitente);
        if (!AppUtil.checkNullOrEmpty(observacion)) {
            traza.setObservacion(observacion);
        } else {
            traza.setObservacion("Documento Archivado");
        }
        traza.setActual(true);
        traza.setFechaCreacion(new Date());
        traza.setAccion(Constantes.ACCION_ARCHIVAR);
        traza.setOrden(1);

        trazaRepository.save(traza);
        expediente.setEstado(Constantes.ESTADO_ELIMINADO);
        expedienteRepository.save(expediente);

        if (accion != null && !StringUtils.isEmpty(accion)) {

            if (accion.equals(Constantes.ACCION_ELIMINAR)) {
                HcDocumentoLegal documentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(expediente.getId());

                if (!documentoLegal.getEstado().equals(Constantes.ESTADO_HC_VENCIDO) && !documentoLegal.getEstado().equals(Constantes.ESTADO_HC_VIGENTE)) {
                    /*Actualizamos lo siguiente
                     * Borrar numero de contrato/adenda
                     * Colocar secuencia de adenda en cero
                     * Borrar el número de contrato de la tabla HC_NUMERACION
                     * Borrar el número del documento de tipo contrato/adenda
                     * Cambiar el estado del contrato a "Solicitud Enviada" y enviar al abogado responsable
                     * -> esto no aplica si el estado era "En Solicitud"
                     * */

                    documentoLegal.setNumero(null);

                    if (documentoLegal.getContrato() != null && !documentoLegal.getEstado().equals(Constantes.ESTADO_HC_EN_SOLICITUD)) {
                        destinatario = documentoLegal.getResponsable();
                        documentoLegal.setEstado(Constantes.ESTADO_HC_SOLICITUD_ENVIADA);
                    }
                    hcDocumentoLegalRepository.save(documentoLegal);

                    if (documentoLegal.getContrato() != null) {
                        HcContrato contrato = documentoLegal.getContrato();
                        HcNumeracion numeracionContrato = hcNumeracionRepository.obtenerNumeracionContrato(contrato.getId());

                        if (numeracionContrato != null) {
                            hcNumeracionRepository.delete(numeracionContrato);
                        }

                        TipoDocumento tdContrato = tipoDocumentoRepository.obtenerTipoDocumentoPorNombre(Constantes.TIPO_DOCUMENTO_CONTRATO);
                        List<Documento> documentos = documentoRepository.obtenerPorExpedienteYTipoDocumento(expediente.getId(), tdContrato.getId());

                        if (documentos != null && !documentos.isEmpty()) {
                            for (Documento doc : documentos) {
                                doc.setTitulo(Constantes.TIPO_DOCUMENTO_CONTRATO);
                                doc.setNumero(Constantes.DOCUMENTO_SIN_NUMERACION);
                                documentoRepository.save(doc);
                            }
                        }
                        hcContratoRepository.save(contrato);

                    } else if (documentoLegal.getAdenda() != null) {
                        HcAdenda adenda = documentoLegal.getAdenda();

                        adenda.setSecuencia(0);

                        TipoDocumento tdAdenda = tipoDocumentoRepository.obtenerTipoDocumentoPorNombre(Constantes.TIPO_DOCUMENTO_ADENDA);
                        List<Documento> documentos = documentoRepository.obtenerPorExpedienteYTipoDocumento(expediente.getId(), tdAdenda.getId());

                        if (documentos != null && !documentos.isEmpty()) {
                            for (Documento doc : documentos) {
                                doc.setTitulo(Constantes.TIPO_DOCUMENTO_ADENDA);
                                doc.setNumero(Constantes.DOCUMENTO_SIN_NUMERACION);
                                documentoRepository.save(doc);
                            }
                        }

                        hcAdendaRepository.save(adenda);

                        //Si es adenda, el expediente debe desaparecer
                        expediente.setEstado(Constantes.ESTADO_ELIMINADO);
                        expedienteRepository.save(expediente);
                    }
                }

                TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getExpedienteEliminado());

                List<Usuario> destinatarios = new ArrayList<Usuario>();
                destinatarios.add(documentoLegal.getResponsable());
                if (documentoLegal.getResponsable().getId() != documentoLegal.getSolicitante().getId()) {
                    destinatarios.add(documentoLegal.getSolicitante());
                }

                enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, traza);
            }
        }

        UsuarioPorTraza usuarioPorTraza = new UsuarioPorTraza(destinatario, traza);
        usuarioPorTraza.setLeido(false);
        usuarioPorTraza.setResponsable(true);
        usuarioPorTraza.setBloqueado(false);
        usuarioPorTrazaRepository.save(usuarioPorTraza);
        LOGGER.info("El expediente " + expediente.getId() + " - [" + expediente.getNumero() + "] ha sido archivado.");
        return Constantes.OK;
    }

    @Override
    public Integer archivarExpedienteWS(Integer idExpediente, String numeroExpediente, String remitente, String observacion) {

        Usuario usuarioRemitente = usuarioRepository.obtenerPorUsuario(remitente);
        ArchivarBean archivarBean = new ArchivarBean();
        archivarBean.setIdExpediente(idExpediente);
        archivarBean.setObservacion(observacion);
        archivarBean.setEliminarSolicitud(null);
        archivarBean.setAccion(null);

        if ((idExpediente == null || idExpediente == 0)) {
            if (numeroExpediente != null) {

                Expediente exp = expedienteRepository.buscarExpediente(numeroExpediente);
                if (exp != null) {
                    return archivarExpediente(exp, usuarioRemitente, observacion, null);
                }
                LOGGER.info("Numero de expediente no existe");
                return 0;
            }
            LOGGER.info("Numero de expediente e ID no existen");
            return 0;
        }
        return archivarExpediente(archivarBean, usuarioRemitente.getId());
    }

    @Override
    public Expediente getExpediente(Integer idExpediente) {
        return expedienteRepository.findById(idExpediente);
    }

    @Override
    public List<Traza> obtenerTrazas(Integer[] idTrazas) {

        List<Traza> trazas = new ArrayList<Traza>();
        for (Integer idTraza : idTrazas) {
            trazas.add(trazaRepository.findById(idTraza));
        }
        return trazas;
    }

    @Override
    public List<String> expedienteDerivacionSimple(List<Traza> trazas) {

        List<String> expedientes = new ArrayList<String>();
        for (Traza traza : trazas) {
            List<UsuarioPorTraza> upts = usuarioPorTrazaRepository.obtenerPorTraza(traza.getId());
            if (upts.size() > 1) {
                expedientes.add(traza.getExpediente().getNumero());
            }
        }
        return expedientes;
    }

    @Override
    public List<String> expedienteDerivadoRol(List<Traza> trazas) {

        List<String> expedientes = new ArrayList<String>();
        for (Traza traza : trazas) {
            List<UsuarioPorTraza> upts = usuarioPorTrazaRepository.obtenerPorTraza(traza.getId());
            boolean rol = false;
            for (UsuarioPorTraza upt : upts) {
                if (upt.getRol() != null) {
                    rol = true;
                }
            }
            if (rol) {
                expedientes.add(traza.getExpediente().getNumero());
            }
        }
        return expedientes;
    }

    @Override
    public List<String> expedienteDerivacionMultiple(List<Traza> trazas) {
        List<String> expedientes = new ArrayList<String>();
        for (Traza traza : trazas) {
            if (traza.getPadre() != null) {
                expedientes.add(traza.getExpediente().getNumero());
            }
        }
        return expedientes;
    }

    @Override
    public List<String> expedienteProcesoWorkflow(List<Traza> trazas) {
        List<String> expedientes = new ArrayList<String>();
        for (Traza traza : trazas) {
            Expediente expediente = traza.getExpediente();
            if (expediente.getProceso().getTipoProceso().getCodigo().equals(Constantes.TIPO_PROCESO_WORKFLOW)) {
                expedientes.add(expediente.getNumero());
            }
        }
        return expedientes;
    }

    @Override
    public List<String> expedienteProcesoIntalio(List<Traza> trazas) {
        List<String> expedientes = new ArrayList<String>();
        for (Traza traza : trazas) {
            Expediente expediente = traza.getExpediente();
            if (expediente.getProceso().getTipoProceso().getCodigo().equals(Constantes.TIPO_PROCESO_INTALIO)) {
                expedientes.add(expediente.getNumero());
            }
        }
        return expedientes;
    }

    @Override
    public Expediente obtenerExpedientePorTraza(Integer idTraza) {
        Traza traza = trazaRepository.findById(idTraza);
        return traza.getExpediente();
    }
}