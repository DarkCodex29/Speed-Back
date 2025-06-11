package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.NotificacionesConfig;
import com.hochschild.speed.back.model.domain.speed.HcDocumentoLegal;
import com.hochschild.speed.back.model.domain.speed.TipoNotificacion;
import com.hochschild.speed.back.model.domain.speed.Traza;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.HcDocumentoLegalRepository;
import com.hochschild.speed.back.repository.speed.TipoNotificacionRepository;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.EnviarFirmaService;
import com.hochschild.speed.back.service.EnviarNotificacionService;
import com.hochschild.speed.back.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("EnviarFirmaService")
public class EnviarFirmaServiceImpl implements EnviarFirmaService {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(EnviarFirmaServiceImpl.class.getName());

    @Autowired
    private EnviarNotificacionService enviarNotificacionService;

    @Autowired
    private HcDocumentoLegalRepository hcDocumentoLegalRepository;

    @Autowired
    private NotificacionesConfig notificacionesConfig;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CommonBusinessLogicService commonBusinessLogicService;

    @Autowired
    private TipoNotificacionRepository tipoNotificacionRepository;

    @Override
    @Transactional
    public ResponseModel enviarFirma(Integer idExpediente, Integer idUsuario) {
        ResponseModel responseModel = new ResponseModel();

        try{
            Usuario usuario = usuarioRepository.findById(idUsuario);

            HcDocumentoLegal documentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegalPorExpediente(idExpediente);
            documentoLegal.setEstado(Constantes.ESTADO_HC_ENVIADO_FIRMA);
            hcDocumentoLegalRepository.save(documentoLegal);

            Map<String, Object> mapTrazaGenerada = commonBusinessLogicService.generarTraza(idExpediente, usuario, documentoLegal.getResponsable(), Constantes.OBS_HC_ENVIADO_FIRMA, Constantes.ACCION_ENVIADO_FIRMA);

            //Notificar por correo
            if(mapTrazaGenerada != null){
                TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getEnvioFirma());

                List<Usuario> destinatarios=new ArrayList<>();
                destinatarios.add(documentoLegal.getResponsable());
                if(documentoLegal.getResponsable().getId()!=documentoLegal.getSolicitante().getId()){
                    destinatarios.add(documentoLegal.getSolicitante());
                }

                enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, (Traza) mapTrazaGenerada.get(Constantes.MAP_KEY_TRAZA));
            }

            responseModel.setHttpSatus(HttpStatus.OK);
            responseModel.setMessage("La solicitud fue enviada a firma");

        }catch(Exception ex){
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseModel.setMessage("Ocurrió un error en la solicitud. Inténtelo nuevamente.");
            LOGGER.error(ex.getMessage(),ex);
        }

        return responseModel;
    }
}
