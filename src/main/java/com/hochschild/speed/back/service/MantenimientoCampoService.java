package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.TipoCampo;
import com.hochschild.speed.back.model.filter.mantenimiento.CampoFilter;
import com.hochschild.speed.back.model.bean.mantenimiento.CampoBean;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.model.domain.speed.Campo;
import java.util.List;

public interface MantenimientoCampoService {
    Campo find(Integer id);
    List<Campo> list(CampoFilter filter);
    List<TipoCampo> fieldType();
    List<String> parameterType();
    ResponseModel save(CampoBean campoBean);
}
