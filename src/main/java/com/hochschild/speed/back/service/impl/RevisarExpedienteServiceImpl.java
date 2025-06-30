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

import java.util.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

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
            return DateUtil.sumarDiasUtiles(expediente.getFechaCreacion(), plazo,
                    expediente.getCreador().getArea().getSede(), feriadoRepository);
        }
        return null;
    }

    @Override
    public void bloquearExpediente(UsuarioPorTraza ut) {
        List<UsuarioPorTraza> trazas = usuarioPorTrazaRepository.buscarTrazasNoUsuario(ut.getId().getTraza().getId(),
                ut.getId().getUsuario().getId());
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
        EnvioPendiente envio = envioPendienteRepository.obtenerPorExpedienteRemitente(expediente.getId(),
                remitente.getId());
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
        return botonesDao.buscarPorPerfilGrid(perfilSesion, Constantes.GRID_EXPEDIENTE,
                expediente.getProceso().getTipoProceso(), paraEstado);
    }

    @Override
    public List<Boton> obtenerBotones(Expediente expediente, Perfil perfil, Boolean responsable, Character paraEstado) {
        return botonesDao.buscarPorPerfilGrid(perfil, Constantes.GRID_EXPEDIENTE,
                expediente.getProceso().getTipoProceso(), paraEstado, responsable);
    }

    @Override
    public List<Boton> obtenerBotonesExceptoParametro(Perfil perfil, Expediente expediente, Usuario usuario,
            Boolean responsable, Character paraEstado, String parametro) {
        // return botonesDao.buscarPorPerfilGridExceptoParametro(perfil,
        // Constantes.GRID_EXPEDIENTE, expediente.getProceso().getTipoProceso(),
        // paraEstado, responsable, parametro);
        List<Boton> botones = botonesDao.buscarPorPerfilGridExceptoParametro(perfil, Constantes.GRID_EXPEDIENTE,
                expediente.getProceso().getTipoProceso(), paraEstado, responsable, parametro);
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
    public List<Boton> obtenerBotonesPorRolExceptoParametro(Perfil perfil, Expediente expediente, Usuario usuario,
            Boolean responsable, Character paraEstado, String parametro) {

        List<Boton> botones = botonesDao.buscarPorPerfilGridExceptoParametro(perfil, Constantes.GRID_EXPEDIENTE,
                expediente.getProceso().getTipoProceso(), paraEstado, responsable, parametro);
        if (botones == null) {
            botones = new ArrayList<Boton>();
            LOGGER.info("ObtenerBotonesPorRolExceptoParametro /- A");
        }

        boolean esAbogado = usuarioEsAbogado(usuario);
        LOGGER.info("ObtenerBotonesPorRolExceptoParametro /- B =" + esAbogado);

        if (esAbogado) {
            String codigoGrid = Constantes.GRID_EXPEDIENTE + "_" + Constantes.CODIGO_ROL_ABOGADO;
            List<Boton> botonesRol = botonesDao.buscarPorPerfilGridExceptoParametro(perfil, codigoGrid,
                    expediente.getProceso().getTipoProceso(), paraEstado, responsable, parametro);
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
        return trazaRepository.obtenerPrimeraTrazaxExpediente(expediente.getId(), Constantes.ACCION_ENVIAR_SOLICITUD,
                Constantes.ACCION_REGISTRO_MANUAL);
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
        LOGGER.info("=== ORDENANDO BOTONES ===");
        LOGGER.info("Botones antes de ordenar: " + botones.size());

        List<Boton> ordenados = new ArrayList<Boton>();
        List<Boton> sinOrden = new ArrayList<Boton>();
        boolean terminado = false;
        int i = 0;
        while (!terminado) {

            for (Boton boton : botones) {
                if (boton.getOrden() != null && boton.getOrden().intValue() == i) {
                    ordenados.add(boton);
                    LOGGER.info("Botón CON orden " + i + ": " + boton.getNombre());
                } else if (boton.getOrden() == null && !sinOrden.contains(boton)) {
                    sinOrden.add(boton);
                    LOGGER.info("Botón SIN orden: " + boton.getNombre());
                }
            }

            i++;
            if (ordenados.size() + sinOrden.size() >= botones.size()) {
                terminado = true;
            }
        }

        // CORREGIDO: Agregar botones sin orden al final
        ordenados.addAll(sinOrden);
        LOGGER.info("Botones después de ordenar: " + ordenados.size());
        LOGGER.info("- Con orden: " + (ordenados.size() - sinOrden.size()));
        LOGGER.info("- Sin orden: " + sinOrden.size());

        return ordenados;
    }

    @Override
    public List<Boton> obtenerBotonesMatrizEstadosRoles(Expediente expediente, Usuario usuario, Perfil perfil,
            Character estadoDocumentoLegal) {
        LOGGER.info("=== INICIANDO MATRIZ ESTADOS VS ROLES ===");
        LOGGER.info("Estado DL: " + estadoDocumentoLegal);
        LOGGER.info("Usuario: " + usuario.getNombreCompleto());
        LOGGER.info("Expediente: " + expediente.getId());

        // Verificar estado según constantes
        LOGGER.info("Verificando estado según constantes:");
        LOGGER.info("- ESTADO_HC_VENCIDO = " + Constantes.ESTADO_HC_VENCIDO + " (debería ser 'O')");
        LOGGER.info("- ESTADO_HC_VIGENTE = " + Constantes.ESTADO_HC_VIGENTE + " (debería ser 'T')");

        if (expediente.getProceso() != null) {
            LOGGER.info("ID Proceso: " + expediente.getProceso().getId());
            LOGGER.info("- PROCESO_CONTRATO = " + Constantes.PROCESO_CONTRATO);
            LOGGER.info("- PROCESO_ADENDA = " + Constantes.PROCESO_ADENDA);
            LOGGER.info("- PROCESO_ADENDA_AUTOMATICA = " + Constantes.PROCESO_ADENDA_AUTOMATICA);
        }

        // Determinar el rol del usuario
        String rolUsuario = determinarRolUsuario(usuario, expediente);
        LOGGER.info("Rol determinado: " + rolUsuario);

        // Determinar tipo de proceso
        String tipoProceso = determinarTipoProceso(expediente);
        LOGGER.info("Tipo proceso: " + tipoProceso);

        // Obtener botones según matriz
        List<Boton> botonesPermitidos = obtenerBotonesSegunMatriz(perfil, expediente, estadoDocumentoLegal, rolUsuario,
                tipoProceso, usuario);

        LOGGER.info("Botones obtenidos: " + botonesPermitidos.size());
        for (Boton boton : botonesPermitidos) {
            LOGGER.info("- " + boton.getNombre());
        }

        return ordenarBotones(botonesPermitidos);
    }

    /**
     * Determina el rol específico del usuario según la matriz del cliente
     * Alineado con Constantes.java para roles exactos
     */
    private String determinarRolUsuario(Usuario usuario, Expediente expediente) {
        List<Rol> roles = rolRepository.buscarActivosPorUsuario(usuario.getId());

        LOGGER.info("Evaluando roles para usuario: " + usuario.getNombreCompleto());
        for (Rol rol : roles) {
            LOGGER.info("- Rol encontrado: " + rol.getCodigo());
        }

        // === LÓGICA CONTEXTUAL POR ESTADO (SEGÚN CLIENTE) ===
        // "Cuando la solicitud es diferente al estado enviado a visado hay que escoger
        // el rol administrador"
        // "Cuando es enviado a visado escoger un rol de visador"

        Character estadoDL = expediente.getDocumentoLegal() != null ? expediente.getDocumentoLegal().getEstado() : null;
        boolean esEnviadoVisado = (estadoDL != null && estadoDL.equals(Constantes.ESTADO_HC_ENVIADO_VISADO));

        LOGGER.info("Estado documento: " + estadoDL + ", Es ENVIADO_VISADO: " + esEnviadoVisado);

        if (esEnviadoVisado) {
            // ESTADO = ENVIADO_VISADO → Priorizar VISADOR si existe
            if (roles.stream().anyMatch(rol -> Constantes.CODIGO_ROL_VISADOR.equals(rol.getCodigo()))) {
                LOGGER.info("Usuario identificado como VISADOR (estado ENVIADO_VISADO)");
                return "VISADOR";
            }
        } else {
            // ESTADO ≠ ENVIADO_VISADO → Priorizar ADMINISTRADOR/ABOGADO_RESPONSABLE si
            // existe

            // Verificar si es Administrador (por código)
            if (roles.stream().anyMatch(rol -> "administrador".equals(rol.getCodigo()))) {
                LOGGER.info("Usuario identificado como ABOGADO_RESPONSABLE (rol administrador, estado no-visado)");
                return "ABOGADO_RESPONSABLE";
            }

            // Verificar si es Abogado Responsable
            if (roles.stream().anyMatch(rol -> Constantes.CODIGO_ROL_ABOGADO_RESPONSABLE.equals(rol.getCodigo()))) {
                LOGGER.info("Usuario identificado como ABOGADO_RESPONSABLE (rol abogadoResponsable)");
                return "ABOGADO_RESPONSABLE";
            }

            // Verificar si es Abogado (incluye administrador sistema)
            if (roles.stream().anyMatch(rol -> Constantes.CODIGO_ROL_ABOGADO.equals(rol.getCodigo()))) {
                LOGGER.info("Usuario identificado como ABOGADO (mapeado a ABOGADO_RESPONSABLE)");
                return "ABOGADO_RESPONSABLE";
            }

            // Verificar por nombre del rol (fallback para roles como "Abogado Responsable")
            if (roles.stream().anyMatch(rol -> rol.getNombre() != null &&
                    (rol.getNombre().toLowerCase().contains("abogado responsable") ||
                            rol.getNombre().toLowerCase().contains("administrador") ||
                            rol.getCodigo().contains("RESP_LEGAL")))) {
                LOGGER.info("Usuario identificado como ABOGADO_RESPONSABLE por nombre/código de rol");
                return "ABOGADO_RESPONSABLE";
            }
        }

        // === LÓGICA FALLBACK (SI NO APLICA LA CONTEXTUAL) ===

        // Verificar si es Visador (caso fallback)
        if (roles.stream().anyMatch(rol -> Constantes.CODIGO_ROL_VISADOR.equals(rol.getCodigo()))) {
            LOGGER.info("Usuario identificado como VISADOR (fallback)");
            return "VISADOR";
        }

        // Verificar si es Solicitante
        if (roles.stream().anyMatch(rol -> Constantes.CODIGO_ROL_SOLICITANTE.equals(rol.getCodigo()))) {
            LOGGER.info("Usuario identificado como SOLICITANTE");
            return "SOLICITANTE";
        }

        // Si es el responsable del documento, se considera Abogado Responsable
        if (expediente.getDocumentoLegal() != null &&
                expediente.getDocumentoLegal().getResponsable() != null &&
                expediente.getDocumentoLegal().getResponsable().getId().equals(usuario.getId())) {
            LOGGER.info("Usuario identificado como ABOGADO_RESPONSABLE por ser responsable del documento");
            return "ABOGADO_RESPONSABLE";
        }

        LOGGER.info("Usuario por defecto: SOLICITANTE");
        return "SOLICITANTE"; // Default
    }

    /**
     * Determina el tipo de proceso según el expediente
     */
    private String determinarTipoProceso(Expediente expediente) {
        // Verificar por ID de proceso según Constantes.java
        if (expediente.getProceso() != null) {
            Integer idProceso = expediente.getProceso().getId();
            if (Constantes.PROCESO_CONTRATO == idProceso) {
                return "CONTRATO";
            } else if (Constantes.PROCESO_ADENDA == idProceso || Constantes.PROCESO_ADENDA_AUTOMATICA == idProceso) {
                return "ADENDA";
            }
        }

        // Fallback: verificar por documento legal
        if (expediente.getDocumentoLegal() != null) {
            if (expediente.getDocumentoLegal().getContrato() != null) {
                return "CONTRATO";
            } else if (expediente.getDocumentoLegal().getAdenda() != null) {
                return "ADENDA";
            }
        }
        return "CONTRATO"; // Default
    }

    /**
     * Obtiene los botones según la matriz Estados vs Roles vs Tipo Proceso
     */
    private List<Boton> obtenerBotonesSegunMatriz(Perfil perfil, Expediente expediente, Character estadoDocumentoLegal,
            String rolUsuario, String tipoProceso, Usuario usuario) {
        // Cargar matriz de permisos alineada con las tablas visuales del cliente
        Map<String, Set<String>> matrizPermisos = inicializarMatrizAlineada(tipoProceso);

        // Obtener código del estado
        String codigoEstado = mapearCodigoEstado(estadoDocumentoLegal);
        String claveMatriz = rolUsuario + "_" + codigoEstado;

        LOGGER.info("Clave matriz: " + claveMatriz);

        // === LÓGICA ESPECIAL PARA ADMINISTRADORES EN TODOS LOS ESTADOS ===
        // Si es administrador actuando como ABOGADO_RESPONSABLE o VISADOR,
        // usar los mismos botones que SOLICITANTE en el mismo estado
        if (claveMatriz.startsWith("ABOGADO_RESPONSABLE_") || claveMatriz.startsWith("VISADOR_")) {
            List<Rol> roles = rolRepository.buscarActivosPorUsuario(usuario.getId());
            boolean tieneRolAdministrador = roles.stream()
                    .anyMatch(rol -> "administrador".equals(rol.getCodigo()));

            if (tieneRolAdministrador) {
                String estadoActual;
                if (claveMatriz.startsWith("ABOGADO_RESPONSABLE_")) {
                    estadoActual = claveMatriz.replace("ABOGADO_RESPONSABLE_", "");
                } else {
                    estadoActual = claveMatriz.replace("VISADOR_", "");
                }
                claveMatriz = "SOLICITANTE_" + estadoActual;
                LOGGER.info("🔄 CASO ESPECIAL: Administrador → Usando matriz " + claveMatriz);
            }
        }

        // Obtener botones permitidos para esta combinación
        Set<String> botonesPermitidos = matrizPermisos.get(claveMatriz);
        if (botonesPermitidos == null) {
            LOGGER.info("No hay botones específicos para: " + claveMatriz);
            return new ArrayList<>();
        }

        // CAMBIO: Obtener TODOS los botones sin filtro de estado - nosotros aplicamos
        // el filtro
        LOGGER.info("=== NUEVA ESTRATEGIA: TRAER TODOS LOS BOTONES Y FILTRAR AQUÍ ===");
        List<Boton> todosLosBotones = botonesDao.buscarPorPerfilGrid(perfil, Constantes.GRID_EXPEDIENTE);

        LOGGER.info("=== ANÁLISIS DE BOTONES DISPONIBLES ===");
        LOGGER.info("Total botones en BD: " + todosLosBotones.size());
        for (Boton boton : todosLosBotones) {
            String nombreMapeado = mapearNombreBoton(boton);
            LOGGER.info("BD -> Nombre: '" + boton.getNombre() + "', URL: '" + boton.getUrl() + "', Mapeado: '"
                    + nombreMapeado + "'");
        }

        LOGGER.info("=== BOTONES ESPERADOS SEGÚN MATRIZ ===");
        for (String esperado : botonesPermitidos) {
            LOGGER.info("Esperado: " + esperado);
        }

        LOGGER.info("=== ANÁLISIS DE BOTONES FALTANTES ===");
        Set<String> botonesEncontrados = new HashSet<>();
        for (Boton boton : todosLosBotones) {
            botonesEncontrados.add(mapearNombreBoton(boton));
        }
        for (String esperado : botonesPermitidos) {
            if (!botonesEncontrados.contains(esperado)) {
                LOGGER.warn("❌ BOTÓN FALTANTE EN BD: " + esperado);
            }
        }

        // Filtrar solo los botones permitidos según matriz
        List<Boton> resultado = new ArrayList<>();
        for (Boton boton : todosLosBotones) {
            String nombreBoton = mapearNombreBoton(boton);
            if (botonesPermitidos.contains(nombreBoton)) {
                resultado.add(boton);
                LOGGER.info("✓ Botón permitido: " + nombreBoton + " -> " + boton.getNombre());
            } else {
                LOGGER.info("✗ Botón rechazado: " + nombreBoton + " -> " + boton.getNombre());
            }
        }

        return resultado;
    }

    /**
     * Inicializa la matriz de permisos 100% alineada con
     * matriz_visibilidad_botones_alineada.json VALIDADO
     * Estados mapeados según Constantes.java:
     * R -> SOLICITUD_ENVIADA, E -> EN_ELABORACION, D -> DOC_ELABORADO, V ->
     * ENVIADO_VISADO,
     * S -> VISADO, F -> ENVIADO_FIRMA, C -> COMUNICACION_INTERESADOS, T -> VIGENTE,
     * O -> VENCIDO
     */
    private Map<String, Set<String>> inicializarMatrizAlineada(String tipoProceso) {
        LOGGER.info("=== INICIALIZANDO MATRIZ ALINEADA (BASADA EN JSON VALIDADO) ===");
        LOGGER.info("Tipo proceso: " + tipoProceso);

        Map<String, Set<String>> matriz = new HashMap<>();

        if ("CONTRATO".equals(tipoProceso)) {
            // ==================== PROCESO CONTRATO ====================

            // ABOGADO RESPONSABLE / ADMINISTRADOR SISTEMA
            matriz.put("ABOGADO_RESPONSABLE_SOLICITUD_ENVIADA", new HashSet<>(Arrays.asList(
                    "ACEPTAR_SOLICITUD", "OBSERVAR_SOLICITUD", "DATOS_CONTRATO", "ADJUNTAR_DOCUMENTO",
                    "VER_HISTORIA", "ELIMINAR")));

            matriz.put("ABOGADO_RESPONSABLE_EN_ELABORACION", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "ADJUNTAR_DOCUMENTO", "VER_HISTORIA", "ELIMINAR", "SEGURIDAD",
                    "CONFIGURACION_ALARMAS")));

            matriz.put("ABOGADO_RESPONSABLE_DOC_ELABORADO", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "ADJUNTAR_DOCUMENTO", "VER_HISTORIA", "ELIMINAR", "SEGURIDAD",
                    "CONFIGURACION_ALARMAS", "ENVIAR_VB")));

            matriz.put("ABOGADO_RESPONSABLE_ENVIADO_VISADO", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "VER_HISTORIA", "ELIMINAR", "SEGURIDAD", "VER_VISADORES", "CANCELAR_VISADO")));

            matriz.put("ABOGADO_RESPONSABLE_VISADO", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "ADJUNTAR_DOCUMENTO", "VER_HISTORIA", "ELIMINAR", "SEGURIDAD",
                    "CONFIGURACION_ALARMAS", "IMPRIMIR_VB", "FIRMA_ELECTRONICA", "ENVIAR_FIRMA")));

            matriz.put("ABOGADO_RESPONSABLE_ENVIADO_FIRMA", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "ADJUNTAR_DOCUMENTO", "VER_HISTORIA", "ELIMINAR", "SEGURIDAD",
                    "CONFIGURACION_ALARMAS", "IMPRIMIR_VB", "ENTREGA_DOCUMENTO", "REENVIAR_FIRMA_ELECTRONICA",
                    "NOTIFICAR_FIRMADO")));

            matriz.put("ABOGADO_RESPONSABLE_COMUNICACION_INTERESADOS", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "VER_HISTORIA", "ELIMINAR", "SEGURIDAD", "CONFIGURACION_ALARMAS",
                    "COMUNICAR_INTERESADOS")));

            matriz.put("ABOGADO_RESPONSABLE_VIGENTE", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "VER_HISTORIA", "SEGURIDAD", "CONFIGURACION_ALARMAS",
                    "NOTIFICAR_FIRMADO", "REABRIR")));

            matriz.put("ABOGADO_RESPONSABLE_VENCIDO", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "VER_HISTORIA", "SEGURIDAD", "CONFIGURACION_ALARMAS",
                    "NOTIFICAR_FIRMADO", "REABRIR")));

            // SOLICITANTE
            matriz.put("SOLICITANTE_SOLICITUD_ENVIADA", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "ADJUNTAR_DOCUMENTO", "VER_HISTORIA")));

            matriz.put("SOLICITANTE_EN_ELABORACION", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "ADJUNTAR_DOCUMENTO", "VER_HISTORIA", "CONFIGURACION_ALARMAS")));

            matriz.put("SOLICITANTE_DOC_ELABORADO", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "ADJUNTAR_DOCUMENTO", "VER_HISTORIA", "CONFIGURACION_ALARMAS")));

            matriz.put("SOLICITANTE_ENVIADO_VISADO", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "VER_HISTORIA", "VER_VISADORES")));

            matriz.put("SOLICITANTE_VISADO", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "ADJUNTAR_DOCUMENTO", "VER_HISTORIA", "CONFIGURACION_ALARMAS", "IMPRIMIR_VB")));

            matriz.put("SOLICITANTE_ENVIADO_FIRMA", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "ADJUNTAR_DOCUMENTO", "VER_HISTORIA", "CONFIGURACION_ALARMAS",
                    "IMPRIMIR_VB", "ENTREGA_DOCUMENTO")));

            matriz.put("SOLICITANTE_COMUNICACION_INTERESADOS", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "VER_HISTORIA", "CONFIGURACION_ALARMAS")));

            matriz.put("SOLICITANTE_VIGENTE", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "VER_HISTORIA", "CONFIGURACION_ALARMAS")));

            matriz.put("SOLICITANTE_VENCIDO", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "VER_HISTORIA", "CONFIGURACION_ALARMAS")));

            // VISADOR - Solo tiene botones en estado ENVIADO_VISADO
            matriz.put("VISADOR_ENVIADO_VISADO", new HashSet<>(Arrays.asList(
                    "DATOS_CONTRATO", "VER_HISTORIA", "VER_VISADORES", "APROBAR", "OBSERVAR")));

        } else if ("ADENDA".equals(tipoProceso)) {
            // ==================== PROCESO ADENDA ====================

            // ABOGADO RESPONSABLE / ADMINISTRADOR SISTEMA
            matriz.put("ABOGADO_RESPONSABLE_SOLICITUD_ENVIADA", new HashSet<>(Arrays.asList(
                    "ACEPTAR_SOLICITUD", "OBSERVAR_SOLICITUD", "ADJUNTAR_DOCUMENTO", "VER_HISTORIA",
                    "ELIMINAR", "DATOS_ADENDA", "VER_CONTRATO")));

            matriz.put("ABOGADO_RESPONSABLE_EN_ELABORACION", new HashSet<>(Arrays.asList(
                    "ADJUNTAR_DOCUMENTO", "VER_HISTORIA", "ELIMINAR", "SEGURIDAD", "CONFIGURACION_ALARMAS",
                    "DATOS_ADENDA", "VER_CONTRATO")));

            matriz.put("ABOGADO_RESPONSABLE_DOC_ELABORADO", new HashSet<>(Arrays.asList(
                    "ADJUNTAR_DOCUMENTO", "VER_HISTORIA", "ELIMINAR", "SEGURIDAD", "CONFIGURACION_ALARMAS",
                    "ENVIAR_VB", "DATOS_ADENDA", "VER_CONTRATO")));

            matriz.put("ABOGADO_RESPONSABLE_ENVIADO_VISADO", new HashSet<>(Arrays.asList(
                    "VER_HISTORIA", "ELIMINAR", "SEGURIDAD", "CONFIGURACION_ALARMAS", "DATOS_ADENDA",
                    "VER_CONTRATO", "VER_VISADORES", "CANCELAR_VISADO")));

            matriz.put("ABOGADO_RESPONSABLE_VISADO", new HashSet<>(Arrays.asList(
                    "ADJUNTAR_DOCUMENTO", "VER_HISTORIA", "ELIMINAR", "SEGURIDAD", "CONFIGURACION_ALARMAS",
                    "DATOS_ADENDA", "VER_CONTRATO", "IMPRIMIR_VB", "FIRMA_ELECTRONICA", "ENVIAR_FIRMA")));

            matriz.put("ABOGADO_RESPONSABLE_ENVIADO_FIRMA", new HashSet<>(Arrays.asList(
                    "ADJUNTAR_DOCUMENTO", "VER_HISTORIA", "ELIMINAR", "SEGURIDAD", "CONFIGURACION_ALARMAS",
                    "DATOS_ADENDA", "VER_CONTRATO", "IMPRIMIR_VB", "ENTREGA_DOCUMENTO", "REENVIAR_FIRMA_ELECTRONICA",
                    "NOTIFICAR_FIRMADO")));

            matriz.put("ABOGADO_RESPONSABLE_COMUNICACION_INTERESADOS", new HashSet<>(Arrays.asList(
                    "VER_HISTORIA", "ELIMINAR", "SEGURIDAD", "CONFIGURACION_ALARMAS", "DATOS_ADENDA",
                    "VER_CONTRATO", "COMUNICAR_INTERESADOS")));

            matriz.put("ABOGADO_RESPONSABLE_VIGENTE", new HashSet<>(Arrays.asList(
                    "VER_HISTORIA", "SEGURIDAD", "CONFIGURACION_ALARMAS", "DATOS_ADENDA", "VER_CONTRATO",
                    "NOTIFICAR_FIRMADO", "REABRIR")));

            matriz.put("ABOGADO_RESPONSABLE_VENCIDO", new HashSet<>(Arrays.asList(
                    "VER_HISTORIA", "SEGURIDAD", "CONFIGURACION_ALARMAS", "DATOS_ADENDA", "VER_CONTRATO",
                    "NOTIFICAR_FIRMADO", "REABRIR")));

            // SOLICITANTE
            matriz.put("SOLICITANTE_SOLICITUD_ENVIADA", new HashSet<>(Arrays.asList(
                    "ADJUNTAR_DOCUMENTO", "VER_HISTORIA", "DATOS_ADENDA", "VER_CONTRATO")));

            matriz.put("SOLICITANTE_EN_ELABORACION", new HashSet<>(Arrays.asList(
                    "ADJUNTAR_DOCUMENTO", "VER_HISTORIA", "CONFIGURACION_ALARMAS", "DATOS_ADENDA", "VER_CONTRATO")));

            matriz.put("SOLICITANTE_DOC_ELABORADO", new HashSet<>(Arrays.asList(
                    "ADJUNTAR_DOCUMENTO", "VER_HISTORIA", "CONFIGURACION_ALARMAS", "DATOS_ADENDA", "VER_CONTRATO")));

            matriz.put("SOLICITANTE_ENVIADO_VISADO", new HashSet<>(Arrays.asList(
                    "VER_HISTORIA", "DATOS_ADENDA", "VER_CONTRATO", "VER_VISADORES")));

            matriz.put("SOLICITANTE_VISADO", new HashSet<>(Arrays.asList(
                    "ADJUNTAR_DOCUMENTO", "VER_HISTORIA", "CONFIGURACION_ALARMAS", "DATOS_ADENDA",
                    "VER_CONTRATO", "IMPRIMIR_VB")));

            matriz.put("SOLICITANTE_ENVIADO_FIRMA", new HashSet<>(Arrays.asList(
                    "ADJUNTAR_DOCUMENTO", "VER_HISTORIA", "CONFIGURACION_ALARMAS", "DATOS_ADENDA",
                    "VER_CONTRATO", "IMPRIMIR_VB", "ENTREGA_DOCUMENTO")));

            matriz.put("SOLICITANTE_COMUNICACION_INTERESADOS", new HashSet<>(Arrays.asList(
                    "VER_HISTORIA", "CONFIGURACION_ALARMAS", "DATOS_ADENDA", "VER_CONTRATO")));

            matriz.put("SOLICITANTE_VIGENTE", new HashSet<>(Arrays.asList(
                    "VER_HISTORIA", "CONFIGURACION_ALARMAS", "DATOS_ADENDA", "VER_CONTRATO")));

            matriz.put("SOLICITANTE_VENCIDO", new HashSet<>(Arrays.asList(
                    "VER_HISTORIA", "CONFIGURACION_ALARMAS", "DATOS_ADENDA", "VER_CONTRATO")));

            // VISADOR - Solo tiene botones en estado ENVIADO_VISADO
            matriz.put("VISADOR_ENVIADO_VISADO", new HashSet<>(Arrays.asList(
                    "VER_HISTORIA", "DATOS_ADENDA", "VER_CONTRATO", "VER_VISADORES", "APROBAR", "OBSERVAR")));
        }

        LOGGER.info("Matriz alineada inicializada con " + matriz.size() + " combinaciones");
        return matriz;
    }

    /**
     * Mapea el código de estado a su nombre estandarizado
     * Alineado exactamente con Constantes.java
     */
    private String mapearCodigoEstado(Character estadoDocumentoLegal) {
        LOGGER.info("Mapeando estado: " + estadoDocumentoLegal);

        if (estadoDocumentoLegal == null) {
            LOGGER.info("Estado es null, retornando SOLICITUD_ENVIADA");
            return "SOLICITUD_ENVIADA";
        }

        String resultado;
        switch (estadoDocumentoLegal) {
            case 'R': // Constantes.ESTADO_HC_SOLICITUD_ENVIADA
                resultado = "SOLICITUD_ENVIADA";
                break;
            case 'E': // Constantes.ESTADO_HC_EN_ELABORACION
                resultado = "EN_ELABORACION";
                break;
            case 'D': // Constantes.ESTADO_HC_ELABORADO
                resultado = "DOC_ELABORADO";
                break;
            case 'V': // Constantes.ESTADO_HC_ENVIADO_VISADO
                resultado = "ENVIADO_VISADO";
                break;
            case 'S': // Constantes.ESTADO_HC_VISADO
                resultado = "VISADO";
                break;
            case 'F': // Constantes.ESTADO_HC_ENVIADO_FIRMA
                resultado = "ENVIADO_FIRMA";
                break;
            case 'C': // Constantes.ESTADO_HC_COMUNICACION
                resultado = "COMUNICACION_INTERESADOS";
                break;
            case 'T': // Constantes.ESTADO_HC_VIGENTE
                resultado = "VIGENTE";
                break;
            case 'O': // Constantes.ESTADO_HC_VENCIDO
                resultado = "VENCIDO";
                break;
            case 'X': // ESTADO INCORRECTO - debería ser 'O'
                LOGGER.warn("ESTADO INCORRECTO: Recibido 'X' pero debería ser 'O' para VENCIDO según Constantes.java");
                resultado = "VENCIDO";
                break;
            default:
                LOGGER.warn("Estado no reconocido: " + estadoDocumentoLegal + ", usando SOLICITUD_ENVIADA por defecto");
                resultado = "SOLICITUD_ENVIADA";
                break;
        }

        LOGGER.info("Estado " + estadoDocumentoLegal + " mapeado a: " + resultado);
        return resultado;
    }

    /**
     * Mapea el nombre del botón de BD a su nombre estandarizado en la matriz
     */
    private String mapearNombreBoton(Boton boton) {
        String url = boton.getUrl() != null ? boton.getUrl().toLowerCase() : "";
        String nombre = boton.getNombre() != null ? boton.getNombre().toLowerCase() : "";

        // Mapeo basado en URL y nombre del botón (actualizado según imagen del cliente)
        if (url.contains("aceptarsolicitud") || nombre.contains("aceptar solicitud"))
            return "ACEPTAR_SOLICITUD";
        if (url.contains("observarsolicitud") || nombre.contains("observar solicitud"))
            return "OBSERVAR_SOLICITUD";

        // CORREGIDO: Primero verificar si es datos de adenda antes que datos de
        // contrato
        if (url.contains("datoscontrato/revisaradenda") || nombre.contains("datos de adenda")
                || nombre.contains("datos adenda"))
            return "DATOS_ADENDA";
        if (url.contains("datoscontrato") || nombre.contains("datos del contrato") || nombre.contains("datos contrato"))
            return "DATOS_CONTRATO";
        if (url.contains("vercontrato") || nombre.contains("ver contrato"))
            return "VER_CONTRATO";
        if (url.contains("adjuntardocumento") || nombre.contains("adjuntar documento"))
            return "ADJUNTAR_DOCUMENTO";
        if (url.contains("verhistoria") || nombre.contains("ver historia"))
            return "VER_HISTORIA";
        if (url.contains("eliminar"))
            return "ELIMINAR";
        if (url.contains("seguridad") || nombre.contains("seguridad"))
            return "SEGURIDAD";
        if (url.contains("configuracionalarmas") || nombre.contains("configuracion alarmas")
                || nombre.contains("configuración alarmas") || nombre.contains("alarmas")
                || nombre.contains("usuarios por alarma") || url.contains("usuariosporalarma"))
            return "CONFIGURACION_ALARMAS";
        if (url.contains("enviarvb") || url.contains("enviarvisado") || nombre.contains("enviar a v°b°")
                || nombre.contains("enviar a v") || url.contains("enviarVB"))
            return "ENVIAR_VB";
        if (url.contains("imprimirhojavb") || nombre.contains("imprimir hoja v°b°")
                || nombre.contains("imprimir hoja v") || url.contains("imprimirVB"))
            return "IMPRIMIR_VB";
        if (url.contains("vervisadores") || nombre.contains("ver visadores"))
            return "VER_VISADORES";
        if (url.contains("entregadocumento") || nombre.contains("entrega de documento")
                || nombre.contains("entrega documento"))
            return "ENTREGA_DOCUMENTO";
        if (url.contains("reenviarf") || nombre.contains("reenviar firma electronica")
                || nombre.contains("reenviar firma electrónica"))
            return "REENVIAR_FIRMA_ELECTRONICA";
        // CORREGIDO: Primero verificar "comunicar a interesados" antes que "comunicar"
        // general
        if (url.contains("comunicarinteresados") || nombre.contains("comunicar a interesados")
                || nombre.contains("comunicar interesados"))
            return "COMUNICAR_INTERESADOS";
        if (url.contains("notificardocumento") || nombre.contains("notificar documento")
                || nombre.contains("notificar firmado") || nombre.contains("notificar"))
            return "NOTIFICAR_FIRMADO";
        if (url.contains("firmaelectronica") || nombre.contains("firma electronica")
                || nombre.contains("firma electrónica"))
            return "FIRMA_ELECTRONICA";
        if (url.contains("enviarafirma") || nombre.contains("enviar a firma") || nombre.contains("enviar firma"))
            return "ENVIAR_FIRMA";
        if (url.contains("reabrir") || nombre.contains("reabrir")
                || url.contains("reabrirexpediente") || nombre.contains("reabrir expediente"))
            return "REABRIR";
        if (url.contains("cancelarvisado") || nombre.contains("cancelar visado"))
            return "CANCELAR_VISADO";
        if (url.contains("aprobar"))
            return "APROBAR";
        if (url.contains("observar"))
            return "OBSERVAR";

        // Si no hay mapeo específico, usar el nombre del botón como está
        return boton.getNombre().toUpperCase().replaceAll(" ", "_");
    }
}