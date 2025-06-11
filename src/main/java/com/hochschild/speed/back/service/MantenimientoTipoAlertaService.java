package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.TipoAlertaBean;
import com.hochschild.speed.back.model.domain.speed.TipoAlerta;
import com.hochschild.speed.back.model.filter.mantenimiento.TipoAlertaFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface MantenimientoTipoAlertaService {
    TipoAlerta find(Integer id);
    List<TipoAlerta> list(TipoAlertaFilter tipoAlertaFilter);
    ResponseModel save(TipoAlertaBean tipoAlertaBean);
}
