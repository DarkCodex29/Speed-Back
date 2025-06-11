package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.HcUbicacionBean;
import com.hochschild.speed.back.model.domain.speed.HcTipoUbicacion;
import com.hochschild.speed.back.model.domain.speed.HcUbicacion;
import com.hochschild.speed.back.model.response.ResponseModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface HcUbicacionService {
    ResponseModel guardarHcUbicacion(HcUbicacionBean hcUbicacionBean);

    ResponseEntity<List<HcTipoUbicacion>> obtenerHcTipoUbicacion();

    ResponseEntity<List<HcUbicacion>> listarUbicaciones();
}
