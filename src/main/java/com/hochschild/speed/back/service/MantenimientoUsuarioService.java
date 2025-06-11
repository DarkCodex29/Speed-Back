package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.JefeBean;
import com.hochschild.speed.back.model.bean.mantenimiento.UsuarioBean;
import com.hochschild.speed.back.model.bean.mantenimiento.UsuariosBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.filter.mantenimiento.UsuarioFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface MantenimientoUsuarioService {
    List<Rol> buscarRolesExistente(Integer idUsuario);
    Usuario find(Integer id);
    List<UsuariosBean> list(UsuarioFilter filter);
    List<Rol> listRoles();
    List<Perfil> listPerfiles();
    List<JefeBean> listJefes(Integer id);
    List<Area> listAreas();
    List<Rol> listRolesUsuarioDisponibles(Integer idUsuario);
    List<Rol> listRolesParticipanteDisponibles(Integer idProceso);
    List<Rol> listRolesProcesoDisponibles(Integer idProceso);
    List<Perfil> listPerfilesDisponibles(Integer idUsuario);
    ResponseModel save(UsuarioBean usuarioBean);
}