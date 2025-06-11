package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.RepresentantesBean;
import com.hochschild.speed.back.model.bean.mantenimiento.RepresentanteCompBean;
import com.hochschild.speed.back.model.bean.mantenimiento.UsuariosBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.domain.speed.utils.UsuarioRepCompDTO;
import com.hochschild.speed.back.model.filter.mantenimiento.RepCompaniaFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface MantenimientoRepCompaniaService {

    RepresentanteComp obtenerPorId(Integer id);
    Integer guardar(UsuarioRepCompDTO usuarioRepComptDTO);
    List<Boton> getBotones(Usuario user);
    List<RepresentanteComp> obtenerActivos();
    List<Usuario> obtenerUsuarioPorAutocomplete(String term);
    RepresentanteComp find(Integer id);
    List<RepresentantesBean> list(RepCompaniaFilter repCompaniaFilter);
    List<UsuariosBean> listUsuarios();
    ResponseModel save(RepresentanteCompBean representantesCompBean);
}
