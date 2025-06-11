package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.FirmaElectronicaConfig;
import com.hochschild.speed.back.config.NotificacionesConfig;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.*;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.DateUtil;
import com.hochschild.speed.back.ws.model.firma_electronica.request.*;
import com.hochschild.speed.back.ws.remote.firma_electronica.client.ContratoClient;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service("FirmaElectronicaService")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class FirmaElectronicaServiceImpl implements FirmaElectronicaService {
    private static final Logger LOGGER = Logger.getLogger(FirmaElectronicaServiceImpl.class.getName());
    private final ContratoClient contratoClient;
    private final HcRepresentantePorDocumentoRepository hcRepresentantePorDocumentoRepository;
    private final FirmaElectronicaConfig firmaElectronicaConfig;
    private final NotificacionesConfig notificacionesConfig;
    private final CommonBusinessLogicService commonBusinessLogicService;
    private final ParametroService parametroService;
    private final EnviarNotificacionService enviarNotificacionService;
    private final BusquedaExpedienteService busquedaExpedienteService;
    private final AlfrescoService alfrescoService;
    private final TrazaRepository trazaRepository;
    private final TipoNotificacionRepository tipoNotificacionRepository;
    private final PlantillaFirmaElectronicaRepository plantillaFirmaElectronicaRepository;
    private final UsuarioRepresentanteCompaniaRepository usuarioRepresentanteCompaniaRepository;
    private final ExpedienteRepository expedienteRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final FirmaElectronicaRepository firmaElectronicaRepository;
    private final UsuarioRepository usuarioRepository;

    private final ParametroRepository parametroRepository;

    private final HcMovimientoTransaccionRepository hcMovimientoTransaccionRepository;

    @Autowired
    public FirmaElectronicaServiceImpl(ContratoClient contratoClient,
                                       HcRepresentantePorDocumentoRepository hcRepresentantePorDocumentoRepository, FirmaElectronicaConfig firmaElectronicaConfig,
                                       NotificacionesConfig notificacionesConfig,
                                       CommonBusinessLogicService commonBusinessLogicService,
                                       ParametroService parametroService,
                                       EnviarNotificacionService enviarNotificacionService,
                                       BusquedaExpedienteService busquedaExpedienteService,
                                       AlfrescoService alfrescoService,
                                       TrazaRepository trazaRepository,
                                       TipoNotificacionRepository tipoNotificacionRepository,
                                       PlantillaFirmaElectronicaRepository plantillaFirmaElectronicaRepository,
                                       UsuarioRepresentanteCompaniaRepository usuarioRepresentanteCompaniaRepository,
                                       ExpedienteRepository expedienteRepository, HcDocumentoLegalRepository hcDocumentoLegalRepository,
                                       FirmaElectronicaRepository firmaElectronicaRepository,
                                       UsuarioRepository usuarioRepository,
                                       ParametroRepository parametroRepository,
                                       HcMovimientoTransaccionRepository hcMovimientoTransaccionRepository) {
        this.contratoClient = contratoClient;
        this.hcRepresentantePorDocumentoRepository = hcRepresentantePorDocumentoRepository;
        this.firmaElectronicaConfig = firmaElectronicaConfig;
        this.notificacionesConfig = notificacionesConfig;
        this.commonBusinessLogicService = commonBusinessLogicService;
        this.parametroService = parametroService;
        this.enviarNotificacionService = enviarNotificacionService;
        this.busquedaExpedienteService = busquedaExpedienteService;
        this.alfrescoService = alfrescoService;
        this.trazaRepository = trazaRepository;
        this.tipoNotificacionRepository = tipoNotificacionRepository;
        this.plantillaFirmaElectronicaRepository = plantillaFirmaElectronicaRepository;
        this.usuarioRepresentanteCompaniaRepository = usuarioRepresentanteCompaniaRepository;
        this.usuarioRepository = usuarioRepository;
        this.expedienteRepository = expedienteRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.firmaElectronicaRepository = firmaElectronicaRepository;
        this.parametroRepository=parametroRepository;
        this.hcMovimientoTransaccionRepository=hcMovimientoTransaccionRepository;
    }

    @Override
    public Map<String, Object> createContrato(Integer idExpediente, Integer idUsuario, String idioma, Integer idRepresentante, String tipoFirma, boolean registrarMovimientoTransaccion) {
        Usuario usuario = usuarioRepository.findById(idUsuario);
        return createContrato(idExpediente, usuario, idioma, idRepresentante, tipoFirma, registrarMovimientoTransaccion);
    }

    @Override
    public Map<String, Object> createContrato(Integer idExpediente, Usuario usuario, String idioma, Integer idRepresentante, String tipoFirma, boolean registrarMovimientoTransaccion) {

        Expediente expediente = commonBusinessLogicService.obtenerExpediente(idExpediente);

        HcDocumentoLegal documentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegalPorExpediente(idExpediente);

        List<Cliente> clientes = hcRepresentantePorDocumentoRepository.obtenerRepresentantesDocumentoLegal(documentoLegal.getId());

        List<Documento> documentos = busquedaExpedienteService.buscarPorExpediente(idExpediente);

        CreateContratoReqBean createContratoReqBean = new CreateContratoReqBean();

        createContratoReqBean.setTitle(documentoLegal.getNumero() + ": " + documentoLegal.getSumilla());

        createContratoReqBean.setUserEmailNotifications(firmaElectronicaConfig.getUserEmailNotifications());

        createContratoReqBean.setLanguage(idioma);

        createContratoReqBean.setReference(documentoLegal.getNumero());

        List<UserReqBean> users = new ArrayList<>();

        List<DocumentReqBean> documents = new ArrayList<>();

        HocDocInfoReqBean hocDocInfo = new HocDocInfoReqBean();

        hocDocInfo.setIdDocumentoLegal(documentoLegal.getId());
        hocDocInfo.setIdResponsable(usuario.getId());
        hocDocInfo.setDocNumber(documentoLegal.getNumero());
        hocDocInfo.setChiefLawyer(documentoLegal.getResponsable().getNombreCompleto());
        hocDocInfo.setDocAbstract(documentoLegal.getSumilla());

        String mensajeError = "";

        Integer idTDContrato = Integer.parseInt(parametroService.buscarParametroPorTipo(Constantes.PARAMETRO_ID_CONTRATO).get(0).getValor());
        Integer idTDAdenda = Integer.parseInt(parametroService.buscarParametroPorTipo(Constantes.PARAMETRO_ID_ADENDA).get(0).getValor());

        //Recuperamos y validamos los documentos
        for (Documento documento : documentos) {
            if (documento.getTipoDocumento().getId().equals(idTDContrato) || documento.getTipoDocumento().getId().equals(idTDAdenda)) {
                hocDocInfo.setDocType(documento.getTipoDocumento().getNombre());
                List<Archivo> archivos = busquedaExpedienteService.obtenerArchivosxDocumento(documento);

                for (Archivo archivo : archivos) {
                    if (archivo.getEstado().equals(Constantes.ESTADO_ACTIVO)) {
                        DocumentReqBean document = new DocumentReqBean();
                        document.setName(archivo.getNombre());
                        Map<String, Object> file = alfrescoService.obtenerArchivo(archivo, null);
                        if (file != null) {
                            String contentType = (String) file.get("mime");
                            InputStream in = (InputStream) file.get("stream");
                            if (("application/pdf").equals(contentType)) {
                                try {
                                    byte[] bytes = IOUtils.toByteArray(in);
                                    document.setBase64(Base64.encodeBase64String(bytes));
                                    documents.add(document);
                                } catch (IOException ex) {
                                    LOGGER.info(ex.getMessage(), ex);
                                    mensajeError += "<li>El archivo " + document.getName() + " no se pudo recuperar del repositorio.</li>";
                                }
                            }
                        }
                    }
                }

                break;
            }
        }

        //Verificamos si se encontraron archivos pdf
        if (documents.isEmpty()) {
            mensajeError += "<li>No se encontraron archivos pdf.</li>";
        }

        boolean hasClienteDni = false;
        boolean hasClienteCe = false;

        //Recuperamos y validamos los clientes
        for (Cliente cliente : clientes) {

            UserReqBean user = new UserReqBean();

            user.setName(cliente.getNombre() + " " + cliente.getApellidoPaterno() + " " + cliente.getApellidoMaterno());
            user.setEmail(cliente.getCorreo());
            user.setIdCliente(cliente.getId());
            user.setIdTipoRepresentante(cliente.getTipo().getId());
            user.setApellidoPaterno(cliente.getApellidoPaterno());
            user.setApellidoMaterno(cliente.getApellidoMaterno());
            user.setNombre(cliente.getNombre());
            user.setNumeroIdentificacion(cliente.getNumeroIdentificacion());

            if (cliente.getTipo()
                    .getCodigo().equals(Constantes.TIPO_CLIENTE_NATURAL)) {
                user.setGroups(Arrays.asList(new String[]{Constantes.DNI_SIGNERS_USER_GROUP}));
                if (!hasClienteDni) {
                    hasClienteDni = true;
                }

            } else if (cliente.getTipo()
                    .getCodigo().equals(Constantes.TIPO_CLIENTE_EXTRANJERO)) {
                user.setGroups(Arrays.asList(new String[]{Constantes.CE_SIGNERS_USER_GROUP}));
                if (!hasClienteCe) {
                    hasClienteCe = true;
                }
            }

            users.add(user);

            if (cliente.getCorreo()
                    == null || cliente.getCorreo().trim().isEmpty()) {
                mensajeError += "<li>El representante legal " + user.getName() + " no tiene correo.</li>";
            }

            if (user.getName()
                    == null || user.getName().trim().isEmpty()) {
                mensajeError += "<li>El representante legal identificado con ID = " + user.getIdCliente() + " no tiene nombre registrado.</li>";
            }
        }

        //Evaluamos que plantilla usar
        if (tipoFirma.equals(Constantes.TIPO_FIRMA_ELECTRONICA_VIDEO_FIRMA)) {

            Parametro parametroVideoFirma = parametroService
                    .obtenerPorTipoValor(
                            Constantes.TIPO_PARAMETRO_FIRMA_ELECTRONICA,
                            Constantes.TIPO_FIRMA_ELECTRONICA_VIDEO_FIRMA);

            if (hasClienteDni && hasClienteCe) {
                createContratoReqBean.setTemplateId(plantillaFirmaElectronicaRepository.obtenerPorTipoFirmaYTipoCliente(
                        parametroVideoFirma.getId(),
                        Constantes.TIPO_CLIENTE_FIRMA_ELECTRONICA_DNI_CE).getPlantilla()
                );
            } else {
                for (UserReqBean user : users) {
                    user.setGroups(Arrays.asList(new String[]{Constantes.SIGNERS_USER_GROUP}));
                }
                if (hasClienteDni) {
                    createContratoReqBean.setTemplateId(plantillaFirmaElectronicaRepository.obtenerPorTipoFirmaYTipoCliente(
                            parametroVideoFirma.getId(),
                            Constantes.TIPO_CLIENTE_FIRMA_ELECTRONICA_DNI).getPlantilla()
                    );
                } else if (hasClienteCe) {
                    createContratoReqBean.setTemplateId(plantillaFirmaElectronicaRepository.obtenerPorTipoFirmaYTipoCliente(
                            parametroVideoFirma.getId(),
                            Constantes.TIPO_CLIENTE_FIRMA_ELECTRONICA_CE).getPlantilla());
                }
            }
        } else if (tipoFirma.equals(Constantes.TIPO_FIRMA_ELECTRONICA_FIRMA_DIBUJADA)) {
            for (UserReqBean user : users) {
                user.setGroups(Arrays.asList(new String[]{Constantes.SIGNERS_USER_GROUP}));
            }
            Parametro parametroFirmaDibujada = parametroService
                    .obtenerPorTipoValor(
                            Constantes.TIPO_PARAMETRO_FIRMA_ELECTRONICA,
                            Constantes.TIPO_FIRMA_ELECTRONICA_FIRMA_DIBUJADA);

            createContratoReqBean.setTemplateId(plantillaFirmaElectronicaRepository.obtenerPorTipoFirmaYTipoCliente(
                    parametroFirmaDibujada.getId(),
                    Constantes.TIPO_CLIENTE_FIRMA_ELECTRONICA_DNI_CE).getPlantilla());
        }

        List<RepresentanteComp> representantesCompanhia = usuarioRepresentanteCompaniaRepository.getList(Constantes.ESTADO_REPRESENTANTE_COMPANHIA_HABILITADO);

        //Recuperamos y validamos los respresentantes de HOC
        for (RepresentanteComp representanteCompanhia : representantesCompanhia) {
            UserReqBean user = new UserReqBean();
            user.setName(representanteCompanhia.getRepresentante().getNombres() + " " + representanteCompanhia.getRepresentante().getApellidos());
            user.setEmail(representanteCompanhia.getCorreo());
            user.setGroups(Arrays.asList(new String[]{Constantes.NOTIFIERS_USER_GROUP}));
            if (representanteCompanhia.getRepresentante().getId().equals(idRepresentante)) {
                LOGGER.info("ENTROOOOOOOOO REP COMP");
                List<PrefilledItemReqBean> prefilledItems = new ArrayList<>();

                PrefilledItemReqBean prefilledItemReqBeanDni = new PrefilledItemReqBean();
                prefilledItemReqBeanDni.setTarget("dni-notifier");
                Map<String, Object> valueDni = new HashMap<>();
                valueDni.put("text", representanteCompanhia.getNroDocumento());
                prefilledItemReqBeanDni.setValue(valueDni);

                PrefilledItemReqBean prefilledItemReqBeanDate = new PrefilledItemReqBean();
                prefilledItemReqBeanDate.setTarget("date-notifier");
                Map<String, Object> valueDate = new HashMap<>();
                valueDate.put("text", DateUtil.convertDateToString(new Date(), DateUtil.FORMAT_DATE_BD));
                prefilledItemReqBeanDate.setValue(valueDate);

                prefilledItems.add(prefilledItemReqBeanDni);
                prefilledItems.add(prefilledItemReqBeanDate);

                user.setPrefilledItems(prefilledItems);
            }
            users.add(user);
            if (representanteCompanhia.getCorreo() == null || ("").equals(representanteCompanhia.getCorreo())) {
                mensajeError += "<li>El representante de la compañía " + user.getName() + " no tiene correo.</li>";
            }
        }

        createContratoReqBean.setHocDocInfo(hocDocInfo);
        createContratoReqBean.setUsers(users);
        createContratoReqBean.setDocuments(documents);

        Parametro parametroFrequency = parametroService.buscarPorId(Constantes.ID_PARAMETRO_FIRMA_ELECTRONICA_FREQUENCY);
        Parametro parametroMaxAttempts = parametroService.buscarPorId(Constantes.ID_PARAMETRO_FIRMA_ELECTRONICA_MAX_ATTEMPTS);

        FlagsBean flags = new FlagsBean();

        RemindersDataBean remindersDataBean = new RemindersDataBean();

        remindersDataBean.setFrequency(Integer.valueOf(parametroFrequency.getValor()));
        remindersDataBean.setMaxAttempts(Integer.valueOf(parametroMaxAttempts.getValor()));

        flags.setRemindersData(remindersDataBean);

        createContratoReqBean.setFlags(flags);

        if (!("").equals(mensajeError)) {
            Map<String, Object> result = new HashMap<>();
            result.put("resultado", "error");
            result.put("mensaje", "No se pudo enviar a firma electrónica. Se presentaron los siguientes errores:<br><br>" + "<ul>" + mensajeError + "</ul>");
            return result;
        } else {
            return createContratoWs(createContratoReqBean, expediente, documentoLegal, usuario, registrarMovimientoTransaccion);
        }
    }

    @Override
    public Map<String, Object> resendContrato(Integer idExpediente, Integer idUsuario, String idioma, Integer idRepresentante, String tipoFirma) {

        Map<String, Object> result;

        Usuario usuario = usuarioRepository.findById(idUsuario);

        Expediente expediente = commonBusinessLogicService.obtenerExpediente(idExpediente);

        HcDocumentoLegal documentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegalPorExpediente(idExpediente);

        HcFirmaElectronicaDocLegal hcFirmaElectronicaDocLegal = firmaElectronicaRepository.obtenerPorIdDocumentoLegal(documentoLegal.getId());

        if (hcFirmaElectronicaDocLegal == null) {
            return createContrato(idExpediente, idUsuario, idioma, idRepresentante, tipoFirma, true);
        } else {
            result = deleteFirmaElectronica(expediente, documentoLegal, hcFirmaElectronicaDocLegal, usuario, true, false);
            if (result.get("resultado").equals("exito")) {
                result = createContrato(idExpediente, idUsuario, idioma, idRepresentante, tipoFirma, false);
            }
            return result;
        }
    }

    @Override
    public Map<String, Object> getContratoDetail(Integer idExpediente) {

        HcDocumentoLegal documentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegalPorExpediente(idExpediente);

        HcFirmaElectronicaDocLegal hcFirmaElectronicaDocLegal = firmaElectronicaRepository.obtenerPorIdDocumentoLegal(documentoLegal.getId());

        if (hcFirmaElectronicaDocLegal == null) {

            Map<String, Object> result = new HashMap<>();
            result.put("serverCode", 404);

            return result;

        } else {
            return contratoClient.get(hcFirmaElectronicaDocLegal.getIdReferenciaIdKeynua());
        }
    }

    @Override
    public Map<String, Object> reenviarNotificacion(Integer idExpediente, HttpServletRequest request) {

        Map<String, Object> result = new HashMap<>();
        HcDocumentoLegal documentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegalPorExpediente(idExpediente);
        TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getReenvioFirmaElectronica());
        Traza ultimaTraza = trazaRepository.obtenerUltimaTrazaPorExpediente(idExpediente);

        if (ultimaTraza != null) {

            HcFirmaElectronicaDocLegal hcFirmaElectronicaDocLegal = firmaElectronicaRepository.obtenerPorIdDocumentoLegal(documentoLegal.getId());

            LOGGER.info("id hcFirmaElectronicaDocLegal:" + hcFirmaElectronicaDocLegal.getId());

            List<HcFirmaElectronicaRepresentante> destinatarios = firmaElectronicaRepository.obtenerPorIdFirmaElectronica(hcFirmaElectronicaDocLegal.getId());

            LOGGER.info("destinatarios size:" + destinatarios.size());

            Object[] arguments = {StringUtils.trimToEmpty(documentoLegal.getNumero())};

            Map<String, String> hocIsotipo = new HashMap<>();

            hocIsotipo.put("cid", Constantes.CID_ISOTIPO_HOC);

            hocIsotipo.put("path", request.getSession().getServletContext().getRealPath(Constantes.PATH_IMAGES) + Constantes.PATH_ISOTIPO_HOC);

            List<Map<String, String>> imagenesInline = new ArrayList<>();

            imagenesInline.add(hocIsotipo);

            enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, ultimaTraza, arguments, imagenesInline);

            result.put("resultado", "exito");
            result.put("mensaje", "Se reenvió la notificación exitosamente");
        } else {
            result.put("resultado", "error");
            result.put("mensaje", "Ocurrió un error al enviar las notificaciones");
        }
        return result;
    }

    private Map<String, Object> createContratoWs(CreateContratoReqBean createContratoReqBean, Expediente expediente, HcDocumentoLegal documentoLegal, Usuario usuario, boolean registrarMovimientoTransaccion) {

        Map<String, Object> result = new HashMap<>();

        LOGGER.info("ingreso a createContratoWs");

        int serverCode = contratoClient.create(createContratoReqBean);

        LOGGER.error("Resultado de crear contrato en Keynua:");
        LOGGER.error(String.valueOf(serverCode));

        switch (serverCode) {
            case 200:
                result.put("resultado", "exito");
                result.put("mensaje", "Se envió a firma exitosamente.");

                expediente.setFirmaElectronica(Constantes.FLAG_EXPEDIENTE_FIRMA_ELECTRONICA);

                //Actualizamos el expediente
                expedienteRepository.save(expediente);

                LOGGER.info("Estado del doc:");
                LOGGER.info(String.valueOf(documentoLegal.getEstado()));

                documentoLegal.setEstado(Constantes.ESTADO_HC_ENVIADO_FIRMA);

                //Actualizamos el documento legal
                hcDocumentoLegalRepository.save(documentoLegal);

                Map<String, Object> mapTrazaGenerada = commonBusinessLogicService.generarTraza(expediente.getId(), usuario, documentoLegal.getResponsable(), Constantes.OBS_HC_ENVIADO_FIRMA_ELECTRONICA, Constantes.ACCION_ENVIADO_FIRMA_ELECTRONICA);

                //Notificar por correo
                if (mapTrazaGenerada != null) {

                    if(registrarMovimientoTransaccion){
                        HcMovimientoTransaccion movimiento = new HcMovimientoTransaccion();
                        movimiento.setCodigoRegistro(documentoLegal.getId());
                        movimiento.setTipoMovimiento(parametroRepository.findById(Constantes.ID_TIPO_MOVIMIENTO_TX_INVENTARIO_SALIDA));
                        movimiento.setTipoRegistro(parametroRepository.findById(Constantes.ID_TIPO_REGISTRO_TX_SALIDA));
                        movimiento.setCantidad(1);
                        movimiento.setFechaMovimiento(new Date());
                        movimiento.setUsuarioCreacion(usuario.getId());
                        movimiento.setFechaCreacion(new Date());

                        hcMovimientoTransaccionRepository.save(movimiento);
                    }

                    TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getEnvioFirma());
                    List<Usuario> destinatarios = new ArrayList<>();
                    destinatarios.add(documentoLegal.getResponsable());
                    if (!documentoLegal.getResponsable().getId().equals(documentoLegal.getSolicitante().getId())) {
                        destinatarios.add(documentoLegal.getSolicitante());
                    }
                    enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, (Traza) mapTrazaGenerada.get(Constantes.MAP_KEY_TRAZA));
                }
                break;

            case 401:
                result.put("resultado", "error");
                result.put("mensaje", "Petición no autorizada.");
                break;
            case -1:
                result.put("resultado", "error");
                result.put("mensaje", "Ocurrió un error de comunicación con el servicio de firma electronica.");
                break;
            default:
                result.put("resultado", "error");
                result.put("mensaje", "Ocurrió un error de interno del lado del servicio de firma electrónica.");
        }
        return result;
    }


    @Override
    public Map<String, Object> deleteFirmaElectronica(Expediente expediente, HcDocumentoLegal documentoLegal, HcFirmaElectronicaDocLegal hcFirmaElectronicaDocLegal, Usuario usuario, boolean actualizarEstadoHcDocumentoLegal, boolean registrarMovimientoTransaccion) {

        Map<String, Object> result = new HashMap<>();

        try {
            result = deleteFirmaElectronicaWs(hcFirmaElectronicaDocLegal.getIdReferenciaIdKeynua());

            if (result.get("resultado").equals("exito")) {
                LOGGER.info("ENTROOOOOOOOOOOOO A ELIMINAR FIRMA WS:");

                if(registrarMovimientoTransaccion){
                    HcMovimientoTransaccion movimiento = new HcMovimientoTransaccion();
                    movimiento.setCodigoRegistro(documentoLegal.getId());
                    movimiento.setTipoMovimiento(parametroRepository.findById(Constantes.ID_TIPO_MOVIMIENTO_TX_INVENTARIO_ANULACION));
                    movimiento.setTipoRegistro(parametroRepository.findById(Constantes.ID_TIPO_REGISTRO_TX_INGRESO));
                    movimiento.setCantidad(1);
                    movimiento.setFechaMovimiento(new Date());
                    movimiento.setUsuarioCreacion(usuario.getId());
                    movimiento.setFechaCreacion(new Date());

                    hcMovimientoTransaccionRepository.save(movimiento);
                }

                result.put("mensaje", "Se eliminó exitosamente la firma electrónica.");

                expediente.setFirmaElectronica(Constantes.FLAG_EXPEDIENTE_SIN_FIRMA_ELECTRONICA);

                //Actualizamos el expediente
                expedienteRepository.save(expediente);

                if(actualizarEstadoHcDocumentoLegal){
                    documentoLegal.setEstado(Constantes.ESTADO_HC_ENVIADO_FIRMA);
                    //Actualizamos el documento legal
                    hcDocumentoLegalRepository.save(documentoLegal);
                }

                commonBusinessLogicService.generarTraza(expediente.getId(), usuario, documentoLegal.getResponsable(), Constantes.OBS_HC_ANULACION_FIRMA_ELECTRONICA, Constantes.ACCION_ENVIADO_FIRMA_ELECTRONICA);

                firmaElectronicaRepository.eliminarHistorialPorIdHcFirmaElectronicaDocLegal(hcFirmaElectronicaDocLegal.getId());

                firmaElectronicaRepository.eliminarRepresentantePorIdHcFirmaElectronicaDocLegal(hcFirmaElectronicaDocLegal.getId());

                firmaElectronicaRepository.eliminarHcFirmaElectronicaDocLegalPorId(hcFirmaElectronicaDocLegal.getId());


            }


        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result.put("resultado", "error");
            result.put("mensaje", "Ocurrió un error al tratar de eliminar el envío de firma anterior.");
        }

        return result;
    }

    @Override
    public Map<String, Object> deleteFirmaElectronica(Integer idExpediente, Integer idUsuario, boolean actualizarEstadoHcDocumentoLegal, boolean registrarMovimientoTransaccion) {
        Usuario usuario = usuarioRepository.findById(idUsuario);

        Expediente expediente = commonBusinessLogicService.obtenerExpediente(idExpediente);

        HcDocumentoLegal documentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegalPorExpediente(idExpediente);

        HcFirmaElectronicaDocLegal hcFirmaElectronicaDocLegal = firmaElectronicaRepository.obtenerPorIdDocumentoLegal(documentoLegal.getId());

        return deleteFirmaElectronica(expediente, documentoLegal, hcFirmaElectronicaDocLegal, usuario, actualizarEstadoHcDocumentoLegal, registrarMovimientoTransaccion);
    }

    private Map<String, Object> deleteFirmaElectronicaWs(String idReferenciaKeynua) {

        Map<String, Object> result = new HashMap<>();

        int serverCode = contratoClient.delete(idReferenciaKeynua);

        LOGGER.info("Resultado de eliminar contrato en Keynua:");
        LOGGER.info(String.valueOf(serverCode));

        switch (serverCode) {
            case 200:
            case 400:
                result.put("resultado", "exito");
                result.put("mensaje", "Se eliminó la firma electronica de Keynua.");
                break;
            case 401:
                result.put("resultado", "error");
                result.put("mensaje", "Petición no autorizada.");
                break;
            case -1:
                result.put("resultado", "error");
                result.put("mensaje", "Ocurrió un error de comunicación con el servicio de firma electrónica.");
                break;
            default:
                result.put("resultado", "error");
                result.put("mensaje", "Ocurrió un error de interno del lado del servicio de firma electrónica.");
        }
        return result;
    }

    @Override
    public List<Usuario> getListaRepresentantes(String estado){
        return usuarioRepresentanteCompaniaRepository.getListUsuariosRepresentantes(estado);
    }
    @Override
    public List<Parametro> getParametrosPorTipo(String parametro){
        return parametroService.buscarParametroPorTipo(parametro);
    }
}
