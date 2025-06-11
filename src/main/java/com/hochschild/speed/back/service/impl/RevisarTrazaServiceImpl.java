package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.TabModelBean;
import com.hochschild.speed.back.model.bean.bandejaEntrada.DetalleExpedienteBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.response.FilterTipoComponent;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.IntalioService;
import com.hochschild.speed.back.service.RevisarExpedienteService;
import com.hochschild.speed.back.service.RevisarTrazaService;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RevisarTrazaServiceImpl implements RevisarTrazaService {
    private static final Logger LOGGER = Logger.getLogger(RevisarTrazaServiceImpl.class.getName());
    private final RevisarExpedienteService revisarExpedienteService;
    private final BotonRepository botonRepository;
    private final TrazaRepository trazaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final HcAdendaRepository hcAdendaRepository;
    private final ParametroRepository parametroRepository;
    private final ExpedienteRepository expedienteRepository;
    private final PerfilRepository perfilRepository;
    private final IntalioService intalioService;
    private final  CommonBusinessLogicService commonBusinessLogicService;
    private final UsuarioAccesoSolicitudRepository usuarioAccesoSolicitudRepository;

    @Autowired
    private HcDocumentoLegalRepository hcDocumentoLegalRepository;

    @Autowired
    public RevisarTrazaServiceImpl(RevisarExpedienteService revisarExpedienteService, BotonRepository botonRepository, TrazaRepository trazaRepository, UsuarioRepository usuarioRepository, RolRepository rolRepository, HcAdendaRepository hcAdendaRepository, ParametroRepository parametroRepository, ExpedienteRepository expedienteRepository, PerfilRepository perfilRepository, IntalioService intalioService, CommonBusinessLogicService commonBusinessLogicService, UsuarioAccesoSolicitudRepository usuarioAccesoSolicitudRepository) {
        this.revisarExpedienteService = revisarExpedienteService;
        this.botonRepository = botonRepository;
        this.trazaRepository = trazaRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.hcAdendaRepository = hcAdendaRepository;
        this.parametroRepository = parametroRepository;
        this.expedienteRepository = expedienteRepository;
        this.perfilRepository = perfilRepository;
        this.intalioService = intalioService;
        this.commonBusinessLogicService = commonBusinessLogicService;
        this.usuarioAccesoSolicitudRepository = usuarioAccesoSolicitudRepository;
    }

    @Override
    public ResponseEntity<FilterTipoComponent> filterExpedienteByStatus(Integer idExpediente, Integer idUsuario, Integer idPerfil){
        FilterTipoComponent filterTipoComponent = new FilterTipoComponent();
        try {
            Usuario usuario = usuarioRepository.findById(idUsuario);
            Perfil perfil = perfilRepository.findById(idPerfil);
            Expediente expediente = expedienteRepository.findExpedienteById(idExpediente);
            if (revisarExpedienteService.usuarioPuedeVerExpedienteConfidencial(usuario, expediente)) {

                if (perfil.getCodigo().equals(Constantes.PERFIL_MESA_DE_PARTES) && !expediente.getEstado().equals(Constantes.ESTADO_GUARDADO)) {
                    filterTipoComponent.setCodigo(Constantes.MOSTRAR_CARGO_EXP);
                    filterTipoComponent.setNombre("MOSTRAR CARGO EXPEDIENTE");
                    return new ResponseEntity<>(filterTipoComponent, HttpStatus.OK);

                } else if (expediente.getEstado().equals(Constantes.ESTADO_REGISTRADO)) {

                    filterTipoComponent.setNombre("Detalle de Expediente");
                    filterTipoComponent.setCodigo(Constantes.DETALLE_EXPEDIENTE);
                    return new ResponseEntity<>(filterTipoComponent, HttpStatus.OK);

                } else if (expediente.getEstado().equals(Constantes.ESTADO_GUARDADO)) {
                    if (expediente.getDocumentoLegal() != null) {
                        //XXX
                        //Es una solicitud guardada, debe mostrarse el formulario para solicitudes
                        filterTipoComponent.setNombre("Nueva Solicitud");
                        filterTipoComponent.setCodigo(Constantes.NUEVA_SOLICITUD);
                        return new ResponseEntity<>(filterTipoComponent, HttpStatus.OK);

                    } else {
                        /* Agregado para ver los documentos */
                        //return "registrarExpediente/nuevoExpediente";
                        filterTipoComponent.setNombre("Nueva Solicitud");
                        filterTipoComponent.setCodigo(Constantes.DETALLE_EXPEDIENTE);
                        return new ResponseEntity<>(filterTipoComponent, HttpStatus.OK);
                    }
                } else if (expediente.getEstado().equals(Constantes.ESTADO_ARCHIVADO)) {
                    if (perfil.getCodigo().equals(Constantes.PERFIL_USUARIO_FINAL)) {
                        //XXX
                        filterTipoComponent.setNombre("Nueva Solicitud");
                        filterTipoComponent.setCodigo(Constantes.DETALLE_EXPEDIENTE);
                        return new ResponseEntity<>(filterTipoComponent, HttpStatus.OK);
                    }
                } else {
                    if (perfil.getCodigo().equals(Constantes.PERFIL_USUARIO_FINAL)) {
                        filterTipoComponent.setNombre("Nueva Solicitud");
                        filterTipoComponent.setCodigo(Constantes.DETALLE_EXPEDIENTE);
                        return new ResponseEntity<>(filterTipoComponent, HttpStatus.OK);
                    }
                    filterTipoComponent.setNombre("Invalido");
                    filterTipoComponent.setCodigo(Constantes.EXPEDIENTE_CONFIDENCIAL_INVALIDO);
                    return new ResponseEntity<>(filterTipoComponent, HttpStatus.OK);
                }

            } else {
                filterTipoComponent.setNombre("Sin Permiso");
                filterTipoComponent.setCodigo(Constantes.EXPEDIENTE_CONFIDENCIAL_INVALIDO);
                return new ResponseEntity<>(filterTipoComponent, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex){
            filterTipoComponent.setNombre("Sin Permiso");
            filterTipoComponent.setCodigo(Constantes.EXPEDIENTE_CONFIDENCIAL_INVALIDO);
            return new ResponseEntity<>(filterTipoComponent, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        filterTipoComponent.setNombre("Confidencial");
        filterTipoComponent.setCodigo(Constantes.EXPEDIENTE_CONFIDENCIAL_INVALIDO);
        return new ResponseEntity<>(filterTipoComponent, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public DetalleExpedienteBean revisarExpedienteById(Integer idExpediente, Integer idUsuario, Integer idPerfil) {
        DetalleExpedienteBean detalleContratoBean = new DetalleExpedienteBean();
        try {
            Usuario usuario = usuarioRepository.findById(idUsuario);
            Perfil perfil = perfilRepository.findById(idPerfil);
            Expediente expediente = expedienteRepository.findExpedienteById(idExpediente);
            HcDocumentoLegal hcDocumentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegalPorExpediente(idExpediente);
            Traza traza = trazaRepository.obtenerUltimaTrazaPorExpediente(idExpediente);
            detalleContratoBean.setIdTraza(traza.getId());
            detalleContratoBean.setUsuario(usuario);
            detalleContratoBean.setExpediente(expediente);
            detalleContratoBean.setTraza(traza);
            if (revisarExpedienteService.usuarioPuedeVerExpedienteConfidencial(usuario, expediente)) {
                detalleContratoBean.setDocumentos(revisarExpedienteService.obtenerDocumentos(expediente));

                Integer tipoProcesoAqui = expediente.getProceso().getId();
                Boolean tieneAccesoSolicitud = false;
                Character estadoExpediente = expediente.getEstado();

                UsuarioPorTraza ut = revisarExpedienteService.obtenerUsuarioPorTraza(traza.getId(), usuario.getId());
                List<UsuarioAccesoSolicitud> representantesConAcceso = usuarioAccesoSolicitudRepository.getUsuariosConAcceso(Constantes.ESTADO_ACCESO_REPRESENTANTE_HABILITADO);
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

                if (hcDocumentoLegal.getEstado().equals(Constantes.ESTADO_HC_ENVIADO_VISADO)) {
                    tieneAccesoSolicitud = false;
                    LOGGER.debug("SE ENCUENTRA EN ESTADO ENVIADO_VISADO = " + tieneAccesoSolicitud);
                }

                if (ut == null) {
                    // Verificamos si el usuario esta reemplazando a otro
                    // usuario
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
                        // documento.setArchivos(revisarDocumentoService.obtenerArchivosxDocumento(documento,
                        // usuario));
                        totalCampos.add(revisarExpedienteService.obtenerCamposPorDocumento(documento));
                    }
                    detalleContratoBean.setCampos(totalCampos);
                    detalleContratoBean.setObservacion(primeraTraza.getObservacion());
                    cleanResponse(detalleContratoBean);
                    return detalleContratoBean;
                } else if (expediente.getEstado().equals(Constantes.ESTADO_REGISTRADO)){

                    LOGGER.debug("RevisarExpedienteController/Cuando se quiere ver el Expediente Registrado - Estado del expediente :" + expediente.getEstado());

                    if (ut != null || tieneAccesoSolicitud == true) {

                        LOGGER.debug("RevisarExpedienteController/ut not null- Validación");
                        LOGGER.debug("-----------------------------------UT--------------------------------------------");
                        LOGGER.debug("RevisarExpediente - boolean tieneAccesoSolicitud:" + tieneAccesoSolicitud);
                        LOGGER.debug("-------------------------------------------------------------------------------");

                        boolean multiple = traza.getPadre() != null;
                        if (expediente.getProceso().getTipoProceso().getCodigo().equals(Constantes.TIPO_PROCESO_INTALIO)) {
                            String url = intalioService.getAjaxFormURL(usuario, expediente.getId());
                            if (url != null) {
                                detalleContratoBean.setUrl(url);
                                //model.addAttribute("urlIntalio", url);
                            }
                        }
                        if (tieneAccesoSolicitud == false) {
                            if (!multiple && !ut.isBloqueado()) {
                                LOGGER.debug("-----------------------------------Tiene valor multiple--------------------------------------------");
                                revisarExpedienteService.bloquearExpediente(ut);
                            }
                            /* Cambiar estado leido */
                            revisarExpedienteService.cambiarEstadoLeido(ut);
                            LOGGER.debug("-----------------------------------Cambiar estado leido--------------------------------------------");
                        }

                        List<Boton> listaBotones;
                        if (multiple) {
                            listaBotones = revisarExpedienteService.obtenerBotones(expediente, perfil, ut.getEstado());
                            LOGGER.debug("RevisarExpedienteController/ 1");

                        } else if (traza.getEstado() != null && traza.getEstado().equals(Constantes.ESTADO_PAUSADO)) {
                            listaBotones = revisarExpedienteService.obtenerBotones(expediente,perfil, false, expediente.getEstado());

                            LOGGER.debug("RevisarExpedienteController/ 2");
                        } else if (expediente.getDocumentoLegal() != null) {
                            /*Es un expediente de Contrato/Adenda, se deben obtener los botones dependiendo del estado del documento
                             *  y del rol que esta viendo el expediente.
                             */
                            if (expediente.getDocumentoLegal().getContrato() != null && tieneAccesoSolicitud == false) {
                                listaBotones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, ut.isResponsable(), expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_ADENDA);
                                LOGGER.debug("RevisarExpedienteController/ 3");
                            } else if (expediente.getDocumentoLegal().getContrato() != null && tieneAccesoSolicitud == true) {
                                listaBotones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, true, expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_ADENDA);
                                LOGGER.debug("RevisarExpedienteController/ 3 B");
                            } else if (expediente.getDocumentoLegal().getAdenda() != null && tieneAccesoSolicitud == false) {
                                listaBotones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, ut.isResponsable(), expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_CONTRATO);
                                LOGGER.debug("RevisarExpedienteController/ 4");
                            } else if (expediente.getDocumentoLegal().getAdenda() != null && tieneAccesoSolicitud == true) {
                                listaBotones = revisarExpedienteService.obtenerBotonesExceptoParametro(perfil, expediente, usuario, true, expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_CONTRATO);
                                LOGGER.debug("RevisarExpedienteController/ 4 B");

                            } else {
                                listaBotones = new ArrayList<Boton>();
                                LOGGER.debug("RevisarExpedienteController/ 5");
                            }
                        } else {
                            listaBotones = revisarExpedienteService.obtenerBotones(expediente, perfil, ut.isResponsable(), expediente.getEstado());
                            LOGGER.debug("RevisarExpedienteController/ 6");
                        }
                        /*else{
							listaBotones=revisarExpedienteService.obtenerBotones(expediente,usuario.getPerfilSesion(),ut.isResponsable(),expediente.getEstado());
						}*/

                        // log.info("Botones perfil :" + listaBotones.size());
                        for (Boton boton : listaBotones) {

                            /*LOGGER.debug("RevisarExpedienteController/ 7");
                            LOGGER.info("Nombre boton :" + boton.getUrl());
                            LOGGER.info("Codigo boton :" + boton.getCodigo());
                            LOGGER.info("Id boton :" + boton.getId());*/
                            if (expediente.getPadre() != null) {
                                if (boton.getVerParalela() != null && boton.getVerParalela()) {
                                    // log.info("bloquear el boton");

                                    boton.setBloqueado(true);
                                }
                            }

                            if (boton.getBloqueable() && ut.isBloqueado()) {
                                LOGGER.info("bloquear el boton");
                                boton.setBloqueado(true);
                            }

                        }
                        // log.info("Botones :" + listaBotones.size());
                        detalleContratoBean.setBotones(listaBotones);
                        if (expediente.getDocumentoLegal() != null) {
                            detalleContratoBean.setEstadoDl(AppUtil.getNombreEstadoDL(expediente.getDocumentoLegal().getEstado()));
                        }

                        //Retorna la vista de adenda automattica
                        LOGGER.debug("RevisarExpedienteController/revisarExpediente - Tipo de proceso :" + tipoProcesoAqui);
                        if (tipoProcesoAqui == 5) {

                            agregarBotonFirmaElectronica(detalleContratoBean,listaBotones, expediente.getDocumentoLegal().getEstado(), expediente.getFirmaElectronica());

                            Integer idDocumentoLegal = expediente.getDocumentoLegal().getId();

                            System.out.println("---------------------------------------------------------------------------------------");
                            System.out.println("IdDocumentoLegal + revisarExpediente");
                            System.out.println(idDocumentoLegal);
                            System.out.println("---------------------------------------------------------------------------------------");

                            HcAdenda hcAdenda = hcAdendaRepository.findById(idDocumentoLegal);

                            //Envio de datos del tipo de adenda
                            detalleContratoBean.setAdenda(hcAdenda);
                            quitarBotonSeguridad(usuario, detalleContratoBean, listaBotones);
                            cleanResponse(detalleContratoBean);
                            return detalleContratoBean;
                        }
                        /* Solo aprobar si es el responsable */
                        quitarBotonSeguridad(usuario, detalleContratoBean, listaBotones);
                        cleanResponse(detalleContratoBean);
                        return detalleContratoBean;
                    }
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
                            listaBotones = revisarExpedienteService.obtenerBotonesPorRolExceptoParametro(perfil, expediente, usuario, false, expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_ADENDA);

                            LOGGER.debug("RevisarExpedienteController/ 8");

                        } else if (expediente.getDocumentoLegal().getAdenda() != null) {
                            listaBotones = revisarExpedienteService.obtenerBotonesPorRolExceptoParametro(perfil, expediente, usuario, false, expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_CONTRATO);

                            LOGGER.debug("RevisarExpedienteController/ 9");
                        } else {
                            listaBotones = new ArrayList<Boton>();

                            LOGGER.debug("RevisarExpedienteController/ 10");
                        }
                    } else {
                        listaBotones = revisarExpedienteService.obtenerBotonesMostrarCargo(perfil);

                        LOGGER.debug("RevisarExpedienteController/ 11");
                    }
                    //
                    if (!verificarRol(this.revisarExpedienteService.buscarRolesPorUsuario(usuario.getId()), Constantes.CODIGO_ROL_ABOGADO_RESPONSABLE)) {
                        modificarBotones(listaBotones, Constantes.URL_ENVIAR_VB);
                    }
                    detalleContratoBean.setBotones(listaBotones);
                    if (expediente.getDocumentoLegal() != null) {
                        detalleContratoBean.setEstadoDl(AppUtil.getNombreEstadoDL(expediente.getDocumentoLegal().getEstado()));
                    }

                    //Retorna la vista de adenda automattica
                    LOGGER.debug("RevisarExpedienteController/revisarExpediente - Tipo de proceso :" + tipoProcesoAqui);
                    LOGGER.debug("RevisarExpedienteController/ut null");

                    if (tipoProcesoAqui == 5) {

                        agregarBotonFirmaElectronica(detalleContratoBean, listaBotones, expediente.getDocumentoLegal().getEstado(), expediente.getFirmaElectronica());

                        Integer idDocumentoLegal = expediente.getDocumentoLegal().getId();

                        System.out.println("---------------------------------------------------------------------------------------");
                        System.out.println("IdDocumentoLegal + revisarExpediente");
                        System.out.println(idDocumentoLegal);
                        System.out.println("---------------------------------------------------------------------------------------");

                        HcAdenda hcAdenda = hcAdendaRepository.findById(idDocumentoLegal);

                        //Envio de datos del tipo de adenda
                        //model.addAttribute("tipoContratoAdenda", hcAdenda);
                        detalleContratoBean.setAdenda(hcAdenda);
                        quitarBotonSeguridad(usuario, detalleContratoBean, listaBotones);
                        cleanResponse(detalleContratoBean);
                        return detalleContratoBean;
                    }

                    quitarBotonSeguridad(usuario, detalleContratoBean, listaBotones);
                    cleanResponse(detalleContratoBean);
                    return detalleContratoBean;
                } else if (expediente.getEstado().equals(Constantes.ESTADO_GUARDADO)) {
                    if (expediente.getDocumentoLegal() != null) {
                        //XXX
                        //Es una solicitud guardada, debe mostrarse el formulario para solicitudes
                        detalleContratoBean.setIdResponsable(expediente.getDocumentoLegal().getResponsable().getId());
                        detalleContratoBean.setNombreResponsable(expediente.getDocumentoLegal().getResponsable().getNombres() + " " + expediente.getDocumentoLegal().getResponsable().getApellidos());
                        detalleContratoBean.setProcesos(revisarExpedienteService.listarProcesos());

                        expediente.setDocumentoLegal(null);
                        detalleContratoBean.setExpediente(expediente);
                        cleanResponse(detalleContratoBean);
                        return detalleContratoBean;
                    } else {
                        detalleContratoBean.setBotones(revisarExpedienteService.obtenerBotones(perfil));
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
                        detalleContratoBean.setTexto(texto);
                        detalleContratoBean.setFlag(true);
                        detalleContratoBean.setExpediente(expediente);

                        /* Obtengo el proceso */
                        if (expediente.getProceso().getTipoProceso().getCodigo().equals(Constantes.CODIGO_COMUNICACIONES_INTERNAS)) {
                            LOGGER.debug("Comunicaciones Internas, traigo todos los responsables");
                            detalleContratoBean.setInternas(true);

                        } else {

                            detalleContratoBean.setInternas(false);
                        }

                        if (flagCliente) {
                            detalleContratoBean.setEditar(true);

                        }
                        detalleContratoBean.setDocumentos(expediente.getDocumentos());
                        detalleContratoBean.setObservacion(expediente.getObservacionMp());

                        cleanResponse(detalleContratoBean);
                        return detalleContratoBean;
                    }
                }

                else if (expediente.getEstado().equals(Constantes.ESTADO_ARCHIVADO)) {
                    if (perfil.getCodigo().equals(Constantes.PERFIL_USUARIO_FINAL)) {
                        //XXX
                        List<Documento> documentos = expediente.getDocumentos();
                        List<List<CampoPorDocumento>> totalCampos = new ArrayList<List<CampoPorDocumento>>();
                        for (Documento documento : documentos) {
                            totalCampos.add(revisarExpedienteService.obtenerCamposPorDocumento(documento));
                        }
                        detalleContratoBean.setCampos(totalCampos);

                        List<Boton> botones = null;
                        if (ut != null) {
                            if (expediente.getDocumentoLegal() != null) {
                                /*XXX Es un expediente de Contrato/Adenda, se deben obtener los botones dependiendo del estado del documento
                                 *  y del rol que esta viendo el expediente.
                                 */
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
                                /*XXX Es un expediente de Contrato/Adenda, se deben obtener los botones dependiendo del estado del documento
                                 *  y del rol que esta viendo el expediente.
                                 */
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

                        detalleContratoBean.setBotones(botones);
                        if (expediente.getDocumentoLegal() != null) {
                            detalleContratoBean.setEstadoDl(AppUtil.getNombreEstadoDL(expediente.getDocumentoLegal().getEstado()));
                        }

                        //Retorna la vista de adenda automattica
                        LOGGER.debug("RevisarExpedienteController/revisarExpediente - Tipo de proceso :" + tipoProcesoAqui);
                        if (tipoProcesoAqui == 5) {

                            agregarBotonFirmaElectronica(detalleContratoBean, botones, expediente.getDocumentoLegal().getEstado(), expediente.getFirmaElectronica());

                            Integer idDocumentoLegal = expediente.getDocumentoLegal().getId();

                            System.out.println("---------------------------------------------------------------------------------------");
                            System.out.println("IdDocumentoLegal + revisarExpediente");
                            System.out.println(idDocumentoLegal);
                            System.out.println("---------------------------------------------------------------------------------------");

                            HcAdenda hcAdenda = hcAdendaRepository.findById(idDocumentoLegal);

                            //Envio de datos del tipo de adenda
                            detalleContratoBean.setAdenda(hcAdenda);
                            quitarBotonSeguridad(usuario, detalleContratoBean, botones);
                            cleanResponse(detalleContratoBean);
                            return detalleContratoBean;
                        }

                        quitarBotonSeguridad(usuario, detalleContratoBean, botones);
                        cleanResponse(detalleContratoBean);
                        return detalleContratoBean;
                    }
                }

                else {
                    /* Here */
                    if (perfil.getCodigo().equals(Constantes.PERFIL_USUARIO_FINAL)) {
                        List<Documento> documentos = expediente.getDocumentos();
                        List<List<CampoPorDocumento>> totalCampos = new ArrayList<List<CampoPorDocumento>>();
                        for (Documento documento : documentos) {
                            totalCampos.add(revisarExpedienteService.obtenerCamposPorDocumento(documento));
                        }
                        detalleContratoBean.setCampos(totalCampos);

                        List<Boton> listaBotones = revisarExpedienteService.obtenerBotones(expediente, perfil, false, expediente.getEstado());
                        detalleContratoBean.setBotones(listaBotones);
                        if (expediente.getDocumentoLegal() != null) {
                            detalleContratoBean.setEstadoDl(AppUtil.getNombreEstadoDL(expediente.getDocumentoLegal().getEstado()));
                        }
                        // return "registrarExpediente/mostrarCargoExpediente";

                        quitarBotonSeguridad(usuario, detalleContratoBean, listaBotones);
                        cleanResponse(detalleContratoBean);
                        return detalleContratoBean;
                    }
                    cleanResponse(detalleContratoBean);
                    return detalleContratoBean;
                }


            }

        } catch (Exception ex){
            LOGGER.error(ex.getMessage(), ex);
        }
        return detalleContratoBean;
    }

    @Override
    public DetalleExpedienteBean obtenerDetalleExpedientePorTraza(Integer idTraza, Integer idUsuario, Integer idPerfil) {

        DetalleExpedienteBean detalleContratoBean = new DetalleExpedienteBean();

        try {

            LOGGER.info("Revisando detalle de la traza con ID [" + idTraza + "]");
            Usuario usuario = usuarioRepository.findById(idUsuario);
            Perfil perfil = perfilRepository.findById(idPerfil);
            Traza traza = trazaRepository.findById(idTraza);
            Boolean trazaAntigua = false;

            if (traza != null) {

                Expediente expediente = traza.getExpediente();
                Traza trazaActual = revisarExpedienteService.obtenerUltimaTraza(expediente.getId(), usuario.getId());
                LOGGER.info("Usuario [" + usuario.getLabel() + "] accede al expediente [" + expediente.getLabel() + "]");
                detalleContratoBean.setIdTraza(traza.getId());
                detalleContratoBean.setUsuario(usuario);

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

                    Boolean registrado = estado.equals(Constantes.ESTADO_REGISTRADO);
                    Boolean archivado = estado.equals(Constantes.ESTADO_ARCHIVADO);
                    Boolean porEnviar = estado.equals(Constantes.ESTADO_POR_ENVIAR);

                    /**
                     * Cuando se quiere ver el Expediente Registrado *
                     */
                    if (registrado || archivado || porEnviar) {
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
                            if (archivado) {//joel
                                botones = revisarExpedienteService.obtenerBotones(expediente, perfil, ut.isResponsable(), estado);
                            } else if (registrado) {
                                if (multiple) {
                                    botones = revisarExpedienteService.obtenerBotones(expediente, perfil, ut.getEstado());
                                } else if (expediente.getDocumentoLegal() != null) {
                                    /*XXX Es un expediente de Contrato/Adenda, se deben obtener los botones dependiendo del estado del documento
                                     *  y del rol que esta viendo el expediente.
                                     */
                                    if (expediente.getDocumentoLegal().getContrato() != null) {
                                        botones = revisarExpedienteService.obtenerBotonesPorRolExceptoParametro(perfil, expediente, usuario, ut.isResponsable(), expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_ADENDA);
                                        LOGGER.info("-------------CASO CONTRATO-------------");
                                        LOGGER.info(botones);
                                        LOGGER.info("---------------------------");
                                    } else if (expediente.getDocumentoLegal().getAdenda() != null) {
                                        botones = revisarExpedienteService.obtenerBotonesPorRolExceptoParametro(perfil, expediente, usuario, ut.isResponsable(), expediente.getDocumentoLegal().getEstado(), Constantes.PARAMETRO_CONTRATO);
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
                                    HcDocumentoLegal documentoLegal = hcAdenda.getDocumentoLegal();
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

                        cleanResponse(detalleContratoBean);
                    }
                }
            }

        } catch (Exception ex) {
            LOGGER.info(ex.getMessage(),ex);
        }
        return detalleContratoBean;
    }

    private static void cleanResponse(DetalleExpedienteBean detalleContratoBean) {
        Expediente expedienteFormateado = detalleContratoBean.getExpediente();
        HcDocumentoLegal hcDocumentoLegalFormateado = detalleContratoBean.getExpediente().getDocumentoLegal();
        HcContrato contratoFormateado = detalleContratoBean.getExpediente().getDocumentoLegal().getContrato();
        HcAdenda adendaFormateada = detalleContratoBean.getExpediente().getDocumentoLegal().getAdenda();
        Traza trazaFormateada = detalleContratoBean.getTraza();
        List<Documento> documentosFormateado = new ArrayList<>();


        for (int i = 0; i < detalleContratoBean.getExpediente().getDocumentos().size(); i++) {

            Documento documento = detalleContratoBean.getExpediente().getDocumentos().get(i);
            List<Archivo> archivosPorDocumento = new ArrayList<>();
            documento.setExpediente(null);

            for (Archivo file : documento.getArchivos()) {
                for (Version version : file.getVersions()) {
                    version.setArchivo(null);
                }
                file.setDocumento(null);

                if (file.getEstado().equals(Constantes.ESTADO_ACTIVO)){
                    archivosPorDocumento.add(file);
                }
            }
            documento.setArchivos(archivosPorDocumento);
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
        detalleContratoBean.setObsTraza(trazaFormateada.getObservacion());
        //Traza limpio
        trazaFormateada.setExpediente(null);
    }

    @Override
    public DetalleExpedienteBean obtenerDetalleExpediente(Integer idExpediente, Integer idUsuario) {
        return null;
    }


    @Override
    public TabModelBean obtenerDatosBasicosExpediente(Integer idTraza) {
        Traza traza = trazaRepository.findById(idTraza);
        Expediente expediente = traza.getExpediente();
        HcDocumentoLegal hcDocumentoLegal= hcDocumentoLegalRepository.findByIdExpediente(expediente.getId());

        TabModelBean tabModelBean=new TabModelBean();
        tabModelBean.setId(expediente.getId());
        tabModelBean.setCode(hcDocumentoLegal.getNumero());

        return tabModelBean;
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
    public void modificarBotones(List<Boton> listaBotones, String url) {
        listaBotones.removeIf(boton -> boton.getUrl().equals(url));
    }
}