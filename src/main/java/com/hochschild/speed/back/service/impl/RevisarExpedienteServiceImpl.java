package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.dao.BotonesDao;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.RevisarExpedienteService;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("RevisarExpedienteService")
public class RevisarExpedienteServiceImpl implements RevisarExpedienteService {
    private static final Logger LOGGER = Logger.getLogger(RevisarExpedienteServiceImpl.class.getName());
    private final ReemplazoRepository reemplazoRepository;
    private final UsuarioPorTrazaRepository usuarioPorTrazaRepository;
    private final TrazaRepository trazaRepository;
    private final ProcesoRepository procesoRepository;
    private final PerfilRepository perfilRepository;
    private final TrazaCopiaRepository trazaCopiaRepository;
    private final EnvioPendienteRepository envioPendienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final DocumentoRepository documentoRepository;
    private final RolRepository rolRepository;
    private final CampoRepository campoRepository;
    private final ParametroRepository parametroRepository;
    private final CampoPorDocumentoRepository campoPorDocumentoRepository;
    private final FeriadoRepository feriadoRepository;
    private final UsuarioEnvioPendienteRepository usuarioEnvioPendienteRepository;
    private final BotonesDao botonesDao;

    @Autowired
    public RevisarExpedienteServiceImpl(ReemplazoRepository reemplazoRepository,
                                        UsuarioPorTrazaRepository usuarioPorTrazaRepository,
                                        TrazaRepository trazaRepository,
                                        ProcesoRepository procesoRepository,
                                        PerfilRepository perfilRepository,
                                        TrazaCopiaRepository trazaCopiaRepository,
                                        EnvioPendienteRepository envioPendienteRepository,
                                        UsuarioRepository usuarioRepository,
                                        DocumentoRepository documentoRepository,
                                        RolRepository rolRepository,
                                        CampoRepository campoRepository,
                                        ParametroRepository parametroRepository,
                                        CampoPorDocumentoRepository campoPorDocumentoRepository,
                                        FeriadoRepository feriadoRepository,
                                        UsuarioEnvioPendienteRepository usuarioEnvioPendienteRepository,
                                        BotonesDao botonesDao) {
        this.reemplazoRepository = reemplazoRepository;
        this.usuarioPorTrazaRepository = usuarioPorTrazaRepository;
        this.trazaRepository = trazaRepository;
        this.procesoRepository = procesoRepository;
        this.perfilRepository = perfilRepository;
        this.trazaCopiaRepository = trazaCopiaRepository;
        this.envioPendienteRepository = envioPendienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.documentoRepository = documentoRepository;
        this.rolRepository = rolRepository;
        this.campoRepository = campoRepository;
        this.parametroRepository = parametroRepository;
        this.campoPorDocumentoRepository = campoPorDocumentoRepository;
        this.feriadoRepository = feriadoRepository;
        this.usuarioEnvioPendienteRepository = usuarioEnvioPendienteRepository;
        this.botonesDao = botonesDao;
    }


    @Override
    public Reemplazo buscarReemplazo(Integer idReemplazante, Proceso proceso) {
        return reemplazoRepository.buscarReemplazo(idReemplazante, proceso, new Date());
    }


    @Override
    public Traza obtenerUltimaTraza(Integer idExpediente, Integer idUsuario) {
        Traza traza = trazaRepository.obtenerUltimaTrazaPorExpediente(idExpediente);
        Usuario usuario = usuarioRepository.findById(idUsuario);

        if (traza != null) {
            if (traza.getEstado() != null) {
                Traza hijo = encontrarTrazaHijo(traza, usuario);

                if (hijo != null) {
                    return hijo;
                }
            }
            return traza;
        }
        return null;
    }

    private Traza encontrarTrazaHijo(Traza traza, Usuario usuario) {

        List<Traza> hijos = trazaRepository.obtenerPorPadre(traza.getId());
        if (hijos.size() > 0) {
            List<Traza> hijosFinales = new ArrayList<Traza>();
            for (Traza hijo : hijos) {
                hijosFinales.add(encontrarTrazaHijo(hijo, usuario));
            }

            Traza actual = null;

            if (hijosFinales != null && !hijosFinales.isEmpty()) {
                for (Traza hijo : hijosFinales) {
                    if (hijo != null) {
                        if (actual == null || actual.getFechaCreacion().before(hijo.getFechaCreacion())) {
                            actual = hijo;
                        }
                    }
                }
            }

            UsuarioPorTraza upt = usuarioPorTrazaRepository.obtenerUsuarioPorTraza(traza.getId(), usuario.getId());
            if (upt != null && upt.getEstado() != null && upt.getEstado().equals(Constantes.ESTADO_PENDIENTE)) {
                if (actual != null && actual.getFechaCreacion().after(traza.getFechaCreacion())) {
                    return actual;
                }
                return traza;
            }
            return actual;
        }
        UsuarioPorTraza upt = usuarioPorTrazaRepository.obtenerUsuarioPorTraza(traza.getId(), usuario.getId());
        if (upt != null && upt.getEstado().equals(Constantes.ESTADO_PENDIENTE)) {
            return traza;
        }
        return null;
    }

    @Override
    public UsuarioPorTraza obtenerUsuarioPorTraza(Integer idTraza, Integer idUsuario) {
        return usuarioPorTrazaRepository.obtenerUsuarioPorTraza(idTraza, idUsuario);
    }

    @Override
    public Boolean usuarioPuedeVerExpedienteConfidencial(Usuario usuario, Expediente expediente) {

        Proceso proceso = expediente.getProceso();
        if (proceso.getTipoConfidencialidad().equals(Constantes.CONFIDENCIALIDAD_PROCESO)) {
            List<Usuario> usuariosParticipantes = usuarioRepository.obtenerParticipantesPorProceso(proceso.getId());
            for (Usuario participante : usuariosParticipantes) {
                if (participante.equals(usuario)) {
                    return true;
                }
            }
            List<Rol> rolesUsuario = rolRepository.obtenerAsignadosUsuario(usuario.getId());
            List<Rol> rolesParticipantes = rolRepository.obtenerParticipantesPorProceso(proceso.getId());
            for (Rol rol : rolesUsuario) {
                if (rolesParticipantes.contains(rol)) {
                    return true;
                }
            }
            return false;

        } else if (proceso.getTipoConfidencialidad().equals(Constantes.CONFIDENCIALIDAD_EXPEDIENTE)) {
            List<Traza> trazas = trazaRepository.obtenerTrazaPorExpediente(expediente.getId());
            for (Traza traza : trazas) {
                List<TrazaCopia> copias = trazaCopiaRepository.obtenerPorTraza(traza.getId());
                for (TrazaCopia copia : copias) {
                    if (usuario.equals(copia.getDestinatario())) {
                        return true;
                    }
                }
                if (usuario.equals(traza.getRemitente())) {
                    return true;
                }
                List<UsuarioPorTraza> upts = usuarioPorTrazaRepository.obtenerPorTraza(traza.getId());
                for (UsuarioPorTraza upt : upts) {
                    if (upt.getId().getUsuario().equals(usuario)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public List<Documento> obtenerDocumentos(Expediente expediente) {
        return documentoRepository.obtenerPorExpediente(expediente.getId());
    }

    @Override
    public List<CampoPorDocumento> obtenerCamposPorDocumento(Documento documento) {
        return campoPorDocumentoRepository.getCamposPorDocumento(documento.getId());
    }

    @Override
    public Date obtenerFechaLimite(Expediente expediente, Integer plazo) {
        if (plazo != null && plazo > 0) {
            return DateUtil.sumarDiasUtiles(expediente.getFechaCreacion(), plazo, expediente.getCreador().getArea().getSede(), feriadoRepository);
        }
        return null;
    }

    @Override
    public void bloquearExpediente(UsuarioPorTraza ut) {
        List<UsuarioPorTraza> trazas = usuarioPorTrazaRepository.buscarTrazasNoUsuario(ut.getId().getTraza().getId(), ut.getId().getUsuario().getId());
        for (UsuarioPorTraza usuarioPorTraza : trazas) {
            usuarioPorTraza.setBloqueado(true);
            usuarioPorTrazaRepository.save(usuarioPorTraza);
        }
    }

    @Override
    public void cambiarEstadoLeido(UsuarioPorTraza ut) {
        LOGGER.info("Cambiar estado a leido");
        if (!ut.isLeido()) {
            ut.setLeido(true);
            usuarioPorTrazaRepository.save(ut);
        }
    }

    @Override
    public EnvioPendiente obtenerEnvioPendiente(Usuario remitente, Expediente expediente) {
        EnvioPendiente envio = envioPendienteRepository.obtenerPorExpedienteRemitente(expediente.getId(), remitente.getId());
        if (envio != null) {
            envio.setUsuarios(usuarioEnvioPendienteRepository.obtenerPorEnvioPendiente(envio.getId()));
            return envio;
        }
        return null;
    }

    @Override
    public List<Boton> obtenerBotones(Perfil perfil) {
        return botonesDao.buscarPorPerfilGrid(perfil, Constantes.GRID_REGISTRAR_EXPEDIENTE, null, null, null);
    }

    @Override
    public List<Boton> obtenerBotones(Expediente expediente, Perfil perfilSesion, Character paraEstado) {
        return botonesDao.buscarPorPerfilGrid(perfilSesion, Constantes.GRID_EXPEDIENTE, expediente.getProceso().getTipoProceso(), paraEstado);
    }

    @Override
    public List<Boton> obtenerBotones(Expediente expediente, Perfil perfil, Boolean responsable, Character paraEstado) {
        return botonesDao.buscarPorPerfilGrid(perfil, Constantes.GRID_EXPEDIENTE, expediente.getProceso().getTipoProceso(), paraEstado, responsable);
    }

    @Override
    public List<Boton> obtenerBotonesExceptoParametro(Perfil perfil, Expediente expediente, Usuario usuario, Boolean responsable, Character paraEstado, String parametro) {
        //return botonesDao.buscarPorPerfilGridExceptoParametro(perfil, Constantes.GRID_EXPEDIENTE, expediente.getProceso().getTipoProceso(), paraEstado, responsable, parametro);
        List<Boton> botones = botonesDao.buscarPorPerfilGridExceptoParametro(perfil, Constantes.GRID_EXPEDIENTE, expediente.getProceso().getTipoProceso(), paraEstado, responsable, parametro);
        if (botones == null) {
            botones = new ArrayList<Boton>();
            LOGGER.info("obtenerBotonesExceptoParametro /- A");
        }
        boolean esAbogado = usuarioEsAbogado(usuario);
        LOGGER.info("obtenerBotonesExceptoParametro /- B =" + esAbogado);
        if (!esAbogado) {
             botones.removeIf(b -> b.getUrl().equals("enviarVisado"));
            LOGGER.info("obtenerBotonesExceptoParametro /- C");
        }

        return ordenarBotones(botones);
    }

    @Override
    public List<Boton> obtenerBotonesPorRolExceptoParametro(Perfil perfil, Expediente expediente, Usuario usuario, Boolean responsable, Character paraEstado, String parametro) {

        List<Boton> botones = botonesDao.buscarPorPerfilGridExceptoParametro(perfil, Constantes.GRID_EXPEDIENTE, expediente.getProceso().getTipoProceso(), paraEstado, responsable, parametro);
        if (botones == null) {
            botones = new ArrayList<Boton>();
            LOGGER.info("ObtenerBotonesPorRolExceptoParametro /- A");
        }

        boolean esAbogado = usuarioEsAbogado(usuario);
        LOGGER.info("ObtenerBotonesPorRolExceptoParametro /- B =" + esAbogado);

        if (esAbogado) {
            String codigoGrid = Constantes.GRID_EXPEDIENTE + "_" + Constantes.CODIGO_ROL_ABOGADO;
            List<Boton> botonesRol = botonesDao.buscarPorPerfilGridExceptoParametro(perfil, codigoGrid, expediente.getProceso().getTipoProceso(), paraEstado, responsable, parametro);
            if (botonesRol != null) {
                botones.addAll(botonesRol);
            }           
            LOGGER.info("ObtenerBotonesPorRolExceptoParametro /- C");
        } else {
        	 botones.removeIf(b -> b.getUrl().equals("enviarVisado"));
        	LOGGER.info("ObtenerBotonesPorRolExceptoParametro /- C");
        }

        return ordenarBotones(botones);
    }

    @Override
    public List<Boton> obtenerBotonesMostrarCargo(Perfil perfil) {
        return botonesDao.buscarPorPerfilGrid(perfil, Constantes.GRID_CARGO);
    }

    @Override
    public Traza getPrimeraTraza(Expediente expediente) {
        return trazaRepository.obtenerPrimeraTrazaxExpediente(expediente.getId(),Constantes.ACCION_ENVIAR_SOLICITUD, Constantes.ACCION_REGISTRO_MANUAL);
    }

    @Override
    public List<Rol> buscarRolesPorUsuario(Integer idUsuario) {
        return rolRepository.buscarActivosPorUsuario(idUsuario);
    }

    @Override
    public List<Proceso> listarProcesos() {
        return procesoRepository.obtenerPorTipo(Constantes.TIPO_PROCESO_HC);
    }

    @Override
    public List<Usuario> devolverUsuariosResponsablesPorProceso(Integer idProceso) {
        return usuarioRepository.obtenerResponsablesPorProceso(idProceso);
    }

    @Override
    public Boolean devolverFlagCliente(Integer idProceso) {
        Proceso proceso = procesoRepository.findById(idProceso);
        boolean flagCliente = proceso.getCliente();
        return flagCliente;
    }

    @Override
    public Boolean usuarioEsAbogado(Usuario usuario) {
        List<Rol> roles = rolRepository.buscarActivosPorUsuario(usuario.getId());
        boolean rolAbogado = false;
        if (roles != null && !roles.isEmpty()) {
            for (Rol rol : roles) {
                if (rol.getCodigo().contains(Constantes.CODIGO_ROL_ABOGADO)) {
                    rolAbogado = true;
                }
            }
        }
        return rolAbogado;
    }

    private List<Boton> ordenarBotones(List<Boton> botones) {

        List<Boton> ordenados = new ArrayList<Boton>();
        List<Boton> sinOrden = new ArrayList<Boton>();
        boolean terminado = false;
        int i = 0;
        while (!terminado) {

            for (Boton boton : botones) {
                if (boton.getOrden() != null && boton.getOrden().intValue() == i) {
                    ordenados.add(boton);
                } else if (boton.getOrden() == null) {
                    sinOrden.add(boton);
                }
            }

            i++;
            if (ordenados.size() + sinOrden.size() >= botones.size()) {
                terminado = true;
            }
        }
        return ordenados;
    }
}