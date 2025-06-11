package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.NotificacionesConfig;
import com.hochschild.speed.back.dao.HcVisadoDao;
import com.hochschild.speed.back.model.bean.ObservarVisadoBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.domain.speed.utils.ElementoListaVisadoDTO;
import com.hochschild.speed.back.model.response.CancelarVisadoResponse;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.EnviarNotificacionService;
import com.hochschild.speed.back.service.VisadoService;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service("VisadoService")
public class VisadoServiceImpl implements VisadoService {
    private static final Logger LOGGER = Logger.getLogger(VisadoService.class.getName());
    private final CommonBusinessLogicService commonBusinessLogicService;
    private final HcVisadoRepository hcVisadoRepository;
    private final HcUsuarioPorVisadoRepository hcUsuarioPorVisadoRepository;

    private final UsuarioRepository usuarioRepository;

    private final TipoNotificacionRepository tipoNotificacionRepository;

    private final EnviarNotificacionService enviarNotificacionService;

    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;

    private final NotificacionesConfig notificacionesConfig;

    private final HcVisadoDao hcVisadoDao;

    @Autowired
    public VisadoServiceImpl(
            CommonBusinessLogicService commonBusinessLogicService,
            HcVisadoRepository hcVisadoRepository,
            HcUsuarioPorVisadoRepository hcUsuarioPorVisadoRepository,
            UsuarioRepository usuarioRepository,
            TipoNotificacionRepository tipoNotificacionRepository,
            EnviarNotificacionService enviarNotificacionService,
            HcDocumentoLegalRepository hcDocumentoLegalRepository,
            NotificacionesConfig notificacionesConfig,
            HcVisadoDao hcVisadoDao) {
        this.commonBusinessLogicService = commonBusinessLogicService;
        this.hcVisadoRepository = hcVisadoRepository;
        this.hcUsuarioPorVisadoRepository = hcUsuarioPorVisadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoNotificacionRepository=tipoNotificacionRepository;
        this.enviarNotificacionService=enviarNotificacionService;
        this.hcDocumentoLegalRepository=hcDocumentoLegalRepository;
        this.notificacionesConfig=notificacionesConfig;
        this.hcVisadoDao = hcVisadoDao;
    }
    @Override
    public List<HcUsuarioPorVisado> obtenerVisadores(Integer idExpediente) {

        List<HcUsuarioPorVisado> hcUsuarioPorVisados;
        HcDocumentoLegal documentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegalPorExpediente(idExpediente);
        HcVisado visado = hcVisadoDao.obtenerUltimoVisado(documentoLegal.getId());
        hcUsuarioPorVisados = hcUsuarioPorVisadoRepository.findByIdVisado(visado.getId());

        for (HcUsuarioPorVisado hcUsuarioPorVisado : hcUsuarioPorVisados) {
            HcUsuarioPorVisadoPK hcUsuarioPorVisadoPK = hcUsuarioPorVisado.getId();
            hcUsuarioPorVisadoPK.setVisado(null);
        }

        return hcUsuarioPorVisados;
    }

    @Override
    public ResponseEntity<List<ElementoListaVisadoDTO>> obtenerListadoVisado(String usuario) {
        try {
            List<ElementoListaVisadoDTO> list = hcVisadoDao.obtenerListaVisado(usuario);
            return ResponseEntity.ok(list);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Override
    public ResponseModel aprobarVisado(Integer idExpediente, Integer idUsuario) {
        ResponseModel responseModel = new ResponseModel();
        Usuario usuario = usuarioRepository.findById(idUsuario);

        HcDocumentoLegal documentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegalPorExpediente(idExpediente);
        HcVisado visado = hcVisadoDao.obtenerUltimoVisado(documentoLegal.getId());
        List<HcUsuarioPorVisado> usuariosVisado = hcUsuarioPorVisadoRepository.findByIdVisado(visado.getId());
        HcUsuarioPorVisado visadorActual = null;
        HcUsuarioPorVisado visadorSiguiente = null;

        if(!usuariosVisado.isEmpty()){
            int i = 0;
            for(HcUsuarioPorVisado usuarioVisado : usuariosVisado){
                if(usuarioVisado.getEstado().equals(Constantes.ESTADO_ENVIADO) && usuarioVisado.getId().getUsuario().getId().intValue() == usuario.getId().intValue()){
                    visadorActual = usuarioVisado;

                    if(usuariosVisado.size()>i+1){
                        //Hay mas visadores
                        visadorSiguiente = usuariosVisado.get(i+1);
                    }
                }
                i++;
            }

            if(visadorActual != null){
                visadorActual.setEstado(Constantes.ESTADO_ACEPTADO);
                visadorActual.setFechaVisado(new Date());
                hcUsuarioPorVisadoRepository.save(visadorActual);

                if(visadorSiguiente != null){
                    //Hay mas visadores
                    visadorSiguiente.setEstado(Constantes.ESTADO_ENVIADO);
                    hcUsuarioPorVisadoRepository.save(visadorSiguiente);

                    Map<String, Object> mapTrazaGenerada = commonBusinessLogicService.generarTraza(idExpediente, usuario, visadorSiguiente.getId().getUsuario(), visado.getObservacion(), Constantes.ACCION_APROBAR);

                    //Notificar por correo
                    if(mapTrazaGenerada != null){
                        TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getEnviarVisado());

                        List<Usuario> destinatarios=new ArrayList<Usuario>();
                        destinatarios.add(visadorSiguiente.getId().getUsuario());
						/* Solo se notifica al siguiente visador
						 * destinatarios.add(documentoLegal.getResponsable());
						if(documentoLegal.getResponsable().getId()!=documentoLegal.getSolicitante().getId()){
							destinatarios.add(documentoLegal.getSolicitante());
						}*/

                        Traza traza = (Traza) mapTrazaGenerada.get(Constantes.MAP_KEY_TRAZA);
                        traza.setPasoVisado(visadorSiguiente.getOrden());

                        enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, traza);
                    }

                }else{
                    //Era el ultimo visador
                    documentoLegal.setEstado(Constantes.ESTADO_HC_VISADO);
                    hcDocumentoLegalRepository.save(documentoLegal);

                    Map<String, Object> mapTrazaGenerada = commonBusinessLogicService.generarTraza(idExpediente, usuario, documentoLegal.getResponsable(), Constantes.OBS_HC_VISADO, Constantes.ACCION_APROBAR);

                    //Notificar por correo
                    if(mapTrazaGenerada != null){
                        TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getVisadoAprobado());

                        List<Usuario> destinatarios=new ArrayList<Usuario>();
                        destinatarios.add(documentoLegal.getResponsable());
                        if(!Objects.equals(documentoLegal.getResponsable().getId(), documentoLegal.getSolicitante().getId())){
                            destinatarios.add(documentoLegal.getSolicitante());
                        }

                        enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, (Traza) mapTrazaGenerada.get(Constantes.MAP_KEY_TRAZA));
                    }
                }

                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setMessage("La solicitud fue aceptada");

            }else{
                responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
                responseModel.setMessage("Su usuario no tiene permisos para visar el documento");
            }
        }else{
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseModel.setMessage("Ocurrio un error al obtener los visadores");
        }

        return responseModel;
    }
    @Override
    public Map<String, Object> obtenerDestinatario(Integer idExpediente){
        HcDocumentoLegal hcDocumentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegalPorExpediente(idExpediente);
        Map<String, Object> response = new HashMap<>();
        response.put("nombreDestinatario", hcDocumentoLegal.getResponsable().getLabel());
        return response;
    }

    @Override
    @Transactional
    public ResponseModel observarVisado(ObservarVisadoBean observacion, Integer idUsuario) {
        Map<String, Object> result = new HashMap<>();
        ResponseModel responseModel = new ResponseModel();
        if(StringUtils.isEmpty(observacion.getObservacion())){
            observacion.setObservacion(Constantes.OBS_HC_OBSERVAR_VISADO);
        }
        try{
            Usuario usuario = usuarioRepository.findById(idUsuario);
            HcDocumentoLegal documentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegalPorExpediente(observacion.getIdExpediente());
            HcVisado visado = hcVisadoDao.obtenerUltimoVisado(documentoLegal.getId());
            List<HcUsuarioPorVisado> usuariosVisado = hcUsuarioPorVisadoRepository.findByIdVisado(visado.getId());
            HcUsuarioPorVisado visadorActual = null;
            //Assert.notNull(observacion.getIdExpediente(), "No se recibio idExpediente");
            //Assert.notNull(usuario, "No se recibio userLogged");
            //AssertUtils.isNotEmptyString(observacion.getObservacion(), "No se recibio observacion");
            LOGGER.info(observacion.getIdExpediente() + ", " + usuario.getUsuario() + ", " + observacion.getObservacion());
            if(!usuariosVisado.isEmpty()){
                for(HcUsuarioPorVisado usuarioVisado : usuariosVisado){
                    if(usuarioVisado.getEstado().equals(Constantes.ESTADO_ENVIADO) && usuarioVisado.getId().getUsuario().getId().intValue() == usuario.getId().intValue()){
                        visadorActual = usuarioVisado;
                    }
                }

                if(visadorActual != null){
                    visadorActual.setEstado(Constantes.ESTADO_OBSERVADO);
                    visadorActual.setFechaVisado(new Date());
                    hcUsuarioPorVisadoRepository.save(visadorActual);

                    documentoLegal.setEstado(Constantes.ESTADO_HC_ELABORADO);
                    hcDocumentoLegalRepository.save(documentoLegal);

                    Map<String, Object> mapTrazaGenerada = commonBusinessLogicService.generarTraza(observacion.getIdExpediente(), usuario, documentoLegal.getResponsable(), observacion.getObservacion(), Constantes.ACCION_OBSERVAR);
                    //Notificar por correo
                    if(mapTrazaGenerada != null){
                        TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getObservarVisado());

                        List<Usuario> destinatarios=new ArrayList<Usuario>();

                        for(HcUsuarioPorVisado usuarioVisado : usuariosVisado){
                            if(usuarioVisado.getEstado().equals(Constantes.ESTADO_ACEPTADO)){
                                destinatarios.add(usuarioVisado.getId().getUsuario());
                            }
                        }

                        destinatarios.add(documentoLegal.getResponsable());
                        if(!documentoLegal.getResponsable().getId().equals(documentoLegal.getSolicitante().getId())){
                            destinatarios.add(documentoLegal.getSolicitante());
                        }

                        enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, (Traza) mapTrazaGenerada.get(Constantes.MAP_KEY_TRAZA));
                    }

                    result.put("resultado", "exito");
                    responseModel.setMessage("ok");
                    responseModel.setHttpSatus(HttpStatus.OK);

                }else{
                    //XXX Esta logica podria cambiar si se hace un reemplazo de visadores
                    result.put("resultado", "error");
                    responseModel.setMessage("error");
                    responseModel.setHttpSatus(HttpStatus.FORBIDDEN);
                }
            }else{
                responseModel.setMessage("error");
                responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex){
            LOGGER.error(ex.getMessage(), ex);
            responseModel.setMessage("error");
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return responseModel;
    }
    @Override

    public ResponseEntity<CancelarVisadoResponse> getInfoCancelarVisado(Integer idExpediente, Integer idUsuario){
        Usuario usuario= usuarioRepository.findById(idUsuario);
        CancelarVisadoResponse response = new CancelarVisadoResponse();
        if(usuario != null){
            HcDocumentoLegal documentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegalPorExpediente(idExpediente);

            if(documentoLegal.getContrato()!=null){
                response.setTipo(Constantes.TIPO_DOCUMENTO_CONTRATO);
            }else if (documentoLegal.getAdenda()!=null){
                response.setTipo(Constantes.TIPO_DOCUMENTO_ADENDA);

            }
            response.setNumero(documentoLegal.getNumero());
            response.setIdExpediente(idExpediente);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @Transactional
    public ResponseModel cancelarVisado(Integer idExpediente, Integer idUsuario){
        ResponseModel result = new ResponseModel();
        Usuario usuario = usuarioRepository.findById(idUsuario);
        HcDocumentoLegal documentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegalPorExpediente(idExpediente);
        HcVisado visado = hcVisadoDao.obtenerUltimoVisado(documentoLegal.getId());
        List<HcUsuarioPorVisado> usuariosVisado = hcUsuarioPorVisadoRepository.findByIdVisado(visado.getId());
        HcUsuarioPorVisado visadorActual = null;

        if(!usuariosVisado.isEmpty()){
            for(HcUsuarioPorVisado usuarioVisado : usuariosVisado){
                if(usuarioVisado.getEstado().equals(Constantes.ESTADO_ENVIADO)){
                    visadorActual = usuarioVisado;
                }
            }

            if(visadorActual != null){
                visadorActual.setEstado(Constantes.ESTADO_CERRADO);
                hcUsuarioPorVisadoRepository.save(visadorActual);

                documentoLegal.setEstado(Constantes.ESTADO_HC_ELABORADO);
                hcDocumentoLegalRepository.save(documentoLegal);

                Map<String, Object> mapTrazaGenerada = commonBusinessLogicService.generarTraza(idExpediente, usuario, documentoLegal.getResponsable(), Constantes.OBS_HC_CANCELAR_VISADO, Constantes.ACCION_CANCELAR_VISADO);

                //Notificar por correo
                if(mapTrazaGenerada != null){
                    TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getCancelarVisado());

                    List<Usuario> destinatarios=new ArrayList<Usuario>();

                    for(HcUsuarioPorVisado usuarioVisado : usuariosVisado){
                        if(usuarioVisado.getEstado().equals(Constantes.ESTADO_ACEPTADO)){
                            destinatarios.add(usuarioVisado.getId().getUsuario());
                        }
                    }

                    destinatarios.add(documentoLegal.getResponsable());
                    if(documentoLegal.getResponsable().getId()!=documentoLegal.getSolicitante().getId()){
                        destinatarios.add(documentoLegal.getSolicitante());
                    }

                    enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, (Traza) mapTrazaGenerada.get(Constantes.MAP_KEY_TRAZA));
                }

                result.setHttpSatus(HttpStatus.OK);
                result.setMessage("Éxito");

            }else{

                result.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
                result.setMessage("Ocurrio un error al obtener el visador actual");
            }
        }else{

            result.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("Ocurrio un error al obtener los visadores");
        }

        return result;
    }
}
