package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.ProcesoReeemplazoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.ReemplazoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.UsuariosBean;
import com.hochschild.speed.back.model.domain.speed.Reemplazo;
import com.hochschild.speed.back.model.filter.mantenimiento.ReemplazoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface MantenimientoReemplazoService {
    Reemplazo find(Integer id);
    List<Reemplazo> list(ReemplazoFilter reemplazoFilter);
    List<UsuariosBean> listUsuarios();
    List<ProcesoReeemplazoBean> listProcesos();
    List<ProcesoReeemplazoBean> listProcesosDisponibles(Integer id);
    List<UsuariosBean> listJefes(Integer id);
    ResponseModel save(ReemplazoBean reemplazoBean);
}
