package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.NotificacionesConfig;
import com.hochschild.speed.back.model.bean.ObservarSolicitudBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.EnviarNotificacionService;
import com.hochschild.speed.back.service.ObservarSolicitudService;
import com.hochschild.speed.back.util.AssertUtils;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("observarSolicitudService")
public class ObservarSolicitudServiceImpl implements ObservarSolicitudService {
    private static final Logger LOGGER = Logger.getLogger(ObservarSolicitudServiceImpl.class.getName());
    private final UsuarioRepository usuarioRepository;
    private final ExpedienteRepository expedienteRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;

    private final TrazaRepository trazaRepository;
    private final CommonBusinessLogicService commonBusinessLogicService;
    private final NotificacionesConfig notificacionesConfig;
    private final TipoNotificacionRepository tipoNotificacionRepository;
    private final EnviarNotificacionService enviarNotificacionService;

    @Autowired
    public ObservarSolicitudServiceImpl(
            UsuarioRepository usuarioRepository,
            ExpedienteRepository expedienteRepository,
            HcDocumentoLegalRepository hcDocumentoLegalRepository,
            TrazaRepository trazaRepository, CommonBusinessLogicService commonBusinessLogicService,
            NotificacionesConfig notificacionesConfig,
            TipoNotificacionRepository tipoNotificacionRepository,
            EnviarNotificacionService enviarNotificacionService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.expedienteRepository = expedienteRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.trazaRepository = trazaRepository;
        this.commonBusinessLogicService = commonBusinessLogicService;
        this.notificacionesConfig = notificacionesConfig;
        this.tipoNotificacionRepository = tipoNotificacionRepository;
        this.enviarNotificacionService = enviarNotificacionService;
    }

    @Override
    public ResponseModel observarSolicitud(ObservarSolicitudBean observarSolicitudBean, Integer idUsuario) {
        ResponseModel responseModel = new ResponseModel();
        Usuario usuario = usuarioRepository.findById(idUsuario);

        try {
            Assert.notNull(observarSolicitudBean.getIdExpediente(), "No se recibio idExpediente");
            Assert.notNull(usuario, "No se recibio userLogged");
            AssertUtils.isNotEmptyString(observarSolicitudBean.getObservacion(), "No se recibio observacion");
            LOGGER.debug(observarSolicitudBean.getIdExpediente() + ", " + usuario.getUsuario() + ", " + observarSolicitudBean.getObservacion());
            Expediente expediente = expedienteRepository.findById(observarSolicitudBean.getIdExpediente());
            Assert.notNull(expediente, "No existe expediente con ID [" + observarSolicitudBean.getIdExpediente() + "]");
            HcDocumentoLegal hcDocumentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(expediente.getId());
            Assert.notNull(hcDocumentoLegal, "No existe documento legal asociado al expediente con ID [" + observarSolicitudBean.getIdExpediente() + "]");
            hcDocumentoLegal.setEstado(Constantes.ESTADO_HC_EN_SOLICITUD);
            hcDocumentoLegalRepository.save(hcDocumentoLegal);

            Map<String, Object> mapTrazaGenerada = commonBusinessLogicService.generarTraza(expediente.getId(), usuario, hcDocumentoLegal.getSolicitante(), observarSolicitudBean.getObservacion(), Constantes.ACCION_OBSERVAR);
            TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getObservarSolicitud());
            List<Usuario> destinatarios = new ArrayList<Usuario>();
            destinatarios.add(expediente.getDocumentoLegal().getResponsable());
            if (expediente.getDocumentoLegal().getResponsable().getId() != expediente.getDocumentoLegal().getSolicitante().getId()) {
                destinatarios.add(expediente.getDocumentoLegal().getSolicitante());
            }
            enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, (Traza) mapTrazaGenerada.get(Constantes.MAP_KEY_TRAZA));

            responseModel.setHttpSatus(HttpStatus.OK);
            responseModel.setMessage("Operación completada");
            responseModel.setId(hcDocumentoLegal.getId());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseModel.setMessage("Operación Fallida");
        }

        return responseModel;
    }

    @Override
    public Map<String, Object> obtenerDestinatario(Integer idExpediente){
        Expediente expediente;
        Traza ultimaTraza;
        expediente = expedienteRepository.findById(idExpediente);
        ultimaTraza = trazaRepository.obtenerUltimaTrazaPorExpediente(expediente.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("nombreDestinatario", ultimaTraza.getRemitente().getUsuario());
        return response;
    }
}
