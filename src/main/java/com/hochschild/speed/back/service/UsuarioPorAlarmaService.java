package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.usuariosPorAlarma.UsuariosAlarmaBean;
import java.util.List;

public interface UsuarioPorAlarmaService {
    List<UsuariosAlarmaBean> obtenerUsuariosPorAlarma (Integer idAlarma);
}
