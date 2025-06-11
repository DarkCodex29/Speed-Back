package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.BotonBean;
import com.hochschild.speed.back.model.bean.mantenimiento.BotonPerfilBean;
import com.hochschild.speed.back.model.domain.speed.Boton;
import com.hochschild.speed.back.model.domain.speed.Perfil;
import com.hochschild.speed.back.model.filter.mantenimiento.BotonFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface MantenimientoBotonService {
    BotonPerfilBean find(Integer id);
    List<Boton> list(BotonFilter botonFilter);
    List<Perfil> listPerfilesDisponibles(Integer idUsuario);
    ResponseModel save(BotonBean botonBean);
}
