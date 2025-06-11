package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.NotificacionesConfig;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.ExpedienteRepository;
import com.hochschild.speed.back.repository.speed.HcDocumentoLegalRepository;
import com.hochschild.speed.back.repository.speed.TipoNotificacionRepository;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.EnviarNotificacionService;
import com.hochschild.speed.back.service.EnviarSolicitudService;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EnviarSolicitudServiceImpl implements EnviarSolicitudService {
    private static final Logger LOGGER = Logger.getLogger(EnviarSolicitudServiceImpl.class.getName());
    private final NotificacionesConfig notificacionesConfig;
    private final ExpedienteRepository expedienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final TipoNotificacionRepository tipoNotificacionRepository;
    private final CommonBusinessLogicService commonBusinessLogicService;
    private final EnviarNotificacionService enviarNotificacionService;

    public EnviarSolicitudServiceImpl(NotificacionesConfig notificacionesConfig,
                                      ExpedienteRepository expedienteRepository,
                                      UsuarioRepository usuarioRepository,
                                      HcDocumentoLegalRepository hcDocumentoLegalRepository,
                                      TipoNotificacionRepository tipoNotificacionRepository,
                                      CommonBusinessLogicService commonBusinessLogicService,
                                      EnviarNotificacionService enviarNotificacionService) {
        this.notificacionesConfig = notificacionesConfig;
        this.expedienteRepository = expedienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.tipoNotificacionRepository = tipoNotificacionRepository;
        this.commonBusinessLogicService = commonBusinessLogicService;
        this.enviarNotificacionService = enviarNotificacionService;
    }

    @Override
    public ResponseModel enviarSolicitud(Integer idExpediente, Integer idUsuario) {

        ResponseModel responseModel = new ResponseModel();

        try {
            HcDocumentoLegal hcDocumentoLegal;
            TipoNotificacion tipoNotificacion;
            Map<String, Object> mapTrazaGenerada;
            Expediente expediente = expedienteRepository.findById(idExpediente);
            Usuario usuario = usuarioRepository.findById(idUsuario);
            hcDocumentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(expediente.getId());
            hcDocumentoLegal.setEstado(Constantes.ESTADO_HC_SOLICITUD_ENVIADA);
            hcDocumentoLegalRepository.save(hcDocumentoLegal);

            mapTrazaGenerada = commonBusinessLogicService.generarTraza(idExpediente, usuario, hcDocumentoLegal.getResponsable(), Constantes.OBS_HC_ENVIO_SOLICITUD_OBSERVADA, Constantes.ACCION_ENVIAR_SOLICITUD);
            tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getEnviarSolicitud());
            List<Usuario> destinatarios = new ArrayList<Usuario>();

            destinatarios.add(expediente.getDocumentoLegal().getResponsable());
            if (expediente.getDocumentoLegal().getResponsable().getId() != expediente.getDocumentoLegal().getSolicitante().getId()) {
                destinatarios.add(expediente.getDocumentoLegal().getSolicitante());
            }
            enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, (Traza) mapTrazaGenerada.get(Constantes.MAP_KEY_TRAZA));
            responseModel.setId(hcDocumentoLegal.getId());
            responseModel.setHttpSatus(HttpStatus.OK);
            responseModel.setMessage("Operación exitosa");
        } catch (Exception ex) {
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseModel.setMessage("Operación exitosa");
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}
