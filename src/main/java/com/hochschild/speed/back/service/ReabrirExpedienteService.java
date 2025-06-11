package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.reabrirExpediente.ReabrirBean;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface ReabrirExpedienteService {
    List<Usuario> abogadosResponsables();
    ResponseModel reabrirExpediente(ReabrirBean reabrirBean, Integer idUsuario);
}
