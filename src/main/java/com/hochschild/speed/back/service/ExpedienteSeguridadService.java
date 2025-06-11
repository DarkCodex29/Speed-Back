package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.domain.speed.utils.UsuarioNotificacionDTO;
import com.hochschild.speed.back.model.response.ResponseModel;

import java.util.List;

public interface ExpedienteSeguridadService {

    ResponseModel verificarPermisos(Integer idExpediente, Integer usuario);

    ResponseModel guardar(Boolean esConfidencial, Integer idExpediente, Integer idUsuario, List<UsuarioNotificacionDTO> usuarios);
}
