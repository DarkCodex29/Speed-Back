package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.PerfilBean;
import com.hochschild.speed.back.model.domain.speed.Perfil;
import com.hochschild.speed.back.model.filter.mantenimiento.PerfilFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface MantenimientoPerfilesService {
    Perfil find(Integer id);
    List<Perfil> list(PerfilFilter filter);
    ResponseModel save(PerfilBean campoBean);
}
