package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.NotificacionesConfig;
import com.hochschild.speed.back.model.bean.comunicarInteresados.ComunicarInteresadosBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.domain.speed.utils.InteresadoDTS;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.ComunicarInteresadosService;
import com.hochschild.speed.back.service.EnviarNotificacionService;
import com.hochschild.speed.back.service.ParametroService;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service("ComunicarInteresadosService")
public class ComunicarInteresadosServiceImpl implements ComunicarInteresadosService {

    private final NotificacionesConfig notificacionesConfig;
    private final ExpedienteRepository expedienteRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final UsuarioRepository usuarioRepository;
    private final HcGrupoRepository hcGrupoRepository;
    private final TipoNotificacionRepository tipoNotificacionRepository;
    private final EnviarNotificacionService enviarNotificacionService;
    private final CommonBusinessLogicService commonBusinessLogicService;
    private final ParametroService parametroService;

    @Autowired
    public ComunicarInteresadosServiceImpl(NotificacionesConfig notificacionesConfig,
                                           ExpedienteRepository expedienteRepository,
                                           HcDocumentoLegalRepository hcDocumentoLegalRepository,
                                           UsuarioRepository usuarioRepository,
                                           HcGrupoRepository hcGrupoRepository,
                                           TipoNotificacionRepository tipoNotificacionRepository,
                                           EnviarNotificacionService enviarNotificacionService,
                                           CommonBusinessLogicService commonBusinessLogicService, ParametroService parametroService) {
        this.notificacionesConfig = notificacionesConfig;
        this.expedienteRepository = expedienteRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.usuarioRepository = usuarioRepository;
        this.hcGrupoRepository = hcGrupoRepository;
        this.tipoNotificacionRepository = tipoNotificacionRepository;
        this.enviarNotificacionService = enviarNotificacionService;
        this.commonBusinessLogicService = commonBusinessLogicService;
        this.parametroService = parametroService;
    }
    @Override
    public List<InteresadoDTS> getInteresadosIdExpediente(Integer idExpediente, Integer idUsuario) {

        Usuario usuario = usuarioRepository.findById(idUsuario);
        List<InteresadoDTS> interesadoDTS = new ArrayList<>();
        if(usuario != null){
            HcDocumentoLegal documentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegalPorExpediente(idExpediente);
            interesadoDTS.add(new InteresadoDTS("usuario",documentoLegal.getResponsable().getId(),documentoLegal.getResponsable().getNombres()+" "+documentoLegal.getResponsable().getApellidos()));
            HcGrupo grupoComunes = hcGrupoRepository.obtenerGrupoPorNombre(parametroService.buscarParametroPorTipo(Constantes.PARAMETRO_NOMBRE_GRUPO_COMUNES).get(0).getValor());
            if(grupoComunes != null){interesadoDTS.add(new InteresadoDTS("grupo",grupoComunes.getId(),"Grupo - "+grupoComunes.getNombre()));
            }
            if(documentoLegal.getResponsable().getId()!=documentoLegal.getSolicitante().getId()){
                interesadoDTS.add(new InteresadoDTS("usuario",documentoLegal.getSolicitante().getId(),documentoLegal.getSolicitante().getNombres()+" "+documentoLegal.getSolicitante().getApellidos()));
            }
        }
        return interesadoDTS;


    }
    @Override
    public List<InteresadoDTS> buscarInteresados(String term) {

        List<InteresadoDTS> interesadoDTS = new ArrayList<>();
        List<InteresadoDTS> interesadoDTSAll = new ArrayList<>();

        if (!AppUtil.checkNullOrEmpty(term)) {
            term = "%" + AppUtil.quitarAcentos(term.toLowerCase().trim()) + "%";

            interesadoDTS = usuarioRepository.buscarInteresados(term);
            interesadoDTSAll = usuarioRepository.buscarInteresadosComplemento(term);

            interesadoDTS.addAll(interesadoDTSAll);

            return interesadoDTS;
        }
        return null;
    }

    @Override
    public List<InteresadoDTS> buscarInteresadosSeguridad(String term) {

        List<InteresadoDTS> interesadoDTS = new ArrayList<>();
        List<InteresadoDTS> interesadoDTSAll = new ArrayList<>();

        if (!AppUtil.checkNullOrEmpty(term)) {
            term = "%" + AppUtil.quitarAcentos(term.toLowerCase().trim()) + "%";

            interesadoDTS = usuarioRepository.buscarInteresadosSeguridad(term, Constantes.TIPO_PARAMETRO_GRUPO, Constantes.VALOR_PARAMETRO_GRUPO_SEGURIDAD);
            interesadoDTSAll = usuarioRepository.buscarInteresadosSeguridadComplemento(term);

            interesadoDTS.addAll(interesadoDTSAll);
            return interesadoDTS;
        }
        return null;
    }

    @Transactional
    public ResponseModel comunicarInteresados(ComunicarInteresadosBean comunicarInteresadosBean, Integer idUsuario) {

        ResponseModel responseModel = new ResponseModel();

        try {

            //Cambiamos estado al expediente y documento
            Expediente expediente = expedienteRepository.findById(comunicarInteresadosBean.getIdExpediente());
            Usuario usuario = usuarioRepository.findById(idUsuario);
            HcDocumentoLegal documentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(comunicarInteresadosBean.getIdExpediente());
            String observacion = "";

            expediente.setEstado(Constantes.ESTADO_ARCHIVADO);
            expedienteRepository.save(expediente);

            documentoLegal.setExpediente(expediente);

            if (documentoLegal.getContrato() != null) {
                observacion += "Contrato ";

                if (!documentoLegal.getContrato().getIndefinido()) {
                    Calendar hoy = Calendar.getInstance();
                    hoy.set(Calendar.HOUR_OF_DAY, 0);
                    hoy.set(Calendar.MINUTE, 0);
                    hoy.set(Calendar.SECOND, 0);
                    hoy.set(Calendar.MILLISECOND, 0);

                    Calendar fechaVencimiento = Calendar.getInstance();
                    fechaVencimiento.setTime(documentoLegal.getContrato().getFechaFin());
                    fechaVencimiento.set(Calendar.HOUR_OF_DAY, 0);
                    fechaVencimiento.set(Calendar.MINUTE, 0);
                    fechaVencimiento.set(Calendar.SECOND, 0);
                    fechaVencimiento.set(Calendar.MILLISECOND, 0);

                    if (hoy.after(fechaVencimiento)) {
                        documentoLegal.setEstado(Constantes.ESTADO_HC_VENCIDO);
                        observacion += "vencido";

                    } else {
                        documentoLegal.setEstado(Constantes.ESTADO_HC_VIGENTE);
                        observacion += "vigente hasta el " + (new SimpleDateFormat("dd/MM/yyyy").format(documentoLegal.getContrato().getFechaFin()));

                    }
                } else {
                    documentoLegal.setEstado(Constantes.ESTADO_HC_VIGENTE);
                    observacion += "de vigencia indefinida";

                }
            } else if (documentoLegal.getAdenda() != null) {

                documentoLegal.setEstado(Constantes.ESTADO_HC_VIGENTE);
                observacion += "Adenda vigente";
            }

            hcDocumentoLegalRepository.save(documentoLegal);

            //Guardamos la traza de archivamiento
            Map<String, Object> mapTrazaGenerada = commonBusinessLogicService.generarTraza(expediente.getId(), usuario, documentoLegal.getResponsable(), observacion, Constantes.ACCION_ARCHIVAR);

            if (mapTrazaGenerada != null) {
                //Enviamos los correos
                TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getComunicacionInteresados());
                List<Usuario> destinatarios = new ArrayList<>();

                for (int i = 0; i < comunicarInteresadosBean.getIdInteresados().length; i++) {
                    if (comunicarInteresadosBean.getEsGrupo()[i] == 0) {//es un usuario
                        destinatarios.add(usuarioRepository.findById(comunicarInteresadosBean.getIdInteresados()[i]));
                    } else {//es grupo
                        List<Usuario> usuariosGrupo = hcGrupoRepository.obtenerUsuariosGrupo(comunicarInteresadosBean.getIdInteresados()[i]);
                        if (usuariosGrupo != null && !usuariosGrupo.isEmpty()) {
                            for (Usuario usu : usuariosGrupo) {
                                if (!destinatarios.contains(usu)) {
                                    destinatarios.add(usu);
                                }
                            }
                        }
                    }
                }
                enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, (Traza) mapTrazaGenerada.get(Constantes.MAP_KEY_TRAZA));
            }

            responseModel.setId(expediente.getId());
            responseModel.setMessage("Operación completada");
            responseModel.setHttpSatus(HttpStatus.OK);

        }catch (Exception ex){
            responseModel.setMessage(ex.getMessage());
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseModel;
    }
}