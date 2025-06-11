package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.reabrirExpediente.ReabrirBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.ReabrirExpedienteService;
import com.hochschild.speed.back.util.Constantes;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("ReabrirExpedienteService")
public class ReabrirExpedienteServiceImpl implements ReabrirExpedienteService {
    private static final Logger LOGGER = Logger.getLogger(ReabrirExpedienteServiceImpl.class.getName());
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final UsuarioPorTrazaRepository usuarioPorTrazaRepository;
    private final ParametroRepository parametroRepository;
    private final ExpedienteRepository expedienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final TrazaRepository trazaRepository;

    public ReabrirExpedienteServiceImpl(HcDocumentoLegalRepository hcDocumentoLegalRepository,
                                        UsuarioPorTrazaRepository usuarioPorTrazaRepository,
                                        ParametroRepository parametroRepository,
                                        ExpedienteRepository expedienteRepository,
                                        UsuarioRepository usuarioRepository,
                                        TrazaRepository trazaRepository) {
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.usuarioPorTrazaRepository = usuarioPorTrazaRepository;
        this.parametroRepository = parametroRepository;
        this.expedienteRepository = expedienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.trazaRepository = trazaRepository;
    }

    @Override
    public List<Usuario> abogadosResponsables() {

        List<Usuario> usuarios = new ArrayList<>();

        try {
            usuarios = usuarioRepository.buscarUsuariosRol(Constantes.CODIGO_ROL_ABOGADO_RESPONSABLE);

            for (Usuario usuario :usuarios) {
                usuario.setRoles(null);
                usuario.setArea(null);
                usuario.setPerfiles(null);
                usuario.setJefe(null);
            }

        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
        }

        return usuarios;
    }

    @Override
    public ResponseModel reabrirExpediente(ReabrirBean reabrirBean, Integer idUsuario) {

        ResponseModel responseModel = new ResponseModel();

        try {

            Expediente expediente = expedienteRepository.findById(reabrirBean.getIdExpediente());
            expediente.setEstado(Constantes.ESTADO_REGISTRADO);
            expedienteRepository.save(expediente);

            Usuario usuario = usuarioRepository.findById(idUsuario);
            Usuario destinatario = usuario;
            Usuario responsable = usuarioRepository.findById(reabrirBean.getIdResponsable());

            if (StringUtils.isBlank(reabrirBean.getObservacion())) {
                reabrirBean.setObservacion(Constantes.OBS_HC_REAPERTURA);
            }

            if (expediente.getDocumentoLegal() != null) {
                HcDocumentoLegal documentoLegal = expediente.getDocumentoLegal();
                if (!documentoLegal.getEstado().equals(Constantes.ESTADO_HC_EN_SOLICITUD)) {
                    destinatario = responsable;
                }

                documentoLegal.setResponsable(responsable);
                hcDocumentoLegalRepository.save(documentoLegal);
            }

            Traza trazaArchivado = trazaRepository.obtenerUltimaTrazaPorExpediente(expediente.getId());
            trazaArchivado.setActual(false);
            trazaRepository.save(trazaArchivado);

            Traza nuevaTraza = new Traza();
            nuevaTraza.setExpediente(expediente);
            nuevaTraza.setRemitente(usuario);
            nuevaTraza.setObservacion(reabrirBean.getObservacion());
            nuevaTraza.setActual(true);
            nuevaTraza.setFechaCreacion(new Date());
            nuevaTraza.setAccion(Constantes.ACCION_REABRIR);
            nuevaTraza.setOrden(trazaArchivado.getOrden() + 1);
            nuevaTraza.setPrioridad(parametroRepository.obtenerPorTipoValor(Constantes.PARAMETRO_PRIORIDAD_EXPEDIENTE, Constantes.VALOR_PRIORIDAD_NORMAL));
            trazaRepository.save(nuevaTraza);

            UsuarioPorTraza usuarioPorTraza = new UsuarioPorTraza(destinatario, nuevaTraza);
            usuarioPorTraza.setLeido(false);
            usuarioPorTraza.setResponsable(true);
            usuarioPorTraza.setBloqueado(false);
            usuarioPorTrazaRepository.save(usuarioPorTraza);

            responseModel.setId(expediente.getId());
            responseModel.setMessage("El documento ha sido reabrierto");
            responseModel.setHttpSatus(HttpStatus.OK);
            return responseModel;

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            responseModel.setMessage(e.getMessage());
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return responseModel;
        }
    }
}
