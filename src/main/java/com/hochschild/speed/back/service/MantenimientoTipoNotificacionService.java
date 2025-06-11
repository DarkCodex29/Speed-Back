package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.TipoNotificacionBean;
import com.hochschild.speed.back.model.domain.speed.TipoNotificacion;
import com.hochschild.speed.back.model.filter.mantenimiento.TipoNotificacionFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface MantenimientoTipoNotificacionService {
    TipoNotificacion find(Integer id);
    List<TipoNotificacion> list(TipoNotificacionFilter tipoNotificacionFilter);
    ResponseModel save(TipoNotificacionBean tipoNotificacionBean);
}
