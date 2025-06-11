package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.*;
import com.hochschild.speed.back.model.domain.speed.Area;
import com.hochschild.speed.back.model.domain.speed.Perfil;
import com.hochschild.speed.back.model.domain.speed.Rol;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.filter.mantenimiento.UsuarioFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.AreaRepository;
import com.hochschild.speed.back.repository.speed.PerfilRepository;
import com.hochschild.speed.back.repository.speed.RolRepository;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.service.MantenimientoUsuarioService;
import com.hochschild.speed.back.util.EncryptUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("MantenimientoUsuarioService")
public class MantenimientoUsuarioServiceImpl implements MantenimientoUsuarioService {
    private static final Logger LOGGER = Logger.getLogger(MantenimientoUsuarioServiceImpl.class.getName());
    private final RolRepository rolRepository;
    private final PerfilRepository perfilRepository;
    private final AreaRepository areaRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public MantenimientoUsuarioServiceImpl(RolRepository rolRepository,
                                           PerfilRepository perfilRepository,
                                           AreaRepository areaRepository,
                                           UsuarioRepository usuarioRepository) {
        this.rolRepository = rolRepository;
        this.perfilRepository = perfilRepository;
        this.areaRepository = areaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Rol> buscarRolesExistente(Integer idUsuario) {
        return rolRepository.obtenerAsignadosUsuario(idUsuario);
    }

    @Override
    public Usuario find(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public List<UsuariosBean> list(UsuarioFilter filter) {

        List<UsuariosBean> usuarios = new ArrayList<>();
        List<Usuario> usuariosBD = usuarioRepository.list(filter.getUsuario(),filter.getNombres(),filter.getApellidos());

        for (Usuario usuario : usuariosBD) {
            UsuariosBean usuarioBean = new UsuariosBean();
            usuarioBean.setId(usuario.getId());
            usuarioBean.setUsuario(usuario.getUsuario());
            usuarioBean.setNombres(usuario.getNombres());
            usuarioBean.setApellidos(usuario.getApellidos());
            usuarioBean.setArea(usuario.getArea() != null ? usuario.getArea().getNombre() : null);
            usuarios.add(usuarioBean);
        }

        return usuarios;
    }

    @Override
    public List<Rol> listRoles() {
        return rolRepository.getRolesActivos();
    }

    @Override
    public List<Perfil> listPerfiles() {
        return perfilRepository.getActivos();
    }

    @Override
    public List<JefeBean> listJefes(Integer id) {

        //OBTENER USUARIO DEL LOGIN (PENDIENTE)
        List<Usuario> usuarios = usuarioRepository.listJefes(443);
        List<JefeBean> jefes = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            JefeBean jefeBean = new JefeBean();
            jefeBean.setId(usuario.getId());
            jefeBean.setNombreCompleto(usuario.getNombreCompleto());
            jefes.add(jefeBean);
        }

        return jefes;
    }

    @Override
    public List<Area> listAreas() {
        return areaRepository.buscarAreas();
    }

    @Override
    public List<Rol> listRolesUsuarioDisponibles(Integer idUsuario) {
        return rolRepository.buscarRolesNoAsignadosUsuario(idUsuario);
    }

    @Override
    public List<Rol> listRolesParticipanteDisponibles(Integer idProceso) {
        return rolRepository.obtenerRolesParticipantesNoAsignados(idProceso);
    }

    @Override
    public List<Rol> listRolesProcesoDisponibles(Integer idProceso) {
        return rolRepository.obtenerRolesProcesosNoAsignados(idProceso);
    }

    @Override
    public List<Perfil> listPerfilesDisponibles(Integer idUsuario) {
        return perfilRepository.buscarPerfilesNoAsignadosUsuario(idUsuario);
    }

    @Override
    public ResponseModel save(UsuarioBean usuarioBean) {

        ResponseModel responseModel = new ResponseModel();
        List<Rol> rolesUsuario = new ArrayList<>();
        List<Perfil> perfilesUsuario = new ArrayList<>();

        try {
            if(!validatePossibleInsertUsername(usuarioBean)){
                return new ResponseModel(HttpStatus.CONFLICT,"El nombre del usuario ya existe, por favor use otro", usuarioBean.getId());
            }
            Usuario usuarioBD = usuarioRepository.findById(usuarioBean.getId());
            Area area = areaRepository.findById(usuarioBean.getIdArea());
            Usuario jefe = usuarioRepository.findById(usuarioBean.getIdJefe());

            for (UsuarioRolBean rol : usuarioBean.getRolesAsignados()) {
                Rol rolBD = rolRepository.findById(rol.getId());
                rolesUsuario.add(rolBD);
            }

            for (UsuarioPerfilBean perfil : usuarioBean.getPerfilesAsignados()) {
                Perfil perfilBD = perfilRepository.findById(perfil.getId());
                perfilesUsuario.add(perfilBD);
            }
            
            if (usuarioBean.getId() != null){

                usuarioBD.setNombres(usuarioBean.getNombres());
                usuarioBD.setApellidos(usuarioBean.getApellidos());
                usuarioBD.setClave(usuarioBean.getClave() != null ? EncryptUtil.encrypt(EncryptUtil.DEFAULT_ENCRYPTION_KEY, usuarioBean.getClave()) : null);
                usuarioBD.setCorreo(usuarioBean.getCorreo());
                usuarioBD.setEstado(usuarioBean.getEstado() ? 'A' : 'I');
                usuarioBD.setFechaCreacion(new Date());
                usuarioBD.setRequiereAprobacion(usuarioBean.getRequiereAprobacion());
                usuarioBD.setUsuario(usuarioBean.getUsuario());
                usuarioBD.setArea(area);
                usuarioBD.setJefe(jefe);
                usuarioBD.setRoles(rolesUsuario);
                usuarioBD.setPerfiles(perfilesUsuario);
                usuarioRepository.save(usuarioBD);
                responseModel.setMessage("Usuario actualizado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(usuarioBD.getId());
            }else{
                Usuario usuario = new Usuario();
                usuario.setNombres(usuarioBean.getNombres());
                usuario.setApellidos(usuarioBean.getApellidos());
                usuario.setClave(usuarioBean.getClave() != null ? EncryptUtil.encrypt(EncryptUtil.DEFAULT_ENCRYPTION_KEY, usuarioBean.getClave()) : null);
                usuario.setCorreo(usuarioBean.getCorreo());
                usuario.setEstado(usuarioBean.getEstado() ? 'A' : 'I');
                usuario.setFechaCreacion(new Date());
                usuario.setRequiereAprobacion(usuarioBean.getRequiereAprobacion());
                usuario.setUsuario(usuarioBean.getUsuario());
                usuario.setArea(area);
                usuario.setJefe(jefe);
                usuario.setRoles(rolesUsuario);
                usuario.setPerfiles(perfilesUsuario);
                usuarioRepository.save(usuario);
                responseModel.setMessage("Usuario creado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(usuario.getId());
            }
        } catch (Exception ex){
            LOGGER.error(ex.getMessage(), ex);
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseModel;
    }
    /*
        Returns true if it is possible to save the usernema of the user
        If the user is new, id == null, then only validate if the username is not used
        if the user exists, id != null, then validate if the user by the new username is the same of the param or not
     */
    private boolean validatePossibleInsertUsername(UsuarioBean usuario) {

        Usuario usuarioAux = usuarioRepository.obtenerPorUsuario(usuario.getUsuario());
        if(usuario.getId() == null) {
            return usuarioAux == null;
        } else {
            if(usuarioAux == null){
                return true;
            } else {
                return usuarioAux.getId().equals(usuario.getId());
            }
        }
    }
}