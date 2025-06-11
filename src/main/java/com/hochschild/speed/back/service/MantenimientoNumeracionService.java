package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.NumeracionBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.filter.mantenimiento.NumeracionFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface MantenimientoNumeracionService {
    Numeracion find(Integer id);
    List<Numeracion> list(NumeracionFilter numeracionFilter);
    List<TipoDocumento> listTipoDocumentos();
    List<Area> listAreas();
    ResponseModel save(NumeracionBean numeracionBean);
}