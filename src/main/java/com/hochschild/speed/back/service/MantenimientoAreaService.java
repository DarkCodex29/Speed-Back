package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.AreaBean;
import com.hochschild.speed.back.model.bean.mantenimiento.AreaPadreBean;
import com.hochschild.speed.back.model.domain.speed.Area;
import com.hochschild.speed.back.model.domain.speed.Sede;
import com.hochschild.speed.back.model.filter.mantenimiento.AreaFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface MantenimientoAreaService {
    List<Sede> buscarSedes();
    List<Area> buscarAreas();
    AreaPadreBean find(Integer id);
    List<AreaPadreBean> list(AreaFilter filter);
    ResponseModel save(AreaBean campoBean);
}
