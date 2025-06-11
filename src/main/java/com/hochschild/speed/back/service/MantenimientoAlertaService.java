package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.AlertaCleanBean;
import com.hochschild.speed.back.model.domain.speed.TipoAlerta;
import com.hochschild.speed.back.model.domain.speed.Grid;
import com.hochschild.speed.back.model.filter.mantenimiento.AlertaFilter;
import com.hochschild.speed.back.model.bean.mantenimiento.AlertaBean;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface MantenimientoAlertaService {
    AlertaCleanBean find(Integer id);
    List<AlertaCleanBean> list(AlertaFilter filter);
    List<Grid> listGrids();
    List<TipoAlerta> listTipoAlertas();
    ResponseModel save(AlertaBean campoBean);
}
