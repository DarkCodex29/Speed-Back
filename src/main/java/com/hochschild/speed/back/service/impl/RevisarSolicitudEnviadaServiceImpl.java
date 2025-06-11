package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.bandejaEntrada.DatosAdendaBean;
import com.hochschild.speed.back.model.bean.bandejaEntrada.DatosContratoBean;
import com.hochschild.speed.back.model.bean.revisarSolicitud.DetalleSolicitudEnviadaBean;
import com.hochschild.speed.back.model.bean.revisarSolicitud.SolicitudEnviadaBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.*;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class RevisarSolicitudEnviadaServiceImpl implements RevisarSolicitudEnviadaService {
    private static final Logger LOGGER = Logger.getLogger(RevisarSolicitudEnviadaServiceImpl.class.getName());
    private final RegistrarSolicitudService registrarSolicitudService;
    private final RevisarExpedienteService revisarExpedienteService;
    private final ParametroService parametroService;
    private final UsuarioRepository usuarioRepository;
    private final ExpedienteRepository expedienteRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final HcUbicacionRepository hcUbicacionRepository;
    private final HcPaisRepository hcPaisRepository;
    private final HcCompaniaRepository hcCompaniaRepository;
    private final HcAdendaRepository hcAdendaRepository;
    private final HcPenalidadPorDocumentoLegalRepository hcPenalidadPorDocumentoLegalRepository;
    private final HcAreaRepository hcAreaRepository;
    private final HcTipoContratoRepository hcTipoContratoRepository;
    private final UsuarioAccesoSolicitudRepository usuarioAccesoSolicitudRepository;
    private final MantenimientoUsuarioService mantenimientoUsuarioService;

    @Autowired
    public RevisarSolicitudEnviadaServiceImpl(RegistrarSolicitudService registrarSolicitudService,
                                              RevisarExpedienteService revisarExpedienteService,
                                              ParametroService parametroService,
                                              UsuarioRepository usuarioRepository,
                                              ExpedienteRepository expedienteRepository,
                                              HcDocumentoLegalRepository hcDocumentoLegalRepository,
                                              HcUbicacionRepository hcUbicacionRepository,
                                              HcPaisRepository hcPaisRepository,
                                              HcCompaniaRepository hcCompaniaRepository,
                                              HcAdendaRepository hcAdendaRepository,
                                              HcPenalidadPorDocumentoLegalRepository hcPenalidadPorDocumentoLegalRepository,
                                              HcAreaRepository hcAreaRepository,
                                              HcTipoContratoRepository hcTipoContratoRepository,
                                              UsuarioAccesoSolicitudRepository usuarioAccesoSolicitudRepository,
                                              MantenimientoUsuarioService mantenimientoUsuarioService) {
        this.registrarSolicitudService = registrarSolicitudService;
        this.revisarExpedienteService = revisarExpedienteService;
        this.parametroService = parametroService;
        this.usuarioRepository = usuarioRepository;
        this.expedienteRepository = expedienteRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.hcUbicacionRepository = hcUbicacionRepository;
        this.hcPaisRepository = hcPaisRepository;
        this.hcCompaniaRepository = hcCompaniaRepository;
        this.hcAdendaRepository = hcAdendaRepository;
        this.hcPenalidadPorDocumentoLegalRepository = hcPenalidadPorDocumentoLegalRepository;
        this.hcAreaRepository = hcAreaRepository;
        this.hcTipoContratoRepository = hcTipoContratoRepository;
        this.usuarioAccesoSolicitudRepository = usuarioAccesoSolicitudRepository;
        this.mantenimientoUsuarioService = mantenimientoUsuarioService;
    }

    @Override
    public DetalleSolicitudEnviadaBean revisarSolicitudesPorEnviar(SolicitudEnviadaBean solicitudEnviadaBean, Integer idUsuario) {

        DetalleSolicitudEnviadaBean detalleSolicitudEnviadaBean = new DetalleSolicitudEnviadaBean();

        try {
            if (solicitudEnviadaBean.getIdProceso().equals(Constantes.PROCESO_CONTRATO)){

                DatosContratoBean datosContratoBean = new DatosContratoBean();
                HcDocumentoLegal hcDocumentoLegal = hcDocumentoLegalRepository.findByIdExpediente(solicitudEnviadaBean.getIdExpediente());
                List<HcPenalidadPorDocumentoLegal> penalidades = hcPenalidadPorDocumentoLegalRepository.obtenerPorIdExpediente(solicitudEnviadaBean.getIdExpediente());

                Expediente expediente = expedienteRepository.findById(solicitudEnviadaBean.getIdExpediente());
                hcDocumentoLegal.setExpediente(expediente);

                datosContratoBean.setIdTraza(null);
                datosContratoBean.setUsuario(usuarioRepository.findById(idUsuario));
                datosContratoBean.setDocumentoLegal(hcDocumentoLegal);
                datosContratoBean.setIdPais(datosContratoBean.getDocumentoLegal().getArea().getCompania().getPais().getId());
                datosContratoBean.setIdCompania(datosContratoBean.getDocumentoLegal().getArea().getCompania().getId());
                datosContratoBean.setPaises(hcPaisRepository.listarPaises());
                datosContratoBean.setCompanias(hcCompaniaRepository.getCompaniasActivasPorPais(datosContratoBean.getIdPais()));
                datosContratoBean.setAreas(hcAreaRepository.listarAreasPorCompania(datosContratoBean.getDocumentoLegal().getArea().getCompania().getId(), Constantes.ESTADO_ACTIVO));
                datosContratoBean.setUbicaciones(hcUbicacionRepository.obtenerUbicacionesDocumentoLegal(datosContratoBean.getDocumentoLegal().getId()));
                datosContratoBean.setRepresentantes(registrarSolicitudService.obtenerRepresentantesDocumentoLegal(datosContratoBean.getDocumentoLegal().getId()));
                datosContratoBean.setUbicacionOperacion(hcUbicacionRepository.getUbicacionActivaPorTipoUbicacion(Constantes.TIPO_UBICACION_OPERACION, datosContratoBean.getIdCompania()));
                datosContratoBean.setUbicacionProyecto(hcUbicacionRepository.getUbicacionActivaPorTipoUbicacion(Constantes.TIPO_UBICACION_PROYECTO, datosContratoBean.getIdCompania()));
                datosContratoBean.setUbicacionOficina(hcUbicacionRepository.getUbicacionActivaPorTipoUbicacion(Constantes.TIPO_UBICACION_OFICINA, datosContratoBean.getIdCompania()));
                datosContratoBean.setListaTipoContrato(hcTipoContratoRepository.getListaTipoContrato());
                datosContratoBean.setListaMonedas(parametroService.buscarParametroPorTipo(Constantes.PARAMETRO_MONEDA));

                //boolean cambiarResponsable = false;
                boolean botoneraEstado = false;
                boolean tieneAccesoSolicitud = false;

                if (datosContratoBean.getUsuario() != null) {
                    Traza traza = revisarExpedienteService.obtenerUltimaTraza(hcDocumentoLegal.getExpediente().getId(), datosContratoBean.getUsuario().getId());
                    UsuarioPorTraza ut = null;

                    if (ut != null){
                        ut = revisarExpedienteService.obtenerUsuarioPorTraza(traza.getId(), datosContratoBean.getUsuario().getId());
                    }
                    List<UsuarioAccesoSolicitud> representantesConAcceso = usuarioAccesoSolicitudRepository.getUsuariosConAcceso(Constantes.ESTADO_ACCESO_REPRESENTANTE_HABILITADO);

                    //Validación para buscar representante
                    for (UsuarioAccesoSolicitud usuarioAccesoSolicitud : representantesConAcceso) {
                        if (datosContratoBean.getUsuario().getId().equals(usuarioAccesoSolicitud.getResponsable().getId())) {
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

                datosContratoBean.setBotoneraActiva(botoneraEstado);

                Expediente expedienteFormateado = datosContratoBean.getDocumentoLegal().getExpediente();
                HcDocumentoLegal hcDocumentoLegalFormateado = datosContratoBean.getDocumentoLegal();
                HcContrato contratoFormateado = hcDocumentoLegalFormateado.getContrato();
                HcAdenda adendaFormateada = hcDocumentoLegalFormateado.getAdenda();
                List<Documento> documentosFormateado = new ArrayList<>();

                //Documentos
                for (int i = 0; i < expedienteFormateado.getDocumentos().size(); i++) {
                    Documento documento = expedienteFormateado.getDocumentos().get(i);
                    List<Archivo> archivosPorDocumento = new ArrayList<>();
                    documento.setExpediente(null);

                    for (Archivo file : documento.getArchivos()) {
                        for (Version version : file.getVersions()) {
                            version.setArchivo(null);
                        }
                        file.setDocumento(null);

                        if (file.getEstado().equals(Constantes.ESTADO_ACTIVO)){
                            File arc = new File(file.getRutaLocal());
                            file.setNombreArchivoDisco(arc.getName());
                            archivosPorDocumento.add(file);
                        }
                    }
                    documento.setArchivos(archivosPorDocumento);
                    documentosFormateado.add(documento);
                }
                datosContratoBean.setDocumentos(documentosFormateado);

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
                for (HcPenalidadPorDocumentoLegal hcPenalidadPorDocumentoLegal : penalidades) {
                    hcPenalidadPorDocumentoLegal.setDocumentoLegal(null);
                }

                hcDocumentoLegalFormateado.setAdenda(adendaFormateada);
                datosContratoBean.setPenalidades(penalidades);
                datosContratoBean.setDocumentoLegal(hcDocumentoLegalFormateado);

                //Datos Contrato
                detalleSolicitudEnviadaBean.setDatosContratoBean(datosContratoBean);
                detalleSolicitudEnviadaBean.setDatosAdendaBean(null);
            }

            if (solicitudEnviadaBean.getIdProceso().equals(Constantes.PROCESO_ADENDA) ||
                    solicitudEnviadaBean.getIdProceso().equals(Constantes.PROCESO_ADENDA_AUTOMATICA)){

                DatosAdendaBean datosAdendaBean = new DatosAdendaBean();
                HcDocumentoLegal hcDocumentoLegal = hcDocumentoLegalRepository.findByIdExpediente(solicitudEnviadaBean.getIdExpediente());


                datosAdendaBean.setUsuario(usuarioRepository.findById(idUsuario));
                datosAdendaBean.setDocumentoLegal(hcDocumentoLegal);
                datosAdendaBean.setIdPais(datosAdendaBean.getDocumentoLegal().getArea().getCompania().getPais().getId());
                datosAdendaBean.setIdCompania(datosAdendaBean.getDocumentoLegal().getArea().getCompania().getId());
                datosAdendaBean.setPaises(hcPaisRepository.listarPaises());
                datosAdendaBean.setCompanias(hcCompaniaRepository.getCompaniasActivasPorPais(datosAdendaBean.getIdPais()));
                datosAdendaBean.setAreas(hcAreaRepository.listarAreasPorCompania(datosAdendaBean.getDocumentoLegal().getArea().getCompania().getId(), Constantes.ESTADO_ACTIVO));
                datosAdendaBean.setUbicaciones(hcUbicacionRepository.obtenerUbicacionesDocumentoLegal(datosAdendaBean.getDocumentoLegal().getId()));
                datosAdendaBean.setRepresentantes(registrarSolicitudService.obtenerRepresentantesDocumentoLegal(datosAdendaBean.getDocumentoLegal().getId()));
                datosAdendaBean.setUbicacionOperacion(hcUbicacionRepository.getUbicacionActivaPorTipoUbicacion(Constantes.TIPO_UBICACION_OPERACION, datosAdendaBean.getIdCompania()));
                datosAdendaBean.setUbicacionProyecto(hcUbicacionRepository.getUbicacionActivaPorTipoUbicacion(Constantes.TIPO_UBICACION_PROYECTO, datosAdendaBean.getIdCompania()));
                datosAdendaBean.setUbicacionOficina(hcUbicacionRepository.getUbicacionActivaPorTipoUbicacion(Constantes.TIPO_UBICACION_OFICINA, datosAdendaBean.getIdCompania()));
                datosAdendaBean.setListaTipoContrato(hcTipoContratoRepository.getListaTipoContrato());
                datosAdendaBean.setListaMonedas(parametroService.buscarParametroPorTipo(Constantes.PARAMETRO_MONEDA));
                datosAdendaBean.setListaProcesos(registrarSolicitudService.buscarTipoAdenda(Constantes.CODIGO_TIPO_CONTRATO, Constantes.ESTADO_CONFIGURACION_HABILITADO));

                boolean botoneraEstado = false;

                if (datosAdendaBean.getUsuario() != null) {

                    LOGGER.info("==================================================");
                    LOGGER.info("ID USUARIO: " + datosAdendaBean.getUsuario().getId());
                    LOGGER.info("==================================================");

                    boolean tieneAccesoSolicitud = false;
                    Traza traza = revisarExpedienteService.obtenerUltimaTraza(hcDocumentoLegal.getExpediente().getId(), datosAdendaBean.getUsuario().getId());
                    UsuarioPorTraza ut = null;
                    if (traza != null){
                        ut = revisarExpedienteService.obtenerUsuarioPorTraza(traza.getId(), datosAdendaBean.getUsuario().getId());
                    }
                    List<UsuarioAccesoSolicitud> representantesConAcceso = usuarioAccesoSolicitudRepository.getUsuariosConAcceso(Constantes.ESTADO_ACCESO_REPRESENTANTE_HABILITADO);

                    //Validación para buscar representante
                    for (int i = 0; i < representantesConAcceso.size(); i++) {
                        if (datosAdendaBean.getUsuario().getId().equals(representantesConAcceso.get(i).getResponsable().getId())) {
                            LOGGER.info("-----------------------------------RESULT--------------------------------------------");
                            LOGGER.info("COINCIDENCIA DE USUARIO");
                            tieneAccesoSolicitud = true;
                            LOGGER.info("-------------------------------------------------------------------------------");
                            break;
                        }
                    }

                    if (datosAdendaBean.getDocumentoLegal().getEstado().equals(Constantes.ESTADO_HC_ENVIADO_VISADO)) {
                        tieneAccesoSolicitud = false;
                        LOGGER.info("SE ENCUENTRA EN ESTADO ENVIADO_VISADO = " + tieneAccesoSolicitud);
                    }

                    if (ut == null) {
                        LOGGER.info("==================================================  A");
                        // Verificamos si el usuario esta reemplazando a otro usuario
                        Reemplazo reemplazo = revisarExpedienteService.buscarReemplazo(datosAdendaBean.getUsuario().getId(), hcDocumentoLegal.getExpediente().getProceso());
                        if (reemplazo != null) {
                            LOGGER.info("================================================== B");
                            ut = revisarExpedienteService.obtenerUsuarioPorTraza(traza.getId(), reemplazo.getReemplazado().getId());
                        }
                    }
                    if (ut != null || tieneAccesoSolicitud) {

                        LOGGER.info(datosAdendaBean.getDocumentoLegal().getEstado());
                        List<Rol> roles = mantenimientoUsuarioService.buscarRolesExistente(datosAdendaBean.getUsuario().getId());

                        if (datosAdendaBean.getDocumentoLegal().getEstado().charValue() == Constantes.ESTADO_HC_EN_SOLICITUD) {
                            LOGGER.info("================================================== C");
                            for (Rol rol : roles) {
                                if (rol.getCodigo().equals(Constantes.CODIGO_ROL_SOLICITANTE)) {
                                    //cambiarResponsable = true;
                                    LOGGER.info("================================================== D");
                                    botoneraEstado = true;
                                }
                            }
                        }

                        if (datosAdendaBean.getDocumentoLegal().getEstado().compareTo(Constantes.ESTADO_HC_VISADO) == 0
                                || datosAdendaBean.getDocumentoLegal().getEstado().charValue() == Constantes.ESTADO_HC_ENVIADO_VISADO
                                || datosAdendaBean.getDocumentoLegal().getEstado().charValue() == Constantes.ESTADO_HC_EN_ELABORACION
                                || datosAdendaBean.getDocumentoLegal().getEstado().charValue() == Constantes.ESTADO_HC_SOLICITUD_ENVIADA
                                || datosAdendaBean.getDocumentoLegal().getEstado().charValue() == Constantes.ESTADO_HC_ELABORADO
                                || datosAdendaBean.getDocumentoLegal().getEstado().charValue() == Constantes.ESTADO_HC_VIGENTE
                                || datosAdendaBean.getDocumentoLegal().getEstado().charValue() == Constantes.ESTADO_HC_VENCIDO) {
                            for (Rol rol : roles) {

                                LOGGER.info("ROL ID = " + rol.getId());
                                LOGGER.info("ROL CODIGO = " + rol.getCodigo());
                                LOGGER.info("ROL NOMBRE = " + rol.getNombre());

                                if (rol.getCodigo().equals(Constantes.CODIGO_ROL_ABOGADO_RESPONSABLE)) {
                                    botoneraEstado = true;
                                }
                                if (rol.getCodigo().equals(Constantes.CODIGO_ROL_ABOGADO)) {
                                    botoneraEstado = true;
                                }
                            }
                        }

                        if (datosAdendaBean.getDocumentoLegal().getEstado().charValue() == Constantes.ESTADO_HC_ENVIADO_FIRMA
                                || datosAdendaBean.getDocumentoLegal().getEstado().charValue() == Constantes.ESTADO_HC_COMUNICACION) {
                            for (Rol rol : roles) {
                                if (rol.getCodigo().equals(Constantes.CODIGO_ROL_ABOGADO_RESPONSABLE)) {
                                    LOGGER.info("================================================== G");
                                    botoneraEstado = true;
                                }
                                if (rol.getCodigo().equals(Constantes.CODIGO_ROL_ABOGADO)) {
                                    LOGGER.info("================================================== H");
                                    botoneraEstado = true;
                                }
                            }
                        }
                    }
                }

                datosAdendaBean.setBotoneraActiva(botoneraEstado);

                LOGGER.info("---------------------------------------------------------------------------------------");
                LOGGER.info("Tipo de proceso + datosContrato/revisarAdenda");
                LOGGER.info(hcDocumentoLegal.getExpediente().getProceso().getNombre());
                LOGGER.info("---------------------------------------------------------------------------------------");

                Integer idDocumentoLegal = datosAdendaBean.getDocumentoLegal().getId();

                LOGGER.info("---------------------------------------------------------------------------------------");
                LOGGER.info("IdDocumentoLegal + datosContrato/revisarAdenda");
                LOGGER.info(idDocumentoLegal);
                LOGGER.info("---------------------------------------------------------------------------------------");

                HcAdenda hcAdenda = hcAdendaRepository.findById(idDocumentoLegal);

                List<Documento> documentosFormateado = new ArrayList<>();
                //Documentos
                if(hcAdenda.getDocumentoLegal().getExpediente() != null
                        &&hcAdenda.getDocumentoLegal().getExpediente().getDocumentos()!=null){
                    for (int i = 0; i < hcAdenda.getDocumentoLegal().getExpediente().getDocumentos().size(); i++) {
                        Documento documento = hcAdenda.getDocumentoLegal().getExpediente().getDocumentos().get(i);
                        List<Archivo> archivosPorDocumento = new ArrayList<>();
                        documento.setExpediente(null);

                        for (Archivo file : documento.getArchivos()) {
                            for (Version version : file.getVersions()) {
                                version.setArchivo(null);
                            }
                            file.setDocumento(null);

                            if (file.getEstado().equals(Constantes.ESTADO_ACTIVO)){
                                File arc = new File(file.getRutaLocal());
                                file.setNombreArchivoDisco(arc.getName());
                                archivosPorDocumento.add(file);
                            }
                        }
                        documento.setArchivos(archivosPorDocumento);
                        documentosFormateado.add(documento);
                    }
                }

                datosAdendaBean.setDocumentos(documentosFormateado);

                //Format Adenda
                hcAdenda.setDocumentoLegal(null);
                if(hcAdenda.getContrato()!=null){
                    hcAdenda.getContrato().setDocumentoLegal(null);
                }
                datosAdendaBean.setAdenda(hcAdenda);

                //Datos Adenda
                detalleSolicitudEnviadaBean.setDatosContratoBean(null);
                detalleSolicitudEnviadaBean.setDatosAdendaBean(datosAdendaBean);

                //Clean Expediente
                hcDocumentoLegal.getExpediente().setDocumentos(null);
                hcDocumentoLegal.getExpediente().setDocumentoLegal(null);
                hcDocumentoLegal.setContrato(null);
                hcDocumentoLegal.setAdenda(null);

                datosAdendaBean.setIdTraza(null);
            }

        } catch (Exception ex) {
            LOGGER.info(ex.getMessage(), ex);
        }
        return detalleSolicitudEnviadaBean;
    }
}