package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.RolBean;
import com.hochschild.speed.back.model.domain.speed.Rol;
import com.hochschild.speed.back.model.filter.mantenimiento.RolFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface MantenimientoRolesService {
    Rol find(Integer id);
    List<Rol> list(RolFilter filter);
    ResponseModel save(RolBean campoBean);
}
