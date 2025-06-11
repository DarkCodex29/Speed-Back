package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.EstadoBean;

import java.util.List;

public interface EstadoService {

    List<EstadoBean> obtenerEstados();

    List<EstadoBean> obtenerEstadosSeguimientoSolicitud();

    String obtenerEstadoPorCodigoCharacter(Character codigo);

    String obtenerEstadoPorCodigoString(String codigo);
}
