package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.bandejaEntrada.DetalleExpedienteBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.IntalioService;
import com.hochschild.speed.back.service.RevisarBuscarDocumentoService;
import com.hochschild.speed.back.service.RevisarExpedienteService;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RevisarBuscarDocumentoServiceImpl implements RevisarBuscarDocumentoService {
    private static final Logger LOGGER = Logger.getLogger(RevisarBuscarDocumentoServiceImpl.class.getName());
    private final RevisarExpedienteService revisarExpedienteService;
    private final BotonRepository botonRepository;
    private final TrazaRepository trazaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ExpedienteRepository expedienteRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final HcAdendaRepository hcAdendaRepository;
    private final ParametroRepository parametroRepository;
    private final PerfilRepository perfilRepository;
    private final UsuarioAccesoSolicitudRepository usuarioAccesoSolicitudRepository;
    private final IntalioService intalioService;

    @Autowired
    public RevisarBuscarDocumentoServiceImpl(RevisarExpedienteService revisarExpedienteService,
                                             BotonRepository botonRepository,
                                             TrazaRepository trazaRepository,
                                             UsuarioRepository usuarioRepository,
                                             RolRepository rolRepository,
                                             ExpedienteRepository expedienteRepository,
                                             HcDocumentoLegalRepository hcDocumentoLegalRepository,
                                             HcAdendaRepository hcAdendaRepository,
                                             ParametroRepository parametroRepository,
                                             PerfilRepository perfilRepository,
                                             UsuarioAccesoSolicitudRepository usuarioAccesoSolicitudRepository,
                                             IntalioService intalioService) {
        this.revisarExpedienteService = revisarExpedienteService;
        this.botonRepository = botonRepository;
        this.trazaRepository = trazaRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.expedienteRepository = expedienteRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.hcAdendaRepository = hcAdendaRepository;
        this.parametroRepository = parametroRepository;
        this.perfilRepository = perfilRepository;
        this.usuarioAccesoSolicitudRepository = usuarioAccesoSolicitudRepository;
        this.intalioService = intalioService;
    }

    @Override
    public DetalleExpedienteBean obtenerDetalleContrato(Integer idExpediente, Integer idUsuario, Integer idPerfil) {

        DetalleExpedienteBean detalleContratoBean = new DetalleExpedienteBean();

        try {

            LOGGER.info("Revisando detalle del expediente con ID [" + idExpediente + "]");
            Usuario usuario = usuarioRepository.findById(idUsuario);
            Traza traza = revisarExpedienteService.obtenerUltimaTraza(idExpediente, usuario.getId());
            Perfil perfil = perfilRepository.findById(idPerfil);
            Boolean trazaAntigua = false;
            Expediente expediente = traza.getExpediente();
            LOGGER.info("Estado del expdiente [" + expediente.getEstado() + "]");

            if (!expediente.getEstado().equals(Constantes.ESTADO_GUARDADO)) {

                Traza trazaActual = revisarExpedienteService.obtenerUltimaTraza(expediente.getId(), usuario.getId());
                detalleContratoBean.setIdTraza(traza.getId());

                if (trazaActual.getId().intValue() != traza.getId().intValue()) {
                    trazaAntigua = true;
                }

                if (revisarExpedienteService.usuarioPuedeVerExpedienteConfidencial(usuario, expediente)) {
                    expediente.setDocumentos(revisarExpedienteService.obtenerDocumentos(expediente));
                    /**
                     * Cuando el usuario tiene perfil de Mesa de Partes, mostramos
                     * el cargo
                     *
                     */
                    if (perfil != null && perfil.getCodigo().equals(Constantes.PERFIL_MESA_DE_PARTES)) {
                        List<Documento> documentos = expediente.getDocumentos();
                        List<List<CampoPorDocumento>> totalCampos = new ArrayList<List<CampoPorDocumento>>();
                        for (Documento documento : documentos) {
                            totalCampos.add(revisarExpedienteService.obtenerCamposPorDocumento(documento));
                        }
                        detalleContratoBean.setCampos(totalCampos);
                    }

                    Proceso proceso = expediente.getProceso();

                    LOGGER.info("Proceso del expediente: [" + proceso.getLabel() + "]");
                    if (proceso.getTipoProceso() != null && proceso.getTipoProceso().getAlerta().equals(Constantes.TIPO_PROCESO_ALERTA_PROCESO)) {
                        detalleContratoBean.setFechaLimite(revisarExpedienteService.obtenerFechaLimite(expediente, proceso.getPlazo()));
                    } else {
                        detalleContratoBean.setFechaLimite(traza.getFechaLimite());
                    }

                    detalleContratoBean.setExpediente(expediente);
                    detalleContratoBean.setTraza(traza);

                    Character estado = expediente.getEstado();
                    Integer tipoProcesoAqui = expediente.getProceso().getId();
                    LOGGER.info("Estado del expediente: [" + estado + "]");


                    /**
                     * Cuando se quiere ver el Expediente Registrado *
                     */

                    if (expediente.getProceso().getTipoProceso().getCodigo().equals(Constantes.TIPO_PROCESO_INTALIO)) {
                        String url = intalioService.getAjaxFormURL(usuario, expediente.getId());
                        if (url != null) {
                            detalleContratoBean.setUrl(url);
                        }
                    }

                    // Si el usuario es responsable en la traza
                    UsuarioPorTraza ut = revisarExpedienteService.obtenerUsuarioPorTraza(traza.getId(), usuario.getId());
                    if (ut == null) {
                        LOGGER.info("El usuario no posee la traza, verificamos si el usuario esta reemplazando a otro usuario");
                        Reemplazo reemplazo = revisarExpedienteService.buscarReemplazo(usuario.getId(), expediente.getProceso());
                        if (reemplazo != null) {
                            LOGGER.info("El usuario reemplaza al usuario que posee la traza!");
                            ut = revisarExpedienteService.obtenerUsuarioPorTraza(traza.getId(), reemplazo.getReemplazado().getId());
                        }
                    }
                    if (ut != null && !trazaAntigua) {
                        LOGGER.info("El usuario posee la traza y es la ultima traza");
                        Boolean multiple = traza.getPadre() != null;
                        LOGGER.info("Derivacion multiple ? -> " + multiple);
                        // Si es una traza proveniente de una derivacion
                        // multiple, no se bloquea a los demas usuarios
                        if (!multiple && !ut.isBloqueado()) {
                            revisarExpedienteService.bloquearExpediente(ut);
                        }

                        // Cambiar estado leido
                        revisarExpedienteService.cambiarEstadoLeido(ut);

                        List<Boton> botones;
                        if (expediente.getEstado().equals(Constantes.ESTADO_ARCHIVADO)) {
                            botones = revisarExpedienteService.obtenerBotones(expediente, perfil, ut.isResponsable(), estado);
                        } else if (expediente.getEstado().equals(Constantes.ESTADO_REGISTRADO)) {
                            if (multiple) {
                                botones = revisarExpedienteService.obtenerBotones(expediente, perfil, ut.getEstado());
                            } else if (expediente.getDocumentoLegal() != null) {
                                /*XXX Es un expediente de Contrato/Adenda, se deben obtener los botones dependiendo del estado del documento
                                 *  y del rol que esta viendo el expediente.
                                 */
                                if (expediente.getDocumentoLegal().getContrato() != null) {
                                    botones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, ut.isResponsable(), expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_ADENDA);
                                    LOGGER.info("-------------CASO CONTRATO-------------");
                                    LOGGER.info(botones);
                                    LOGGER.info("---------------------------");
                                } else if (expediente.getDocumentoLegal().getAdenda() != null) {
                                    botones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, ut.isResponsable(), expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_CONTRATO);
                                    LOGGER.info("-------------CASO ADENDAS--------------");
                                    LOGGER.info(botones);
                                    LOGGER.info("---------------------------------------");
                                } else {
                                    botones = new ArrayList<Boton>();
                                }
                            } else {
                                botones = revisarExpedienteService.obtenerBotones(expediente, perfil, ut.isResponsable(), estado);
                                LOGGER.info("------------CASO SIN DOCUMENTO LEGAL--------------");
                                LOGGER.info(botones);
                                LOGGER.info("--------------------------------------------------");
                            }
                        } else {
                            botones = revisarExpedienteService.obtenerBotones(expediente, perfil, false, estado);
                            EnvioPendiente envio = revisarExpedienteService.obtenerEnvioPendiente(usuario, expediente);
                            detalleContratoBean.setEnvioPendiente(envio);
                            LOGGER.info("-------------CASO NO REGISTRADO--------------");
                            LOGGER.info(botones);
                            LOGGER.info("---------------------------------------------");
                        }

                        // Cambiamos label derivar por aprobar
                        // no se debe cambiar a aprobar segun word adjuntado
                        if (traza.getAccion().equals(Constantes.ACION_PARA_APROBAR)) {
                            for (Boton boton : botones) {
                                if (boton.getParametro() != null && boton.getParametro().equals("Derivar Expediente")) {
                                    boton.setNombre("Aprobar");
                                }
                            }
                            detalleContratoBean.setMostrarParaAprobar(false);
                        } else {
                            detalleContratoBean.setMostrarParaAprobar(true);
                        }

                        if (botones != null) {
                            for (Boton boton : botones) {

                                LOGGER.info("Nombre boton :" + boton.getUrl());
                                LOGGER.info("Codigo boton :" + boton.getCodigo());
                                LOGGER.info("Id boton :" + boton.getId());

                                if (expediente.getPadre() != null) {
                                    if (boton.getVerParalela() != null && boton.getVerParalela()) {
                                        boton.setBloqueado(true);
                                    }
                                }
                                if (boton.getBloqueable() && ut.isBloqueado()) {
                                    boton.setBloqueado(true);
                                }
                            }
                        }

                        detalleContratoBean.setBotones(botones);

                        if (expediente.getDocumentoLegal() != null) {
                            detalleContratoBean.setEstadoDl(AppUtil.getNombreEstadoDL(expediente.getDocumentoLegal().getEstado()));
                        }

                        //RETORNA LA VISTA DE CONTRATO
                        LOGGER.info("RevisarExpedienteController/revisarTraza - Tipo de proceso :" + tipoProcesoAqui);

                        if (botones != null) {
                            if (tipoProcesoAqui == 5) {
                                agregarBotonFirmaElectronica(detalleContratoBean, botones, expediente.getDocumentoLegal().getEstado(), expediente.getFirmaElectronica());
                                Integer idDocumentoLegal = expediente.getDocumentoLegal().getId();

                                LOGGER.info("---------------------------------------------------------------------------------------");
                                LOGGER.info("IdDocumentoLegal + datosContrato/revisarAdenda");
                                LOGGER.info(idDocumentoLegal);
                                LOGGER.info("---------------------------------------------------------------------------------------");

                                HcAdenda hcAdenda = hcAdendaRepository.findById(idDocumentoLegal);
                                HcDocumentoLegal documentoLegal = detalleContratoBean.getDocumentoLegal();
                                documentoLegal.setAdenda(hcAdenda);

                                //Envio de datos del tipo de adenda
                                detalleContratoBean.setDocumentoLegal(documentoLegal);
                                quitarBotonSeguridad(usuario, detalleContratoBean, botones);
                            }
                            quitarBotonSeguridad(usuario, detalleContratoBean, botones);
                        }
                    }

                    if (trazaAntigua) {
                        List<Documento> documentos = expediente.getDocumentos();
                        List<List<CampoPorDocumento>> totalCampos = new ArrayList<List<CampoPorDocumento>>();
                        for (Documento documento : documentos) {
                            totalCampos.add(revisarExpedienteService.obtenerCamposPorDocumento(documento));
                        }
                        detalleContratoBean.setCampos(totalCampos);

                        List<Boton> listaBotones = null;

                        if (expediente.getDocumentoLegal() != null) {
                            /*Es un expediente de Contrato/Adenda, se deben obtener los botones dependiendo del estado del documento
                             *  y del rol que esta viendo el expediente.
                             */
                            if (expediente.getDocumentoLegal().getContrato() != null) {
                                LOGGER.info("---------------------------------------------------------------------------------------");
                                LOGGER.info("DOCUMENTO LEGAL => CONTRATO");
                                LOGGER.info("---------------------------------------------------------------------------------------");

                                listaBotones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, false, expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_ADENDA);
                            } else if (expediente.getDocumentoLegal().getAdenda() != null) {
                                LOGGER.info("---------------------------------------------------------------------------------------");
                                LOGGER.info("DOCUMENTO LEGAL => ADENDA");
                                LOGGER.info("---------------------------------------------------------------------------------------");

                                listaBotones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, false, expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_CONTRATO);
                            } else {
                                LOGGER.info("---------------------------------------------------------------------------------------");
                                LOGGER.info("DOCUMENTO LEGAL => NO EXISTE");
                                LOGGER.info("---------------------------------------------------------------------------------------");
                                listaBotones = new ArrayList<Boton>();
                            }
                        } else {
                            listaBotones = revisarExpedienteService.obtenerBotonesMostrarCargo(perfil);
                        }

                        detalleContratoBean.setBotones(listaBotones);

                        if (expediente.getDocumentoLegal() != null) {
                            detalleContratoBean.setEstadoDl(AppUtil.getNombreEstadoDL(expediente.getDocumentoLegal().getEstado()));
                        }

                        LOGGER.info("TIPO DE PROCESO B:" + tipoProcesoAqui);
                        quitarBotonSeguridad(usuario, detalleContratoBean, listaBotones);
                    }

                    List<Documento> documentos = expediente.getDocumentos();
                    List<List<CampoPorDocumento>> totalCampos = new ArrayList<List<CampoPorDocumento>>();

                    if (documentos != null) {
                        for (Documento documento : documentos) {
                            List<CampoPorDocumento> campoPorDocumentos = revisarExpedienteService.obtenerCamposPorDocumento(documento);
                            totalCampos.add(campoPorDocumentos);
                        }
                    }
                    detalleContratoBean.setCampos(totalCampos);

                    List<Boton> listaBotones = revisarExpedienteService.obtenerBotonesMostrarCargo(perfil);
                    detalleContratoBean.setBotones(listaBotones);

                    if (expediente.getDocumentoLegal() != null) {
                        detalleContratoBean.setEstadoDl(AppUtil.getNombreEstadoDL(expediente.getDocumentoLegal().getEstado()));
                    }
                    LOGGER.info("TIPO DE PROCESO C:" + tipoProcesoAqui);

                    if (listaBotones != null) {
                        quitarBotonSeguridad(usuario, detalleContratoBean, listaBotones);
                    }

                    //VALIDACIONES DE BOTONES NO RESPONSABLE DE TRAZA

                    if (expediente.getDocumentoLegal().getContrato() != null) {
                        LOGGER.info("---------------------------------------------------------------------------------------");
                        LOGGER.info("DOCUMENTO LEGAL => CONTRATO");
                        LOGGER.info("---------------------------------------------------------------------------------------");

                        listaBotones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, false, expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_ADENDA);
                    } else if (expediente.getDocumentoLegal().getAdenda() != null) {
                        LOGGER.info("---------------------------------------------------------------------------------------");
                        LOGGER.info("DOCUMENTO LEGAL => ADENDA");
                        LOGGER.info("---------------------------------------------------------------------------------------");

                        listaBotones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, false, expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_CONTRATO);

                    }

                    //VALIDACION DE BOTONES RESPONSABLE DE TRAZA
                    if (ut != null) {
                        if (expediente.getDocumentoLegal().getContrato() != null) {
                            listaBotones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, ut.isResponsable(), expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_ADENDA);
                            LOGGER.info("-------------CASO CONTRATO-------------");
                            LOGGER.info(listaBotones);
                            LOGGER.info("---------------------------");
                        } else if (expediente.getDocumentoLegal().getAdenda() != null) {
                            listaBotones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, ut.isResponsable(), expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_CONTRATO);
                            LOGGER.info("-------------CASO ADENDAS--------------");
                            LOGGER.info(listaBotones);
                            LOGGER.info("---------------------------------------");
                        }
                    }
                    quitarBotonSeguridad(usuario, detalleContratoBean, listaBotones);

                    detalleContratoBean.setBotones(listaBotones);

                    Expediente expedienteFormateado = detalleContratoBean.getExpediente();
                    HcDocumentoLegal hcDocumentoLegalFormateado = detalleContratoBean.getExpediente().getDocumentoLegal();
                    HcContrato contratoFormateado = detalleContratoBean.getExpediente().getDocumentoLegal().getContrato();
                    HcAdenda adendaFormateada = detalleContratoBean.getExpediente().getDocumentoLegal().getAdenda();
                    Traza trazaFormateada = detalleContratoBean.getTraza();
                    List<Documento> documentosFormateado = new ArrayList<>();


                    for (int i = 0; i < detalleContratoBean.getExpediente().getDocumentos().size(); i++) {

                        Documento documento = detalleContratoBean.getExpediente().getDocumentos().get(i);
                        documento.setExpediente(null);

                        for (Archivo files : documento.getArchivos()) {
                            for (Version version : files.getVersions()) {
                                version.setArchivo(null);
                            }
                            files.setDocumento(null);
                        }
                        documentosFormateado.add(documento);
                    }

                    detalleContratoBean.setExpediente(expedienteFormateado);
                    detalleContratoBean.setDocumentoLegal(hcDocumentoLegalFormateado);
                    detalleContratoBean.setContrato(contratoFormateado);
                    detalleContratoBean.setDocumentos(documentosFormateado);
                    detalleContratoBean.setTraza(trazaFormateada);

                    //Expediente limpio
                    expedienteFormateado.setDocumentoLegal(null);
                    expedienteFormateado.setDocumentos(null);

                    //DocumentoLegal limpio
                    hcDocumentoLegalFormateado.setExpediente(null);

                    //Contrato limpio
                    if (contratoFormateado != null) {
                        contratoFormateado.setDocumentoLegal(null);
                        hcDocumentoLegalFormateado.setContrato(contratoFormateado);
                    }

                    //Adenda limpio
                    if (adendaFormateada != null) {
                        HcContrato contratoAdendaFormateado = adendaFormateada.getContrato();
                        contratoAdendaFormateado.setDocumentoLegal(null);
                        adendaFormateada.setDocumentoLegal(null);
                        adendaFormateada.setContrato(contratoAdendaFormateado);
                        detalleContratoBean.setContrato(null);
                    }

                    //Traza limpio
                    trazaFormateada.setExpediente(null);

                }
            } else {

                Expediente expedienteFormateado = expedienteRepository.findById(idExpediente);
                HcDocumentoLegal hcDocumentoLegalFormateado = hcDocumentoLegalRepository.findByIdExpediente(expedienteFormateado.getId());
                HcContrato contratoFormateado = hcDocumentoLegalFormateado.getContrato();
                HcAdenda adendaFormateada = hcDocumentoLegalFormateado.getAdenda();
                Traza trazaFormateada = trazaRepository.obtenerUltimaTrazaPorExpediente(expedienteFormateado.getId());
                List<Documento> documentosFormateado = new ArrayList<>();

                Proceso proceso = expedienteFormateado.getProceso();

                if (proceso.getTipoProceso() != null && proceso.getTipoProceso().getAlerta().equals(Constantes.TIPO_PROCESO_ALERTA_PROCESO)) {
                    detalleContratoBean.setFechaLimite(revisarExpedienteService.obtenerFechaLimite(expedienteFormateado, proceso.getPlazo()));
                } else {
                    detalleContratoBean.setFechaLimite(null);
                }

                detalleContratoBean.setEstadoDl(AppUtil.getNombreEstadoDL(hcDocumentoLegalFormateado.getEstado()));

                for (int i = 0; i < expedienteFormateado.getDocumentos().size(); i++) {

                    Documento documento = expedienteFormateado.getDocumentos().get(i);
                    documento.setExpediente(null);

                    for (Archivo files : documento.getArchivos()) {
                        for (Version version : files.getVersions()) {
                            version.setArchivo(null);
                        }
                        files.setDocumento(null);
                    }
                    documentosFormateado.add(documento);
                }

                detalleContratoBean.setExpediente(expedienteFormateado);
                detalleContratoBean.setDocumentoLegal(hcDocumentoLegalFormateado);
                detalleContratoBean.setContrato(contratoFormateado);
                detalleContratoBean.setDocumentos(documentosFormateado);
                detalleContratoBean.setTraza(trazaFormateada);

                //Expediente limpio
                expedienteFormateado.setDocumentoLegal(null);
                expedienteFormateado.setDocumentos(null);

                //DocumentoLegal limpio
                hcDocumentoLegalFormateado.setExpediente(null);

                //Contrato limpio
                if (contratoFormateado != null) {
                    contratoFormateado.setDocumentoLegal(null);
                    hcDocumentoLegalFormateado.setContrato(contratoFormateado);
                }

                //Adenda limpio
                if (adendaFormateada != null) {
                    HcContrato contratoAdendaFormateado = adendaFormateada.getContrato();
                    contratoAdendaFormateado.setDocumentoLegal(null);
                    adendaFormateada.setDocumentoLegal(null);
                    adendaFormateada.setContrato(contratoAdendaFormateado);
                    detalleContratoBean.setContrato(null);
                }

                //Traza limpio
                trazaFormateada.setExpediente(null);
            }

        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
        }
        return detalleContratoBean;
    }

    @Override
    public DetalleExpedienteBean obtenerDetalleContrato2(Integer idExpediente, Integer idUsuario, Integer idPerfil) {

        DetalleExpedienteBean detalleExpedienteBean = new DetalleExpedienteBean();

        LOGGER.info("Revisando detalle del expediente con ID [" + idExpediente + "]");
        Usuario usuario = usuarioRepository.findById(idUsuario);
        Perfil perfil = perfilRepository.findById(idPerfil);
        Boolean trazaAntigua = false;
        Expediente expediente = expedienteRepository.findById(idExpediente);
        LOGGER.info("Estado del expdiente [" + expediente.getEstado() + "]");


            LOGGER.info("Controlador para ver detalle del expediente");

            if (usuario != null) {

                HcDocumentoLegal documentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(idExpediente);

                if (revisarExpedienteService.usuarioPuedeVerExpedienteConfidencial(usuario, expediente)) {
                    expediente.setDocumentos(revisarExpedienteService.obtenerDocumentos(expediente));
                    detalleExpedienteBean.setExpediente(expediente);

                    Traza traza = revisarExpedienteService.obtenerUltimaTraza(expediente.getId(), usuario.getId());
                    detalleExpedienteBean.setTraza(traza);

                    //Identificamos que tipo de proceso es
                    Integer tipoProcesoAqui = expediente.getProceso().getId();
                    Boolean tieneAccesoSolicitud = false;
                    Character estadoExpediente = expediente.getEstado();

                    /* Si el usuario es responsable en la traza */
                    UsuarioPorTraza ut = revisarExpedienteService.obtenerUsuarioPorTraza(traza.getId(), usuario.getId());
                    List<UsuarioAccesoSolicitud> representantesConAcceso = usuarioAccesoSolicitudRepository.getUsuariosConAcceso(Constantes.ESTADO_ACCESO_REPRESENTANTE_HABILITADO);

                    //Validación para buscar representante
                    for (int i = 0; i < representantesConAcceso.size(); i++) {
                        if (usuario.getId().equals(representantesConAcceso.get(i).getResponsable().getId())) {
                            LOGGER.debug("-----------------------------------RESULT--------------------------------------------");
                            LOGGER.debug("SE ENCONTRO UNA COINCIDENCIA DE USUARIO");
                            LOGGER.debug("USUARIO " + representantesConAcceso.get(i).getResponsable().getId());
                            tieneAccesoSolicitud = true;
                            LOGGER.debug("-------------------------------------------------------------------------------");
                            break;
                        }
                    }

                    if (documentoLegal.getEstado().equals(Constantes.ESTADO_HC_ENVIADO_VISADO)) {
                        tieneAccesoSolicitud = false;
                    }

                    if (ut == null) {
                        Reemplazo reemplazo = revisarExpedienteService.buscarReemplazo(usuario.getId(), expediente.getProceso());
                        if (reemplazo != null) {
                            ut = revisarExpedienteService.obtenerUsuarioPorTraza(traza.getId(), reemplazo.getReemplazado().getId());
                        }
                    }
                    if (perfil.getCodigo().equals(Constantes.PERFIL_MESA_DE_PARTES) && !expediente.getEstado().equals(Constantes.ESTADO_GUARDADO)) {
                        Traza primeraTraza = revisarExpedienteService.getPrimeraTraza(expediente);
                        List<Documento> documentos = expediente.getDocumentos();
                        List<List<CampoPorDocumento>> totalCampos = new ArrayList<List<CampoPorDocumento>>();
                        for (Documento documento : documentos) {
                            totalCampos.add(revisarExpedienteService.obtenerCamposPorDocumento(documento));
                        }

                        detalleExpedienteBean.setCampos(totalCampos);
                        detalleExpedienteBean.setObsTraza(primeraTraza.getObservacion());

                        //return
                        cleanResponse(detalleExpedienteBean);
                        return detalleExpedienteBean;

                    } // Cuando se quiere ver el Expediente Registrado
                    else if (expediente.getEstado().equals(Constantes.ESTADO_REGISTRADO)) {

                        if (ut != null || tieneAccesoSolicitud) {
                            boolean multiple = traza.getPadre() != null;
                            if (expediente.getProceso().getTipoProceso().getCodigo().equals(Constantes.TIPO_PROCESO_INTALIO)) {
                                String url = intalioService.getAjaxFormURL(usuario, expediente.getId());
                                if (url != null) {
                                    detalleExpedienteBean.setUrl(url);
                                }
                            }
                            if (!tieneAccesoSolicitud) {
                                if (!multiple && !ut.isBloqueado()) {
                                    revisarExpedienteService.bloquearExpediente(ut);
                                }
                                /* Cambiar estado leido */
                                revisarExpedienteService.cambiarEstadoLeido(ut);
                            }

                            List<Boton> listaBotones = null;

                            if (multiple) {
                                listaBotones = revisarExpedienteService.obtenerBotones(expediente, perfil, ut.getEstado());

                            } else if (traza.getEstado() != null && traza.getEstado().equals(Constantes.ESTADO_PAUSADO)) {
                                listaBotones = revisarExpedienteService.obtenerBotones(expediente, perfil, false, expediente.getEstado());
                            } else if (expediente.getDocumentoLegal() != null) {
                                if (expediente.getDocumentoLegal().getContrato() != null && !tieneAccesoSolicitud) {
                                    listaBotones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, ut.isResponsable(), expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_ADENDA);

                                } else if (expediente.getDocumentoLegal().getContrato() != null && tieneAccesoSolicitud) {
                                    listaBotones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, true, expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_ADENDA);

                                } else if (expediente.getDocumentoLegal().getAdenda() != null && !tieneAccesoSolicitud) {
                                    listaBotones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, ut.isResponsable(), expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_CONTRATO);

                                } else if (expediente.getDocumentoLegal().getAdenda() != null && tieneAccesoSolicitud) {
                                    listaBotones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, true, expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_CONTRATO);
                                } else {
                                    listaBotones = new ArrayList<Boton>();
                                }
                            } else {
                                if (ut != null) {
                                    listaBotones = revisarExpedienteService.obtenerBotones(expediente, perfil, ut.isResponsable(), expediente.getEstado());
                                }
                            }

                            if (listaBotones != null && !listaBotones.isEmpty()) {
                                for (Boton boton : listaBotones) {
                                    if (expediente.getPadre() != null) {
                                        if (boton.getVerParalela() != null && boton.getVerParalela()) {
                                            boton.setBloqueado(true);
                                        }
                                    }
                                    if (boton.getBloqueable()) {
                                        if (ut != null && ut.isBloqueado()) {
                                            LOGGER.info("bloquear el boton");
                                            boton.setBloqueado(true);
                                        }
                                    }
                                }
                                detalleExpedienteBean.setBotones(listaBotones);
                            }

                            if (expediente.getDocumentoLegal() != null) {
                                detalleExpedienteBean.setEstadoDl(AppUtil.getNombreEstadoDL(expediente.getDocumentoLegal().getEstado()));
                            }

                            if (tipoProcesoAqui == 5) {

                                agregarBotonFirmaElectronica(detalleExpedienteBean, listaBotones, expediente.getDocumentoLegal().getEstado(), expediente.getFirmaElectronica());

                                Integer idDocumentoLegal = expediente.getDocumentoLegal().getId();
                                HcAdenda hcAdenda = hcAdendaRepository.findById(idDocumentoLegal);
                                detalleExpedienteBean.setAdenda(hcAdenda);
                                quitarBotonSeguridad(usuario, detalleExpedienteBean, listaBotones);

                                //return
                                cleanResponse(detalleExpedienteBean);
                                return detalleExpedienteBean;
                            }
                            /* Solo aprobar si es el responsable */
                            quitarBotonSeguridad(usuario, detalleExpedienteBean, listaBotones);


                            // return
                            cleanResponse(detalleExpedienteBean);
                            return detalleExpedienteBean;
                        }
                        List<Documento> documentos = expediente.getDocumentos();
                        List<List<CampoPorDocumento>> totalCampos = new ArrayList<List<CampoPorDocumento>>();
                        for (Documento documento : documentos) {
                            totalCampos.add(revisarExpedienteService.obtenerCamposPorDocumento(documento));
                        }
                        detalleExpedienteBean.setCampos(totalCampos);

                        List<Boton> listaBotones = null;

                        if (expediente.getDocumentoLegal() != null) {
                            if (expediente.getDocumentoLegal().getContrato() != null) {
                                listaBotones = revisarExpedienteService.obtenerBotonesPorRolExceptoParametro(perfil, expediente, usuario, false, expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_ADENDA);
                            } else if (expediente.getDocumentoLegal().getAdenda() != null) {
                                listaBotones = revisarExpedienteService.obtenerBotonesPorRolExceptoParametro(perfil, expediente, usuario, false, expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_CONTRATO);
                            } else {
                                listaBotones = new ArrayList<Boton>();
                            }
                        } else {
                            listaBotones = revisarExpedienteService.obtenerBotonesMostrarCargo(perfil);
                        }

                        if (!verificarRol(this.revisarExpedienteService.buscarRolesPorUsuario(usuario.getId()), Constantes.CODIGO_ROL_ABOGADO_RESPONSABLE)) {
                            modificarBotones(listaBotones, Constantes.URL_ENVIAR_VB);
                        }
                        detalleExpedienteBean.setBotones(listaBotones);

                        if (expediente.getDocumentoLegal() != null) {
                            detalleExpedienteBean.setEstadoDl(AppUtil.getNombreEstadoDL(expediente.getDocumentoLegal().getEstado()));
                        }

                        if (tipoProcesoAqui == 5) {

                            agregarBotonFirmaElectronica(detalleExpedienteBean, listaBotones, expediente.getDocumentoLegal().getEstado(), expediente.getFirmaElectronica());

                            Integer idDocumentoLegal = expediente.getDocumentoLegal().getId();
                            HcAdenda hcAdenda = hcAdendaRepository.findById(idDocumentoLegal);
                            detalleExpedienteBean.setAdenda(hcAdenda);
                            quitarBotonSeguridad(usuario, detalleExpedienteBean, listaBotones);

                            //return
                            cleanResponse(detalleExpedienteBean);
                            return detalleExpedienteBean;
                        }

                        quitarBotonSeguridad(usuario, detalleExpedienteBean, listaBotones);

                        //return
                        cleanResponse(detalleExpedienteBean);
                        return detalleExpedienteBean;
                    } // Cuando se quiere modificar un Expediente Guardado
                    else if (expediente.getEstado().equals(Constantes.ESTADO_GUARDADO)) {
                        if (expediente.getDocumentoLegal() != null) {
                            //XXX
                            //Es una solicitud guardada, debe mostrarse el formulario para solicitudes
                            detalleExpedienteBean.setIdResponsable(expediente.getDocumentoLegal().getResponsable().getId());
                            detalleExpedienteBean.setNombreResponsable(expediente.getDocumentoLegal().getResponsable().getNombres() + " " + expediente.getDocumentoLegal().getResponsable().getApellidos());

                            detalleExpedienteBean.setProcesos(revisarExpedienteService.listarProcesos());
                            expediente.setDocumentoLegal(null);
                            detalleExpedienteBean.setExpediente(expediente);


                            //return
                            cleanResponse(detalleExpedienteBean);
                            return detalleExpedienteBean;
                        } else {
                            detalleExpedienteBean.setBotones(revisarExpedienteService.obtenerBotones(perfil));
                            List<Usuario> usuarios = revisarExpedienteService.devolverUsuariosResponsablesPorProceso(expediente.getProceso().getId());

                            String texto = "";
                            for (int i = 0; i < usuarios.size(); i++) {
                                if (i < usuarios.size() - 1) {
                                    texto += usuarios.get(i).getNombres() + " " + usuarios.get(i).getApellidos() + ", ";
                                } else {
                                    texto += usuarios.get(i).getNombres() + " " + usuarios.get(i).getApellidos();
                                }
                            }
                            boolean flagCliente = revisarExpedienteService.devolverFlagCliente(expediente.getProceso().getId());
                            detalleExpedienteBean.setTexto(texto);
                            detalleExpedienteBean.setFlag(true);
                            detalleExpedienteBean.setExpediente(expediente);
                            /* Obtengo el proceso */
                            if (expediente.getProceso().getTipoProceso().getCodigo().equals(Constantes.CODIGO_COMUNICACIONES_INTERNAS)) {
                                detalleExpedienteBean.setInternas(true);

                            } else {
                                detalleExpedienteBean.setInternas(false);
                            }

                            if (flagCliente) {
                                detalleExpedienteBean.setEditar(true);
                            }
                            detalleExpedienteBean.setObservacion(expediente.getObservacionMp());

                            /// return
                            cleanResponse(detalleExpedienteBean);
                            return detalleExpedienteBean;
                        }
                    } else if (expediente.getEstado().equals(Constantes.ESTADO_ARCHIVADO)) {
                        if (perfil.getCodigo().equals(Constantes.PERFIL_USUARIO_FINAL)) {
                            //XXX
                            List<Documento> documentos = expediente.getDocumentos();
                            List<List<CampoPorDocumento>> totalCampos = new ArrayList<List<CampoPorDocumento>>();
                            for (Documento documento : documentos) {
                                totalCampos.add(revisarExpedienteService.obtenerCamposPorDocumento(documento));
                            }
                            detalleExpedienteBean.setCampos(totalCampos);
                            List<Boton> botones = null;
                            if (ut != null) {
                                if (expediente.getDocumentoLegal() != null) {
                                    if (expediente.getDocumentoLegal().getContrato() != null) {
                                        botones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, ut.isResponsable(), expediente.getEstado(), Constantes.PARAMETRO_ADENDA);
                                    } else if (expediente.getDocumentoLegal().getAdenda() != null) {
                                        botones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, ut.isResponsable(), expediente.getEstado(), Constantes.PARAMETRO_CONTRATO);
                                    } else {
                                        botones = new ArrayList<Boton>();
                                    }
                                } else {
                                    botones = revisarExpedienteService.obtenerBotones(expediente, perfil, ut.isResponsable(), expediente.getEstado());
                                }
                            } else {
                                if (expediente.getDocumentoLegal() != null) {
                                    boolean esAbogado = revisarExpedienteService.usuarioEsAbogado(usuario);
                                    boolean responsable = esAbogado ? true : false;

                                    if (expediente.getDocumentoLegal().getContrato() != null) {
                                        botones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, responsable, expediente.getEstado(), Constantes.PARAMETRO_ADENDA);
                                    } else if (expediente.getDocumentoLegal().getAdenda() != null) {
                                        botones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, responsable, expediente.getEstado(), Constantes.PARAMETRO_CONTRATO);
                                    } else {
                                        botones = new ArrayList<Boton>();
                                    }
                                } else {
                                    botones = revisarExpedienteService.obtenerBotones(expediente, perfil, expediente.getEstado());
                                }
                            }

                            detalleExpedienteBean.setBotones(botones);
                            if (expediente.getDocumentoLegal() != null) {
                                detalleExpedienteBean.setEstadoDl(AppUtil.getNombreEstadoDL(expediente.getDocumentoLegal().getEstado()));
                            }

                            if (tipoProcesoAqui == 5) {
                                agregarBotonFirmaElectronica(detalleExpedienteBean, botones, expediente.getDocumentoLegal().getEstado(), expediente.getFirmaElectronica());
                                Integer idDocumentoLegal = expediente.getDocumentoLegal().getId();
                                HcAdenda hcAdenda = hcAdendaRepository.findById(idDocumentoLegal);
                                detalleExpedienteBean.setAdenda(hcAdenda);

                                quitarBotonSeguridad(usuario, detalleExpedienteBean, botones);

                                // return
                                cleanResponse(detalleExpedienteBean);
                                return detalleExpedienteBean;
                            }

                            quitarBotonSeguridad(usuario, detalleExpedienteBean, botones);

                            // return
                            cleanResponse(detalleExpedienteBean);
                            return detalleExpedienteBean;

                        }
                    } else {
                        /* Here */
                        if (perfil.getCodigo().equals(Constantes.PERFIL_USUARIO_FINAL)) {
                            List<Documento> documentos = expediente.getDocumentos();
                            List<List<CampoPorDocumento>> totalCampos = new ArrayList<List<CampoPorDocumento>>();
                            for (Documento documento : documentos) {
                                totalCampos.add(revisarExpedienteService.obtenerCamposPorDocumento(documento));
                            }
                            detalleExpedienteBean.setCampos(totalCampos);

                            List<Boton> listaBotones = revisarExpedienteService.obtenerBotones(expediente, perfil, false, expediente.getEstado());
                            detalleExpedienteBean.setBotones(listaBotones);
                            if (expediente.getDocumentoLegal() != null) {
                                detalleExpedienteBean.setEstadoDl(AppUtil.getNombreEstadoDL(expediente.getDocumentoLegal().getEstado()));
                            }
                            quitarBotonSeguridad(usuario, detalleExpedienteBean, listaBotones);
                            cleanResponse(detalleExpedienteBean);
                            return detalleExpedienteBean;
                            // return
                        }
                        cleanResponse(detalleExpedienteBean);
                        return detalleExpedienteBean;

                        //return invalido
                    }
                }

                cleanResponse(detalleExpedienteBean);
                return detalleExpedienteBean;
            }

        return detalleExpedienteBean;
    }

    private static void cleanResponse(DetalleExpedienteBean detalleExpedienteBean) {
        Expediente expedienteFormateado = detalleExpedienteBean.getExpediente();
        HcDocumentoLegal hcDocumentoLegalFormateado = detalleExpedienteBean.getExpediente().getDocumentoLegal();
        HcContrato contratoFormateado = detalleExpedienteBean.getExpediente().getDocumentoLegal().getContrato();
        HcAdenda adendaFormateada = detalleExpedienteBean.getExpediente().getDocumentoLegal().getAdenda();
        Traza trazaFormateada = detalleExpedienteBean.getTraza();
        List<Documento> documentosFormateado = new ArrayList<>();


        for (int i = 0; i < detalleExpedienteBean.getExpediente().getDocumentos().size(); i++) {

            Documento documento = detalleExpedienteBean.getExpediente().getDocumentos().get(i);
            documento.setExpediente(null);

            for (Archivo files : documento.getArchivos()) {
                for (Version version : files.getVersions()) {
                    version.setArchivo(null);
                }
                files.setDocumento(null);
            }
            documentosFormateado.add(documento);
        }

        detalleExpedienteBean.setExpediente(expedienteFormateado);
        detalleExpedienteBean.setDocumentoLegal(hcDocumentoLegalFormateado);
        detalleExpedienteBean.setContrato(contratoFormateado);
        detalleExpedienteBean.setDocumentos(documentosFormateado);
        detalleExpedienteBean.setTraza(trazaFormateada);
        detalleExpedienteBean.setObsTraza(trazaFormateada.getObservacion());
        //Expediente limpio
        expedienteFormateado.setDocumentoLegal(null);
        expedienteFormateado.setDocumentos(null);

        //DocumentoLegal limpio
        hcDocumentoLegalFormateado.setExpediente(null);

        //Contrato limpio
        if (contratoFormateado != null) {
            contratoFormateado.setDocumentoLegal(null);
            hcDocumentoLegalFormateado.setContrato(contratoFormateado);
        }

        //Adenda limpio
        if (adendaFormateada != null) {
            HcContrato contratoAdendaFormateado = adendaFormateada.getContrato();
            contratoAdendaFormateado.setDocumentoLegal(null);
            adendaFormateada.setDocumentoLegal(null);
            adendaFormateada.setContrato(contratoAdendaFormateado);
            detalleExpedienteBean.setContrato(null);
        }

        //Traza limpio
        trazaFormateada.setExpediente(null);
    }

    public void modificarBotones(List<Boton> listaBotones, String url) {
        for (Boton boton : listaBotones) {
            if (boton.getUrl().equals(url)) {
                boton.setTipo("btn-hide");
            }
        }
    }

    public boolean verificarRol(List<Rol> roles, String codigo) {
        if (roles == null) {
            return false;
        }
        for (Rol rol : roles) {
            if (rol.getCodigo().equals(codigo)) {
                return true;
            }
        }
        return false;
    }

    private void quitarBotonSeguridad(Usuario usuario, DetalleExpedienteBean detalleContratoBean, List<Boton> listaBotones) {

        List<Rol> rolesUsuario = rolRepository.buscarActivosPorUsuario(usuario.getId());
        List<Parametro> rolesSeguridad = parametroRepository.obtenerPorTipo(Constantes.TIPO_PARAMETRO_ROL_SEGURIDAD);
        boolean tieneRolSeguridad = false;
        outerloop:
        for (Rol rolUsuario : rolesUsuario) {
            LOGGER.info("ROL USUARIO nombre: " + rolUsuario.getNombre());
            LOGGER.info("ROL USUARIO SCA: " + rolUsuario.getCodigoSCA());
            for (Parametro rolSeguridad : rolesSeguridad) {
                LOGGER.info("ROL NOMBRE: " + rolSeguridad.getDescripcion());
                if (rolSeguridad.getDescripcion().equals(rolUsuario.getCodigoSCA())) {
                    tieneRolSeguridad = true;
                    break outerloop;
                }
            }
        }

        LOGGER.info("TIENE ROL REGURIDAD: " + tieneRolSeguridad);

        List<Boton> listaBotonesNew = new ArrayList<>();

        listaBotonesNew.addAll(listaBotones);

        if (!tieneRolSeguridad) {
            for (Boton boton : listaBotones) {
                if (boton.getParametro().equals(Constantes.SEGURIDAD_NOMBRE_PARAMETRO)) {
                    listaBotonesNew.remove(boton);
                }
            }
        }
        detalleContratoBean.setBotones(listaBotonesNew);
    }

    private void agregarBotonFirmaElectronica(DetalleExpedienteBean detalleContratoBean, List<Boton> listaBotones, Character estadoDocumentoLegal, String flagFirmaEletronica) {

        if (estadoDocumentoLegal.equals(Constantes.ESTADO_HC_ENVIADO_FIRMA) && flagFirmaEletronica.equals(Constantes.FLAG_EXPEDIENTE_SIN_FIRMA_ELECTRONICA)) {
            listaBotones.add(0, botonRepository.obtenerBotonPorUrl(Constantes.FIRMA_ELECTRONICA_BTN_URL));
            detalleContratoBean.setBotones(listaBotones);
        }

    }
}