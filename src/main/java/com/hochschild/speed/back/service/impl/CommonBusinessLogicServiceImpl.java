package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.SunatReniecService;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

@Service("commonBusinessLogicService")
@Transactional
public class CommonBusinessLogicServiceImpl implements CommonBusinessLogicService {
    private static final Logger LOGGER = Logger.getLogger(CommonBusinessLogicServiceImpl.class.getName());
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final SunatReniecService sunatReniecService;
    private final HcAlarmaRepository hcAlarmaRepository;
    private final ClienteRepository clienteRepository;
    private final HcDestinatarioAlarmaRepository hcDestinatarioAlarmaRepository;
    private final DocumentoRepository documentoRepository;
    private final HcGrupoRepository hcGrupoRepository;
    private final TrazaRepository trazaRepository;
    private final ExpedienteRepository expedienteRepository;
    private final ParametroRepository parametroRepository;
    private final UsuarioPorTrazaRepository usuarioPorTrazaRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public CommonBusinessLogicServiceImpl(HcDocumentoLegalRepository hcDocumentoLegalRepository,
                                          SunatReniecService sunatReniecService,
                                          HcAlarmaRepository hcAlarmaRepository,
                                          ClienteRepository clienteRepository,
                                          HcDestinatarioAlarmaRepository hcDestinatarioAlarmaRepository,
                                          DocumentoRepository documentoRepository, HcGrupoRepository hcGrupoRepository,
                                          TrazaRepository trazaRepository,
                                          ExpedienteRepository expedienteRepository,
                                          ParametroRepository parametroRepository,
                                          UsuarioPorTrazaRepository usuarioPorTrazaRepository, UsuarioRepository usuarioRepository) {
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.sunatReniecService = sunatReniecService;
        this.hcAlarmaRepository = hcAlarmaRepository;
        this.clienteRepository = clienteRepository;
        this.hcDestinatarioAlarmaRepository = hcDestinatarioAlarmaRepository;
        this.documentoRepository = documentoRepository;
        this.hcGrupoRepository = hcGrupoRepository;
        this.trazaRepository = trazaRepository;
        this.expedienteRepository = expedienteRepository;
        this.parametroRepository = parametroRepository;
        this.usuarioPorTrazaRepository = usuarioPorTrazaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Map<String, Object> generarTraza(Integer idExpediente, Usuario remitente, Constantes.Destinatario destinatario, String observacion, String accion) {

        Expediente expediente;
        List<Usuario> destinatarios;
        Map<String, Object> result;
        Traza nuevaTraza, ultimaTraza;
        Usuario userDestinatario;
        UsuarioPorTraza usuarioPorTraza;

        Assert.notNull(idExpediente, "No se recibio idExpediente");
        Assert.notNull(remitente, "No se recibio usuario en sesion");
        expediente = expedienteRepository.findById(idExpediente);
        Assert.notNull(expediente, "No existe expediente con ID [" + idExpediente + "]");
        ultimaTraza = trazaRepository.obtenerUltimaTrazaPorExpediente(expediente.getId());
        Assert.notNull(ultimaTraza, "No existe traza para el expediente con ID [" + idExpediente + "]");
        ultimaTraza.setActual(false);
        trazaRepository.save(ultimaTraza);
        nuevaTraza = new Traza();
        nuevaTraza.setExpediente(expediente);
        nuevaTraza.setRemitente(remitente);
        nuevaTraza.setActual(true);
        nuevaTraza.setFechaCreacion(new Date());
        nuevaTraza.setAccion(accion);
        nuevaTraza.setOrden(ultimaTraza.getOrden() + 1);
        nuevaTraza.setPrioridad(parametroRepository.findObtenerPorTipoValor(Constantes.PARAMETRO_PRIORIDAD_EXPEDIENTE, Constantes.VALOR_PRIORIDAD_NORMAL));
        nuevaTraza.setObservacion(observacion);
        nuevaTraza.setProceso(expediente.getProceso());
        trazaRepository.save(nuevaTraza);

        switch (destinatario) {
            case DEVOLVER:
                userDestinatario = ultimaTraza.getRemitente();
                break;
            case NO_ENVIAR:
            default:
                userDestinatario = remitente;
                break;
        }

        usuarioPorTraza = new UsuarioPorTraza(userDestinatario, nuevaTraza);
        usuarioPorTraza.setResponsable(true);
        usuarioPorTraza.setAprobado(false);
        usuarioPorTraza.setBloqueado(false);
        usuarioPorTraza.setLeido(false);
        usuarioPorTrazaRepository.save(usuarioPorTraza);
        destinatarios = new ArrayList<>();
        destinatarios.add(ultimaTraza.getRemitente());
        destinatarios.add(remitente);
        result = new HashMap<>();
        result.put(Constantes.MAP_KEY_TRAZA, nuevaTraza);
        result.put(Constantes.MAP_KEY_DESTINATARIOS, destinatarios);
        return result;
    }

    @Override
    public Map<String, Object> generarTraza(Integer idExpediente, Usuario remitente, Usuario userDestinatario, String observacion, String accion) {

        Expediente expediente;
        List<Usuario> destinatarios;
        Map<String, Object> result;
        Traza nuevaTraza, ultimaTraza;
        UsuarioPorTraza usuarioPorTraza;

        Assert.notNull(idExpediente, "No se recibio idExpediente");
        Assert.notNull(remitente, "No se recibio usuario en sesion");
        expediente = expedienteRepository.findById(idExpediente);
        Assert.notNull(expediente, "No existe expediente con ID [" + idExpediente + "]");
        ultimaTraza = trazaRepository.obtenerUltimaTrazaPorExpediente(expediente.getId());
        Assert.notNull(ultimaTraza, "No existe traza para el expediente con ID [" + idExpediente + "]");
        ultimaTraza.setActual(false);
        trazaRepository.save(ultimaTraza);
        nuevaTraza = new Traza();
        nuevaTraza.setExpediente(expediente);
        nuevaTraza.setRemitente(remitente);
        nuevaTraza.setActual(true);
        nuevaTraza.setFechaCreacion(new Date());
        nuevaTraza.setAccion(accion);
        nuevaTraza.setOrden(ultimaTraza.getOrden() + 1);
        nuevaTraza.setPrioridad(parametroRepository.findObtenerPorTipoValor(Constantes.PARAMETRO_PRIORIDAD_EXPEDIENTE, Constantes.VALOR_PRIORIDAD_NORMAL));
        nuevaTraza.setObservacion(observacion);
        nuevaTraza.setProceso(expediente.getProceso());
        trazaRepository.save(nuevaTraza);

        usuarioPorTraza = new UsuarioPorTraza(userDestinatario, nuevaTraza);
        usuarioPorTraza.setResponsable(true);
        usuarioPorTraza.setAprobado(false);
        usuarioPorTraza.setBloqueado(false);
        usuarioPorTraza.setLeido(false);
        usuarioPorTrazaRepository.save(usuarioPorTraza);
        destinatarios = new ArrayList<>();
        destinatarios.add(ultimaTraza.getRemitente());
        destinatarios.add(remitente);
        result = new HashMap<>();
        result.put(Constantes.MAP_KEY_TRAZA, nuevaTraza);
        result.put(Constantes.MAP_KEY_DESTINATARIOS, destinatarios);
        return result;
    }

    @Override
    public Expediente obtenerExpediente(Integer idExpediente) {

        Expediente expediente;

        Assert.notNull(idExpediente, "No se recibio idExpediente");
        LOGGER.info("idExpediente [{}] " + idExpediente);
        expediente = expedienteRepository.findById(idExpediente);
        Assert.notNull(expediente, "No existe expediente con ID [" + idExpediente + "]");
        return expediente;
    }

    @Override
    public Cliente obtenerDatosSunat(Integer idCliente) {

        Cliente cliente = clienteRepository.findById(idCliente);

        if (cliente != null) {

            Cliente clientews = sunatReniecService.consultaSunatReniec(AppUtil.getIDTipoDocumentoClienteSunat(cliente.getTipo().getCodigo()), cliente.getNumeroIdentificacion());

            if (cliente.getTipo().getCodigo().equals(Constantes.TIPO_CLIENTE_JURIDICA)) {
                if (clientews != null && !StringUtils.isEmpty(clientews.getRazonSocial())) {
                    //Actualizamos razon social
                    cliente.setRazonSocial(clientews.getRazonSocial());
                    clienteRepository.save(cliente);
                }
            }
        }
        return cliente;
    }

    @Override
    public HcDocumentoLegal obtenerHcDocumentoLegalPorExpediente(Integer idExpediente) {
        return hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(idExpediente);
    }
    @Override
    public HcDocumentoLegal obtenerHcDocumentoLegal(Integer idDocumentoLegal){
        return hcDocumentoLegalRepository.findById(idDocumentoLegal);
    }
    @Override
    public Documento obtenerDocumento(Integer idDocumento) {
        return documentoRepository.findById(idDocumento);
    }
    @Override
    public void crearAlarma(HcDocumentoLegal documentoLegal, Date fechaAlarma) {

        //Crear una nueva alarma por defecto en caso se tenga
        HcAlarma alarmaDefecto = new HcAlarma();
        alarmaDefecto.setDocumentoLegal(documentoLegal);
        alarmaDefecto.setDias_activacion(Integer.parseInt(parametroRepository.obtenerPorTipo(Constantes.PARAMETRO_ALARMA_DIAS_ACTIVACION).get(0).getValor()));
        alarmaDefecto.setDias_intervalo(Integer.parseInt(parametroRepository.obtenerPorTipo(Constantes.PARAMETRO_ALARMA_INTERVALO).get(0).getValor()));
        alarmaDefecto.setEstado(Constantes.ESTADO_ACTIVO);
        alarmaDefecto.setFechaAlarma(fechaAlarma);
        alarmaDefecto.setTitulo(parametroRepository.obtenerPorTipo(Constantes.PARAMETRO_ALARMA_TITULO).get(0).getValor());
        alarmaDefecto.setMensaje(parametroRepository.obtenerPorTipo(Constantes.PARAMETRO_ALARMA_MENSAJE).get(0).getValor());

        hcAlarmaRepository.save(alarmaDefecto);

        //Destinatarios por defecto
        HcGrupo grupoComunes = hcGrupoRepository.obtenerGrupoPorNombre(parametroRepository.obtenerPorTipo(Constantes.PARAMETRO_NOMBRE_GRUPO_COMUNES).get(0).getValor());

        if (grupoComunes != null) {
            HcDestinatarioAlarma destinatario = new HcDestinatarioAlarma();
            destinatario.setAlarma(alarmaDefecto);
            destinatario.setGrupo(grupoComunes);

            hcDestinatarioAlarmaRepository.save(destinatario);
        }

        HcDestinatarioAlarma destinatarioResponsable = new HcDestinatarioAlarma();
        destinatarioResponsable.setAlarma(alarmaDefecto);
        destinatarioResponsable.setUsuario(documentoLegal.getResponsable());
        hcDestinatarioAlarmaRepository.save(destinatarioResponsable);

        if (documentoLegal.getResponsable().getId() != documentoLegal.getSolicitante().getId()) {
            HcDestinatarioAlarma destinatario = new HcDestinatarioAlarma();
            destinatario.setAlarma(alarmaDefecto);
            destinatario.setUsuario(documentoLegal.getSolicitante());

            hcDestinatarioAlarmaRepository.save(destinatario);
        }
    }

    @Override
    public void desactivarAlarmas(HcDocumentoLegal documentoLegal) {
        //Desactivar las alarmas del contrato
        List<HcAlarma> alarmas = hcAlarmaRepository.obtenerAlarmas(documentoLegal.getId());
        if (alarmas != null && !alarmas.isEmpty()) {
            for (HcAlarma alarma : alarmas) {
                if (alarma.getEstado().equals(Constantes.ESTADO_ACTIVO)) {
                    alarma.setEstado(Constantes.ESTADO_INACTIVO);
                    hcAlarmaRepository.save(alarma);
                }
            }
        }
    }


    @Override
    public HcDocumentoLegal buscarContratoPorAdenda(Integer idAdenda){
        return hcDocumentoLegalRepository.buscarContratoPorAdenda(idAdenda);
    }

    @Override
    public List<Usuario> buscarUsuarios(String term){
        return usuarioRepository.buscarUsuariosActivosPorNombre(term);
    }

    @Override
    public List<Usuario> buscarUsuariosActivos(){
        return usuarioRepository.obtenerTodosUsuariosActivos();
    }
}