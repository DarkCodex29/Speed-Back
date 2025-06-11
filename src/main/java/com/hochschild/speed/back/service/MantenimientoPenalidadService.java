package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.PenalidadBean;
import com.hochschild.speed.back.model.bean.mantenimiento.ReiteranciasBean;
import com.hochschild.speed.back.model.bean.mantenimiento.TipoValorBean;
import com.hochschild.speed.back.model.domain.speed.HcPenalidad;
import com.hochschild.speed.back.model.filter.mantenimiento.PenalidadFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface MantenimientoPenalidadService {
    HcPenalidad find(Integer id);
    List<HcPenalidad> list(PenalidadFilter penalidadFilter);
    List<ReiteranciasBean> listReiterancias();
    List<TipoValorBean> listTipoValores();
    ResponseModel save(PenalidadBean penalidadBean);
}
