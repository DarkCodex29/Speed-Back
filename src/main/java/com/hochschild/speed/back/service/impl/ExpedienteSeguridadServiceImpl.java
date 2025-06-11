package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.domain.speed.utils.UsuarioNotificacionDTO;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.ExpedienteSeguridadService;
import com.hochschild.speed.back.util.Constantes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("ExpedienteSeguridadService")
public class ExpedienteSeguridadServiceImpl implements ExpedienteSeguridadService {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ExpedienteSeguridadServiceImpl.class.getName());
    private final UsuarioRepository usuarioRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final RolRepository rolRepository;
    private final HcGrupoRepository hcGrupoRepository;
    private final ParametroRepository parametroRepository;
    private final ExpedienteSeguridadRepository expedienteSeguridadRepository;

    private final HcSeguridadPorUsuarioRepository hcSeguridadPorUsuarioRepository;

    public ExpedienteSeguridadServiceImpl(UsuarioRepository usuarioRepository, HcDocumentoLegalRepository hcDocumentoLegalRepository, RolRepository rolRepository, HcGrupoRepository hcGrupoRepository, ParametroRepository parametroRepository, ExpedienteSeguridadRepository expedienteSeguridadRepository, HcSeguridadPorUsuarioRepository hcSeguridadPorUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.rolRepository = rolRepository;
        this.hcGrupoRepository = hcGrupoRepository;
        this.parametroRepository = parametroRepository;
        this.expedienteSeguridadRepository = expedienteSeguridadRepository;
        this.hcSeguridadPorUsuarioRepository=hcSeguridadPorUsuarioRepository;
    }

    @Override
    public ResponseModel verificarPermisos(Integer idExpediente, Integer idUsuario) {

        ResponseModel responseModel = new ResponseModel();

        Map<String, Object> result;
        Usuario usuario = usuarioRepository.findById(idUsuario);

        if (usuario != null) {
            result = verificarPermisos(idExpediente, usuario);
            if (result.get("resultado") == "tieneAcceso"){
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setMessage("El usuario tiene acceso");
            }
        } else {
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseModel.setMessage("El usuario no tiene acceso");
        }
        return responseModel;
    }

    public Map<String, Object> verificarPermisos(Integer idExpediente, Usuario usuario) {

        Map<String, Object> result = new HashMap<>();
        List<Rol> rolesUsuario = rolRepository.buscarActivosPorUsuario(usuario.getId());
        List<Parametro> rolesSeguridad = parametroRepository.obtenerPorTipo(Constantes.TIPO_PARAMETRO_ROL_SEGURIDAD);
        boolean tieneRolSeguridad = false;
        outerloop:
        for (Rol rolUsuario : rolesUsuario) {
            LOGGER.info("ROL USUARIO nombre: " + rolUsuario.getNombre());
            LOGGER.info("ROL USUARIO SCA: " + rolUsuario.getCodigoSCA());
            for (Parametro rolSeguridad : rolesSeguridad) {
                LOGGER.info("ROL NOMBRE: " + rolSeguridad.getDescripcion());
                if (rolSeguridad.getDescripcion().equals(rolUsuario.getCodigoSCA())) {
                    tieneRolSeguridad = true;
                    break outerloop;
                }
            }
        }
        if (tieneRolSeguridad) {
            result.put("resultado", "tieneAcceso");
            return result;
        } else {
            HcSeguridadPorDocumentoLegal hcSeguridadPorDocumentoLegal = obtenerExpedienteSeguridadPorIdExpediente(idExpediente);
            if (hcSeguridadPorDocumentoLegal == null) {
                result.put("resultado", "tieneAcceso");
                return result;
            } else {
                LOGGER.info("ID USUARIO SESION: " + usuario.getId());
                for (UsuarioNotificacionDTO usuarioSeguridad : hcSeguridadPorDocumentoLegal.getUsuarios()) {
                    LOGGER.info("USUARIO GRUPO SEGURIDAD NOMBRE: " + usuarioSeguridad.getUsuario());
                    LOGGER.info("USUARIO GRUPO SEGURIDAD ID: " + usuarioSeguridad.getId());
                    if (usuarioSeguridad.getEsGrupo() == 0) {
                        LOGGER.info("USUARIO NO ES GRUPO");
                        if (usuarioSeguridad.getId().equals(usuario.getId())) {
                            LOGGER.info("USUARIO TIENE ACCESO 1");
                            result.put("resultado", "tieneAcceso");
                            return result;
                        }
                    } else {
                        LOGGER.info("USUARIO ES GRUPO");
                        List<Usuario> usuariosGrupo = hcGrupoRepository.obtenerUsuariosGrupo(usuarioSeguridad.getId());
                        for (Usuario usuarioGrupo : usuariosGrupo) {
                            LOGGER.info("USUARIO MIEMBRO DEL GRUPO NOMBRE: " + usuarioGrupo.getNombres());
                            LOGGER.info("USUARIO MIEMBRO DEL GRUPO ID: " + usuarioGrupo.getId());
                            if (usuarioGrupo.getId().equals(usuario.getId())) {
                                LOGGER.info("USUARIO TIENE ACCESO 2");
                                result.put("resultado", "tieneAcceso");
                                return result;
                            }
                        }
                    }
                }
            }
            result.put("resultado", "noTieneAcceso");
            result.put("mensaje", "No cuenta con permisos para acceder a este documento.");
            return result;
        }
    }

    public HcSeguridadPorDocumentoLegal obtenerExpedienteSeguridadPorIdExpediente(Integer idExpediente) {

        HcDocumentoLegal hcDocumentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(idExpediente);
        HcSeguridadPorDocumentoLegal hcSeguridadPorDocumentoLegalBD = expedienteSeguridadRepository.obtenerHcSeguridadPorDocumentoLegalPorIdDocumentoLegal(hcDocumentoLegal.getId(), Constantes.ESTADO_SEGURIDAD_ACTIVO);
        if (hcSeguridadPorDocumentoLegalBD != null) {
            List<UsuarioNotificacionDTO> usuarios = new ArrayList<>();
            List<HcSeguridadPorUsuario> usuariosBD = expedienteSeguridadRepository.obtenerUsuariosPorIdHcSeguridadPorDocumentoLegal(hcSeguridadPorDocumentoLegalBD.getId(), Constantes.ESTADO_SEGURIDAD_ACTIVO);
            for (HcSeguridadPorUsuario usuarioBD : usuariosBD) {
                UsuarioNotificacionDTO usuarioNotificacionDTO = new UsuarioNotificacionDTO();
                usuarioNotificacionDTO.setId(usuarioBD.getCodigoUsuario());
                if (usuarioBD.getTipoUsuario().getValor().equals(Constantes.TIPO_SEGURIDAD_USUARIO)) {
                    Usuario usuarioTemp = usuarioRepository.obtenerPorId(usuarioBD.getCodigoUsuario());
                    usuarioNotificacionDTO.setUsuario(usuarioTemp.getNombres() + " " + usuarioTemp.getApellidos());
                    usuarioNotificacionDTO.setEsGrupo(0);
                } else if (usuarioBD.getTipoUsuario().getValor().equals(Constantes.TIPO_SEGURIDAD_GRUPO)) {
                    HcGrupo grupoTemp = hcGrupoRepository.findById(usuarioBD.getCodigoUsuario());
                    usuarioNotificacionDTO.setUsuario("Grupo - "+grupoTemp.getNombre());
                    usuarioNotificacionDTO.setEsGrupo(1);
                }
                usuarios.add(usuarioNotificacionDTO);
            }
            hcSeguridadPorDocumentoLegalBD.setUsuarios(usuarios);
        }
        return hcSeguridadPorDocumentoLegalBD;
    }

    @Override
    public ResponseModel guardar(Boolean esConfidencial, Integer idExpediente, Integer idUsuario, List<UsuarioNotificacionDTO> usuarios) {
        Usuario usuario = usuarioRepository.findById(idUsuario);

        HcDocumentoLegal hcDocumentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(idExpediente);
        HcSeguridadPorDocumentoLegal hcSeguridadPorDocumentoLegalBD = expedienteSeguridadRepository.obtenerHcSeguridadPorDocumentoLegalPorIdDocumentoLegal(hcDocumentoLegal.getId(), Constantes.ESTADO_SEGURIDAD_ACTIVO);

        if (hcSeguridadPorDocumentoLegalBD != null) {
            expedienteSeguridadRepository.eliminarHcSeguridadPorUsuarioPorIdHcSeguridadPorDocumentoLegal(hcSeguridadPorDocumentoLegalBD.getId(), Constantes.ESTADO_SEGURIDAD_ELIMINADO);
            expedienteSeguridadRepository.eliminarHcSeguridadPorDocumentoLegalPorId(hcSeguridadPorDocumentoLegalBD.getId(), Constantes.ESTADO_SEGURIDAD_ELIMINADO);

        }

        if (esConfidencial) {

            HcSeguridadPorDocumentoLegal hcSeguridadPorDocumentoLegal = new HcSeguridadPorDocumentoLegal();
            hcSeguridadPorDocumentoLegal.setDocumentoLegal(hcDocumentoLegal);
            hcSeguridadPorDocumentoLegal.setFechaRegistro(new Date());
            hcSeguridadPorDocumentoLegal.setEliminado(Constantes.ESTADO_SEGURIDAD_ACTIVO);

            expedienteSeguridadRepository.save(hcSeguridadPorDocumentoLegal);

            for (UsuarioNotificacionDTO usuarioNotificacion : usuarios) {

                HcSeguridadPorUsuario hcSeguridadPorUsuario = new HcSeguridadPorUsuario();
                hcSeguridadPorUsuario.setHcSeguridadPorDocumentoLegal(hcSeguridadPorDocumentoLegal);
                if (usuarioNotificacion.getEsGrupo() == 1) {
                    hcSeguridadPorUsuario.setTipoUsuario(parametroRepository.obtenerPorTipoValor(Constantes.TIPO_SEGURIDAD, Constantes.TIPO_SEGURIDAD_GRUPO));
                } else {
                    hcSeguridadPorUsuario.setTipoUsuario(parametroRepository.obtenerPorTipoValor(Constantes.TIPO_SEGURIDAD, Constantes.TIPO_SEGURIDAD_USUARIO));
                }
                hcSeguridadPorUsuario.setFechaRegistro(new Date());
                hcSeguridadPorUsuario.setCodigoUsuario(usuarioNotificacion.getId());
                hcSeguridadPorUsuario.setEliminado(Constantes.ESTADO_SEGURIDAD_ACTIVO);

                hcSeguridadPorUsuarioRepository.save(hcSeguridadPorUsuario);
            }
        }
        ResponseModel responseModel = new ResponseModel();
        responseModel.setHttpSatus(HttpStatus.OK);
        responseModel.setMessage("Se ha registrado la configuración de seguridad");
        return responseModel;
    }
}