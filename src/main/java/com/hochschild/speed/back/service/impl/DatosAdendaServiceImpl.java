package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.bandejaEntrada.DatosAdendaBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.filter.bandejaEntrada.AdendaFilter;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.*;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("DatosAdendaService")
public class DatosAdendaServiceImpl implements DatosAdendaService {
    private static final Logger LOGGER = Logger.getLogger(DatosAdendaServiceImpl.class.getName());
    private final RegistrarSolicitudService registrarSolicitudService;
    private final RevisarExpedienteService revisarExpedienteService;
    private final ParametroService parametroService;
    private final UsuarioRepository usuarioRepository;
    private final ExpedienteRepository expedienteRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final HcUbicacionRepository hcUbicacionRepository;
    private final HcPaisRepository hcPaisRepository;
    private final HcCompaniaRepository hcCompaniaRepository;
    private final HcAreaRepository hcAreaRepository;
    private final HcAdendaRepository hcAdendaRepository;
    private final HcTipoContratoRepository hcTipoContratoRepository;
    private final UsuarioAccesoSolicitudRepository usuarioAccesoSolicitudRepository;
    private final MantenimientoUsuarioService mantenimientoUsuarioService;

    @Autowired
    public DatosAdendaServiceImpl(RegistrarSolicitudService registrarSolicitudService,
                                  RevisarExpedienteService revisarExpedienteService,
                                  ParametroService parametroService,
                                  UsuarioRepository usuarioRepository,
                                  ExpedienteRepository expedienteRepository,
                                  HcDocumentoLegalRepository hcDocumentoLegalRepository,
                                  HcUbicacionRepository hcUbicacionRepository,
                                  HcPaisRepository hcPaisRepository,
                                  HcCompaniaRepository hcCompaniaRepository,
                                  HcAreaRepository hcAreaRepository,
                                  HcAdendaRepository hcAdendaRepository, HcTipoContratoRepository hcTipoContratoRepository,
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
        this.hcAreaRepository = hcAreaRepository;
        this.hcAdendaRepository = hcAdendaRepository;
        this.hcTipoContratoRepository = hcTipoContratoRepository;
        this.usuarioAccesoSolicitudRepository = usuarioAccesoSolicitudRepository;
        this.mantenimientoUsuarioService = mantenimientoUsuarioService;
    }

    @Override
    public DatosAdendaBean botonDatosAdenda(AdendaFilter adendaBean, Integer idUsuario) {

        DatosAdendaBean datosAdendaBean = new DatosAdendaBean();

        try {
            HcDocumentoLegal hcDocumentoLegal = hcDocumentoLegalRepository.findByIdExpediente(adendaBean.getIdExpediente());

            //Clean Expediente
            hcDocumentoLegal.setExpediente(null);
            hcDocumentoLegal.setContrato(null);
            hcDocumentoLegal.setAdenda(null);

            Expediente expediente = expedienteRepository.findById(adendaBean.getIdExpediente());
            expediente.setDocumentoLegal(null);
            expediente.setDocumentos(null);

            datosAdendaBean.setIdTraza(adendaBean.getIdTraza());
            datosAdendaBean.setUsuario(usuarioRepository.findById(idUsuario));
            datosAdendaBean.setDocumentoLegal(hcDocumentoLegal);
            datosAdendaBean.setPaises(hcPaisRepository.listarPaises());
            datosAdendaBean.setCompanias(hcCompaniaRepository.getCompaniasActivasPorPais(datosAdendaBean.getIdPais()));
            datosAdendaBean.setUbicaciones(hcUbicacionRepository.obtenerUbicacionesDocumentoLegal(datosAdendaBean.getDocumentoLegal().getId()));
            datosAdendaBean.setRepresentantes(registrarSolicitudService.obtenerRepresentantesDocumentoLegal(datosAdendaBean.getDocumentoLegal().getId()));
            datosAdendaBean.setUbicacionOperacion(hcUbicacionRepository.getUbicacionActivaPorTipoUbicacion(Constantes.TIPO_UBICACION_OPERACION, datosAdendaBean.getIdCompania()));
            datosAdendaBean.setUbicacionProyecto(hcUbicacionRepository.getUbicacionActivaPorTipoUbicacion(Constantes.TIPO_UBICACION_PROYECTO, datosAdendaBean.getIdCompania()));
            datosAdendaBean.setUbicacionOficina(hcUbicacionRepository.getUbicacionActivaPorTipoUbicacion(Constantes.TIPO_UBICACION_OFICINA, datosAdendaBean.getIdCompania()));
            datosAdendaBean.setListaTipoContrato(hcTipoContratoRepository.getListaTipoContrato());
            datosAdendaBean.setListaMonedas(parametroService.buscarParametroPorTipo(Constantes.PARAMETRO_MONEDA));
            datosAdendaBean.setListaProcesos(registrarSolicitudService.buscarTipoAdenda(Constantes.CODIGO_TIPO_CONTRATO, Constantes.ESTADO_CONFIGURACION_HABILITADO));

            if (datosAdendaBean.getDocumentoLegal() != null && datosAdendaBean.getDocumentoLegal().getArea() != null){
                datosAdendaBean.setIdPais(datosAdendaBean.getDocumentoLegal().getArea().getCompania().getPais().getId());
                datosAdendaBean.setIdCompania(datosAdendaBean.getDocumentoLegal().getArea().getCompania().getId());
                datosAdendaBean.setAreas(hcAreaRepository.listarAreasPorCompania(datosAdendaBean.getDocumentoLegal().getArea().getCompania().getId(), Constantes.ESTADO_ACTIVO));
            }

            String nombreTipoProceso = expediente.getProceso().getNombre();
            Integer tipoProcesoAqui = expediente.getProceso().getId();

            Boolean botoneraEstado = false;

            if (datosAdendaBean.getUsuario() != null) {

                LOGGER.info("==================================================");
                LOGGER.info("ID USUARIO: " + datosAdendaBean.getUsuario().getId());
                LOGGER.info("==================================================");

                Boolean tieneAccesoSolicitud = false;

                Traza traza = revisarExpedienteService.obtenerUltimaTraza(expediente.getId(), datosAdendaBean.getUsuario().getId());
                UsuarioPorTraza ut = revisarExpedienteService.obtenerUsuarioPorTraza(traza.getId(), datosAdendaBean.getUsuario().getId());
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
                    Reemplazo reemplazo = revisarExpedienteService.buscarReemplazo(datosAdendaBean.getUsuario().getId(), expediente.getProceso());
                    if (reemplazo != null) {
                        LOGGER.info("================================================== B");
                        ut = revisarExpedienteService.obtenerUsuarioPorTraza(traza.getId(), reemplazo.getReemplazado().getId());
                    }
                }
                if (ut != null || tieneAccesoSolicitud == true) {

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

            if (botoneraEstado) {
                datosAdendaBean.setBotoneraActiva(true);
            } else {
                datosAdendaBean.setBotoneraActiva(false);
            }

            LOGGER.info("---------------------------------------------------------------------------------------");
            LOGGER.info("Tipo de proceso + datosContrato/revisarAdenda");
            LOGGER.info(nombreTipoProceso);
            LOGGER.info("---------------------------------------------------------------------------------------");

            Integer idDocumentoLegal = datosAdendaBean.getDocumentoLegal().getId();

            LOGGER.info("---------------------------------------------------------------------------------------");
            LOGGER.info("IdDocumentoLegal + datosContrato/revisarAdenda");
            LOGGER.info(idDocumentoLegal);
            LOGGER.info("---------------------------------------------------------------------------------------");

            HcAdenda hcAdenda = hcAdendaRepository.findById(idDocumentoLegal);
            hcAdenda.setDocumentoLegal(null);
            hcAdenda.setContrato(null);
            datosAdendaBean.setAdenda(hcAdenda);

        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }

        return datosAdendaBean;
    }
}