package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.SedeBean;
import com.hochschild.speed.back.model.domain.speed.Sede;
import com.hochschild.speed.back.model.domain.speed.Ubigeo;
import com.hochschild.speed.back.model.filter.mantenimiento.SedeFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface MantenimientoSedeService {
    Sede find(Integer id);
    List<Sede> list(SedeFilter sedeFilter);
    List<Ubigeo> listDepartamentos();
    List<Ubigeo> listProvincias(Integer id);
    List<Ubigeo> listDistritos(Integer id);
    ResponseModel save(SedeBean sedeBean);
}
