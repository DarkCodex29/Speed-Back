package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.registrarSolicitud.*;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.AdjuntarDocumentoService;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.RegistrarExpedienteService;
import com.hochschild.speed.back.service.RegistroSolicitudService;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class RegistroSolicitudServiceImpl implements RegistroSolicitudService {
    private static final Logger LOGGER = Logger.getLogger(RegistroSolicitudServiceImpl.class.getName());
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final DocumentoRepository documentoRepository;
    private final ClienteRepository clienteRepository;
    private final HcAreaRepository hcAreaRepository;
    private final ExpedienteRepository expedienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioPorRolRepository usuarioPorRolRepository;
    private final HcContratoRepository hcContratoRepository;
    private final HcAdendaRepository hcAdendaRepository;
    private final ParametroRepository parametroRepository;
    private final RolRepository rolRepository;
    private final HcUbicacionRepository hcUbicacionRepository;
    private final HcTipoContratoRepository hcTipoContratoRepository;
    private final HcRepresentantePorDocumentoRepository hcRepresentantePorDocumentoRepository;
    private final HcRepresentantePorContraparteRepository hcRepresentantePorContraparteRepository;
    private final HcUbicacionPorDocumentoRepository hcUbicacionPorDocumentoRepository;
    private final HcTipoContratoConfiguracionRepository hcTipoContratoConfiguracionRepository;
    private final HcPenalidadPorDocumentoLegalRepository hcPenalidadPorDocumentoLegalRepository;
    private final HcReiteranciaPorPenalidadRepository hcReiteranciaPorPenalidadRepository;
    private final HcPenalidadRepository hcPenalidadRepository;
    private final RegistrarExpedienteService registrarExpedienteService;
    private final CommonBusinessLogicService commonService;

    private final AdjuntarDocumentoService adjuntarDocumentoService;

    @Autowired
    public RegistroSolicitudServiceImpl(HcDocumentoLegalRepository hcDocumentoLegalRepository,
                                        DocumentoRepository documentoRepository,
                                        HcAreaRepository hcAreaRepository,
                                        ClienteRepository clienteRepository,
                                        ExpedienteRepository expedienteRepository,
                                        UsuarioRepository usuarioRepository,
                                        UsuarioPorRolRepository usuarioPorRolRepository,
                                        HcContratoRepository hcContratoRepository,
                                        HcAdendaRepository hcAdendaRepository,
                                        ParametroRepository parametroRepository,
                                        RolRepository rolRepository,
                                        HcUbicacionRepository hcUbicacionRepository,
                                        HcTipoContratoRepository hcTipoContratoRepository,
                                        HcRepresentantePorDocumentoRepository hcRepresentantePorDocumentoRepository,
                                        HcRepresentantePorContraparteRepository hcRepresentantePorContraparteRepository,
                                        HcUbicacionPorDocumentoRepository hcUbicacionPorDocumentoRepository,
                                        HcTipoContratoConfiguracionRepository hcTipoContratoConfiguracionRepository,
                                        HcPenalidadPorDocumentoLegalRepository hcPenalidadPorDocumentoLegalRepository,
                                        HcReiteranciaPorPenalidadRepository hcReiteranciaPorPenalidadRepository,
                                        HcPenalidadRepository hcPenalidadRepository,
                                        RegistrarExpedienteService registrarExpedienteService,
                                        CommonBusinessLogicService commonService,
                                        AdjuntarDocumentoService adjuntarDocumentoService) {
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.documentoRepository = documentoRepository;
        this.hcAreaRepository = hcAreaRepository;
        this.clienteRepository = clienteRepository;
        this.expedienteRepository = expedienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioPorRolRepository = usuarioPorRolRepository;
        this.hcContratoRepository = hcContratoRepository;
        this.hcAdendaRepository = hcAdendaRepository;
        this.parametroRepository = parametroRepository;
        this.rolRepository = rolRepository;
        this.hcUbicacionRepository = hcUbicacionRepository;
        this.hcTipoContratoRepository = hcTipoContratoRepository;
        this.hcRepresentantePorDocumentoRepository = hcRepresentantePorDocumentoRepository;
        this.hcRepresentantePorContraparteRepository = hcRepresentantePorContraparteRepository;
        this.hcUbicacionPorDocumentoRepository = hcUbicacionPorDocumentoRepository;
        this.hcTipoContratoConfiguracionRepository = hcTipoContratoConfiguracionRepository;
        this.hcPenalidadPorDocumentoLegalRepository = hcPenalidadPorDocumentoLegalRepository;
        this.hcReiteranciaPorPenalidadRepository = hcReiteranciaPorPenalidadRepository;
        this.hcPenalidadRepository = hcPenalidadRepository;
        this.registrarExpedienteService = registrarExpedienteService;
        this.commonService = commonService;
        this.adjuntarDocumentoService=adjuntarDocumentoService;
    }

    @Override
    public List<Usuario> buscarUsuarioSolicitante(String codigoRol) {

        List<Usuario> usuarios = new ArrayList<>();

        try {
            usuarios = usuarioRepository.buscarUsuariosRol(codigoRol);

        } catch (Exception ex) {
            LOGGER.info(ex);
        }

        return usuarios;
    }

    @Override
    @Transactional
    public List<Usuario> buscarUsuarioResponsable(String codigoRol) {

        List<Usuario> usuarios = new ArrayList<>();
        List<UsuarioPorRol> usuarioPorRoles;
        Rol rol = rolRepository.findByCodigo(codigoRol);

        try {
            usuarioPorRoles = usuarioPorRolRepository.findByIdRol(rol.getId());
            for (UsuarioPorRol usuarioPorRol : usuarioPorRoles) {
                usuarios.add(usuarioPorRol.getId().getUsuario());
            }

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return usuarios;
    }

    @Override
    public List<Cliente> buscarClientesContraparte(Character estado) {

        List<Cliente> result = new ArrayList<>();

        try {
            result = clienteRepository.buscarClientesContraparte(estado);
        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
            result = new ArrayList<>();
        }
        return result;
    }

    @Override
    @Transactional
    public List<Cliente> buscarClientesRepresentanteLegal(Character estado) {

        List<Cliente> usuarios = new ArrayList<>();
        try {
            usuarios = clienteRepository.buscarClientesRepresentanteLegal(estado);
        } catch (Exception ex) {
            LOGGER.info(ex);
        }

        return usuarios;
    }

    @Override
    @Transactional
    public List<HcArea> listarAreasPorCompania(Integer idCompania, Character estado) {

        List<HcArea> areas = new ArrayList<>();
        try {
            areas = hcAreaRepository.listarAreasPorCompania(idCompania, estado);
        } catch (Exception ex) {
            LOGGER.info(ex);
        }

        return areas;
    }

    @Override
    @Transactional
    public List<HcUbicacion> obtenerUbicacionOperacionPorPorCompania(String operacion, Integer idCompania, Character estado) {

        List<HcUbicacion> ubicaciones = new ArrayList<>();
        try {
            ubicaciones = hcUbicacionRepository.getUbicacionActivaPorTipo(operacion, idCompania, estado);
        } catch (Exception ex) {
            LOGGER.info(ex);
        }

        return ubicaciones;
    }

    @Override
    @Transactional
    public List<HcUbicacion> obtenerUbicacionOficinaPorPorCompania(String operacion, Integer idCompania, Character estado) {

        List<HcUbicacion> ubicaciones = new ArrayList<>();
        try {
            ubicaciones = hcUbicacionRepository.getUbicacionActivaPorTipo(operacion, idCompania, estado);
        } catch (Exception ex) {
            LOGGER.info(ex);
        }

        return ubicaciones;
    }

    @Override
    @Transactional
    public List<HcUbicacion> obtenerUbicacionProyectoPorPorCompania(String operacion, Integer idCompania, Character estado) {

        List<HcUbicacion> ubicaciones = new ArrayList<>();
        try {
            ubicaciones = hcUbicacionRepository.getUbicacionActivaPorTipo(operacion, idCompania, estado);
        } catch (Exception ex) {
            LOGGER.info(ex);
        }

        return ubicaciones;
    }

    @Override
    public List<HcUbicacion> obtenerUbicacionExploracionPorPorCompania(String operacion, Integer idCompania, Character estado) {

        List<HcUbicacion> ubicaciones = new ArrayList<>();
        try {
            ubicaciones = hcUbicacionRepository.getUbicacionActivaPorTipo(operacion, idCompania, estado);
        } catch (Exception ex) {
            LOGGER.info(ex);
        }

        return ubicaciones;
    }

    @Override
    @Transactional
    public List<Cliente> obtenerClientesRepresentantesContraparte(Integer idContraparte) {

        List<HcRepresentantePorContraparte> representantePorContrapartes = new ArrayList<>();
        List<Cliente> clientes = new ArrayList<>();
        try {
            representantePorContrapartes = hcRepresentantePorContraparteRepository.obtenerRepresentantesContraparte(idContraparte);

            for (HcRepresentantePorContraparte hcRepresentantePorContraparte : representantePorContrapartes) {
                clientes.add(hcRepresentantePorContraparte.getId().getRepresentante());
            }

        } catch (Exception ex) {
            LOGGER.info(ex);
        }

        return clientes;
    }

    @Override
    @Transactional
    public List<ContratoSumillaBean> autocompletarContrato(AutoContratoBean autoContratoBean) {

        List<ContratoSumillaBean> contratosLegales = new ArrayList<>();
        try {
            //contratosLegales = registroSolicitudDao.getContratoByAdenda(autoContratoBean.getNumeroSumilla());
            autoContratoBean.setNumeroSumilla("%" + autoContratoBean.getNumeroSumilla() + "%");
            //autoContratoBean.setNumeroSumilla("%" + "alquiler"+"%");
            LOGGER.info("ENV - Sumilla : " + autoContratoBean.getNumeroSumilla());

            contratosLegales = hcDocumentoLegalRepository.autocompletarContrato(autoContratoBean.getNumeroSumilla());


        } catch (Exception ex) {
            LOGGER.info(ex);
        }

        return contratosLegales;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseModel guardarHcDocumentoLegal(DocumentoLegalBean documentoLegalBean, Integer idUsuario) {

        ResponseModel responseModel = new ResponseModel();

        try {
            HcDocumentoLegal documentoLegal;
            LOGGER.info("ID EXPEDIENTE: " + documentoLegalBean.getIdExpediente());
            Usuario usuario = usuarioRepository.findById(idUsuario);

            //Save DocumentoLegal
            documentoLegal = guardarHcDocumentoLegal(documentoLegalBean, usuario);

            if (documentoLegalBean.getIdDocumentoLegal() != null) {
                LOGGER.info("GuardarDocumentoLegal - Id = " + documentoLegalBean.getIdDocumentoLegal());
                borrarRepresentantesDocumentoLegal(documentoLegalBean.getIdDocumentoLegal());
                borrarUbicacionesDocumentoLegal(documentoLegalBean.getIdDocumentoLegal());
            }

            if (documentoLegalBean.getEsContrato() && documentoLegalBean.getContrato() != null) {

                LOGGER.info("----------------------------------------------");
                LOGGER.info("UPDATE REPRESENTANTES Y UBICACIONES - CONTRATO");
                LOGGER.info("----------------------------------------------");

                if (documentoLegalBean.getContrato().getIdContraparte() != null) {
                    //Borramos los representantes antiguos
                    borrarRepresentantesContraparte(documentoLegalBean.getContrato().getIdContraparte());
                }
                if (documentoLegalBean.getContrato().getRepresentantesLegales() != null && !documentoLegalBean.getContrato().getRepresentantesLegales().isEmpty()) {

                    for (RepresentanteLegalBean x : documentoLegalBean.getContrato().getRepresentantesLegales()) {

                        HcRepresentantePorDocumentoPk representanteDocumentoPk = new HcRepresentantePorDocumentoPk();

                        Cliente rep = new Cliente();
                        rep.setId(x.getIdRepresentanteLegal());
                        representanteDocumentoPk.setCliente(rep);
                        representanteDocumentoPk.setDocumentoLegal(documentoLegal);

                        HcRepresentantePorDocumento hcRepresentantePorDocumento = new HcRepresentantePorDocumento();
                        hcRepresentantePorDocumento.setId(representanteDocumentoPk);
                        hcRepresentantePorDocumentoRepository.save(hcRepresentantePorDocumento);

                        if (documentoLegal.getContraparte() != null && documentoLegal.getContraparte().getId() != null) {

                            //Registramos el representante nuevo
                            HcRepresentantePorContrapartePK representanteContrapartePk = new HcRepresentantePorContrapartePK();
                            Cliente cnt = new Cliente();
                            cnt.setId(documentoLegal.getContraparte().getId());
                            representanteContrapartePk.setContraparte(cnt);
                            representanteContrapartePk.setRepresentante(rep);

                            HcRepresentantePorContraparte hcRepresentantePorContraparte = new HcRepresentantePorContraparte();
                            hcRepresentantePorContraparte.setId(representanteContrapartePk);
                            hcRepresentantePorContraparteRepository.save(hcRepresentantePorContraparte);
                        }
                    }
                }

                if (documentoLegalBean.getContrato().getUbicaciones() != null && !documentoLegalBean.getContrato().getUbicaciones().isEmpty()) {

                    for (UbicacionesBean x : documentoLegalBean.getContrato().getUbicaciones()) {

                        HcUbicacionPorDocumentoPk ubicacionDocumentoPk = new HcUbicacionPorDocumentoPk();
                        HcUbicacion hcUbicacion = new HcUbicacion();

                        hcUbicacion.setId(x.getIdUbicacion());
                        ubicacionDocumentoPk.setUbicacion(hcUbicacion);
                        ubicacionDocumentoPk.setDocumentoLegal(documentoLegal);

                        HcUbicacionPorDocumento hcUbicacionPorDocumento = new HcUbicacionPorDocumento();
                        hcUbicacionPorDocumento.setId(ubicacionDocumentoPk);
                        hcUbicacionPorDocumentoRepository.save(hcUbicacionPorDocumento);
                    }
                }
            }

            else if ((documentoLegalBean.getEsAdenda() || documentoLegalBean.getEsAdendaAutomatica()) && documentoLegalBean.getAdenda() != null) {

                LOGGER.info("--------------------------------------------");
                LOGGER.info("UPDATE REPRESENTANTES Y UBICACIONES - ADENDA");
                LOGGER.info("--------------------------------------------");

                if (documentoLegalBean.getAdenda().getIdContraparte() != null) {
                    //Borramos los representantes antiguos
                    borrarRepresentantesContraparte(documentoLegalBean.getAdenda().getIdContraparte());
                }

                if (documentoLegalBean.getAdenda().getRepresentantesLegales() != null && !documentoLegalBean.getAdenda().getRepresentantesLegales().isEmpty()) {

                    for (RepresentanteLegalBean x : documentoLegalBean.getAdenda().getRepresentantesLegales()) {

                        HcRepresentantePorDocumentoPk representanteDocumentoPk = new HcRepresentantePorDocumentoPk();

                        Cliente rep = new Cliente();
                        rep.setId(x.getIdRepresentanteLegal());
                        representanteDocumentoPk.setCliente(rep);
                        representanteDocumentoPk.setDocumentoLegal(documentoLegal);

                        HcRepresentantePorDocumento hcRepresentantePorDocumento = new HcRepresentantePorDocumento();
                        hcRepresentantePorDocumento.setId(representanteDocumentoPk);
                        hcRepresentantePorDocumentoRepository.save(hcRepresentantePorDocumento);

                        if (documentoLegal.getContraparte() != null && documentoLegal.getContraparte().getId() != null) {

                            //Registramos el representante nuevo
                            HcRepresentantePorContrapartePK representanteContrapartePk = new HcRepresentantePorContrapartePK();
                            Cliente cnt = new Cliente();
                            cnt.setId(documentoLegal.getContraparte().getId());
                            representanteContrapartePk.setContraparte(cnt);
                            representanteContrapartePk.setRepresentante(rep);

                            HcRepresentantePorContraparte hcRepresentantePorContraparte = new HcRepresentantePorContraparte();
                            hcRepresentantePorContraparte.setId(representanteContrapartePk);
                            hcRepresentantePorContraparteRepository.save(hcRepresentantePorContraparte);
                        }
                    }
                }

                if (documentoLegalBean.getAdenda().getUbicaciones() != null && !documentoLegalBean.getAdenda().getUbicaciones().isEmpty()) {

                    for (UbicacionesBean x : documentoLegalBean.getAdenda().getUbicaciones()) {

                        HcUbicacionPorDocumentoPk ubicacionDocumentoPk = new HcUbicacionPorDocumentoPk();
                        HcUbicacion hcUbicacion = new HcUbicacion();

                        hcUbicacion.setId(x.getIdUbicacion());
                        ubicacionDocumentoPk.setUbicacion(hcUbicacion);
                        ubicacionDocumentoPk.setDocumentoLegal(documentoLegal);

                        HcUbicacionPorDocumento hcUbicacionPorDocumento = new HcUbicacionPorDocumento();
                        hcUbicacionPorDocumento.setId(ubicacionDocumentoPk);
                        hcUbicacionPorDocumentoRepository.save(hcUbicacionPorDocumento);
                    }
                }
            }
            responseModel.setHttpSatus(HttpStatus.OK);
            responseModel.setMessage("Operación completada");
            responseModel.setId(documentoLegal.getId());
            LOGGER.info("ID : " + responseModel.getId());
            LOGGER.info("/guardarDocumentoLegal FINAL DEL FLUJO");

            return responseModel;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);

            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseModel.setMessage("Operación Fallida");

            return responseModel;
        }
    }

    @Override
    @Transactional
    public ResponseModel guardarPenalidadesPorDocumentoLegal(PenalidadesBean penalidadesBean) {

        ResponseModel responseModel = new ResponseModel();

        if (penalidadesBean == null || ( penalidadesBean.getPenalidades() == null || penalidadesBean.getPenalidades().isEmpty())) {
            responseModel.setMessage("Datos incompletos.");
            return responseModel;
        }
        try {
            //Eliminar existentes
            List<HcPenalidadPorDocumentoLegal> existente = hcPenalidadPorDocumentoLegalRepository.obtenerPorIdExpediente(penalidadesBean.getIdExpediente());
            if (!existente.isEmpty()) {
                for (HcPenalidadPorDocumentoLegal hcPD : existente) {
                    List<HcReiteranciaPorPenalidad> rei = hcReiteranciaPorPenalidadRepository.obtenerPorHcPenalidadPorDocumentoLegal(hcPD.getId());
                    hcReiteranciaPorPenalidadRepository.delete(rei);
                    hcPenalidadPorDocumentoLegalRepository.delete(hcPD);
                }
            }

            //Guardar
            Expediente expediente = expedienteRepository.findById(penalidadesBean.getIdExpediente());
            if (expediente != null && !expediente.isAplicaPenalidad()) {
                responseModel.setMessage("No aplica penalidad, no se guarda");
                return responseModel;
            }

            assert expediente != null;
            //HcDocumentoLegal documentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(expediente.getId());
            HcDocumentoLegal documentoLegal = hcDocumentoLegalRepository.findByIdExpediente(expediente.getId());

            for (PenalidadBean penalidad : penalidadesBean.getPenalidades()) {
                if (penalidad.getAplica() != null && penalidad.getAplica()) {
                    //Guardar penalidad
                    HcPenalidadPorDocumentoLegal penalidadPorHCDL = new HcPenalidadPorDocumentoLegal();
                    penalidadPorHCDL.setDocumentoLegal(documentoLegal);
                    penalidadPorHCDL.setPenalidad(hcPenalidadRepository.findById(penalidad.getIdPenalidad()));
                    penalidadPorHCDL.setReiterancia(penalidad.getReiterancias().size());
                    penalidadPorHCDL.setAplica(penalidad.getAplica());
                    hcPenalidadPorDocumentoLegalRepository.save(penalidadPorHCDL);
                    //Guardar reiterancias
                    List<ReiteranciasBean> lr = penalidad.getReiterancias();
                    for (ReiteranciasBean rj : lr) {
                        HcReiteranciaPorPenalidad reiterancia = new HcReiteranciaPorPenalidad();
                        reiterancia.setHcPenalidadPorDocumentoLegal(penalidadPorHCDL);
                        reiterancia.setTipoValor(rj.getMoneda());
                        reiterancia.setValor(rj.getValor());
                        reiterancia.setReiterancia(rj.getDescripcion());
                        reiterancia.setIndex(rj.getIndex());
                        hcReiteranciaPorPenalidadRepository.save(reiterancia);
                    }
                }
            }
            responseModel.setMessage("Penalidades guardadas correctamente");
            responseModel.setId(penalidadesBean.getIdExpediente());
            responseModel.setHttpSatus(HttpStatus.OK);
            return responseModel;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            responseModel.setMessage(e.getMessage());
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseModel;
    }

    @Override
    @Transactional
    public ResponseModel registrarExpediente(ExpedienteBean expedienteBean, Integer idUsuario) {

        ResponseModel responseModel = new ResponseModel();
        Usuario usuario = usuarioRepository.findById(idUsuario);

        if (usuario != null) {
            try {
                if (registrarExpedienteService.registrarExpedienteSolicitudHC(usuario, expedienteBean.getIdExpediente(), expedienteBean.getAbogadoResponsable(), true) > 0) {
                    responseModel.setId(expedienteBean.getIdExpediente());
                    responseModel.setMessage("Registro de expediente exitoso");
                    responseModel.setHttpSatus(HttpStatus.OK);
                }
            } catch (RuntimeException ex3) {

                responseModel.setMessage("No se pudo registrar el expediente \n Ocurrio un error \n Contactese con el administrador del Sistema");
                responseModel.setMessage("Exito");
                responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);

                if (ex3.getMessage() != null) {

                    if (ex3.getMessage().equals("ALFRESCO")) {
                        responseModel.setMessage("No se pudo registrar el expediente \n Ocurrio un error al subir al Alfresco \n Contactese con el administrador del Sistema");
                        responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                    if (ex3.getMessage().equals("FirmaElectronica")) {

                        HcDocumentoLegal rollbackForDocumentoLegal;
                        rollbackForDocumentoLegal = commonService.obtenerHcDocumentoLegalPorExpediente(expedienteBean.getIdExpediente());

                        LOGGER.info("----------GENERANDO ROLLBALK--------------");
                        LOGGER.info("DOCUMENTO LEGAL = " + rollbackForDocumentoLegal.getId());
                        LOGGER.info("ADENDA ID TEST = " + rollbackForDocumentoLegal.getAdenda().getId());
                        LOGGER.info("EXPEDIENTE = " + expedienteBean.getIdExpediente());
                        LOGGER.info("------------------------------------------");

                        borrarDocumentosDocumentoLegal(expedienteBean.getIdExpediente());
                        borrarRepresentantesDocumentoLegal(rollbackForDocumentoLegal.getAdenda().getId());
                        borrarUbicacionesDocumentoLegal(rollbackForDocumentoLegal.getAdenda().getId());
                        borrarAdenda(rollbackForDocumentoLegal.getAdenda().getId());
                        borrarDocumentoLegal(expedienteBean.getIdExpediente());

                        responseModel.setMessage("El servicio de firma electrónica falló");
                        responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    if (ex3.getMessage().equals("NoHayRutaAlfresco")) {
                        responseModel.setMessage("El archivo no tiene configurada una ruta en alfresco");
                        responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }

            }
        } else {
            responseModel.setMessage("No pudo enviarse la solicitud");
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseModel;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public HcDocumentoLegal guardarHcDocumentoLegal(DocumentoLegalBean documentoLegalBean, Usuario usuario) {

        LOGGER.info("========================================================");
        LOGGER.info("DATOS DEL DOCUMENTO LEGAL");
        LOGGER.info("========================================================");

        HcDocumentoLegal hcDocumentoLegal = hcDocumentoLegalRepository.findByIdExpediente(documentoLegalBean.getIdExpediente());
        String situacionSunat = null;

        if (documentoLegalBean.getContrato() != null && documentoLegalBean.getContrato().getSituacionSunatContraparte() != null) {
            situacionSunat = documentoLegalBean.getContrato().getSituacionSunatContraparte();
        }

        if (hcDocumentoLegal != null && hcDocumentoLegal.getId() != null) {

            LOGGER.info("========================================================");
            LOGGER.info("DOCUMENTO LEGAL EXISTENTE");
            LOGGER.info("========================================================");
            return actualizarDocumentoLegal(documentoLegalBean, usuario);

        } else {

            LOGGER.info("========================================================");
            LOGGER.info("ES NUEVO");
            LOGGER.info("========================================================");
            hcDocumentoLegal = new HcDocumentoLegal();
            HcContrato contrato = null;
            HcAdenda adenda = null;

            //Usuario solicitante
            LOGGER.info("========================================================");
            LOGGER.info("DATA PARA CONTRATO MANUAL");
            LOGGER.info("========================================================");
            hcDocumentoLegal.setSumilla(documentoLegalBean.getSumilla());
            if (documentoLegalBean.getFechaBorrador() != null) {
                hcDocumentoLegal.setFechaBorrador(DateUtil.convertStringToDate(documentoLegalBean.getFechaBorrador(), DateUtil.FORMAT_DATE_XML));
            }

            if (documentoLegalBean.getEsContrato() && documentoLegalBean.getContrato() != null && documentoLegalBean.getContrato().getIdArea() != null) {
                hcDocumentoLegal.setArea(hcAreaRepository.findById(documentoLegalBean.getContrato().getIdArea()));
            }
            if ((documentoLegalBean.getEsAdenda()||documentoLegalBean.getEsAdendaAutomatica()) && documentoLegalBean.getAdenda() != null && documentoLegalBean.getAdenda().getIdArea() != null) {
                hcDocumentoLegal.setArea(hcAreaRepository.findById(documentoLegalBean.getAdenda().getIdArea()));
            }

            if (documentoLegalBean.getEsContrato() && documentoLegalBean.getContrato() != null && documentoLegalBean.getContrato().getIdContraparte() != null) {
                hcDocumentoLegal.setContraparte(clienteRepository.findById(documentoLegalBean.getContrato().getIdContraparte()));


                hcDocumentoLegal.setCnt_correo_contacto(documentoLegalBean.getContrato().getEmailContraparte());
                hcDocumentoLegal.setCnt_domicilio(documentoLegalBean.getContrato().getDomicilioContraparte());
                hcDocumentoLegal.setCnt_nombre_contacto(documentoLegalBean.getContrato().getNombreContraparte());
                hcDocumentoLegal.setCnt_telefono_contacto(documentoLegalBean.getContrato().getTelefonoContraparte());


                Cliente cliente = clienteRepository.findById(hcDocumentoLegal.getContraparte().getId());
                cliente.setSituacionSunat(situacionSunat);
                clienteRepository.save(cliente);
            } else if ((documentoLegalBean.getEsAdenda()||documentoLegalBean.getEsAdendaAutomatica()) && documentoLegalBean.getAdenda() != null && documentoLegalBean.getAdenda().getIdContraparte() != null) {
                hcDocumentoLegal.setContraparte(clienteRepository.findById(documentoLegalBean.getAdenda().getIdContraparte()));

                hcDocumentoLegal.setCnt_correo_contacto(documentoLegalBean.getAdenda().getEmailContraparte());
                hcDocumentoLegal.setCnt_domicilio(documentoLegalBean.getAdenda().getDomicilioContraparte());
                hcDocumentoLegal.setCnt_nombre_contacto(documentoLegalBean.getAdenda().getNombreContraparte());
                hcDocumentoLegal.setCnt_telefono_contacto(documentoLegalBean.getAdenda().getTelefonoContraparte());

                Cliente cliente = clienteRepository.findById(hcDocumentoLegal.getContraparte().getId());
                cliente.setSituacionSunat(situacionSunat);
                clienteRepository.save(cliente);
            }

            if(documentoLegalBean.getSolicitante()!=null){
                hcDocumentoLegal.setSolicitante(usuarioRepository.findById(documentoLegalBean.getSolicitante()));
            }else{
                hcDocumentoLegal.setSolicitante(usuario);
            }

            hcDocumentoLegal.setExpediente(expedienteRepository.findExpedienteById(documentoLegalBean.getIdExpediente()));
            hcDocumentoLegal.setResponsable(usuarioRepository.findById(documentoLegalBean.getAbogadoResponsable()));
            hcDocumentoLegal.setFechaSolicitud(new Date());
            hcDocumentoLegal.setFechaMovimiento(new Date());
            hcDocumentoLegal.setUbicacionDocumento(parametroRepository.obtenerPorTipoValor(Constantes.PARAMETRO_SEGUIMIENTO,Constantes.PARAMETRO_SEGUIMIENTO_LEGAL));
            hcDocumentoLegal.setEstado(Constantes.ESTADO_HC_EN_SOLICITUD);

            if (documentoLegalBean.getEsContrato() && documentoLegalBean.getContrato() != null) {

                LOGGER.info("========================================================");
                LOGGER.info("ES CONTRATO");
                LOGGER.info("========================================================");

                Expediente expediente = expedienteRepository.findExpedienteById(documentoLegalBean.getIdExpediente());
                boolean isIndefinido = documentoLegalBean.getContrato().getEsIndefinido() != null ? documentoLegalBean.getContrato().getEsIndefinido() : false;

                if (documentoLegalBean.getContrato().getAplicaArrendamiento()) {
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
                            expedienteRepository.save(expediente);
                        } else {
                            expediente.setAplicaArrendamiento("N");
                            expedienteRepository.save(expediente);
                        }
                    }
                } else {
                    expediente.setAplicaArrendamiento("N");
                    expedienteRepository.save(expediente);
                }

                contrato = new HcContrato();
                contrato.setFechaInicio(DateUtil.convertStringToDate(documentoLegalBean.getContrato().getFechaInicio(), DateUtil.FORMAT_DATE_XML));
                contrato.setFechaFin(DateUtil.convertStringToDate(documentoLegalBean.getContrato().getFechaFin(), DateUtil.FORMAT_DATE_XML));

                Boolean indefinido = documentoLegalBean.getContrato().getEsIndefinido() != null ? documentoLegalBean.getContrato().getEsIndefinido() : false;
                contrato.setIndefinido(indefinido);

                if (documentoLegalBean.getContrato().getIdMoneda() != null) {
                    contrato.setMoneda(parametroRepository.findById(documentoLegalBean.getContrato().getIdMoneda()));
                }

                if (documentoLegalBean.getContrato().getEsPrecioFijo()) {
                    contrato.setModalidad_pago("PF");
                }
                if (documentoLegalBean.getContrato().getEsPrecioUnitario()) {
                    contrato.setModalidad_pago("PU");
                }

                contrato.setPrecioUnitario(documentoLegalBean.getContrato().getMontoFijoTotalEstimado());
                contrato.setAdelanto(documentoLegalBean.getContrato().getAplicaAdelanto());


                if (documentoLegalBean.getContrato().getMontoFijoTotalEstimado() != null) {
                    contrato.setMonto(AppUtil.arreglarMontoValor(String.valueOf(documentoLegalBean.getContrato().getMontoFijoTotalEstimado())));
                } else {
                    contrato.setMonto(null);
                }

                if (documentoLegalBean.getContrato().getMontoAdelanto() != null) {
                    contrato.setMonto_adelanto(documentoLegalBean.getContrato().getMontoAdelanto());
                } else {
                    contrato.setMonto_adelanto(null);
                }
                contrato.setPeriodicidad(documentoLegalBean.getContrato().getPeriodicidadPago());
                contrato.setRenovacion_auto(documentoLegalBean.getContrato().getAplicaRenovacionAutomatica());
                contrato.setPeriodo_renovar(documentoLegalBean.getContrato().getPeriodoRenovar());
                contrato.setDescripcion(documentoLegalBean.getContrato().getPropositoObservaciones());

                if (documentoLegalBean.getTipoContrato() != null) {
                    HcTipoContrato hcTipoContrato = hcTipoContratoRepository.findTipoContratoById(documentoLegalBean.getTipoContrato());
                    contrato.setTipo_contrato(hcTipoContrato);
                }

                contrato.setArrendamiento(documentoLegalBean.getContrato().getAplicaArrendamiento());

                if (documentoLegalBean.getContrato().getAplicaModalidadPago()) {
                    contrato.setAplicaModalidadPago(1);
                } else {
                    contrato.setAplicaModalidadPago(0);
                }

                if (documentoLegalBean.getContrato().getAplicaPeriodicidad()) {
                    contrato.setAplicaPeriodicidad(1);
                } else {
                    contrato.setAplicaPeriodicidad(0);
                }

                LOGGER.info("========================================================");
                LOGGER.info("FIN CONTRATO");
                LOGGER.info("========================================================");

            }
            else if (documentoLegalBean.getAdenda() != null && (documentoLegalBean.getEsAdenda() || documentoLegalBean.getEsAdendaAutomatica())) {

                LOGGER.info("========================================================");
                LOGGER.info("ES ADENDA");
                LOGGER.info("========================================================");

                adenda = new HcAdenda();
                HcContrato contratoFromAdenda = hcContratoRepository.findHcContratoById(documentoLegalBean.getAdenda().getIdContrato());
                adenda.setContrato(contratoFromAdenda);

                adenda.setInicioVigencia(DateUtil.convertStringToDate(documentoLegalBean.getAdenda().getFechaInicio(), DateUtil.FORMAT_DATE_XML));
                adenda.setNuevaFechaFin(DateUtil.convertStringToDate(documentoLegalBean.getAdenda().getFechaFin(), DateUtil.FORMAT_DATE_XML));
                adenda.setModifica_fin(documentoLegalBean.getAdenda().getAplicaFechaFin());

                Boolean indefinido = documentoLegalBean.getAdenda().getEsIndefinido() != null ? documentoLegalBean.getAdenda().getEsIndefinido() : false;
                adenda.setIndefinido(indefinido);
                adenda.setDescripcion(documentoLegalBean.getAdenda().getPropositoObservaciones());

                Integer secuenciaActual = hcAdendaRepository.obtenerSecuenciaAdendaPorContrato(documentoLegalBean.getAdenda().getIdContrato());
                secuenciaActual = secuenciaActual != null ? secuenciaActual + 1 : 1;
                adenda.setSecuencia(secuenciaActual);

                HcTipoContrato hcTipoContrato;
                if (documentoLegalBean.getEsAdendaAutomatica() != null && documentoLegalBean.getAdenda() != null) {
                    hcTipoContrato = new HcTipoContrato();
                    hcTipoContrato.setId(documentoLegalBean.getAdenda().getTipoDocumento());
                } else {
                    hcTipoContrato = contratoFromAdenda.getTipo_contrato();
                }

                HcTipoContratoConfiguracion hcTipoContratoConfiguracion;

                if (Boolean.TRUE.equals(documentoLegalBean.getEsAdendaAutomatica()) && documentoLegalBean.getAdenda() != null) {

                    LOGGER.info("========================================================");
                    LOGGER.info("ES ADENDA AUTOMATICA");
                    LOGGER.info(documentoLegalBean.getAdenda().getTipoDocumento());
                    LOGGER.info("========================================================");

                    adenda.setHcTipoContrato(hcTipoContrato);
                    hcTipoContratoConfiguracion = hcTipoContratoConfiguracionRepository.findByIdTipoContrato(documentoLegalBean.getAdenda().getTipoDocumento());

                    if (documentoLegalBean.getEsAvance()) {
                        hcDocumentoLegal.setEstado(Constantes.ESTADO_HC_EN_SOLICITUD);
                    } else {
                        LOGGER.info("HcTipoContratoConfiguracion-estadoSolicitud");
                        LOGGER.info(documentoLegalBean);
                        if(documentoLegalBean.getAdenda().getAplicaFechaFin() && documentoLegalBean.getAdenda().getFechaFin()!=null){
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            try {
                                if(documentoLegalBean.getAdenda().getEsIndefinido()!=null && documentoLegalBean.getAdenda().getEsIndefinido()){
                                    hcDocumentoLegal.setEstado('T'); //vigente por indefinido.
                                }else{
                                    LocalDate fechaFin = LocalDate.parse(documentoLegalBean.getAdenda().getFechaFin(), formatter);
                                    LocalDate fechaActual = LocalDate.now();

                                    if(fechaFin.isBefore(fechaActual)){
                                        hcDocumentoLegal.setEstado('O'); //vencido
                                    }else{
                                        hcDocumentoLegal.setEstado('T'); //vigente
                                    }
                                }
                            } catch (DateTimeParseException e) {
                                System.out.println("Formato de fecha inválido: " + documentoLegalBean.getAdenda().getFechaFin());
                            }
                        } else {
                            hcDocumentoLegal.setEstado(hcTipoContratoConfiguracion.getEstadoSolicitud().charAt(0));
                        }
                        LOGGER.info(hcDocumentoLegal.getEstado());
                        LOGGER.info("----------------------------");
                        hcDocumentoLegal.setSumilla(hcTipoContratoConfiguracion.getSumilla());
                    }

                    hcDocumentoLegal.setFechaBorrador(new Date());


                }
                hcDocumentoLegal.setNumero(adenda.getContrato().getDocumentoLegal().getNumero() + "-" + Constantes.NUMERACION_ADENDA_PREFIJO + StringUtils.leftPad(secuenciaActual.toString(), 2, '0'));

                LOGGER.info("========================================================");
                LOGGER.info("FIN ADENDA");
                LOGGER.info("========================================================");
            }

            hcDocumentoLegal.setContrato(null);
            hcDocumentoLegal.setAdenda(null);

            LOGGER.info("Datos del documento legal - guardarHcDocumentoLegal");
            LOGGER.info("Estado = " + hcDocumentoLegal.getEstado());
            LOGGER.info("Fecha borrador = " + hcDocumentoLegal.getFechaBorrador());
            LOGGER.info("Sumilla = " + hcDocumentoLegal.getSumilla());
            LOGGER.info("Contraparte = " + hcDocumentoLegal.getContraparte().getId());

            hcDocumentoLegalRepository.save(hcDocumentoLegal);

            LOGGER.info("Datos del documento legal - GUARDADO");
            LOGGER.info("Estado = " + hcDocumentoLegal.getEstado());
            LOGGER.info("Fecha borrador = " + hcDocumentoLegal.getFechaBorrador());
            LOGGER.info("Sumilla = " + hcDocumentoLegal.getSumilla());

            if (contrato != null) {
                contrato.setDocumentoLegal(hcDocumentoLegal);
                hcContratoRepository.save(contrato);
            }
            if (adenda != null) {
                adenda.setDocumentoLegal(hcDocumentoLegal);
                hcAdendaRepository.save(adenda);
                guardarDocumentosAdendaAutomatica(hcDocumentoLegal, usuario.getId());
            }

            return hcDocumentoLegal;
        }
    }

    private void guardarDocumentosAdendaAutomatica(HcDocumentoLegal hcDocumentoLegal, Integer idUsuario){

        AdjuntarDocumentoBean adjuntarDocumentoBeanOtro=new AdjuntarDocumentoBean();

        adjuntarDocumentoBeanOtro.setId(null);
        adjuntarDocumentoBeanOtro.setIdExpediente(hcDocumentoLegal.getExpediente().getId());
        adjuntarDocumentoBeanOtro.setIdTipoDocumento(Integer.parseInt(parametroRepository.obtenerPorTipo(Constantes.PARAMETRO_ID_DOCUMENTOS_SOLICITUD).get(0).getValor()));
        adjuntarDocumentoBeanOtro.setTitulo("Documentos Solicitud");
        adjuntarDocumentoBeanOtro.setNumero(null);
        adjuntarDocumentoBeanOtro.setArchivo(null);

        adjuntarDocumentoService.guardarDocumentoGeneral(adjuntarDocumentoBeanOtro, idUsuario, null);

        AdjuntarDocumentoBean adjuntarDocumentoBeanFirmado=new AdjuntarDocumentoBean();

        String cadena1 = "Adenda ";
        String cadena2 = hcDocumentoLegal.getNumero();

        adjuntarDocumentoBeanFirmado.setId(null);
        adjuntarDocumentoBeanFirmado.setIdExpediente(hcDocumentoLegal.getExpediente().getId());
        adjuntarDocumentoBeanFirmado.setIdTipoDocumento(Integer.parseInt(parametroRepository.obtenerPorTipo(Constantes.PARAMETRO_ID_ADENDA).get(0).getValor()));
        adjuntarDocumentoBeanFirmado.setTitulo(cadena1.concat(cadena2));
        adjuntarDocumentoBeanFirmado.setNumero(hcDocumentoLegal.getNumero());
        adjuntarDocumentoBeanFirmado.setArchivo(null);

        adjuntarDocumentoService.guardarDocumentoGeneral(adjuntarDocumentoBeanFirmado, idUsuario, null);

    }

    public HcDocumentoLegal actualizarDocumentoLegal(DocumentoLegalBean documentoLegalBean, Usuario usuario) {

        LOGGER.info("========================================================");
        LOGGER.info("actualizarDocumentoLegal");
        LOGGER.info("========================================================");

        HcDocumentoLegal documentoActual = hcDocumentoLegalRepository.findById(documentoLegalBean.getIdDocumentoLegal());
        HcContrato contrato = null;
        HcAdenda adenda = null;

        LOGGER.info("========================================================");
        LOGGER.info("DATA PARA CONTRATO MANUAL");
        LOGGER.info("========================================================");
        documentoActual.setSumilla(documentoLegalBean.getSumilla());
        if (documentoLegalBean.getFechaBorrador() != null) {
            documentoActual.setFechaBorrador(DateUtil.convertStringToDate(documentoLegalBean.getFechaBorrador(), DateUtil.FORMAT_DATE_XML));
        }

        if(documentoLegalBean.getSolicitante()!=null){
            documentoActual.setSolicitante(usuarioRepository.findById(documentoLegalBean.getSolicitante()));
        }else{
            documentoActual.setSolicitante(usuario);
        }

        documentoActual.setResponsable(usuarioRepository.findById(documentoLegalBean.getAbogadoResponsable()));

        if (documentoLegalBean.getEsContrato()
                && documentoLegalBean.getContrato() != null
                && documentoLegalBean.getContrato().getIdArea() != null) {
            documentoActual.setArea(hcAreaRepository.findById(documentoLegalBean.getContrato().getIdArea()));
        }

        if ((documentoLegalBean.getEsAdenda()||documentoLegalBean.getEsAdendaAutomatica()) && documentoLegalBean.getAdenda() != null && documentoLegalBean.getAdenda().getIdArea() != null) {
            documentoActual.setArea(hcAreaRepository.findById(documentoLegalBean.getAdenda().getIdArea()));
        }

        LOGGER.info("========================================================");
        LOGGER.info("Antes del flujo");
        LOGGER.info("========================================================");
        LOGGER.info("id del bean = " + documentoLegalBean.getContrato());
        LOGGER.info("========================================================");
        if (documentoLegalBean.getEsContrato()
                && documentoLegalBean.getContrato() != null
                && documentoLegalBean.getContrato().getIdContraparte() != null) {
            LOGGER.info("========================================================");
            LOGGER.info("Entro a flujo");
            LOGGER.info("========================================================");

            documentoActual.setContraparte(clienteRepository.findById(documentoLegalBean.getContrato().getIdContraparte()));
            LOGGER.info("id que se va a guardar = " + documentoActual.getContraparte().getId());
            LOGGER.info("========================================================");
            if (documentoLegalBean.getContrato().getSituacionSunatContraparte() != null) {
                Cliente cliente = clienteRepository.findById(documentoLegalBean.getContrato().getIdContraparte());
                LOGGER.info(documentoLegalBean.getContrato().getSituacionSunatContraparte());
                cliente.setSituacionSunat(documentoLegalBean.getContrato().getSituacionSunatContraparte());
                clienteRepository.save(cliente);
            }
        }

        if (documentoActual.getContrato() != null) {

            LOGGER.info("========================================================");
            LOGGER.info("ES CONTRATO");
            LOGGER.info("========================================================");

            documentoActual.setCnt_domicilio(documentoLegalBean.getContrato().getDomicilioContraparte());
            documentoActual.setCnt_nombre_contacto(documentoLegalBean.getContrato().getNombreContraparte());
            documentoActual.setCnt_telefono_contacto(documentoLegalBean.getContrato().getTelefonoContraparte());
            documentoActual.setCnt_correo_contacto(documentoLegalBean.getContrato().getEmailContraparte());

            contrato = hcContratoRepository.findById(documentoActual.getId());
            Expediente expediente = expedienteRepository.findById(documentoLegalBean.getIdExpediente());
            boolean isIndefinido = documentoActual.getContrato().getIndefinido() != null ? documentoActual.getContrato().getIndefinido() : false;

            if (documentoLegalBean.getContrato() != null && documentoLegalBean.getContrato().getAplicaArrendamiento()) {

                LOGGER.info("GuardarHcDocumentoLegal- idArrendamiento");
                LOGGER.info(documentoLegalBean.getContrato().getAplicaArrendamiento());

                if (isIndefinido) {
                    expediente.setAplicaArrendamiento("S");
                    expedienteRepository.save(expediente);
                } else {
                    Calendar inicio = new GregorianCalendar();
                    Calendar fin = new GregorianCalendar();
                    inicio.setTime(documentoActual.getContrato().getFechaInicio());
                    fin.setTime(documentoActual.getContrato().getFechaFin());
                    int difA = fin.get(Calendar.YEAR) - inicio.get(Calendar.YEAR);
                    int difM = difA * 12 + fin.get(Calendar.MONTH) - inicio.get(Calendar.MONTH);
                    if (difM >= 12) {
                        expediente.setAplicaArrendamiento("S");
                        expedienteRepository.save(expediente);
                    } else {
                        expediente.setAplicaArrendamiento("N");
                        expedienteRepository.save(expediente);
                    }
                }
            } else {
                expediente.setAplicaArrendamiento("N");
                expedienteRepository.save(expediente);
            }

            contrato.setFechaInicio(DateUtil.convertStringToDate(documentoLegalBean.getContrato().getFechaInicio(), DateUtil.FORMAT_DATE_XML));
            contrato.setFechaFin(DateUtil.convertStringToDate(documentoLegalBean.getContrato().getFechaFin(), DateUtil.FORMAT_DATE_XML));

            Boolean indefinido = documentoLegalBean.getContrato().getEsIndefinido() != null ? documentoLegalBean.getContrato().getEsIndefinido() : false;
            contrato.setIndefinido(indefinido);

            if (documentoLegalBean.getContrato() != null && documentoLegalBean.getContrato().getIdMoneda() != null) {
                contrato.setMoneda(parametroRepository.findById(documentoLegalBean.getContrato().getIdMoneda()));
            } else {
                contrato.setMoneda(null);
            }

            if (documentoLegalBean.getContrato().getEsPrecioFijo()) {
                contrato.setModalidad_pago("PF");
            }
            if (documentoLegalBean.getContrato().getEsPrecioUnitario()) {
                contrato.setModalidad_pago("PU");
            }

            contrato.setPrecioUnitario(documentoLegalBean.getContrato().getMontoFijoTotalEstimado());
            contrato.setAdelanto(documentoLegalBean.getContrato().getAplicaAdelanto());

            if (documentoLegalBean.getContrato().getMontoFijoTotalEstimado() != null) {
                contrato.setMonto(AppUtil.arreglarMontoValor(String.valueOf(documentoLegalBean.getContrato().getMontoFijoTotalEstimado())));
            } else {
                contrato.setMonto(null);
            }

            if (documentoLegalBean.getContrato().getMontoAdelanto() != null) {
                contrato.setMonto_adelanto(AppUtil.arreglarMontoValor(String.valueOf(documentoLegalBean.getContrato().getMontoAdelanto())));
            } else {
                contrato.setMonto_adelanto(null);
            }

            if (documentoLegalBean.getContrato().getAplicaModalidadPago()) {
                contrato.setAplicaModalidadPago(1);
            } else {
                contrato.setAplicaModalidadPago(0);
            }

            if (documentoLegalBean.getContrato().getAplicaPeriodicidad()) {
                contrato.setAplicaPeriodicidad(1);
            } else {
                contrato.setAplicaPeriodicidad(0);
            }

            contrato.setPeriodicidad(documentoLegalBean.getContrato().getPeriodicidadPago());
            contrato.setRenovacion_auto(documentoLegalBean.getContrato().getAplicaRenovacionAutomatica());
            contrato.setPeriodo_renovar(documentoLegalBean.getContrato().getPeriodoRenovar());
            contrato.setDescripcion(documentoLegalBean.getContrato().getPropositoObservaciones());

            if (documentoLegalBean.getTipoContrato() != null) {
                HcTipoContrato hcTipoContrato = hcTipoContratoRepository.findTipoContratoById(documentoLegalBean.getTipoContrato());
                contrato.setTipo_contrato(hcTipoContrato);
            }

            LOGGER.info("Valor de id arrendamiento - guardarHcDocumentoLegal = " + documentoLegalBean.getContrato().getAplicaArrendamiento());
            contrato.setArrendamiento(documentoLegalBean.getContrato().getAplicaArrendamiento());

        } else if (documentoLegalBean.getAdenda() != null) {

            LOGGER.info("========================================================");
            LOGGER.info("ES ADENDA");
            LOGGER.info("========================================================");

            documentoActual.setCnt_domicilio(documentoLegalBean.getAdenda().getDomicilioContraparte());
            documentoActual.setCnt_nombre_contacto(documentoLegalBean.getAdenda().getNombreContraparte());
            documentoActual.setCnt_telefono_contacto(documentoLegalBean.getAdenda().getTelefonoContraparte());
            documentoActual.setCnt_correo_contacto(documentoLegalBean.getAdenda().getEmailContraparte());

            adenda = hcAdendaRepository.findById(documentoLegalBean.getIdDocumentoLegal());
            HcContrato contratoFromAdenda = hcContratoRepository.findById(documentoLegalBean.getAdenda().getIdContrato());
            adenda.setContrato(contratoFromAdenda);

            adenda.setInicioVigencia(DateUtil.convertStringToDate(documentoLegalBean.getAdenda().getFechaInicio(), DateUtil.FORMAT_DATE_XML));
            adenda.setNuevaFechaFin(DateUtil.convertStringToDate(documentoLegalBean.getAdenda().getFechaFin(), DateUtil.FORMAT_DATE_XML));
            adenda.setModifica_fin(documentoLegalBean.getAdenda().getAplicaFechaFin());

            Boolean indefinido = documentoLegalBean.getAdenda().getEsIndefinido() != null ? documentoLegalBean.getAdenda().getEsIndefinido() : false;
            adenda.setIndefinido(indefinido);
            adenda.setDescripcion(documentoLegalBean.getAdenda().getPropositoObservaciones());

            HcTipoContrato hcTipoContrato;
            if (documentoLegalBean.getEsAdendaAutomatica() != null && documentoLegalBean.getAdenda() != null) {
                hcTipoContrato = new HcTipoContrato();
                hcTipoContrato.setId(documentoLegalBean.getAdenda().getTipoDocumento());
            } else {
                hcTipoContrato = contratoFromAdenda.getTipo_contrato();
            }

            HcTipoContratoConfiguracion hcTipoContratoConfiguracion;

            if (documentoLegalBean.getEsAdendaAutomatica()) {

                LOGGER.info("VALOR DE ADENDA AUTOMATICA");
                LOGGER.info(documentoLegalBean.getAdenda().getTipoDocumento());
                LOGGER.info("----------------------------");
                adenda.setHcTipoContrato(hcTipoContrato);

                hcTipoContratoConfiguracion = hcTipoContratoConfiguracionRepository.findByIdTipoContrato(documentoLegalBean.getAdenda().getTipoDocumento());

                LOGGER.info("VALOR DE ESTADO DE SOLICITUD");
                LOGGER.info(hcTipoContratoConfiguracion.getEstadoSolicitud());
                LOGGER.info("----------------------------");

                if (documentoLegalBean.getEsAvance()) {
                    documentoActual.setEstado(Constantes.ESTADO_HC_EN_SOLICITUD);
                } else {
                    LOGGER.info("HcTipoContratoConfiguracion-estadoSolicitud");
                    //Convertimos el string de estadoSolicitud a char porque sabemos de antemano que es un string de un solo caracter
                    documentoActual.setEstado(hcTipoContratoConfiguracion.getEstadoSolicitud().charAt(0));
                    LOGGER.info(documentoActual.getEstado());
                    LOGGER.info("----------------------------");
                    documentoActual.setSumilla(hcTipoContratoConfiguracion.getSumilla());
                }
                documentoActual.setFechaBorrador(new Date());
            }
        }

        documentoActual.setContrato(null);
        documentoActual.setAdenda(null);

        LOGGER.info("Datos del documento legal - guardarHcDocumentoLegal");
        LOGGER.info("Estado = " + documentoActual.getEstado());
        LOGGER.info("Fecha borrador = " + documentoActual.getFechaBorrador());
        LOGGER.info("Sumilla = " + documentoActual.getSumilla());

        LOGGER.info("ID CONTRAPARTE = " + documentoActual.getContraparte().getId());

        hcDocumentoLegalRepository.save(documentoActual);

        if (contrato != null) {
            contrato.setDocumentoLegal(documentoActual);
            hcContratoRepository.save(contrato);
        }
        if (adenda != null) {
            adenda.setDocumentoLegal(documentoActual);
            hcAdendaRepository.save(adenda);
        }

        LOGGER.info("Datos del documento legal - gGUARDADO ACTUALIZAR");
        LOGGER.info("Estado = " + documentoActual.getEstado());
        LOGGER.info("Fecha borrador = " + documentoActual.getFechaBorrador());
        LOGGER.info("Sumilla = " + documentoActual.getSumilla());

        return documentoActual;
    }

    @Override
    @Transactional
    public void borrarRepresentantesDocumentoLegal(Integer idDocumentoLegal) {
        LOGGER.info("[Service] - borrarRepresentantes()");
        List<HcRepresentantePorDocumento> lista = hcRepresentantePorDocumentoRepository.obtenerHcRepresentantesDocumentoLegal(idDocumentoLegal);
        if (lista != null && !lista.isEmpty()) {
            hcRepresentantePorDocumentoRepository.delete(lista);
        }
    }

    @Override
    @Transactional
    public void borrarUbicacionesDocumentoLegal(Integer idDocumentoLegal) {
        LOGGER.info("[Service] - borrarUbicacionesDocumentoLegal()");
        List<HcUbicacionPorDocumento> lista = hcUbicacionPorDocumentoRepository.obtenerTodoUbicacionesDocumentoLegal(idDocumentoLegal);
        if (lista != null && !lista.isEmpty()) {
            hcUbicacionPorDocumentoRepository.delete(lista);
        }
    }

    @Override
    @Transactional
    public void borrarRepresentantesContraparte(Integer idContraparte) {
        LOGGER.info("[Service] - borrarRepresentantesContraparte()");
        List<HcRepresentantePorContraparte> lista = hcRepresentantePorContraparteRepository.obtenerRepresentantesContraparte(idContraparte);
        if (lista != null && !lista.isEmpty()) {
            hcRepresentantePorContraparteRepository.delete(lista);
        }
    }

    @Override
    @Transactional
    public void borrarDocumentosDocumentoLegal(Integer idExpediente) {
        LOGGER.info("[Service] - borrarDocumentosDocumentoLegal()");
        List<Documento> lista = documentoRepository.obtenerPorExpediente(idExpediente);
        if (lista != null && !lista.isEmpty()) {
            documentoRepository.delete(lista);
        }
    }

    @Override
    @Transactional
    public void borrarAdenda(Integer idAdenda) {
        LOGGER.info("[Service] - borrarAdenda()");
        HcAdenda adenda;
        adenda = hcAdendaRepository.findById(idAdenda);
        if (adenda != null) {
            LOGGER.info("[Service] - borrarAdenda()Id" + adenda.getId());
            hcAdendaRepository.delete(adenda);
        }
    }

    @Override
    @Transactional
    public void borrarDocumentoLegal(Integer idExpediente) {
        LOGGER.info("[Service] - borrarsDocumentoLegal()");
        HcDocumentoLegal hcDocumentoLegal;
        hcDocumentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(idExpediente);
        if (hcDocumentoLegal != null) {
            hcDocumentoLegalRepository.delete(hcDocumentoLegal);
        }
    }

    @Override
    public List<PenalidadBean> obtenerPenalidadesPorExpediente(Integer idExpediente) {
        List<PenalidadBean> listaPenalidades = new ArrayList<>();
        //obtener penalidades guardadas
        List<HcPenalidadPorDocumentoLegal> penalidadesGuardados = hcPenalidadPorDocumentoLegalRepository.obtenerPorIdExpediente(idExpediente);
        List<HcPenalidad> penalidades = hcPenalidadRepository.findAll();

        for (HcPenalidad hcPenalidad : penalidades) {
            if (buscarPenalidad(penalidadesGuardados, hcPenalidad) != null) {
                LOGGER.debug("------ buscarPenalidad / penalidadesGuardados------");
                System.out.println(hcPenalidad.isEstado());
                LOGGER.debug("---------------------------------------------------");

                HcPenalidadPorDocumentoLegal guardado = buscarPenalidad(penalidadesGuardados, hcPenalidad);
                PenalidadBean penalidad = new PenalidadBean();
                penalidad.setIdPenalidad(hcPenalidad.getId());
                penalidad.setDescripcion(hcPenalidad.getDescripcion());
                penalidad.setAplica(guardado.isAplica());
                penalidad.setReiterancias(generarSubFilasGuardado(hcPenalidad.getReiterancia(), guardado.getId()));

                penalidad.setAplicaValorDefecto(hcPenalidad.isAplicaPorDefecto());
                penalidad.setNumeroReiterancia(hcPenalidad.getNumeroReiterancia());
                penalidad.setTipoValor(hcPenalidad.getTipoValor());
                penalidad.setValor(hcPenalidad.getValor());

                listaPenalidades.add(penalidad);

            } else if (hcPenalidad.isEstado()) {

                LOGGER.debug("------ buscarPenalidad / diferente------");
                System.out.println(hcPenalidad.isEstado());
                LOGGER.debug("---------------------------------------------------");

                PenalidadBean penalidad = new PenalidadBean();
                penalidad.setIdPenalidad(hcPenalidad.getId());
                penalidad.setDescripcion(hcPenalidad.getDescripcion());
                penalidad.setReiterancias(generarSubFilas(hcPenalidad.getReiterancia()));
                penalidad.setAplicaValorDefecto(hcPenalidad.isAplicaPorDefecto());
                penalidad.setNumeroReiterancia(hcPenalidad.getNumeroReiterancia());
                penalidad.setTipoValor(hcPenalidad.getTipoValor());
                penalidad.setValor(hcPenalidad.getValor());

                listaPenalidades.add(penalidad);
            }

        }
        return listaPenalidades;
    }

    public List<ReiteranciasBean> generarSubFilasGuardado(Integer cantidad, Integer idPxDl) {
        List<ReiteranciasBean> res = new ArrayList<>();
        List<HcReiteranciaPorPenalidad> listaGuardado = hcReiteranciaPorPenalidadRepository.obtenerPorHcPenalidadPorDocumentoLegal(idPxDl);
        for (HcReiteranciaPorPenalidad re : listaGuardado) {
            ReiteranciasBean reiterancia = new ReiteranciasBean();
            reiterancia.setIndex(re.getIndex());
            reiterancia.setDescripcion(obtenerTexto(re.getIndex() + 1));
            reiterancia.setMoneda(re.getTipoValor());
            reiterancia.setValor(re.getValor());
            res.add(reiterancia);
        }
        return res;
    }

    public List<ReiteranciasBean> generarSubFilas(Integer cantidad) {
        List<ReiteranciasBean> res = new ArrayList<>();
        if (cantidad == null || cantidad <= 0) {
            ReiteranciasBean sinReiterancia = new ReiteranciasBean();
            sinReiterancia.setIndex(0);
            sinReiterancia.setDescripcion("Sin reiterancias");
            sinReiterancia.setMoneda("soles");
            res.add(sinReiterancia);
            return res;
        }

        for (int i = 0; i < cantidad; i++) {
            ReiteranciasBean reiterancia = new ReiteranciasBean();
            reiterancia.setIndex(i);
            reiterancia.setDescripcion(obtenerTexto(i + 1));
            reiterancia.setMoneda("soles");
            res.add(reiterancia);
        }

        return res;
    }

    public HcPenalidadPorDocumentoLegal buscarPenalidad(List<HcPenalidadPorDocumentoLegal> lista, HcPenalidad penalidad) {
        for (HcPenalidadPorDocumentoLegal l : lista) {
            if (l.getPenalidad().getId().intValue() == penalidad.getId().intValue()) {
                return l;
            }
        }
        return null;
    }

    public String obtenerTexto(Integer i) {
        switch (i) {
            case 1:
                return "Primer Incumplimiento";
            case 2:
                return "Segundo Incumplimiento";
            case 3:
                return "Tercer Incumplimiento";
            case 4:
                return "Cuarto Incumplimiento";
            case 5:
                return "Quinto Incumplimiento";
            case 6:
                return "Sexto Incumplimiento";
            case 7:
                return "Septimo Incumplimiento";
            case 8:
                return "Octavo Incumplimiento";
            case 9:
                return "Noveno Incumplimiento";
            case 10:
                return "Decimo Incumplimiento";
            default:
                return i + "? Incumplimiento";
        }
    }

    @Override
    public ContratoSumillaBean getContratoSumillaById(Integer idHcContrato){
        return hcDocumentoLegalRepository.findContratoSumillaBeanByIdHcDocLegal(idHcContrato);
    }
}
