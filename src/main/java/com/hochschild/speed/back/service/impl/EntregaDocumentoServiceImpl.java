package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.NotificacionesConfig;
import com.hochschild.speed.back.model.domain.speed.HcDocumentoLegal;
import com.hochschild.speed.back.model.domain.speed.TipoNotificacion;
import com.hochschild.speed.back.model.domain.speed.Traza;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.domain.speed.utils.EntregaDocumentoDTO;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.TipoNotificacionRepository;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.EntregaDocumentoService;
import com.hochschild.speed.back.service.EnviarNotificacionService;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EntregaDocumentoServiceImpl implements EntregaDocumentoService {
    @Autowired
    private CommonBusinessLogicService commonBusinessLogicService;

    @Autowired
    private EnviarNotificacionService enviarNotificacionService;

    @Autowired
    private TipoNotificacionRepository tipoNotificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private NotificacionesConfig notificacionesConfig;
    @Override
    @Transactional
    public ResponseModel registrarEntrega(EntregaDocumentoDTO entregaDocumentoDTO, Integer idUsuario) {
        ResponseModel result = new ResponseModel();
        Usuario usuario = usuarioRepository.findById(idUsuario);
        if(StringUtils.isEmpty(entregaDocumentoDTO.getObservacion())){
            entregaDocumentoDTO.setObservacion( Constantes.OBS_HC_ENTREGA_DOCUMENTO );
        }

        try{
            HcDocumentoLegal documentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegalPorExpediente(entregaDocumentoDTO.getIdExpediente());

            Map<String, Object> mapTrazaGenerada = commonBusinessLogicService.generarTraza(entregaDocumentoDTO.getIdExpediente(), usuario, documentoLegal.getResponsable(), entregaDocumentoDTO.getObservacion(), Constantes.ACCION_ENTREGA_DOCUMENTO);

            //Enviar correo de notificacion
            if(mapTrazaGenerada != null){
                TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getEntregaDocumento());

                List<Usuario> destinatarios=new ArrayList<>();
                destinatarios.add(documentoLegal.getResponsable());
                if(documentoLegal.getResponsable().getId()!=documentoLegal.getSolicitante().getId()){
                    destinatarios.add(documentoLegal.getSolicitante());
                }

                enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, (Traza) mapTrazaGenerada.get(Constantes.MAP_KEY_TRAZA));
            }

            result.setMessage("exito");
            result.setHttpSatus(HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            result.setMessage("error");
            result.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }
}
