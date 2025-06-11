package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.FeriadoBean;
import com.hochschild.speed.back.model.filter.mantenimiento.FeriadoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.model.domain.speed.Feriado;
import java.util.List;

public interface MantenimientoFeriadoService {
    Feriado find(Integer id);
    List<Feriado> list(FeriadoFilter feriadoFilter);
    ResponseModel save(FeriadoBean feriadoBean);
}
