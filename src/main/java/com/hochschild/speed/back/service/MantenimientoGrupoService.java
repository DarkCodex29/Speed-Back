package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.GrupoUsuariosBean;
import com.hochschild.speed.back.model.bean.mantenimiento.UsuariosBean;
import com.hochschild.speed.back.model.filter.mantenimiento.GrupoFilter;
import com.hochschild.speed.back.model.bean.mantenimiento.GrupoBean;
import com.hochschild.speed.back.model.domain.speed.Parametro;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.model.domain.speed.HcGrupo;
import java.util.List;

public interface MantenimientoGrupoService {
    List<HcGrupo> obtenerActivos();
    GrupoUsuariosBean find(Integer id);
    List<HcGrupo> list(GrupoFilter grupoFilter);
    List<Parametro> listTipoGrupos();
    List<UsuariosBean> listUsuarios();
    ResponseModel save(GrupoBean grupoBean);
}
