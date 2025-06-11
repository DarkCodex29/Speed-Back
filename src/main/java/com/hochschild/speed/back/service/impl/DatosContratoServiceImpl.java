package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.dao.HcDocumentoLegalDao;
import com.hochschild.speed.back.model.bean.bandejaEntrada.DatosContratoBean;
import com.hochschild.speed.back.model.bean.registrarSolicitud.DocumentoLegalBean;
import com.hochschild.speed.back.model.bean.registrarSolicitud.RepresentanteLegalBean;
import com.hochschild.speed.back.model.bean.registrarSolicitud.UbicacionesBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.filter.bandejaEntrada.ContratoBean;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.*;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service("DatosContratoService")
public class DatosContratoServiceImpl implements DatosContratoService {
    private static final Logger LOGGER = Logger.getLogger(DatosContratoServiceImpl.class.getName());
    private final HcDocumentoLegalDao hcDocumentoLegalDao;
    private final CommonBusinessLogicService commonBusinessLogicService;
    private final RegistrarSolicitudService registrarSolicitudService;
    private final RegistroSolicitudService registroSolicitudService;
    private final RevisarExpedienteService revisarExpedienteService;
    private final ParametroService parametroService;
    private final TrazaRepository trazaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ExpedienteRepository expedienteRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final HcUbicacionRepository hcUbicacionRepository;
    private final HcPaisRepository hcPaisRepository;
    private final HcCompaniaRepository hcCompaniaRepository;
    private final HcAdendaRepository hcAdendaRepository;
    private final ClienteRepository clienteRepository;
    private final HcRepresentantePorDocumentoRepository hcRepresentantePorDocumentoRepository;
    private final HcPenalidadPorDocumentoLegalRepository hcPenalidadPorDocumentoLegalRepository;
    private final HcTipoContratoConfiguracionRepository hcTipoContratoConfiguracionRepository;
    private final ParametroRepository parametroRepository;
    private final HcUbicacionPorDocumentoRepository hcUbicacionPorDocumentoRepository;
    private final HcAreaRepository hcAreaRepository;
    private final HcTipoContratoRepository hcTipoContratoRepository;
    private final HcContratoRepository hcContratoRepository;
    private final UsuarioAccesoSolicitudRepository usuarioAccesoSolicitudRepository;
    private final MantenimientoUsuarioService mantenimientoUsuarioService;

    @Autowired
    public DatosContratoServiceImpl(HcDocumentoLegalDao hcDocumentoLegalDao,
                                    CommonBusinessLogicService commonBusinessLogicService,
                                    RegistrarSolicitudService registrarSolicitudService,
                                    RegistroSolicitudService registroSolicitudService,
                                    RevisarExpedienteService revisarExpedienteService,
                                    ParametroService parametroService,
                                    TrazaRepository trazaRepository, UsuarioRepository usuarioRepository,
                                    ExpedienteRepository expedienteRepository,
                                    HcDocumentoLegalRepository hcDocumentoLegalRepository,
                                    HcUbicacionRepository hcUbicacionRepository,
                                    HcPaisRepository hcPaisRepository,
                                    HcCompaniaRepository hcCompaniaRepository,
                                    HcAdendaRepository hcAdendaRepository,
                                    ClienteRepository clienteRepository,
                                    HcRepresentantePorDocumentoRepository hcRepresentantePorDocumentoRepository,
                                    HcPenalidadPorDocumentoLegalRepository hcPenalidadPorDocumentoLegalRepository,
                                    HcTipoContratoConfiguracionRepository hcTipoContratoConfiguracionRepository,
                                    ParametroRepository parametroRepository,
                                    HcUbicacionPorDocumentoRepository hcUbicacionPorDocumentoRepository,
                                    HcAreaRepository hcAreaRepository,
                                    HcTipoContratoRepository hcTipoContratoRepository,
                                    HcContratoRepository hcContratoRepository,
                                    UsuarioAccesoSolicitudRepository usuarioAccesoSolicitudRepository,
                                    MantenimientoUsuarioService mantenimientoUsuarioService) {
        this.hcDocumentoLegalDao = hcDocumentoLegalDao;
        this.commonBusinessLogicService = commonBusinessLogicService;
        this.registrarSolicitudService = registrarSolicitudService;
        this.registroSolicitudService = registroSolicitudService;
        this.revisarExpedienteService = revisarExpedienteService;
        this.parametroService = parametroService;
        this.trazaRepository = trazaRepository;
        this.usuarioRepository = usuarioRepository;
        this.expedienteRepository = expedienteRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.hcUbicacionRepository = hcUbicacionRepository;
        this.hcPaisRepository = hcPaisRepository;
        this.hcCompaniaRepository = hcCompaniaRepository;
        this.hcAdendaRepository = hcAdendaRepository;
        this.clienteRepository = clienteRepository;
        this.hcRepresentantePorDocumentoRepository = hcRepresentantePorDocumentoRepository;
        this.hcPenalidadPorDocumentoLegalRepository = hcPenalidadPorDocumentoLegalRepository;
        this.hcTipoContratoConfiguracionRepository = hcTipoContratoConfiguracionRepository;
        this.parametroRepository = parametroRepository;
        this.hcUbicacionPorDocumentoRepository = hcUbicacionPorDocumentoRepository;
        this.hcAreaRepository = hcAreaRepository;
        this.hcTipoContratoRepository = hcTipoContratoRepository;
        this.hcContratoRepository = hcContratoRepository;
        this.usuarioAccesoSolicitudRepository = usuarioAccesoSolicitudRepository;
        this.mantenimientoUsuarioService = mantenimientoUsuarioService;
    }

    @Override
    @Transactional
    public DatosContratoBean botonDatosContrato(ContratoBean contratoBean, Integer idUsuario) {

        DatosContratoBean datosContratoBean = new DatosContratoBean();

        try {
            HcDocumentoLegal hcDocumentoLegal = hcDocumentoLegalRepository.findByIdExpediente(contratoBean.getIdExpediente());
            //List<HcPenalidadPorDocumentoLegal> penalidades = hcPenalidadPorDocumentoLegalRepository.obtenerPorIdExpediente(contratoBean.getIdExpediente());
            Expediente expediente = expedienteRepository.findById(contratoBean.getIdExpediente());
            hcDocumentoLegal.setExpediente(expediente);
            datosContratoBean.setUsuario(usuarioRepository.findById(idUsuario));
            datosContratoBean.setDocumentoLegal(hcDocumentoLegal);
            datosContratoBean.setPaises(hcPaisRepository.listarPaises());

            datosContratoBean.setUbicaciones(hcUbicacionRepository.obtenerUbicacionesDocumentoLegal(datosContratoBean.getDocumentoLegal().getId()));
            datosContratoBean.setRepresentantes(registrarSolicitudService.obtenerRepresentantesDocumentoLegal(datosContratoBean.getDocumentoLegal().getId()));
            datosContratoBean.setUbicacionOperacion(hcUbicacionRepository.getUbicacionActivaPorTipoUbicacion(Constantes.TIPO_UBICACION_OPERACION, datosContratoBean.getIdCompania()));
            datosContratoBean.setUbicacionProyecto(hcUbicacionRepository.getUbicacionActivaPorTipoUbicacion(Constantes.TIPO_UBICACION_PROYECTO, datosContratoBean.getIdCompania()));
            datosContratoBean.setUbicacionOficina(hcUbicacionRepository.getUbicacionActivaPorTipoUbicacion(Constantes.TIPO_UBICACION_OFICINA, datosContratoBean.getIdCompania()));
            datosContratoBean.setListaTipoContrato(hcTipoContratoRepository.getListaTipoContrato());
            datosContratoBean.setListaMonedas(parametroService.buscarParametroPorTipo(Constantes.PARAMETRO_MONEDA));

            if (datosContratoBean.getDocumentoLegal() != null && datosContratoBean.getDocumentoLegal().getArea() != null){
                datosContratoBean.setIdPais(datosContratoBean.getDocumentoLegal().getArea().getCompania().getPais().getId());
                datosContratoBean.setIdCompania(datosContratoBean.getDocumentoLegal().getArea().getCompania().getId());
                datosContratoBean.setAreas(hcAreaRepository.listarAreasPorCompania(datosContratoBean.getDocumentoLegal().getArea().getCompania().getId(), Constantes.ESTADO_ACTIVO));
                datosContratoBean.setCompanias(hcCompaniaRepository.getCompaniasActivasPorPais(datosContratoBean.getDocumentoLegal().getArea().getCompania().getPais().getId()));
            }

            //boolean cambiarResponsable = false;
            Boolean botoneraEstado = false;
            Boolean tieneAccesoSolicitud = false;

            if (datosContratoBean.getUsuario() != null) {
                Traza traza = revisarExpedienteService.obtenerUltimaTraza(hcDocumentoLegal.getExpediente().getId(), datosContratoBean.getUsuario().getId());
                datosContratoBean.setIdTraza(traza.getId());

                UsuarioPorTraza ut = revisarExpedienteService.obtenerUsuarioPorTraza(traza.getId(), datosContratoBean.getUsuario().getId());
                List<UsuarioAccesoSolicitud> representantesConAcceso = usuarioAccesoSolicitudRepository.getUsuariosConAcceso(Constantes.ESTADO_ACCESO_REPRESENTANTE_HABILITADO);

                //Validación para buscar representante
                for (int i = 0; i < representantesConAcceso.size(); i++) {
                    if (datosContratoBean.getUsuario().getId().equals(representantesConAcceso.get(i).getResponsable().getId())) {
                        LOGGER.info("-----------------------------------RESULT--------------------------------------------");
                        LOGGER.info("COINCIDENCIA DE USUARIO");
                        tieneAccesoSolicitud = true;
                        LOGGER.info("-------------------------------------------------------------------------------");
                        break;
                    }
                }

                if (datosContratoBean.getDocumentoLegal().getEstado().equals(Constantes.ESTADO_HC_ENVIADO_VISADO)) {
                    tieneAccesoSolicitud = false;
                    LOGGER.info("SE ENCUENTRA EN ESTADO ENVIADO_VISADO = " + tieneAccesoSolicitud);
                }

                if (ut == null) {
                    LOGGER.info("NO EXISTE USUARIO POR TRAZAAAAA");
                    // Verificamos si el usuario esta reemplazando a otro usuario
                    Reemplazo reemplazo = revisarExpedienteService.buscarReemplazo(datosContratoBean.getUsuario().getId(), hcDocumentoLegal.getExpediente().getProceso());
                    if (reemplazo != null) {
                        ut = revisarExpedienteService.obtenerUsuarioPorTraza(traza.getId(), reemplazo.getReemplazado().getId());
                        LOGGER.info("ASIGNANDO REEMPLAZO");
                    }
                }

                LOGGER.info("TIENE ACCESO: " + tieneAccesoSolicitud);
                LOGGER.info("EXISTE USUARIO POR TRAZAAAAA");
                LOGGER.info("botoneraEstado: " + botoneraEstado);
                List<Rol> roles = mantenimientoUsuarioService.buscarRolesExistente(datosContratoBean.getUsuario().getId());
                LOGGER.info("ESTADO DEL DOCUMENTO: " + datosContratoBean.getDocumentoLegal().getEstado());
                LOGGER.info("==================================================");
                LOGGER.info("ROLES");
                for (Rol rol : roles) {
                    LOGGER.info("CÓDIGO: " + rol.getCodigo());
                    LOGGER.info("DESCRIPCIÓN: " + rol.getDescripcion());
                }

                LOGGER.info("==================================================");

                if (datosContratoBean.getDocumentoLegal().getEstado().compareTo(Constantes.ESTADO_HC_EN_SOLICITUD) == 0) {
                    for (Rol rol : roles) {
                        if (rol.getCodigo().equals(Constantes.CODIGO_ROL_SOLICITANTE)) {
                            botoneraEstado = true;
                            break;
                        }
                    }
                }

                if (datosContratoBean.getDocumentoLegal().getEstado().compareTo(Constantes.ESTADO_HC_VISADO) == 0
                        || datosContratoBean.getDocumentoLegal().getEstado().compareTo(Constantes.ESTADO_HC_ENVIADO_VISADO) == 0
                        || datosContratoBean.getDocumentoLegal().getEstado().compareTo(Constantes.ESTADO_HC_EN_ELABORACION) == 0
                        || datosContratoBean.getDocumentoLegal().getEstado().compareTo(Constantes.ESTADO_HC_SOLICITUD_ENVIADA) == 0
                        || datosContratoBean.getDocumentoLegal().getEstado().compareTo(Constantes.ESTADO_HC_ELABORADO) == 0
                        || datosContratoBean.getDocumentoLegal().getEstado().compareTo(Constantes.ESTADO_HC_VIGENTE) == 0
                        || datosContratoBean.getDocumentoLegal().getEstado().compareTo(Constantes.ESTADO_HC_VENCIDO) == 0) {
                    for (Rol rol : roles) {
                        if (rol.getCodigo().equals(Constantes.CODIGO_ROL_ABOGADO_RESPONSABLE)) //cambiarResponsable = true;
                        {
                            botoneraEstado = true;
                            break;
                        }
                        if (rol.getCodigo().equals(Constantes.CODIGO_ROL_ABOGADO)) {
                            //cambiarResponsable = true;
                            botoneraEstado = true;
                            break;
                        }
                    }
                }

                if (datosContratoBean.getDocumentoLegal().getEstado().compareTo(Constantes.ESTADO_HC_ENVIADO_FIRMA) == 0 || datosContratoBean.getDocumentoLegal().getEstado().compareTo(Constantes.ESTADO_HC_COMUNICACION) == 0) {
                    for (Rol rol : roles) {
                        if (rol.getCodigo().equals(Constantes.CODIGO_ROL_ABOGADO_RESPONSABLE)) {
                            botoneraEstado = true;
                            break;
                        }
                        if (rol.getCodigo().equals(Constantes.CODIGO_ROL_ABOGADO)) {
                            botoneraEstado = true;
                            break;
                        }
                    }
                }
                LOGGER.info("es responsable...");
            }
            LOGGER.info("botoneraEstado: " + botoneraEstado);

            if (botoneraEstado) {
                datosContratoBean.setBotoneraActiva(true);
            } else {
                datosContratoBean.setBotoneraActiva(false);
            }

            Expediente expedienteFormateado = datosContratoBean.getDocumentoLegal().getExpediente();
            HcDocumentoLegal hcDocumentoLegalFormateado = datosContratoBean.getDocumentoLegal();
            HcContrato contratoFormateado = hcDocumentoLegalFormateado.getContrato();
            HcAdenda adendaFormateada = hcDocumentoLegalFormateado.getAdenda();

            //Expediente limpio
            expedienteFormateado.setDocumentoLegal(null);
            expedienteFormateado.setDocumentos(null);

            //DocumentoLegal limpio
            hcDocumentoLegalFormateado.setExpediente(expedienteFormateado);

            //Contrato limpio
            if (contratoFormateado != null) {
                contratoFormateado.setDocumentoLegal(null);
                hcDocumentoLegalFormateado.setContrato(contratoFormateado);
            }

            //Adenda limpio
            if (adendaFormateada != null) {
                HcContrato contrato = adendaFormateada.getContrato();
                contrato.setDocumentoLegal(null);
                adendaFormateada.setDocumentoLegal(null);
                adendaFormateada.setContrato(contrato);
            }

            //Penalidades
            /*for (HcPenalidadPorDocumentoLegal hcPenalidadPorDocumentoLegal : penalidades) {
                hcPenalidadPorDocumentoLegal.setDocumentoLegal(null);
            }*/

            hcDocumentoLegalFormateado.setAdenda(adendaFormateada);
            datosContratoBean.setPenalidades(null);
            datosContratoBean.setDocumentoLegal(hcDocumentoLegalFormateado);

        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return datosContratoBean;
    }

    @Override
    @Transactional
    public ResponseModel guardarHcDocumentoLegal(DocumentoLegalBean documentoLegalBean, Integer idUsuario) {

        ResponseModel responseModel = new ResponseModel();

        try {
            HcDocumentoLegal documentoLegal = new HcDocumentoLegal();
            Usuario usuario = usuarioRepository.findById(idUsuario);
            documentoLegal.setResponsable(new Usuario(documentoLegalBean.getAbogadoResponsable()));
            documentoLegal.setSolicitante(new Usuario(documentoLegalBean.getSolicitante()));

            documentoLegal = guardarHcDocumentoLegal(documentoLegalBean, usuario);

            if (documentoLegalBean.getEsContrato() && documentoLegalBean.getContrato() != null) {
                registroSolicitudService.borrarRepresentantesDocumentoLegal(documentoLegal.getId());
                for (RepresentanteLegalBean x : documentoLegalBean.getContrato().getRepresentantesLegales()) {

                    HcRepresentantePorDocumentoPk hrpdPK = new HcRepresentantePorDocumentoPk();
                    Cliente cli = new Cliente();
                    cli.setId(x.getIdRepresentanteLegal());
                    hrpdPK.setCliente(cli);
                    hrpdPK.setDocumentoLegal(documentoLegal);

                    HcRepresentantePorDocumento hcRepresentante = new HcRepresentantePorDocumento();
                    hcRepresentante.setId(hrpdPK);
                    hcRepresentantePorDocumentoRepository.save(hcRepresentante);
                }

                registroSolicitudService.borrarUbicacionesDocumentoLegal(documentoLegal.getId());
                for (UbicacionesBean x : documentoLegalBean.getContrato().getUbicaciones()) {
                    HcUbicacionPorDocumentoPk hcUbicacionPorDocumentoPk = new HcUbicacionPorDocumentoPk();
                    HcUbicacion ubi = new HcUbicacion();
                    ubi.setId(x.getIdUbicacion());
                    hcUbicacionPorDocumentoPk.setUbicacion(ubi);
                    hcUbicacionPorDocumentoPk.setDocumentoLegal(documentoLegal);

                    HcUbicacionPorDocumento hupd = new HcUbicacionPorDocumento();
                    hupd.setId(hcUbicacionPorDocumentoPk);

                    hcUbicacionPorDocumentoRepository.save(hupd);
                }
            }

            if (documentoLegalBean.getEsAdenda() && documentoLegalBean.getAdenda() != null) {

                registroSolicitudService.borrarRepresentantesDocumentoLegal(documentoLegal.getId());
                for (RepresentanteLegalBean x : documentoLegalBean.getAdenda().getRepresentantesLegales()) {

                    HcRepresentantePorDocumentoPk hrpdPK = new HcRepresentantePorDocumentoPk();
                    Cliente cli = new Cliente();
                    cli.setId(x.getIdRepresentanteLegal());
                    hrpdPK.setCliente(cli);
                    hrpdPK.setDocumentoLegal(documentoLegal);

                    HcRepresentantePorDocumento hcRepresentante = new HcRepresentantePorDocumento();
                    hcRepresentante.setId(hrpdPK);
                    hcRepresentantePorDocumentoRepository.save(hcRepresentante);
                }

                registroSolicitudService.borrarUbicacionesDocumentoLegal(documentoLegal.getId());
                for (UbicacionesBean x : documentoLegalBean.getAdenda().getUbicaciones()) {
                    HcUbicacionPorDocumentoPk hcubicacionPk = new HcUbicacionPorDocumentoPk();
                    HcUbicacion ubi = new HcUbicacion();
                    ubi.setId(x.getIdUbicacion());
                    hcubicacionPk.setUbicacion(ubi);
                    hcubicacionPk.setDocumentoLegal(documentoLegal);

                    HcUbicacionPorDocumento hupd = new HcUbicacionPorDocumento();
                    hupd.setId(hcubicacionPk);

                    hcUbicacionPorDocumentoRepository.save(hupd);
                }
            }

            if (documentoLegal.getId() != null) {
                responseModel.setId(documentoLegal.getId());
                responseModel.setMessage("Actualizacion completada");
                responseModel.setHttpSatus(HttpStatus.OK);
            } else {
                responseModel.setMessage("Operacion fallida");
                responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return responseModel;
    }

    @Override
    @Transactional
    public HcDocumentoLegal guardarHcDocumentoLegal(DocumentoLegalBean documentoLegalBean, Usuario usuario) {

        LOGGER.info("========================================================");
        LOGGER.info("DATOS DEL DOCUMENTO LEGAL");
        LOGGER.info("========================================================");

        //implementacion para arrendamiento
        Expediente expediente = expedienteRepository.findById(documentoLegalBean.getIdExpediente());

        if (documentoLegalBean.getEsContrato() && documentoLegalBean.getContrato().getAplicaArrendamiento()) {

            boolean isIndefinido = documentoLegalBean.getContrato().getEsIndefinido() != null ? documentoLegalBean.getContrato().getEsIndefinido() : false;
            LOGGER.info("------ Arrendamiento ------");
            LOGGER.info(String.valueOf(documentoLegalBean.getContrato().getAplicaArrendamiento()));
            LOGGER.info("------ Fin Arrendamiento ------");

            if (documentoLegalBean.getContrato() != null && documentoLegalBean.getContrato().getAplicaArrendamiento()) {
                if (isIndefinido) {
                    expediente.setAplicaArrendamiento("S");
                    expedienteRepository.save(expediente);
                } else {
                    Calendar inicio = new GregorianCalendar();
                    Calendar fin = new GregorianCalendar();
                    inicio.setTime(DateUtil.convertStringToDate(documentoLegalBean.getContrato().getFechaInicio(), DateUtil.FORMAT_DATE_XML));
                    fin.setTime(DateUtil.convertStringToDate(documentoLegalBean.getContrato().getFechaFin(), DateUtil.FORMAT_DATE_XML));
                    int difA = fin.get(Calendar.YEAR) - inicio.get(Calendar.YEAR);
                    int difM = difA * 12 + fin.get(Calendar.MONTH) - inicio.get(Calendar.MONTH);
                    if (difM >= 12) {
                        expediente.setAplicaArrendamiento("S");
                        expedienteRepository.saveAndFlush(expediente);
                    } else {
                        expediente.setAplicaArrendamiento("N");
                        expedienteRepository.saveAndFlush(expediente);
                    }
                }
            } else {
                expediente.setAplicaArrendamiento("N");
                expedienteRepository.saveAndFlush(expediente);
            }
        }

        HcDocumentoLegal documentoActual = hcDocumentoLegalRepository.findById(documentoLegalBean.getIdDocumentoLegal());
        documentoActual.setSumilla(documentoLegalBean.getSumilla());
        documentoActual.setFechaBorrador(DateUtil.convertStringToDate(documentoLegalBean.getFechaBorrador().toString(), DateUtil.FORMAT_DATE_XML));
        documentoActual.setSolicitante(usuarioRepository.findById(documentoLegalBean.getSolicitante()));

        if (documentoLegalBean.getContrato() != null) {
            documentoActual.setCnt_nombre_contacto(documentoLegalBean.getContrato().getNombreContraparte());
            documentoActual.setCnt_telefono_contacto(documentoLegalBean.getContrato().getTelefonoContraparte());
            documentoActual.setCnt_correo_contacto(documentoLegalBean.getContrato().getEmailContraparte());
            documentoActual.setContraparte(clienteRepository.findById(documentoLegalBean.getContrato().getIdContraparte()));
        }

        if (documentoLegalBean.getAdenda() != null) {
            documentoActual.setCnt_nombre_contacto(documentoLegalBean.getAdenda().getNombreContraparte());
            documentoActual.setCnt_telefono_contacto(documentoLegalBean.getAdenda().getTelefonoContraparte());
            documentoActual.setCnt_correo_contacto(documentoLegalBean.getAdenda().getEmailContraparte());
            documentoActual.setContraparte(clienteRepository.findById(documentoLegalBean.getAdenda().getIdContraparte()));
        }

        if (documentoLegalBean.getContrato() != null && documentoLegalBean.getContrato().getIdArea() != null) {
            documentoActual.setArea(hcAreaRepository.findById(documentoLegalBean.getContrato().getIdArea()));
        }
        Usuario usuarioNuevo = usuarioRepository.findById(documentoLegalBean.getAbogadoResponsable());
        Usuario usuarioActual = usuarioRepository.findById(documentoActual.getResponsable().getId());

        if (usuarioNuevo != usuarioActual) {
            documentoActual.setResponsable(usuarioRepository.findById(documentoLegalBean.getAbogadoResponsable()));
            commonBusinessLogicService.generarTraza(documentoLegalBean.getIdExpediente(), usuario, usuarioNuevo,
                    Constantes.OBS_HC_CAMBIO_ABOGADO_RESPONSABLE, Constantes.ACCION_CAMBIO_ABOGADO_RESPONSABLE);
        }

        hcDocumentoLegalRepository.saveAndFlush(documentoActual);

        if (documentoActual.getContrato() != null) {

            // Es un contrato
            HcContrato contratoActual = hcContratoRepository.findById(documentoLegalBean.getIdDocumentoLegal());
            contratoActual.setTipo_contrato(hcTipoContratoRepository.findById(documentoLegalBean.getTipoContrato()));

            Boolean indefinido = documentoLegalBean.getContrato().getEsIndefinido() != null ? documentoLegalBean.getContrato().getEsIndefinido() : false;
            contratoActual.setIndefinido(indefinido);

            if (documentoLegalBean.getContrato().getIdMoneda() != null) {
                contratoActual.setMoneda(parametroRepository.findById(documentoLegalBean.getContrato().getIdMoneda()));
            }

            contratoActual.setArrendamiento(documentoLegalBean.getContrato().getAplicaArrendamiento());

            if (documentoLegalBean.getContrato().getEsPrecioFijo()) {
                contratoActual.setModalidad_pago("PF");
            }
            if (documentoLegalBean.getContrato().getEsPrecioUnitario()) {
                contratoActual.setModalidad_pago("PU");
            }

            if (documentoLegalBean.getContrato().getMontoFijoTotalEstimado() != null) {
                contratoActual.setMonto(AppUtil.arreglarMontoValor(String.valueOf(documentoLegalBean.getContrato().getMontoFijoTotalEstimado())));
            } else {
                contratoActual.setMonto(null);
            }

            if (documentoLegalBean.getContrato().getMontoAdelanto() != null) {
                contratoActual.setMonto_adelanto(AppUtil.arreglarMontoValor(String.valueOf(documentoLegalBean.getContrato().getMontoAdelanto())));
            } else {
                contratoActual.setMonto_adelanto(null);
            }

            if (documentoLegalBean.getContrato().getAplicaModalidadPago()) {
                contratoActual.setAplicaModalidadPago(1);
            } else {
                contratoActual.setAplicaModalidadPago(0);
            }

            if (documentoLegalBean.getContrato().getAplicaPeriodicidad()) {
                contratoActual.setAplicaPeriodicidad(1);
            } else {
                contratoActual.setAplicaPeriodicidad(0);
            }

            contratoActual.setPeriodicidad(documentoLegalBean.getContrato().getPeriodicidadPago());
            contratoActual.setRenovacion_auto(documentoLegalBean.getContrato().getAplicaRenovacionAutomatica());
            contratoActual.setPeriodo_renovar(documentoLegalBean.getContrato().getPeriodoRenovar());
            contratoActual.setDescripcion(documentoLegalBean.getContrato().getPropositoObservaciones());
            contratoActual.setFechaInicio(DateUtil.convertStringToDate(documentoLegalBean.getContrato().getFechaInicio(), DateUtil.FORMAT_DATE_XML));
            contratoActual.setFechaFin(DateUtil.convertStringToDate(documentoLegalBean.getContrato().getFechaFin(), DateUtil.FORMAT_DATE_XML));

            hcContratoRepository.saveAndFlush(contratoActual);
            hcDocumentoLegalDao.solicitudActualizarVigencia(documentoActual.getId());

        } else if (documentoActual.getAdenda() != null) {
            // Es una adenda
            HcAdenda adendaActual = hcAdendaRepository.findById(documentoLegalBean.getIdDocumentoLegal());
            HcContrato contratoFromAdenda = hcContratoRepository.findById(documentoLegalBean.getAdenda().getIdContrato());

            adendaActual.setContrato(contratoFromAdenda);
            adendaActual.setInicioVigencia(DateUtil.convertStringToDate(documentoLegalBean.getAdenda().getFechaInicio(), DateUtil.FORMAT_DATE_XML));
            adendaActual.setModifica_fin(documentoLegalBean.getAdenda().getAplicaFechaFin());
            adendaActual.setNuevaFechaFin(DateUtil.convertStringToDate(documentoLegalBean.getAdenda().getFechaFin(), DateUtil.FORMAT_DATE_XML));

            Boolean indefinido = documentoLegalBean.getAdenda().getEsIndefinido() != null ? documentoLegalBean.getAdenda().getEsIndefinido() : false;
            adendaActual.setIndefinido(indefinido);
            adendaActual.setDescripcion(documentoLegalBean.getAdenda().getPropositoObservaciones());

            HcTipoContrato hcTipoContrato;
            if (documentoLegalBean.getEsAdendaAutomatica() && documentoLegalBean.getAdenda() != null) {
                hcTipoContrato = new HcTipoContrato();
                hcTipoContrato.setId(documentoLegalBean.getAdenda().getTipoDocumento());
            } else {
                hcTipoContrato = contratoFromAdenda.getTipo_contrato();
            }

            HcTipoContratoConfiguracion hcTipoContratoConfiguracion;

            if (documentoLegalBean.getEsAdendaAutomatica() && documentoLegalBean.getAdenda() != null) {

                HcDocumentoLegal documentoLegalUpdate = hcDocumentoLegalRepository.findById(documentoLegalBean.getIdDocumentoLegal());

                LOGGER.info("VALOR DE ADENDA AUTOMATICA - EDITAR");
                LOGGER.info(documentoLegalBean.getAdenda().getTipoDocumento());
                LOGGER.info("----------------------------");
                adendaActual.setHcTipoContrato(hcTipoContrato);

                hcTipoContratoConfiguracion = hcTipoContratoConfiguracionRepository.findByIdTipoContrato(documentoLegalBean.getAdenda().getTipoDocumento());

                LOGGER.info("VALOR DE ESTADO DE SOLICITUD");
                LOGGER.info(hcTipoContratoConfiguracion.getEstadoSolicitud());
                LOGGER.info("----------------------------");

                if (hcTipoContratoConfiguracion.getEstadoSolicitud().equals(Constantes.ESTADO_HC_ENVIADO_FIRMA.toString())) {

                    LOGGER.info("ESTADO F");
                    documentoLegalUpdate.setEstado(Constantes.ESTADO_HC_ENVIADO_FIRMA);
                    LOGGER.info(documentoLegalUpdate.getEstado());
                    LOGGER.info("----------------------------");
                    documentoLegalUpdate.setSumilla(hcTipoContratoConfiguracion.getSumilla());
                } else if (hcTipoContratoConfiguracion.getEstadoSolicitud().equals(Constantes.ESTADO_HC_VIGENTE.toString())) {

                    LOGGER.info("ESTADO T");
                    documentoLegalUpdate.setEstado(Constantes.ESTADO_HC_VIGENTE);
                    LOGGER.info(documentoLegalUpdate.getEstado());
                    LOGGER.info("----------------------------");
                    documentoLegalUpdate.setSumilla(hcTipoContratoConfiguracion.getSumilla());
                }

                documentoLegalUpdate.setFechaBorrador(new Date());
                LOGGER.info("--------------------------");
                LOGGER.info("UPDATE DOCUMENTO LEGAL");
                hcDocumentoLegalRepository.saveAndFlush(documentoLegalUpdate);
            }

            hcAdendaRepository.saveAndFlush(adendaActual);

            hcDocumentoLegalDao.solicitudActualizarVigencia(documentoActual.getId());
        }
        return documentoActual;
    }
}